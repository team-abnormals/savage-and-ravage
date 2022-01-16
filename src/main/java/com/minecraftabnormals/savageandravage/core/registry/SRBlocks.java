package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.common.block.ChiseledGloomyTilesBlock;
import com.minecraftabnormals.savageandravage.common.block.PottedCreeperSporesBlock;
import com.minecraftabnormals.savageandravage.common.block.RunedGloomyTilesBlock;
import com.minecraftabnormals.savageandravage.common.block.SporeBombBlock;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.teamabnormals.blueprint.common.block.VerticalSlabBlock;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.ToIntFunction;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRBlocks {
	public static final BlockSubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getBlockSubHelper();

	public static final RegistryObject<Block> GLOOMY_TILES = HELPER.createBlock("gloomy_tiles", () -> new Block(Properties.GLOOMY_TILES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> CHISELED_GLOOMY_TILES = HELPER.createBlock("chiseled_gloomy_tiles", () -> new ChiseledGloomyTilesBlock(Properties.LIGHTABLE_GLOOMY_TILES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_STAIRS = HELPER.createBlock("gloomy_tile_stairs", () -> new StairBlock(() -> GLOOMY_TILES.get().defaultBlockState(), Properties.GLOOMY_TILES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_WALL = HELPER.createBlock("gloomy_tile_wall", () -> new WallBlock(Properties.GLOOMY_TILES), CreativeModeTab.TAB_DECORATIONS);
	public static final RegistryObject<Block> GLOOMY_TILE_SLAB = HELPER.createBlock("gloomy_tile_slab", () -> new SlabBlock(Properties.GLOOMY_TILES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "gloomy_tile_vertical_slab", () -> new VerticalSlabBlock(Properties.GLOOMY_TILES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> RUNED_GLOOMY_TILES = HELPER.createBlock("runed_gloomy_tiles", () -> new RunedGloomyTilesBlock(Properties.LIGHTABLE_GLOOMY_TILES), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> BLAST_PROOF_PLATES = HELPER.createBlock("blast_proof_plates", () -> new Block(Properties.BLAST_PROOF_PLATES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_STAIRS = HELPER.createBlock("blast_proof_stairs", () -> new StairBlock(() -> BLAST_PROOF_PLATES.get().defaultBlockState(), Properties.BLAST_PROOF_PLATES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_SLAB = HELPER.createBlock("blast_proof_slab", () -> new SlabBlock(Properties.BLAST_PROOF_PLATES), CreativeModeTab.TAB_BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "blast_proof_vertical_slab", () -> new VerticalSlabBlock(Properties.BLAST_PROOF_PLATES), CreativeModeTab.TAB_BUILDING_BLOCKS);

	public static final RegistryObject<Block> POTTED_CREEPER_SPORES = HELPER.createBlockNoItem("potted_creeper_spores", () -> new PottedCreeperSporesBlock(SRItems.CREEPER_SPORES, Block.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<Block> SPORE_BOMB = HELPER.createBlock("spore_bomb", () -> new SporeBombBlock(Block.Properties.copy(Blocks.TNT)), CreativeModeTab.TAB_REDSTONE);
	public static final RegistryObject<Block> CREEPER_SPORE_SACK = HELPER.createCompatBlock("quark", "creeper_spore_sack", () -> new Block(Block.Properties.of(Material.WOOL, MaterialColor.TERRACOTTA_LIGHT_GREEN).strength(0.5F).sound(SoundType.WOOL)), CreativeModeTab.TAB_DECORATIONS);

	public static class Properties {
		public static final Block.Properties GLOOMY_TILES = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).sound(SoundType.STONE);
		public static final Block.Properties LIGHTABLE_GLOOMY_TILES = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).lightLevel(getLightValuePowered(7)).sound(SoundType.STONE);
		public static final Block.Properties BLAST_PROOF_PLATES = Block.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(3.0F, 1200.0F).sound(SoundType.METAL);

		public static ToIntFunction<BlockState> getLightValuePowered(int lightValue) {
			return (stateHolder) -> stateHolder.getValue(BlockStateProperties.POWERED) ? lightValue : 0;
		}
	}
}
