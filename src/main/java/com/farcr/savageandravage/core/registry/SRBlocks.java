package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.common.block.ImprovedFlowerPotBlock;
import com.farcr.savageandravage.common.block.RunedGloomyTilesBlock;
import com.farcr.savageandravage.common.block.SporeBombBlock;
import com.farcr.savageandravage.common.block.VerticalSlabBlock;
import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.util.BlockProperties;
import com.farcr.savageandravage.core.util.RegistryUtils;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.rmi.registry.Registry;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRBlocks {
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, SavageAndRavage.MODID);

	public static RegistryObject<Block> GLOOMY_TILES                = RegistryUtils.createBlock("gloomy_tiles", () -> new Block(Block.Properties.from(Blocks.PURPUR_BLOCK)), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> CHISELED_GLOOMY_TILES       = RegistryUtils.createBlock("chiseled_gloomy_tiles", () -> new Block(BlockProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> GLOOMY_TILE_STAIRS 		    = RegistryUtils.createBlock("gloomy_tile_stairs", () -> new StairsBlock(GLOOMY_TILES.get().getDefaultState(), BlockProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> GLOOMY_TILE_WALL 			= RegistryUtils.createBlock("gloomy_tile_wall", () -> new WallBlock(BlockProperties.GLOOMY_TILES), ItemGroup.DECORATIONS);
	public static RegistryObject<Block> GLOOMY_TILE_SLAB 			= RegistryUtils.createBlock("gloomy_tile_slab", () -> new SlabBlock(BlockProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> GLOOMY_TILE_VERTICAL_SLAB   = RegistryUtils.createBlockCompat("quark", "gloomy_tile_vertical_slab", () -> new VerticalSlabBlock(BlockProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> RUNED_GLOOMY_TILES          = RegistryUtils.createBlock("runed_gloomy_tiles", () -> new RunedGloomyTilesBlock(BlockProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);

	public static RegistryObject<Block> BLAST_PROOF_PLATES          = RegistryUtils.createBlock("blast_proof_plates", () -> new Block(BlockProperties.BLAST_PROOF_PLATES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> BLAST_PROOF_STAIRS 		    = RegistryUtils.createBlock("blast_proof_stairs", () -> new StairsBlock(BLAST_PROOF_PLATES.get().getDefaultState(), BlockProperties.BLAST_PROOF_PLATES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> BLAST_PROOF_SLAB 			= RegistryUtils.createBlock("blast_proof_slab", () -> new SlabBlock(BlockProperties.BLAST_PROOF_PLATES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> BLAST_PROOF_VERTICAL_SLAB   = RegistryUtils.createBlockCompat("quark", "blast_proof_vertical_slab", () -> new VerticalSlabBlock(BlockProperties.BLAST_PROOF_PLATES), ItemGroup.BUILDING_BLOCKS);

	public static RegistryObject<Block> POTTED_CREEPIE               = RegistryUtils.createBlockNoItem("potted_creeper_spores", () -> new ImprovedFlowerPotBlock(SRItems.CREEPER_SPORES, Block.Properties.from(Blocks.FLOWER_POT)));
	public static RegistryObject<Block> SPORE_BOMB 			        = RegistryUtils.createBlock("spore_bomb", () -> new SporeBombBlock(Block.Properties.from(Blocks.TNT)), ItemGroup.REDSTONE);
	public static RegistryObject<Block> CREEPER_SPORE_SACK		    = RegistryUtils.createBlockCompat("quark", "creeper_spore_sack", () -> new Block(Block.Properties.create(Material.WOOL, MaterialColor.LIME_TERRACOTTA).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);

	
	//Imported from Buzzier Bees.
	public static void registerFlammable(Block block, int encouragement, int flammability) {
		FireBlock fire = (FireBlock) Blocks.FIRE;
		fire.setFireInfo(block, encouragement, flammability);
	}
	
	public static void registerFlammables() {
		registerFlammable(SRBlocks.SPORE_BOMB.get(), 15, 100);
	}
}
