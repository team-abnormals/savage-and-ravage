package com.minecraftabnormals.savageandravage.core.registry;

import com.teamabnormals.abnormals_core.common.blocks.AbnormalsStairsBlock;
import com.teamabnormals.abnormals_core.common.blocks.VerticalSlabBlock;
import com.teamabnormals.abnormals_core.core.utils.DataUtils;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import com.minecraftabnormals.savageandravage.common.block.ImprovedFlowerPotBlock;
import com.minecraftabnormals.savageandravage.common.block.RunedGloomyTilesBlock;
import com.minecraftabnormals.savageandravage.common.block.SporeBombBlock;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.other.SRProperties;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRBlocks {
	public static final RegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER;

	public static final RegistryObject<Block> GLOOMY_TILES				= HELPER.createBlock("gloomy_tiles", () -> new Block(Block.Properties.from(Blocks.PURPUR_BLOCK)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> CHISELED_GLOOMY_TILES		= HELPER.createBlock("chiseled_gloomy_tiles", () -> new Block(SRProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_STAIRS		= HELPER.createBlock("gloomy_tile_stairs", () -> new AbnormalsStairsBlock(GLOOMY_TILES.get().getDefaultState(), SRProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_WALL			= HELPER.createBlock("gloomy_tile_wall", () -> new WallBlock(SRProperties.GLOOMY_TILES), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> GLOOMY_TILE_SLAB			= HELPER.createBlock("gloomy_tile_slab", () -> new SlabBlock(SRProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_VERTICAL_SLAB	= HELPER.createCompatBlock("quark", "gloomy_tile_vertical_slab", () -> new VerticalSlabBlock(SRProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> RUNED_GLOOMY_TILES		= HELPER.createBlock("runed_gloomy_tiles", () -> new RunedGloomyTilesBlock(SRProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> BLAST_PROOF_PLATES		= HELPER.createBlock("blast_proof_plates", () -> new Block(SRProperties.BLAST_PROOF_PLATES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_STAIRS		= HELPER.createBlock("blast_proof_stairs", () -> new AbnormalsStairsBlock(BLAST_PROOF_PLATES.get().getDefaultState(), SRProperties.BLAST_PROOF_PLATES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_SLAB			= HELPER.createBlock("blast_proof_slab", () -> new SlabBlock(SRProperties.BLAST_PROOF_PLATES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_VERTICAL_SLAB	= HELPER.createCompatBlock("quark", "blast_proof_vertical_slab", () -> new VerticalSlabBlock(SRProperties.BLAST_PROOF_PLATES), ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> POTTED_CREEPIE			= HELPER.createBlockNoItem("potted_creeper_spores", () -> new ImprovedFlowerPotBlock(SRItems.CREEPER_SPORES, Block.Properties.from(Blocks.FLOWER_POT)));
	public static final RegistryObject<Block> SPORE_BOMB 				= HELPER.createBlock("spore_bomb", () -> new SporeBombBlock(Block.Properties.from(Blocks.TNT)), ItemGroup.REDSTONE);
	public static final RegistryObject<Block> CREEPER_SPORE_SACK 		= HELPER.createCompatBlock("quark", "creeper_spore_sack", () -> new Block(Block.Properties.create(Material.WOOL, MaterialColor.LIME_TERRACOTTA).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);

	public static void registerFlammables() {
		DataUtils.registerFlammable(SRBlocks.SPORE_BOMB.get(), 15, 100);
	}
}
