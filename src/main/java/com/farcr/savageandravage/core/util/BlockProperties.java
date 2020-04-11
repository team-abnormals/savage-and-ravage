package com.farcr.savageandravage.core.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockProperties {

	public static final Block.Properties GLOOMY_TILES = Block.Properties.create(Material.ROCK, MaterialColor.GRAY).hardnessAndResistance(1.5F, 6.0F);
	public static final Block.Properties SPORE_BOMB = Block.Properties.from(Blocks.TNT);
	public static final Block.Properties BLAST_PROOF_PLATES = Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 1200.0F);
}