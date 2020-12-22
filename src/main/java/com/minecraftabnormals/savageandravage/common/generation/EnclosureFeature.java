package com.minecraftabnormals.savageandravage.common.generation;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class EnclosureFeature extends Feature<NoFeatureConfig> {
    public boolean alreadyGenerated = false;

    public EnclosureFeature(Codec<NoFeatureConfig> featureConfigCodec) {
        super(featureConfigCodec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        //TODO make this actually place an enclosure, maybe handle pillager outpost detection because placement bad?
        BlockPos newPos = new BlockPos(pos.getX(), generator.getGroundHeight(), pos.getY());
        for(int i=0; i<20; i++) {
            reader.setBlockState(newPos, Blocks.DIAMOND_BLOCK.getDefaultState(), 16);
            newPos = newPos.offset(Direction.UP);
        }
        return true;
    }
}
