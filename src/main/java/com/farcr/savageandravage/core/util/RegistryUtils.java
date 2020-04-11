package com.farcr.savageandravage.core.util;

import com.farcr.savageandravage.common.item.SRSpawnEgg;
import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.registry.SRBlocks;
import com.farcr.savageandravage.core.registry.SRItems;
import com.farcr.savageandravage.core.registry.SRSounds;
import com.google.common.base.Supplier;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;

public class RegistryUtils {
		
	public static <I extends Item> RegistryObject<I> createItem(String name, Supplier<? extends I> supplier) {
		RegistryObject<I> item = SRItems.ITEMS.register(name, supplier);
		return item;
	}
	
	public static BlockItem createSimpleItemBlock(Block block, ItemGroup itemGroup) {
        return (BlockItem) new BlockItem(block, new Item.Properties().group(itemGroup)).setRegistryName(block.getRegistryName());
    }

    //Imported from Buzzier Bees
	public static RegistryObject<Item> createSpawnEggItem(String entityName, Supplier<EntityType<?>> supplier, int primaryColor, int secondaryColor, ItemGroup group) {
		RegistryObject<Item> spawnEgg = SRItems.ITEMS.register(entityName + "_spawn_egg", () -> new SRSpawnEgg(supplier, primaryColor, secondaryColor, new Item.Properties().group(group)));
		SRItems.SPAWN_EGGS.add(spawnEgg);
		return spawnEgg;
	}
	
	public static <B extends Block> RegistryObject<B> createBlock(String name, Supplier<? extends B> supplier, ItemGroup itemGroup) {
        RegistryObject<B> block = SRBlocks.BLOCKS.register(name, supplier);
        SRItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(itemGroup)));
        return block;
    }
	
	public static <B extends Block> RegistryObject<B> createBlockCompat(String mod, String name, Supplier<? extends B> supplier, ItemGroup itemGroup) {
		ItemGroup determinedGroup = ModList.get().isLoaded(mod) || mod == "indev" ? itemGroup : null;
		RegistryObject<B> block = SRBlocks.BLOCKS.register(name, supplier);
		SRItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(determinedGroup)));
		return block;		
    }

    public static <B extends Block> RegistryObject<B> createBlockNoItem(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = SRBlocks.BLOCKS.register(name, supplier);
        return block;
    }
    
    public static <I extends Item> RegistryObject<I> createCompatItem(String mod, String name, Supplier<? extends I> compat_supplier, Supplier<? extends I> supplier) {
    	Supplier<? extends I> determinedSupplier = ModList.get().isLoaded(mod) ? compat_supplier : supplier;
    	RegistryObject<I> item = SRItems.ITEMS.register(name, determinedSupplier);
		return item;
	}
}
