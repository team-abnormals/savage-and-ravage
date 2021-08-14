package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public class EldritchConchItem extends Item {
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.TOTEM_OF_UNDYING);

	public EldritchConchItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		int standingOnY = MathHelper.floor(player.getY()) - 1;
		double headY = player.getY() + 1.0D;
		float yawRadians = (float) (Math.toRadians(90 + player.yRot));
		boolean hasSucceeded = false;
		if (player.xRot > 70) {
			for (int i = 0; i < 5; i++) {
				float rotatedYaw = yawRadians + (float) i * (float) Math.PI * 0.4F;
				if (this.spawnFangs(player.getX() + (double) MathHelper.cos(rotatedYaw) * 1.5D, headY, player.getZ() + (double) MathHelper.sin(rotatedYaw) * 1.5D, standingOnY, rotatedYaw, 0, world, player))
					hasSucceeded = true;
			}
			for (int k = 0; k < 8; k++) {
				float rotatedYaw = yawRadians + (float) k * (float) Math.PI * 2.0F / 8.0F + 1.2566371F;
				if (this.spawnFangs(player.getX() + (double) MathHelper.cos(rotatedYaw) * 2.5D, headY, player.getZ() + (double) MathHelper.sin(rotatedYaw) * 2.5D, standingOnY, rotatedYaw, 3, world, player))
					hasSucceeded = true;
			}
		} else {
			for (int l = 0; l < 16; l++) {
				double d2 = 1.25D * (double) (l + 1);
				if (this.spawnFangs(player.getX() + (double) MathHelper.cos(yawRadians) * d2, headY, player.getZ() + (double) MathHelper.sin(yawRadians) * d2, standingOnY, yawRadians, l, world, player))
					hasSucceeded = true;
			}
		}
		ItemStack stack = player.getItemInHand(hand);
		if (hasSucceeded) {
			player.playSound(SRSounds.GENERIC_PREPARE_ATTACK.get(), 1.0F, 1.0F);
			player.getCooldowns().addCooldown(this, 60);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
			return ActionResult.success(stack);
		}
		return ActionResult.pass(stack);
	}

	private boolean spawnFangs(double x, double y, double z, int lowestYCheck, float rotationYaw, int warmupDelayTicks, World world, PlayerEntity player) {
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
			world.addFreshEntity(new EvokerFangsEntity(world, x, (double) pos.getY() + yCorrection, z, rotationYaw, warmupDelayTicks, player));
			return true;
		}
		return false;
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
