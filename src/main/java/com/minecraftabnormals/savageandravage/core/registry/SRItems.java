package com.minecraftabnormals.savageandravage.core.registry;

import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;
import com.minecraftabnormals.savageandravage.common.item.CreeperSporesItem;
import com.minecraftabnormals.savageandravage.common.item.GrieferArmorItem;
import com.minecraftabnormals.savageandravage.common.item.SRArmorMaterial;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRItems {
	public static final RegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER;

	public static final RegistryObject<Item> CREEPER_SPORES 		= HELPER.createItem("creeper_spores", () -> new CreeperSporesItem(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> BLAST_PROOF_PLATING 	= HELPER.createItem("blast_proof_plating", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> GRIEFER_HELMET 		= HELPER.createItem("griefer_helmet", () -> new GrieferArmorItem(25, SRArmorMaterial.GRIEFER, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> GRIEFER_CHESTPLATE 	= HELPER.createItem("griefer_chestplate", () -> new GrieferArmorItem(30, SRArmorMaterial.GRIEFER, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> GRIEFER_LEGGINGS 		= HELPER.createItem("griefer_leggings", () -> new GrieferArmorItem(25, SRArmorMaterial.GRIEFER, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
	public static final RegistryObject<Item> GRIEFER_BOOTS 			= HELPER.createItem("griefer_boots", () -> new GrieferArmorItem(20, SRArmorMaterial.GRIEFER, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
	
	public static final RegistryObject<Item> SKELETON_VILLAGER_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_villager", SREntities.SKELETON_VILLAGER::get, 11447986, 9407641);
	public static final RegistryObject<Item> GRIEFER_SPAWN_EGG = HELPER.createSpawnEggItem("griefer", SREntities.GRIEFER::get, 8296024, 16037892);

}