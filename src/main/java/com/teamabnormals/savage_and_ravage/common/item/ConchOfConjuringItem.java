package com.teamabnormals.savage_and_ravage.common.item;

import com.teamabnormals.savage_and_ravage.core.registry.SRSounds;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ConchOfConjuringItem extends Item {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.TOTEM_OF_UNDYING);

	public ConchOfConjuringItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		int standingOnY = Mth.floor(player.getY()) - 1;
		double headY = player.getY() + 1.0D;
		float yawRadians = (float) (Math.toRadians(90 + player.getYRot()));
		boolean hasSucceeded = false;
		if (player.getXRot() > 70) {
			for (int i = 0; i < 5; i++) {
				float rotatedYaw = yawRadians + (float) i * (float) Math.PI * 0.4F;
				if (this.spawnFangs(player.getX() + (double) Mth.cos(rotatedYaw) * 1.5D, headY, player.getZ() + (double) Mth.sin(rotatedYaw) * 1.5D, standingOnY, rotatedYaw, 0, world, player))
					hasSucceeded = true;
			}
			for (int k = 0; k < 8; k++) {
				float rotatedYaw = yawRadians + (float) k * (float) Math.PI * 2.0F / 8.0F + 1.2566371F;
				if (this.spawnFangs(player.getX() + (double) Mth.cos(rotatedYaw) * 2.5D, headY, player.getZ() + (double) Mth.sin(rotatedYaw) * 2.5D, standingOnY, rotatedYaw, 3, world, player))
					hasSucceeded = true;
			}
		} else {
			for (int l = 0; l < 16; l++) {
				double d2 = 1.25D * (double) (l + 1);
				if (this.spawnFangs(player.getX() + (double) Mth.cos(yawRadians) * d2, headY, player.getZ() + (double) Mth.sin(yawRadians) * d2, standingOnY, yawRadians, l, world, player))
					hasSucceeded = true;
			}
		}
		ItemStack stack = player.getItemInHand(hand);
		if (hasSucceeded) {
			player.playSound(SRSounds.GENERIC_PREPARE_ATTACK.get(), 1.0F, 1.0F);
			player.getCooldowns().addCooldown(this, 60);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
			return InteractionResultHolder.success(stack);
		}
		return InteractionResultHolder.pass(stack);
	}

	private boolean spawnFangs(double x, double y, double z, int lowestYCheck, float rotationYaw, int warmupDelayTicks, Level world, Player player) {
		BlockPos pos = new BlockPos(x, y, z);
		boolean shouldSpawn = false;
		double yCorrection = 0.0D;

		do {
			BlockPos belowPos = pos.below();
			BlockState stateDown = world.getBlockState(belowPos);
			if (stateDown.isFaceSturdy(world, belowPos, Direction.UP)) {
				if (!world.isEmptyBlock(pos)) {
					BlockState state = world.getBlockState(pos);
					VoxelShape voxelshape = state.getCollisionShape(world, pos);
					if (!voxelshape.isEmpty()) {
						yCorrection = voxelshape.max(Direction.Axis.Y);
					}
				}

				shouldSpawn = true;
				break;
			}

			pos = belowPos;
		} while (pos.getY() >= lowestYCheck);

		if (shouldSpawn) {
			world.addFreshEntity(new EvokerFangs(world, x, (double) pos.getY() + yCorrection, z, rotationYaw, warmupDelayTicks, player));
			return true;
		}
		return false;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
