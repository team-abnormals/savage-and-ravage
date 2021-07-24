package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.abnormals_core.common.items.AbnormalsSpawnEggItem;
import com.minecraftabnormals.abnormals_core.core.util.registry.ItemSubRegistryHelper;
import com.minecraftabnormals.savageandravage.common.item.*;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.other.SRTiers;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRItems {
	public static final ItemSubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> CREEPER_SPORES = HELPER.createItem("creeper_spores", () -> new CreeperSporesItem(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> MISCHIEF_ARROW = HELPER.createItem("mischief_arrow", () -> new MischiefArrowItem(new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

	public static final RegistryObject<Item> BLAST_PROOF_PLATING = HELPER.createItem("blast_proof_plating", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> GRIEFER_HELMET = HELPER.createItem("griefer_helmet", () -> new GrieferArmorItem(SRTiers.GRIEFER, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> GRIEFER_CHESTPLATE = HELPER.createItem("griefer_chestplate", () -> new GrieferArmorItem(SRTiers.GRIEFER, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> GRIEFER_LEGGINGS = HELPER.createItem("griefer_leggings", () -> new GrieferArmorItem(SRTiers.GRIEFER, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> GRIEFER_BOOTS = HELPER.createItem("griefer_boots", () -> new GrieferArmorItem(SRTiers.GRIEFER, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

	public static final RegistryObject<Item> WAND_OF_FREEZING = HELPER.createItem("wand_of_freezing", () -> new WandOfFreezingItem(new Item.Properties().durability(250).rarity(Rarity.UNCOMMON).tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> CLEAVER_OF_BEHEADING = HELPER.createItem("cleaver_of_beheading", () -> new CleaverOfBeheadingItem(SRTiers.CLEAVER, 11.0F, -3.5F, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> ELDRITCH_CONCH = HELPER.createItem("eldritch_conch", () -> new EldritchConchItem(new Item.Properties().durability(375).rarity(Rarity.UNCOMMON).tab(ItemGroup.TAB_COMBAT)));
	public static final RegistryObject<Item> MASK_OF_DISHONESTY = HELPER.createItem("mask_of_dishonesty", () -> new MaskOfDishonestyItem(SRTiers.MASK, EquipmentSlotType.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON).tab(ItemGroup.TAB_COMBAT)));

	public static final RegistryObject<AbnormalsSpawnEggItem> SKELETON_VILLAGER_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_villager", SREntities.SKELETON_VILLAGER::get, 11447986, 9407641);
	public static final RegistryObject<AbnormalsSpawnEggItem> GRIEFER_SPAWN_EGG = HELPER.createSpawnEggItem("griefer", SREntities.GRIEFER::get, 8296024, 16037892);
	public static final RegistryObject<AbnormalsSpawnEggItem> ICEOLOGER_SPAWN_EGG = HELPER.createSpawnEggItem("iceologer", SREntities.ICEOLOGER::get, 9343891, 1388394);
	public static final RegistryObject<AbnormalsSpawnEggItem> EXECUTIONER_SPAWN_EGG = HELPER.createSpawnEggItem("executioner", SREntities.EXECUTIONER::get, 0x8E9393, 0x6F484C);
	public static final RegistryObject<AbnormalsSpawnEggItem> TRICKSTER_SPAWN_EGG = HELPER.createSpawnEggItem("trickster", SREntities.TRICKSTER::get, 0x8E9393, 0x6F484C);

	public static void registerItemProperties() {
		ItemModelsProperties.register(Items.CROSSBOW, new ResourceLocation(SavageAndRavage.MOD_ID, "mischief_arrow"), (stack, world, entity) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, SRItems.MISCHIEF_ARROW.get()) ? 1.0F : 0.0F);
		ItemModelsProperties.register(Items.CROSSBOW, new ResourceLocation("spectral_arrow"), (stack, world, entity) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, Items.SPECTRAL_ARROW) ? 1.0F : 0.0F);
	}
}