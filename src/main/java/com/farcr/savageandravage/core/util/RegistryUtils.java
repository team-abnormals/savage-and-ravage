package com.farcr.savageandravage.core.util;

import com.farcr.savageandravage.core.registry.SRBlocks;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;
import com.google.common.base.Supplier;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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

	public static <E extends EntityType<?>> RegistryObject<E> createEntity(String name, Supplier<? extends E> supplier) {
		RegistryObject<E> entity = SREntities.ENTITIES.register(name, supplier);
		return entity;
	}
}
