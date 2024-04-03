package com.teamabnormals.savage_and_ravage.core.registry;

import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.savage_and_ravage.common.block.ChiseledGloomyTilesBlock;
import com.teamabnormals.savage_and_ravage.common.block.PottedCreeperSporesBlock;
import com.teamabnormals.savage_and_ravage.common.block.RunedGloomyTilesBlock;
import com.teamabnormals.savage_and_ravage.common.block.SporeBombBlock;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

@EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SRBlocks {
	public static final BlockSubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getBlockSubHelper();

	public static final RegistryObject<Block> GLOOMY_TILES = HELPER.createBlock("gloomy_tiles", () -> new Block(SRProperties.GLOOMY_TILES));
	public static final RegistryObject<Block> CHISELED_GLOOMY_TILES = HELPER.createBlock("chiseled_gloomy_tiles", () -> new ChiseledGloomyTilesBlock(SRProperties.LIGHTABLE_GLOOMY_TILES));
	public static final RegistryObject<Block> GLOOMY_TILE_STAIRS = HELPER.createBlock("gloomy_tile_stairs", () -> new StairBlock(() -> GLOOMY_TILES.get().defaultBlockState(), SRProperties.GLOOMY_TILES));
	public static final RegistryObject<Block> GLOOMY_TILE_WALL = HELPER.createBlock("gloomy_tile_wall", () -> new WallBlock(SRProperties.GLOOMY_TILES));
	public static final RegistryObject<Block> GLOOMY_TILE_SLAB = HELPER.createBlock("gloomy_tile_slab", () -> new SlabBlock(SRProperties.GLOOMY_TILES));
	public static final RegistryObject<Block> RUNED_GLOOMY_TILES = HELPER.createBlock("runed_gloomy_tiles", () -> new RunedGloomyTilesBlock(SRProperties.LIGHTABLE_GLOOMY_TILES));

	public static final RegistryObject<Block> BLAST_PROOF_PLATES = HELPER.createBlock("blast_proof_plates", () -> new Block(SRProperties.BLAST_PROOF_PLATES));
	public static final RegistryObject<Block> BLAST_PROOF_STAIRS = HELPER.createBlock("blast_proof_stairs", () -> new StairBlock(() -> BLAST_PROOF_PLATES.get().defaultBlockState(), SRProperties.BLAST_PROOF_PLATES));
	public static final RegistryObject<Block> BLAST_PROOF_SLAB = HELPER.createBlock("blast_proof_slab", () -> new SlabBlock(SRProperties.BLAST_PROOF_PLATES));

	public static final RegistryObject<Block> POTTED_CREEPER_SPORES = HELPER.createBlockNoItem("potted_creeper_spores", () -> new PottedCreeperSporesBlock(SRItems.CREEPER_SPORES, Block.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<Block> SPORE_BOMB = HELPER.createBlock("spore_bomb", () -> new SporeBombBlock(Block.Properties.copy(Blocks.TNT)));
	public static final RegistryObject<Block> CREEPER_SPORE_SACK = HELPER.createBlock("creeper_spore_sack", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GREEN).strength(0.5F).sound(SoundType.WOOL)));

	public static void setupTabEditors() {
		CreativeModeTabContentsPopulator.mod(SavageAndRavage.MOD_ID)
				.tab(BUILDING_BLOCKS)
				.addItemsBefore(of(Blocks.BRICKS), GLOOMY_TILES, RUNED_GLOOMY_TILES, GLOOMY_TILE_STAIRS, GLOOMY_TILE_SLAB, GLOOMY_TILE_WALL, CHISELED_GLOOMY_TILES)
				.addItems(BLAST_PROOF_PLATES, BLAST_PROOF_STAIRS, BLAST_PROOF_SLAB)
				.tab(NATURAL_BLOCKS)
				.addItemsAfter(modLoaded(Blocks.PEARLESCENT_FROGLIGHT, "quark"), CREEPER_SPORE_SACK)
				.tab(COMBAT)
				.addItemsAfter(of(Items.TNT), SPORE_BOMB)
				.tab(REDSTONE_BLOCKS)
				.addItemsAfter(of(Items.TNT), SPORE_BOMB);
	}

	public static Predicate<ItemStack> modLoaded(ItemLike item, String... modids) {
		return stack -> of(item).test(stack) && BlockSubRegistryHelper.areModsLoaded(modids);
	}

	public static class SRProperties {
		public static final Block.Properties GLOOMY_TILES = Block.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).sound(SoundType.STONE);
		public static final Block.Properties LIGHTABLE_GLOOMY_TILES = Block.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).lightLevel(getLightValuePowered(7)).sound(SoundType.STONE);
		public static final Block.Properties BLAST_PROOF_PLATES = Block.Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(3.0F, 1200.0F).sound(SoundType.METAL);

		public static ToIntFunction<BlockState> getLightValuePowered(int lightValue) {
			return (stateHolder) -> stateHolder.getValue(BlockStateProperties.POWERED) ? lightValue : 0;
		}
	}
}
