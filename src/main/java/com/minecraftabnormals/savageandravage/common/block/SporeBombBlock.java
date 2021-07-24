package com.minecraftabnormals.savageandravage.common.block;

import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
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
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.TNT);

	public SporeBombBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) {
		SporeBombEntity sporebomb = new SporeBombEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter);
		world.addFreshEntity(sporebomb);
		world.playSound(null, sporebomb.getX(), sporebomb.getY(), sporebomb.getZ(), SoundEvents.CREEPER_PRIMED, SoundCategory.BLOCKS, 1.0F, 0.5F);
	}

	@Override
	public void wasExploded(World world, BlockPos pos, Explosion explosionIn) {
		SporeBombEntity sporebomb = new SporeBombEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosionIn.getSourceMob());
		sporebomb.setFuse((short) (world.getRandom().nextInt(sporebomb.getLife() / 4) + sporebomb.getLife() / 8));
		world.addFreshEntity(sporebomb);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (world.hasNeighborSignal(pos)) {
			this.catchFire(state, world, pos, null, null);
			world.removeBlock(pos, false);
		}
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this.asItem(), group, items);
	}
}
