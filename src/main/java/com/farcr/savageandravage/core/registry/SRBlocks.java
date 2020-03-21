package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.util.RegistryUtils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRBlocks {
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, SavageAndRavage.MODID);

	public static RegistryObject<Block> GLOOMY_TILES                = RegistryUtils.createBlock("gloomy_tiles", () -> new Block(Block.Properties.from(Blocks.PURPUR_BLOCK)), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> GLOOMY_TILE_STAIRS 		    = RegistryUtils.createBlock("gloomy_tile_stairs", () -> new StairsBlock(GLOOMY_TILES.get().getDefaultState(), BlockProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> GLOOMY_TILE_SLAB 			= RegistryUtils.createBlock("gloomy_tile_slab", () -> new SlabBlock(BlockProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static RegistryObject<Block> GLOOMY_TILE_WALL 			= RegistryUtils.createBlock("gloomy_tile_wall", () -> new WallBlock(BlockProperties.GLOOMY_TILES), ItemGroup.DECORATIONS);
	public static RegistryObject<Block> GLOOMY_TILE_VERTICAL_SLAB   = RegistryUtils.createBlockCompat("quark", "gloomy_tile_vertical_slab", () -> new VerticalSlabBlock(BlockProperties.GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	
	public static RegistryObject<Block> CREEPER_SPORE_SACK		    = RegistryUtils.createBlockCompat("quark", "creeper_spore_sack", () -> new Block(Block.Properties.create(Material.WOOL, MaterialColor.LIME_TERRACOTTA).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
}
