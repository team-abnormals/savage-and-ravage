package com.teamabnormals.savage_and_ravage.core.registry;

import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamabnormals.savage_and_ravage.common.item.*;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRTiers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SRItems {
	public static final ItemSubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> CREEPER_SPORES = HELPER.createItem("creeper_spores", () -> new CreeperSporesItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> MISCHIEF_ARROW = HELPER.createItem("mischief_arrow", () -> new MischiefArrowItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

	public static final RegistryObject<Item> BLAST_PROOF_PLATING = HELPER.createItem("blast_proof_plating", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> GRIEFER_HELMET = HELPER.createItem("griefer_helmet", () -> new GrieferArmorItem(SRTiers.GRIEFER, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> GRIEFER_CHESTPLATE = HELPER.createItem("griefer_chestplate", () -> new GrieferArmorItem(SRTiers.GRIEFER, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> GRIEFER_LEGGINGS = HELPER.createItem("griefer_leggings", () -> new GrieferArmorItem(SRTiers.GRIEFER, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> GRIEFER_BOOTS = HELPER.createItem("griefer_boots", () -> new GrieferArmorItem(SRTiers.GRIEFER, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

	public static final RegistryObject<Item> WAND_OF_FREEZING = HELPER.createItem("wand_of_freezing", () -> new WandOfFreezingItem(new Item.Properties().durability(250).rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> CLEAVER_OF_BEHEADING = HELPER.createItem("cleaver_of_beheading", () -> new CleaverOfBeheadingItem(SRTiers.CLEAVER, 11.0F, -3.4F, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> CONCH_OF_CONJURING = HELPER.createItem("conch_of_conjuring", () -> new ConchOfConjuringItem(new Item.Properties().durability(375).rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> MASK_OF_DISHONESTY = HELPER.createItem("mask_of_dishonesty", () -> new MaskOfDishonestyItem(SRTiers.MASK, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));

	public static final RegistryObject<ForgeSpawnEggItem> SKELETON_VILLAGER_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_villager", SREntityTypes.SKELETON_VILLAGER::get, 0xAEAEB2, 0x8F8C99);
	public static final RegistryObject<ForgeSpawnEggItem> GRIEFER_SPAWN_EGG = HELPER.createSpawnEggItem("griefer", SREntityTypes.GRIEFER::get, 0x7E9658, 0xF4B804);
	public static final RegistryObject<ForgeSpawnEggItem> ICEOLOGER_SPAWN_EGG = HELPER.createSpawnEggItem("iceologer", SREntityTypes.ICEOLOGER::get, 0x8E9393, 0x152F6A);
	public static final RegistryObject<ForgeSpawnEggItem> EXECUTIONER_SPAWN_EGG = HELPER.createSpawnEggItem("executioner", SREntityTypes.EXECUTIONER::get, 0x8E9393, 0x51272D);
	public static final RegistryObject<ForgeSpawnEggItem> TRICKSTER_SPAWN_EGG = HELPER.createSpawnEggItem("trickster", SREntityTypes.TRICKSTER::get, 0x617743, 0x734B4B);

	public static void registerItemProperties() {
		ItemProperties.register(Items.CROSSBOW, new ResourceLocation(SavageAndRavage.MOD_ID, "mischief_arrow"), (stack, world, entity, integer) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, SRItems.MISCHIEF_ARROW.get()) ? 1.0F : 0.0F);
		ItemProperties.register(Items.CROSSBOW, new ResourceLocation(SavageAndRavage.MOD_ID, "spectral_arrow"), (stack, world, entity, integer) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, Items.SPECTRAL_ARROW) ? 1.0F : 0.0F);
	}
}