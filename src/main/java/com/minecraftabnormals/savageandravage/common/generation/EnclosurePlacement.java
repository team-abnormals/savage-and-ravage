package com.minecraftabnormals.savageandravage.common.generation;

import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.Random;
import java.util.stream.Stream;

public class EnclosurePlacement extends Placement<NoPlacementConfig> {
    //the thread safety here doesn't seem to be causing freeze, but that's only because it hasn't been reached yet
    //private static final Hashtable<BlockPos, Long> GENERATED_POSITIONS = new Hashtable<>();

    public EnclosurePlacement(Codec<NoPlacementConfig> configCodec) {
        super(configCodec);
    }

    //TODO checks for other pillager outpost decorations and caves
    @Override
    public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, NoPlacementConfig config, BlockPos pos) {
        if(rand.nextInt(50)==0) {
            long seed = helper.field_242889_a.getSeed();
            StructureSeparationSettings settings = helper.chunkGenerator.func_235957_b_().func_236197_a_(Structure.PILLAGER_OUTPOST);
            if (settings != null) {
                ChunkPos chunk = new ChunkPos(pos);
                BlockPos outpost = Structure.PILLAGER_OUTPOST.getChunkPosForStructure(settings, seed, new SharedSeedRandom(seed), chunk.x, chunk.z).asBlockPos();
                int xDiff = Math.abs(outpost.getX() - pos.getX());
                int zDiff = Math.abs(outpost.getZ() - pos.getZ());
                if (xDiff < 50 && zDiff < 50 && xDiff > 16 && zDiff > 16) {
                    int x = pos.getX() - 5 + rand.nextInt(10);
                    int z = pos.getZ() - 5 + rand.nextInt(10);
                    return Stream.of(new BlockPos(x, helper.func_242893_a(Heightmap.Type.WORLD_SURFACE_WG, x, z), z));
                }
            }
        }
        return Stream.empty();
    }

    /*//TODO this logic is wrong.
    private boolean checkPreviousGeneration(BlockPos pos, long seed) {
        if(!GENERATED_POSITIONS.containsKey(pos)) {
            GENERATED_POSITIONS.put(pos, seed);
            return true;
        } else return GENERATED_POSITIONS.get(pos) != seed;
    }*/

       /*private BlockPos nearestOutpostPos(ChunkGenerator chunkGenerator, long seed, SharedSeedRandom random, int originalX, int originalZ) {
        StructureSeparationSettings settings = chunkGenerator.func_235957_b_().func_236197_a_(Structure.PILLAGER_OUTPOST);
        if (settings != null){
            for (int x = originalX - 2; x <= originalX + 2; x++) {
                for (int z = originalZ - 2; z <= originalZ + 2; ++z) {
                    ChunkPos outpostPos = Structure.PILLAGER_OUTPOST.getChunkPosForStructure(settings, seed, random, x, z);
                    if (x == outpostPos.x && z == outpostPos.z) {
                        return outpostPos.asBlockPos();
                    }
                }
            }
        }
        return null;
    }*/
}
