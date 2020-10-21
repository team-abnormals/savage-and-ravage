package com.minecraftabnormals.savageandravage.common.block;

import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.teamabnormals.abnormals_core.core.utils.ItemStackUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SporeBombBlock extends TNTBlock {
    public SporeBombBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) {
        SporeBombEntity sporebomb = new SporeBombEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter);
        world.addEntity(sporebomb);
        world.playSound(null, sporebomb.getPosX(), sporebomb.getPosY(), sporebomb.getPosZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.BLOCKS, 1.0F, 0.5F);
    }

    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosionIn) {
        SporeBombEntity sporebomb = new SporeBombEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosionIn.getExplosivePlacedBy());
        sporebomb.setFuse((short) (world.getRandom().nextInt(sporebomb.getFuse() / 4) + sporebomb.getFuse() / 8));
        world.addEntity(sporebomb);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (world.isBlockPowered(pos)) {
            this.catchFire(state, world, pos, null, null);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (ItemStackUtils.isInGroup(this.asItem(), group)) {
            int targetIndex = ItemStackUtils.findIndexOfItem(Items.TNT, items);
            if (targetIndex != -1) {
                items.add(targetIndex + 1, new ItemStack(this));
            } else {
                super.fillItemGroup(group, items);
            }
        }
    }
}
