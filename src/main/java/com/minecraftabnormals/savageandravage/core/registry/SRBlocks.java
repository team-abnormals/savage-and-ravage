package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.abnormals_core.common.blocks.AbnormalsStairsBlock;
import com.minecraftabnormals.abnormals_core.common.blocks.VerticalSlabBlock;
import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.abnormals_core.core.util.registry.BlockSubRegistryHelper;
import com.minecraftabnormals.savageandravage.common.block.ChiseledGloomyTilesBlock;
import com.minecraftabnormals.savageandravage.common.block.PottedCreeperSporesBlock;
import com.minecraftabnormals.savageandravage.common.block.RunedGloomyTilesBlock;
import com.minecraftabnormals.savageandravage.common.block.SporeBombBlock;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRBlocks {
	public static final BlockSubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getBlockSubHelper();

	public static final TargetedItemGroupFiller CRACKED_POLISHED_BLACKSTONE_BRICKS  = new TargetedItemGroupFiller(() -> Items.CRACKED_POLISHED_BLACKSTONE_BRICKS);
	private static final TargetedItemGroupFiller POLISHED_BLACKSTONE_BRICK_WALL     = new TargetedItemGroupFiller(() -> Items.POLISHED_BLACKSTONE_BRICK_WALL);
	private static final TargetedItemGroupFiller GUNPOWDER_SACK                     = new TargetedItemGroupFiller(() -> ForgeRegistries.ITEMS.getValue(new ResourceLocation("quark", "gunpowder_sack"))); //Doesn't seem to insert after, may need to look into

	public static final RegistryObject<Block> GLOOMY_TILES              = HELPER.createBlock("gloomy_tiles", () -> new Block(Properties.GLOOMY_TILES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {CRACKED_POLISHED_BLACKSTONE_BRICKS.fillItem(this.asItem(), group, items);} }, ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> CHISELED_GLOOMY_TILES     = HELPER.createBlock("chiseled_gloomy_tiles", () -> new ChiseledGloomyTilesBlock(Properties.LIGHTABLE_GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_STAIRS        = HELPER.createBlock("gloomy_tile_stairs", () -> new AbnormalsStairsBlock(GLOOMY_TILES.get().getDefaultState(), Properties.GLOOMY_TILES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {CRACKED_POLISHED_BLACKSTONE_BRICKS.fillItem(this.asItem(), group, items);}}, ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_WALL          = HELPER.createBlock("gloomy_tile_wall", () -> new WallBlock(Properties.GLOOMY_TILES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {POLISHED_BLACKSTONE_BRICK_WALL.fillItem(this.asItem(), group, items);}}, ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> GLOOMY_TILE_SLAB          = HELPER.createBlock("gloomy_tile_slab", () -> new SlabBlock(Properties.GLOOMY_TILES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {CRACKED_POLISHED_BLACKSTONE_BRICKS.fillItem(this.asItem(), group, items);}}, ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> GLOOMY_TILE_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "gloomy_tile_vertical_slab", () -> new VerticalSlabBlock(Properties.GLOOMY_TILES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {CRACKED_POLISHED_BLACKSTONE_BRICKS.fillItem(this.asItem(), group, items);}}, ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> RUNED_GLOOMY_TILES        = HELPER.createBlock("runed_gloomy_tiles", () -> new RunedGloomyTilesBlock(Properties.LIGHTABLE_GLOOMY_TILES), ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> BLAST_PROOF_PLATES        = HELPER.createBlock("blast_proof_plates", () -> new Block(Properties.BLAST_PROOF_PLATES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {CRACKED_POLISHED_BLACKSTONE_BRICKS.fillItem(this.asItem(), group, items);}}, ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_STAIRS        = HELPER.createBlock("blast_proof_stairs", () -> new AbnormalsStairsBlock(BLAST_PROOF_PLATES.get().getDefaultState(), Properties.BLAST_PROOF_PLATES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {CRACKED_POLISHED_BLACKSTONE_BRICKS.fillItem(this.asItem(), group, items);}}, ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_SLAB          = HELPER.createBlock("blast_proof_slab", () -> new SlabBlock(Properties.BLAST_PROOF_PLATES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {CRACKED_POLISHED_BLACKSTONE_BRICKS.fillItem(this.asItem(), group, items);}}, ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> BLAST_PROOF_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "blast_proof_vertical_slab", () -> new VerticalSlabBlock(Properties.BLAST_PROOF_PLATES) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {CRACKED_POLISHED_BLACKSTONE_BRICKS.fillItem(this.asItem(), group, items);}}, ItemGroup.BUILDING_BLOCKS);

	public static final RegistryObject<Block> POTTED_CREEPER_SPORES     = HELPER.createBlockNoItem("potted_creeper_spores", () -> new PottedCreeperSporesBlock(SRItems.CREEPER_SPORES, Block.Properties.from(Blocks.FLOWER_POT)));
	public static final RegistryObject<Block> SPORE_BOMB                = HELPER.createBlock("spore_bomb", () -> new SporeBombBlock(Block.Properties.from(Blocks.TNT)), ItemGroup.REDSTONE);
	public static final RegistryObject<Block> CREEPER_SPORE_SACK        = HELPER.createCompatBlock("quark", "creeper_spore_sack", () -> new Block(Block.Properties.create(Material.WOOL, MaterialColor.LIME_TERRACOTTA).hardnessAndResistance(0.5F).sound(SoundType.CLOTH)) {public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {if(ModList.get().isLoaded("quark")) GUNPOWDER_SACK.fillItem(this.asItem(), group, items);}}, ItemGroup.DECORATIONS);

	public static class Properties {
		public static final Block.Properties GLOOMY_TILES           = Block.Properties.create(Material.ROCK, MaterialColor.LIGHT_BLUE_TERRACOTTA).setRequiresTool().hardnessAndResistance(1.5F, 6.0F);
		public static final Block.Properties LIGHTABLE_GLOOMY_TILES = Block.Properties.create(Material.ROCK, MaterialColor.LIGHT_BLUE_TERRACOTTA).setRequiresTool().hardnessAndResistance(1.5F, 6.0F).setLightLevel(getLightValuePowered(7));
		public static final Block.Properties BLAST_PROOF_PLATES     = Block.Properties.create(Material.IRON, MaterialColor.GREEN).setRequiresTool().hardnessAndResistance(2.0F, 1200.0F).sound(SoundType.METAL);

		public static ToIntFunction<BlockState> getLightValuePowered(int lightValue) {
			return (stateHolder) -> stateHolder.get(BlockStateProperties.POWERED) ? lightValue : 0;
		}
	}
}
