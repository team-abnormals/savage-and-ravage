package com.minecraftabnormals.savageandravage.common.generation;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class EnclosurePlacement extends Placement<NoPlacementConfig> {

    public EnclosurePlacement(Codec<NoPlacementConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld worldIn, ChunkGenerator generatorIn, Random random, NoPlacementConfig configIn, BlockPos pos) {
        //TODO Make it check for pillager outpost in radius (>1 chunk, <4 chunks or something) then check for other enclosures within 4 chunks of the outpost
        return Stream.empty();
    }
}
