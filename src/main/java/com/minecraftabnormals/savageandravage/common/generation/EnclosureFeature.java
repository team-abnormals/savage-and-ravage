package com.minecraftabnormals.savageandravage.common.generation;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.Random;

public class EnclosureFeature extends Feature<NoFeatureConfig> {

    public EnclosureFeature(Codec<NoFeatureConfig> featureConfigCodec) {
        super(featureConfigCodec);
    }

    /**
     * Generates the blocks
     * */
    @Override
    public boolean func_230362_a_(ISeedReader seedReader, StructureManager structureManager, ChunkGenerator chunkGenerator, Random rand, BlockPos pos, NoFeatureConfig noConfig) {
        //TODO make this actually place an enclosure
        BlockPos newPos = pos;
        for(int i=0; i<20; i++) {
            seedReader.setBlockState(newPos, Blocks.DIAMOND_BLOCK.getDefaultState(), 16);
            newPos = newPos.offset(Direction.UP);
        }
        return true;
    }
}
