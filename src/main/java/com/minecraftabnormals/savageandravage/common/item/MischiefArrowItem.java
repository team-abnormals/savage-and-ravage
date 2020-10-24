package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.savageandravage.common.entity.MischiefArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MischiefArrowItem extends ArrowItem {

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
}
