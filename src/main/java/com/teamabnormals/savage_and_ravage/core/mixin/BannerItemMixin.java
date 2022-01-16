package com.teamabnormals.savage_and_ravage.core.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BannerItem.class)
public abstract class BannerItemMixin extends StandingAndWallBlockItem {

	public BannerItemMixin(Block floorBlock, Block wallBlockIn, Properties propertiesIn) {
		super(floorBlock, wallBlockIn, propertiesIn);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}
}
