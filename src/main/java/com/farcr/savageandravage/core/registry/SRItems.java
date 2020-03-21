package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.util.RegistryUtils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, SavageAndRavage.MODID);
	
	public static RegistryObject<Item> BLAST_PROOF_PLATING = RegistryUtils.createItem("blast_proof_plating", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
	public static RegistryObject<Item> GRIEFER_HELMET = RegistryUtils.createItem("griefer_helmet", () -> new Item(new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_CHESTPLATE = RegistryUtils.createItem("griefer_chestplate", () -> new Item(new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_LEGGINGS = RegistryUtils.createItem("griefer_leggings", () -> new Item(new Item.Properties().group(ItemGroup.COMBAT)));
	public static RegistryObject<Item> GRIEFER_BOOTS = RegistryUtils.createItem("griefer_boots", () -> new Item(new Item.Properties().group(ItemGroup.COMBAT)));
	
	public static RegistryObject<Item> CREEPER_SPORES = RegistryUtils.createItem("creeper_spores", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
}
