package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.savageandravage.common.entity.MischiefArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class MischiefArrowItem extends ArrowItem {
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.SPECTRAL_ARROW);

	public MischiefArrowItem(Properties builder) {
		super(builder);
	}

	@Override
	public AbstractArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		return new MischiefArrowEntity(world, shooter);
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player) {
		return false;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
