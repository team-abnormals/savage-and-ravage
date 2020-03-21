package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.util.RegistryUtils;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, SavageAndRavage.MODID);
	
	public static RegistryObject<Item> BLAST_PROOF_PLATING = RegistryUtils.createItem("blast_proof_plating", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static RegistryObject<Item> GRIEFER_HELMET = RegistryUtils.createItem("griefer_helmet", () -> new ArmorItem(ArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_CHESTPLATE = RegistryUtils.createItem("griefer_chestplate", () -> new ArmorItem(ArmorMaterial.IRON, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_LEGGINGS = RegistryUtils.createItem("griefer_leggings", () -> new ArmorItem(ArmorMaterial.IRON, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_BOOTS = RegistryUtils.createItem("griefer_boots", () -> new ArmorItem(ArmorMaterial.IRON, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
	
	public static RegistryObject<Item> POULTRY_FARMING_HAT = RegistryUtils.createItem("poultry_farming_hat", () -> new Item(new Item.Properties().group(ItemGroup.TOOLS	)));
	
	public static RegistryObject<Item> CREEPER_SPORES = RegistryUtils.createItem("creeper_spores", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
}
