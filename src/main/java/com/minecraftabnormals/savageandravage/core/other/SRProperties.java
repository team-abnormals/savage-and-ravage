package com.minecraftabnormals.savageandravage.core.other;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.properties.BlockStateProperties;

import java.util.function.ToIntFunction;

public class SRProperties {

    public static final Block.Properties GLOOMY_TILES = Block.Properties.create(Material.ROCK, MaterialColor.LIGHT_BLUE_TERRACOTTA).setRequiresTool().hardnessAndResistance(1.5F, 6.0F);
    public static final Block.Properties LIGHTABLE_GLOOMY_TILES = Block.Properties.create(Material.ROCK, MaterialColor.LIGHT_BLUE_TERRACOTTA).setRequiresTool().hardnessAndResistance(1.5F, 6.0F).setLightLevel(getLightValuePowered(7));
    public static final Block.Properties BLAST_PROOF_PLATES = Block.Properties.create(Material.IRON, MaterialColor.GREEN).setRequiresTool().hardnessAndResistance(2.0F, 1200.0F);

    public static ToIntFunction<BlockState> getLightValuePowered(int lightValue) {
        return (stateHolder) -> stateHolder.get(BlockStateProperties.POWERED) ? lightValue : 0;
    }
}