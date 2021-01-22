package com.minecraftabnormals.savageandravage.common.world.gen.feature;

import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import com.minecraftabnormals.savageandravage.common.entity.GrieferEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.Random;

public class EnclosureFeature extends Feature<NoFeatureConfig> {
    private static final Direction[] horizontalDirections = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private final BlockPos.Mutable currentPos = new BlockPos.Mutable(); //Cached to avoid unnecessary BlockPos creation
    private BlockPos originalStartPos;

    public EnclosureFeature(Codec<NoFeatureConfig> featureConfigCodec) {
        super(featureConfigCodec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos centerPos, NoFeatureConfig config) {
        int minY = centerPos.getY() - (4 + rand.nextInt(2)); // The lowest y to use when making the pit
        originalStartPos = centerPos;
        centerPos = findSuitablePosition(reader, centerPos, minY, rand);
        if (centerPos != null) {
            // These position arrays use the surface as their Y level - the air block above the ground
            ArrayList<BlockPos> holePositions = new ArrayList<>(); // Positions at the hole - i.e. which X and Z values are to be cut out
            ArrayList<BlockPos> edgePositions = new ArrayList<>(); // Positions at the edge of the hole, where 'drop-offs' might be placed
            ArrayList<BlockPos> outlinePositions; // Positions for fences to be placed on or the griefer to spawn
            // Adding the 'starting positions' - the center and its neighbours
            holePositions.add(centerPos);
            for (Direction dir : horizontalDirections) {
                edgePositions.add(centerPos.offset(dir));
            }
            for (int i = 0; i <= 5 + rand.nextInt(5); i++) {
                edgePositions = expandHole(edgePositions, holePositions, centerPos, reader, rand);
            }
            generateEdges(edgePositions, reader, rand);
            outlinePositions = findOutlines(edgePositions, holePositions, reader);
            generateHole(holePositions, minY, reader, rand);
            ArrayList<BlockPos> clearOutlinePositions = generateFences(outlinePositions, reader, rand);
            for (BlockPos outlinePos : outlinePositions) {
                fixFenceConnections(reader, outlinePos);
            }
            if (!clearOutlinePositions.isEmpty()) {
                GrieferEntity griefer = SREntities.GRIEFER.get().create(reader.getWorld());
                if (griefer != null) {
                    BlockPos grieferPos = clearOutlinePositions.get(rand.nextInt(clearOutlinePositions.size())).toMutable(); //put the griefer at a random location on the edge of the hole
                    griefer.setLocationAndAngles(grieferPos.getX(), grieferPos.getY(), grieferPos.getZ(), 0, 0);
                    griefer.enablePersistence();
                    griefer.onInitialSpawn(reader, reader.getDifficultyForLocation(grieferPos), SpawnReason.CHUNK_GENERATION, null, null);
                    reader.addEntity(griefer);
                }
            }
            generateDecorations(getDecorationStarts(outlinePositions, edgePositions, holePositions, reader), reader, rand);
            return true;
        }
        return false;
    }

    /**
     * For the area 3 blocks around the center position, checks if the area 5 blocks around it are suitable, returning
     * one of these valid positions, or null if none are valid.
     */
    private BlockPos findSuitablePosition(ISeedReader reader, BlockPos centerPos, int minY, Random rand) {
        BlockPos.Mutable pos = centerPos.toMutable();
        ArrayList<BlockPos> suitablePositions = new ArrayList<>();
        for (int bigX = centerPos.getX() - 3; bigX < centerPos.getX() + 3; bigX++) {
            for (int bigZ = centerPos.getZ() - 3; bigZ < centerPos.getZ() + 3; bigZ++) {
                boolean areaClear = true;
                for (int x = bigX - 4; x <= bigX + 4; x++) {
                    for (int z = bigZ - 4; z <= bigZ + 4; z++) {
                        for (int y = minY; y <= centerPos.getY() + 1; y++) {
                            pos.setPos(x, y, z);
                            // isOpaqueCube should return false for surface positions (where y>=centerPos.getY() is true) but true for underground positions (where y >= centerPos.getY() is false)
                            // This has the effect of checking that the area is 'clear'
                            if (((y >= centerPos.getY()) == reader.getBlockState(pos).isOpaqueCube(reader, pos)) || Math.abs(originalStartPos.getX() - pos.getX()) > 11 || Math.abs(originalStartPos.getZ() - pos.getZ()) > 11) {
                                areaClear = false;
                            }
                        }
                    }
                }
                if (areaClear) {
                    pos.setPos(bigX, centerPos.getY(), bigZ);
                    suitablePositions.add(pos);
                }
            }
        }
        return !suitablePositions.isEmpty() ? suitablePositions.get(rand.nextInt(suitablePositions.size())) : null;
    }

    /**
     * Like isAreaClear, but it only checks one position and two y levels - ground and the block above
     */
    private boolean isSurfacePositionClear(ISeedReader reader, BlockPos pos) {
        // This is a quick fix to stop decorations from intersecting outposts. It's not ideal as it restricts decoration positions decoration positions in certain directions where they shouldn't be restricted.
        // If enclosure placement could be deferred until the other jigsaw blocks are placed, this would be obsolete.
        if (Math.abs(originalStartPos.getX() - pos.getX()) < 12 && Math.abs(originalStartPos.getZ() - pos.getZ()) < 12) {
            if (!(reader.getBlockState(pos).isOpaqueCube(reader, pos))) {
                return reader.getBlockState(pos.offset(Direction.DOWN)).isOpaqueCube(reader, pos.offset(Direction.DOWN));
            }
        }
        return false;
    }

    /**
     * Takes in an arraylist of the positions at the edge of a hole and makes it bigger. Also increases the size of the holePositions
     */
    private ArrayList<BlockPos> expandHole(ArrayList<BlockPos> edgePositions, ArrayList<BlockPos> holePositions, BlockPos centerPos, ISeedReader reader, Random rand) {
        ArrayList<BlockPos> newEdgePositions = new ArrayList<>(edgePositions); // Caching edgePositions as elements need to be removed
        for (BlockPos edgePos : edgePositions) {
            // This makes it less likely to expand further as it gets larger, preventing ridiculous hole sizes
            if (rand.nextInt(49) > edgePos.distanceSq(centerPos)) {
                ArrayList<BlockPos> validPotentialPositions = new ArrayList<>();
                boolean hasHoleNeighbor = false;
                boolean isNothingBlocking = true;
                for (Direction dir : horizontalDirections) {
                    currentPos.setPos(edgePos.offset(dir).toMutable());
                    if (!holePositions.contains(currentPos)) {
                        if (!edgePositions.contains(currentPos)) {
                            if (isSurfacePositionClear(reader, currentPos)) {
                                validPotentialPositions.add(currentPos.toImmutable());
                            } else {
                                isNothingBlocking = false;
                                break;
                            }
                        }
                    } else hasHoleNeighbor = true;
                }
                if (isNothingBlocking) {
                    if (hasHoleNeighbor) {
                        if (validPotentialPositions.size() == 0) {
                            holePositions.add(edgePos); // Prevents pillars in the pit
                            newEdgePositions.remove(edgePos);
                        } else {
                            // This random check makes it less likely that positions with fewer neighbours will expand further
                            // In theory, this prevents straight lines in one direction from going on too far
                            if ((rand.nextFloat() < (1 / (validPotentialPositions.size() + 1.0f)))) {
                                newEdgePositions.addAll(validPotentialPositions);
                                newEdgePositions.remove(edgePos);
                                holePositions.add(edgePos);
                            }
                        }

                    } else newEdgePositions.remove(edgePos);
                }
            }
        }
        return newEdgePositions;
    }

    /**
     * Generates the blocks at drop off positions, updating the edge positions with the failed drop offs
     */
    private void generateEdges(ArrayList<BlockPos> edgePositions, ISeedReader reader, Random rand) {
        for (BlockPos edgePos : edgePositions) {
            if (rand.nextFloat() < 0.6f) { // Randomised to make the hole look a bit more natural
                reader.setBlockState(edgePos.offset(Direction.DOWN), Blocks.AIR.getDefaultState(), 3);
                reader.setBlockState(edgePos, Blocks.AIR.getDefaultState(), 3);
                reader.setBlockState(edgePos.offset(Direction.UP), Blocks.AIR.getDefaultState(), 3);
            }
        }
    }

    private ArrayList<BlockPos> findOutlines(ArrayList<BlockPos> edgePositions, ArrayList<BlockPos> holePositions, ISeedReader reader) {
        ArrayList<BlockPos> outlinePositions = new ArrayList<>();
        for (BlockPos edgePos : edgePositions) {
            // The rotate y stuff is used to get diagonal neighbours, is there a better way?
            for (Direction dir : horizontalDirections) {
                currentPos.setPos(edgePos.offset(dir));
                if (!edgePositions.contains(currentPos) && !holePositions.contains(currentPos) && !(reader.getBlockState(currentPos).isOpaqueCube(reader, currentPos))) {
                    outlinePositions.add(currentPos.toImmutable()); // Needs to be cloned
                }
                currentPos.setPos(currentPos.offset(dir.rotateY()));
                if (!edgePositions.contains(currentPos) && !holePositions.contains(currentPos) && !(reader.getBlockState(currentPos).isOpaqueCube(reader, currentPos))) {
                    outlinePositions.add(currentPos.toImmutable());
                }
            }
        }
        return outlinePositions;
    }

    /**
     * Takes in an arraylist of hole positions and generates the hole from them, including mobs inside
     */
    private void generateHole(ArrayList<BlockPos> holePositions, int minY, ISeedReader reader, Random rand) {
        for (BlockPos holePos : holePositions) {
            currentPos.setPos(holePos);
            for (int i = minY; i < holePos.getY() + 2; i++) { // holePos.getY() is the block 1 above the surface, so < is used
                currentPos.setY(i);
                if (i == minY) {
                    reader.setBlockState(currentPos, Blocks.COARSE_DIRT.getDefaultState(), 3);
                } else if (i == minY + 1) {
                    reader.setBlockState(currentPos, rand.nextFloat() < 0.4f ? rand.nextFloat() > 0.3f ? Blocks.GRASS.getDefaultState() : Blocks.DEAD_BUSH.getDefaultState() : Blocks.AIR.getDefaultState(), 3);
                } else {
                    reader.setBlockState(currentPos, Blocks.AIR.getDefaultState(), 3);
                }
            }
            if (rand.nextFloat() < 0.3f) {
                MobEntity entity = rand.nextFloat() < 0.5f ? EntityType.CREEPER.create(reader.getWorld()) : SREntities.CREEPIE.get().create(reader.getWorld());
                if (entity != null) {
                    entity.setLocationAndAngles(currentPos.getX() + 0.5, minY + 1, currentPos.getZ() + 0.5, 0, 0);
                    entity.enablePersistence();
                    if (entity instanceof CreepieEntity) {
                        ((CreepieEntity) entity).attackPlayersOnly = true;
                    }
                    entity.onInitialSpawn(reader, reader.getDifficultyForLocation(currentPos), SpawnReason.CHUNK_GENERATION, null, null);
                    reader.addEntity(entity);
                }
            }
        }
    }

    /**
     * Takes in an arraylist of positions of the outline around the hole, and generates fences at them.
     * Returns the outlines excluding fence positions
     */
    private ArrayList<BlockPos> generateFences(ArrayList<BlockPos> outlinePositions, ISeedReader reader, Random rand) {
        BlockPos.Mutable secondFencePos = new BlockPos.Mutable();
        ArrayList<BlockPos> nonFenceOutlines = new ArrayList<>(outlinePositions);
        if (!nonFenceOutlines.isEmpty()) {
            for (BlockPos firstFencePos : outlinePositions) {
                if (rand.nextFloat() < 0.25f) {
                    for (Direction dir : horizontalDirections) {
                        secondFencePos.setPos(firstFencePos.offset(dir));
                        if (nonFenceOutlines.contains(secondFencePos) && reader.getBlockState(secondFencePos.offset(Direction.DOWN)).isOpaqueCube(reader, secondFencePos.offset(Direction.DOWN))) {
                            reader.setBlockState(firstFencePos, Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                            reader.setBlockState(secondFencePos, Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                            // Removed so that the griefer doesn't spawn in a fence. Also prevents redundant placement.
                            nonFenceOutlines.remove(firstFencePos);
                            nonFenceOutlines.remove(secondFencePos);
                            break;
                        }
                    }
                }
            }
        }
        return nonFenceOutlines;
    }

    /**
     * Fixes fence connections between the block at pos and its neighbors
     */
    private void fixFenceConnections(ISeedReader reader, BlockPos pos) {
        BlockState originalState = reader.getBlockState(pos);
        if (originalState.getBlock() instanceof FenceBlock) {
            BlockPos.Mutable neighborPos = new BlockPos.Mutable();
            for (Direction dir : horizontalDirections) {
                neighborPos.setPos(pos.offset(dir));
                BlockState neighborState = reader.getBlockState(neighborPos);
                if (neighborState.getBlock() instanceof FenceBlock) {
                    originalState = originalState.with(SixWayBlock.FACING_TO_PROPERTY_MAP.get(dir), true);
                    neighborState = neighborState.with(SixWayBlock.FACING_TO_PROPERTY_MAP.get(dir.getOpposite()), true);
                    reader.setBlockState(pos, originalState, 3);
                    reader.setBlockState(neighborPos.toImmutable(), neighborState, 3);
                }
            }
        }
    }

    /**
     * Finds valid positions to place decorations from
     */
    private ArrayList<Pair<Direction, BlockPos>> getDecorationStarts(ArrayList<BlockPos> outlinePositions, ArrayList<BlockPos> edgePositions, ArrayList<BlockPos> holePositions, ISeedReader reader) {
        ArrayList<Pair<Direction, BlockPos>> decorationStarts = new ArrayList<>();
        for (BlockPos outlinePos : outlinePositions) {
            for (Direction dir : horizontalDirections) {
                currentPos.setPos(outlinePos.offset(dir));
                if (!outlinePositions.contains(currentPos) && !edgePositions.contains(currentPos)) {
                    // Tries to allow positions at the diagonal sides of holes by trying again if the position arrays contain a thing
                    for (int attempts = 0; attempts < 4; attempts++) {
                        currentPos.setPos(currentPos.offset(dir));
                        if (!outlinePositions.contains(currentPos) && !edgePositions.contains(currentPos)) {
                            if (isSurfacePositionClear(reader, currentPos)) {
                                boolean shouldTryAgain = false;
                                BlockPos start = currentPos.toImmutable();
                                boolean isClear = true;
                                currentPos.setPos(start.offset(dir.rotateYCCW(), 2));
                                BlockPos.Mutable mainForwardPos = currentPos.toMutable();
                                for (int i = 0; i < 5 && isClear; i++) {
                                    for (int j = 0; j < 5 && isClear; j++) {
                                        if (!isSurfacePositionClear(reader, currentPos)) {
                                            isClear = false;
                                        } else if (outlinePositions.contains(currentPos) || edgePositions.contains(currentPos) || holePositions.contains(currentPos)) {
                                            isClear = false;
                                            shouldTryAgain = true;
                                        } else {
                                            BlockPos.Mutable checkingPos = new BlockPos.Mutable();
                                            for (Direction subDir : horizontalDirections) {
                                                checkingPos.setPos(currentPos.offset(subDir));
                                                if (reader.getBlockState(checkingPos).isOpaqueCube(reader, checkingPos)) {
                                                    isClear = false;
                                                    break;
                                                } else if (outlinePositions.contains(checkingPos) || edgePositions.contains(checkingPos) || holePositions.contains(checkingPos)) {
                                                    isClear = false;
                                                    shouldTryAgain = true;
                                                    break;
                                                }
                                            }
                                        }
                                        currentPos.setPos(currentPos.offset(dir.rotateY()));
                                    }
                                    mainForwardPos.setPos(mainForwardPos.offset(dir));
                                    currentPos.setPos(mainForwardPos);
                                }
                                if (isClear) {
                                    decorationStarts.add(new Pair<>(dir, start));
                                }
                                if (!shouldTryAgain) break;
                            }
                        }
                    }
                }
            }
        }
        return decorationStarts;
    }

    /**
     * Chooses positions from a potential decoration positions list and generates decorations
     */
    private void generateDecorations(ArrayList<Pair<Direction, BlockPos>> potentialStarts, ISeedReader reader, Random rand) {
        int decorationIndex = 0;
        BlockPos[] decorationCenters = new BlockPos[3];
        while (decorationIndex < 3 && !potentialStarts.isEmpty()) {
            Pair<Direction, BlockPos> positionInfo = potentialStarts.get(rand.nextInt(potentialStarts.size()));
            currentPos.setPos(positionInfo.getSecond());
            Direction dir = positionInfo.getFirst();
            BlockPos[][] decorationPositions = new BlockPos[5][5]; // First bracket for along, second for ahead
            // Caching decoration locations to make it easier to set blockstates
            currentPos.setPos(currentPos.offset(dir.rotateYCCW(), 2));
            BlockPos.Mutable mainForwardPos = currentPos.toMutable();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (j == 2 && i == 2) {
                        decorationCenters[decorationIndex] = currentPos.toImmutable();
                    }
                    decorationPositions[j][i] = currentPos.toImmutable();
                    currentPos.setPos(currentPos.offset(dir.rotateY()));
                }
                mainForwardPos.setPos(mainForwardPos.offset(dir));
                currentPos.setPos(mainForwardPos);
            }
            boolean doesNotIntersect = true;
            if (decorationIndex > 0) {
                currentPos.setPos(decorationCenters[decorationIndex]);
                for (int i = 0; i < decorationIndex; i++) {
                    if (Math.abs(currentPos.getX() - decorationCenters[i].getX()) <= 5 && Math.abs(currentPos.getZ() - decorationCenters[i].getZ()) <= 5) {
                        doesNotIntersect = false;
                        break;
                    }
                }
            }
            potentialStarts.remove(positionInfo);
            if (doesNotIntersect) {
                if (decorationIndex == 0) {
                    switch (rand.nextInt(ModList.get().isLoaded("quark") ? 3 : 2)) {
                        case 0:
                            for (int i = 0; i <= 1; i++) {
                                for (int j = 1; j <= 2; j++) {
                                    currentPos.setPos(decorationPositions[j][i]);
                                    reader.setBlockState(currentPos, Blocks.CRAFTING_TABLE.getDefaultState(), 3);
                                    currentPos.setPos(currentPos.offset(Direction.UP));
                                    if (i == 1 && j == 2) {
                                        reader.setBlockState(currentPos, Blocks.CHEST.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, dir), 3);
                                        LockableLootTileEntity.setLootTable(reader, rand, currentPos, new ResourceLocation(SavageAndRavage.MOD_ID, "chests/enclosure"));
                                    } else {
                                        reader.setBlockState(currentPos, Blocks.CRAFTING_TABLE.getDefaultState(), 3);
                                    }
                                }
                            }
                            break;
                        case 1:
                            for (int i = 0; i < 3; i++) {
                                if (i == 0) {
                                    BlockState stairsState = SRBlocks.BLAST_PROOF_STAIRS.get().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, dir);
                                    reader.setBlockState(decorationPositions[1][i], stairsState, 3);
                                    reader.setBlockState(decorationPositions[2][i], stairsState, 3);
                                    reader.setBlockState(decorationPositions[3][i], stairsState, 3);
                                } else if (i == 1) {
                                    reader.setBlockState(decorationPositions[1][i], SRBlocks.BLAST_PROOF_STAIRS.get().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, dir.rotateY()), 3);
                                    reader.setBlockState(decorationPositions[2][i], SRBlocks.BLAST_PROOF_PLATES.get().getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[2][i].offset(Direction.UP), Blocks.CHEST.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, dir), 3);
                                    LockableLootTileEntity.setLootTable(reader, rand, decorationPositions[2][i].offset(Direction.UP), new ResourceLocation(SavageAndRavage.MOD_ID, "chests/enclosure"));
                                    reader.setBlockState(decorationPositions[3][i], SRBlocks.BLAST_PROOF_STAIRS.get().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, dir.rotateYCCW()), 3);
                                } else {
                                    BlockState stairsState = SRBlocks.BLAST_PROOF_STAIRS.get().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, dir.getOpposite());
                                    reader.setBlockState(decorationPositions[1][i], stairsState, 3);
                                    reader.setBlockState(decorationPositions[2][i], stairsState, 3);
                                    reader.setBlockState(decorationPositions[3][i], stairsState, 3);
                                }
                            }
                            break;
                        case 2:
                            reader.setBlockState(decorationPositions[2][0], SRBlocks.CREEPER_SPORE_SACK.get().getDefaultState(), 3);
                            reader.setBlockState(decorationPositions[3][0], Blocks.CHEST.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, dir), 3);
                            LockableLootTileEntity.setLootTable(reader, rand, decorationPositions[3][0], new ResourceLocation(SavageAndRavage.MOD_ID, "chests/enclosure"));
                    }
                } else {
                    switch (rand.nextInt(3)) {
                        case 0:
                            // Big cage with spore bomb
                            for (int i = 0; i < 6; i++) {
                                if (i == 0 || i == 4) {
                                    for (int j = 0; j < 5; j++) {
                                        reader.setBlockState(decorationPositions[j][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                        reader.setBlockState(decorationPositions[j][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                        if (i == 4) {
                                            reader.setBlockState(decorationPositions[2][i], Blocks.DARK_OAK_PLANKS.getDefaultState(), 3);
                                        }
                                    }
                                } else if (i == 5) {
                                    reader.setBlockState(decorationPositions[2][4].offset(dir), Blocks.DARK_OAK_BUTTON.getDefaultState().with(HorizontalFaceBlock.HORIZONTAL_FACING, dir), 3);
                                } else {
                                    reader.setBlockState(decorationPositions[0][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[0][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[4][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[4][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    if (i == 3) {
                                        reader.setBlockState(decorationPositions[2][i], SRBlocks.SPORE_BOMB.get().getDefaultState(), 3);
                                    }
                                }
                            }
                            break;
                        case 1:
                            // Medium cage
                            for (int i = 0; i < 4; i++) {
                                if (i > 0 && i < 3) {
                                    reader.setBlockState(decorationPositions[1][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[1][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[4][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[4][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    CreeperEntity creeper = EntityType.CREEPER.create(reader.getWorld());
                                    if (creeper != null) {
                                        currentPos.setPos(decorationPositions[2 + rand.nextInt(2)][i]);
                                        creeper.setLocationAndAngles(currentPos.getX(), currentPos.getY(), currentPos.getZ(), 0, 0);
                                        creeper.enablePersistence();
                                        creeper.onInitialSpawn(reader, reader.getDifficultyForLocation(currentPos), SpawnReason.CHUNK_GENERATION, null, null);
                                        reader.addEntity(creeper);
                                    }
                                } else {
                                    for (int j = 1; j < 5; j++) {
                                        reader.setBlockState(decorationPositions[j][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                        reader.setBlockState(decorationPositions[j][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    }
                                }
                            }
                            break;
                        case 2:
                            // Small cage
                            for (int i = 0; i < 3; i++) {
                                if (i == 1) {
                                    reader.setBlockState(decorationPositions[1][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[1][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[3][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    reader.setBlockState(decorationPositions[3][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    CreeperEntity creeper = EntityType.CREEPER.create(reader.getWorld());
                                    if (creeper != null) {
                                        currentPos.setPos(decorationPositions[2][i]);
                                        creeper.setLocationAndAngles(currentPos.getX(), currentPos.getY(), currentPos.getZ(), 0, 0);
                                        creeper.enablePersistence();
                                        creeper.onInitialSpawn(reader, reader.getDifficultyForLocation(currentPos), SpawnReason.CHUNK_GENERATION, null, null);
                                        reader.addEntity(creeper);
                                    }
                                } else {
                                    for (int j = 1; j < 4; j++) {
                                        reader.setBlockState(decorationPositions[j][i], Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                        reader.setBlockState(decorationPositions[j][i].offset(Direction.UP), Blocks.DARK_OAK_FENCE.getDefaultState(), 3);
                                    }
                                }
                            }
                    }
                }
                for (BlockPos[] positions : decorationPositions) {
                    for (BlockPos position : positions) {
                        reader.getBlockState(position).updateNeighbours(reader, position, 3);
                        reader.getBlockState(position.offset(Direction.UP)).updateNeighbours(reader, position.offset(Direction.UP), 3);
                    }
                }
                decorationIndex++;
            }
        }
    }
}
