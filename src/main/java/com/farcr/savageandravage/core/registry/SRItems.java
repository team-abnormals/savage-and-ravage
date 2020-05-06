package com.farcr.savageandravage.core.registry;

import java.util.List;

import com.farcr.savageandravage.common.item.CreeperSporesItem;
import com.farcr.savageandravage.common.item.GrieferArmorItem;
import com.farcr.savageandravage.common.item.SRArmorMaterial;
import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.util.RegistryUtils;
import com.google.common.collect.Lists;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, SavageAndRavage.MODID);
	public static final List<RegistryObject<Item>> SPAWN_EGGS = Lists.newArrayList();

	public static RegistryObject<Item> BLAST_PROOF_PLATING = RegistryUtils.createItem("blast_proof_plating", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static RegistryObject<Item> GRIEFER_HELMET = RegistryUtils.createItem("griefer_helmet", () -> new GrieferArmorItem("25", SRArmorMaterial.GRIEFER, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_CHESTPLATE = RegistryUtils.createItem("griefer_chestplate", () -> new GrieferArmorItem("30", SRArmorMaterial.GRIEFER, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_LEGGINGS = RegistryUtils.createItem("griefer_leggings", () -> new GrieferArmorItem("25", SRArmorMaterial.GRIEFER, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_BOOTS = RegistryUtils.createItem("griefer_boots", () -> new GrieferArmorItem("20", SRArmorMaterial.GRIEFER, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
	
	public static RegistryObject<Item> CREEPER_SPORES = RegistryUtils.createItem("creeper_spores", () -> new CreeperSporesItem(new Item.Properties().group(ItemGroup.MATERIALS)));

	public static RegistryObject<Item> SKELETON_VILLAGER_SPAWN_EGG = RegistryUtils.createSpawnEggItem("skeleton_villager", () -> SREntities.SKELETON_VILLAGER.get(), 11447986, 9407641, ItemGroup.MISC);

}