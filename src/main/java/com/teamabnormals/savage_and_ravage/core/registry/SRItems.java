package com.teamabnormals.savage_and_ravage.core.registry;

import com.teamabnormals.savage_and_ravage.common.item.CleaverOfBeheadingItem;
import com.teamabnormals.savage_and_ravage.common.item.ConchOfConjuringItem;
import com.teamabnormals.savage_and_ravage.common.item.CreeperSporesItem;
import com.teamabnormals.savage_and_ravage.common.item.GrieferArmorItem;
import com.teamabnormals.savage_and_ravage.common.item.MaskOfDishonestyItem;
import com.teamabnormals.savage_and_ravage.common.item.MischiefArrowItem;
import com.teamabnormals.savage_and_ravage.common.item.WandOfFreezingItem;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRTiers;
import com.teamabnormals.blueprint.common.item.BlueprintSpawnEggItem;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

	public static final RegistryObject<BlueprintSpawnEggItem> SKELETON_VILLAGER_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_villager", SREntities.SKELETON_VILLAGER::get, 0xAEAEB2, 0x8F8C99);
	public static final RegistryObject<BlueprintSpawnEggItem> GRIEFER_SPAWN_EGG = HELPER.createSpawnEggItem("griefer", SREntities.GRIEFER::get, 0x7E9658, 0xF4B804);
	public static final RegistryObject<BlueprintSpawnEggItem> ICEOLOGER_SPAWN_EGG = HELPER.createSpawnEggItem("iceologer", SREntities.ICEOLOGER::get, 0x8E9393, 0x152F6A);
	public static final RegistryObject<BlueprintSpawnEggItem> EXECUTIONER_SPAWN_EGG = HELPER.createSpawnEggItem("executioner", SREntities.EXECUTIONER::get, 0x8E9393, 0x51272D);
	public static final RegistryObject<BlueprintSpawnEggItem> TRICKSTER_SPAWN_EGG = HELPER.createSpawnEggItem("trickster", SREntities.TRICKSTER::get, 0x617743, 0x734B4B);

	public static void registerItemProperties() {
		ItemProperties.register(Items.CROSSBOW, new ResourceLocation(SavageAndRavage.MOD_ID, "mischief_arrow"), (stack, world, entity, integer) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, SRItems.MISCHIEF_ARROW.get()) ? 1.0F : 0.0F);
		ItemProperties.register(Items.CROSSBOW, new ResourceLocation("spectral_arrow"), (stack, world, entity, integer) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, Items.SPECTRAL_ARROW) ? 1.0F : 0.0F);
	}
}