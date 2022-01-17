package com.teamabnormals.savage_and_ravage.common.block;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.savage_and_ravage.common.entity.item.SporeBomb;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SporeBombBlock extends TntBlock {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.TNT);

	public SporeBombBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
		SporeBomb sporebomb = new SporeBomb(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter);
		world.addFreshEntity(sporebomb);
		world.playSound(null, sporebomb.getX(), sporebomb.getY(), sporebomb.getZ(), SoundEvents.CREEPER_PRIMED, SoundSource.BLOCKS, 1.0F, 0.5F);
	}

	@Override
	public void wasExploded(Level world, BlockPos pos, Explosion explosionIn) {
		SporeBomb sporebomb = new SporeBomb(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosionIn.getSourceMob());
		sporebomb.setFuse((short) (world.getRandom().nextInt(sporebomb.getFuse() / 4) + sporebomb.getFuse() / 8));
		world.addFreshEntity(sporebomb);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (world.hasNeighborSignal(pos)) {
			this.onCaughtFire(state, world, pos, null, null);
			world.removeBlock(pos, false);
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this.asItem(), group, items);
	}
}
