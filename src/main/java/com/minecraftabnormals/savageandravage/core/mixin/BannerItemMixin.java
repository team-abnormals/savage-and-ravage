package com.minecraftabnormals.savageandravage.core.mixin;

import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BannerItem.class)
public abstract class BannerItemMixin extends WallOrFloorItem {

	public BannerItemMixin(Block floorBlock, Block wallBlockIn, Properties propertiesIn) {
		super(floorBlock, wallBlockIn, propertiesIn);
	}

	@Override
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return EquipmentSlotType.HEAD;
	}
}
