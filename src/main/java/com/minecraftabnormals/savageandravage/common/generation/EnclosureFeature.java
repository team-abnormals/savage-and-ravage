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

    public EnclosureFeature(Codec<NoFeatureConfig> featureConfigCodec) {
        super(featureConfigCodec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        //TODO make this actually place an enclosure, along with choosing a location
        //pos.offset isn't working properly for cardinals
        for(int i=0; i<20; i++) {
            reader.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState(), 16);
            pos = pos.offset(Direction.UP);
        }
        return true;
    }
}
