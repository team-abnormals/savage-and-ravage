package com.minecraftabnormals.savageandravage.common.entity.goals;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.TargetBlock;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class AttackTargetBlockRandomlyGoal<T extends MobEntity & ICrossbowUser> extends Goal {
	private final T goalOwner;
	private int focusTime;
	private int timeTillNextShoot;
	private BlockPos nearestTargetPos;
	private CrossbowState state = CrossbowState.UNCHARGED;

	public AttackTargetBlockRandomlyGoal(T mobIn) {
		this.goalOwner = mobIn;
	}

	@Override
	public boolean shouldExecute() {
		if (this.goalOwner.getRNG().nextFloat() < 0.005F && this.goalOwner.getAttackTarget() == null) {
			World world = this.goalOwner.world;
			BlockPos.Mutable checkingPos = new BlockPos.Mutable();
			for (double x = this.goalOwner.getPosX() - 15; x <= this.goalOwner.getPosX() + 15; x++) {
				for (double y = this.goalOwner.getPosY() - 5; y <= this.goalOwner.getPosY() + 5; y++) {
					for (double z = this.goalOwner.getPosZ() - 15; z <= this.goalOwner.getPosZ() + 15; z++) {
						checkingPos.setPos(x, y, z);
						if (world.getBlockState(checkingPos).getBlock() instanceof TargetBlock) {
							this.nearestTargetPos = checkingPos.toImmutable();
							this.focusTime = 200 + this.goalOwner.getRNG().nextInt(70);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.focusTime > 0 && this.goalOwner.getAttackTarget() == null;
	}

	@Override
	public void startExecuting() {
		this.timeTillNextShoot = goalOwner.getRNG().nextInt(15) + 15;
	}

	@Override
	public void tick() {
		this.focusTime--;
		this.timeTillNextShoot--;
		this.goalOwner.getNavigator().clearPath();
		ItemStack activeStack = this.goalOwner.getActiveItemStack();
		this.goalOwner.getLookController().setLookPosition(nearestTargetPos.getX(), nearestTargetPos.getY() + 1, nearestTargetPos.getZ());
		if (this.timeTillNextShoot <= 0 && this.state == CrossbowState.UNCHARGED) {
			this.goalOwner.setActiveHand(ProjectileHelper.getHandWith(this.goalOwner, Items.CROSSBOW));
			this.state = CrossbowState.CHARGING;
			this.goalOwner.setCharging(true);
			this.timeTillNextShoot = goalOwner.getRNG().nextInt(25) + 40;
		} else if (this.state == CrossbowState.CHARGING) {
			if (!this.goalOwner.isHandActive()) {
				this.state = CrossbowState.UNCHARGED;
			}

			int i = this.goalOwner.getItemInUseMaxCount();
			if (i >= CrossbowItem.getChargeTime(activeStack) || CrossbowItem.isCharged(activeStack)) {
				this.goalOwner.stopActiveHand();
				this.state = CrossbowState.CHARGED;
				this.goalOwner.setCharging(false);
			}
		} else if (this.state == CrossbowState.CHARGED && this.goalOwner.getAttackTarget() == null && this.timeTillNextShoot == 0) {
			Hand hand = ProjectileHelper.getHandWith(this.goalOwner, Items.CROSSBOW);
			ItemStack heldStack = this.goalOwner.getHeldItem(hand);
			this.shootArrow(heldStack);
			CrossbowItem.setCharged(heldStack, false);
			this.state = CrossbowState.UNCHARGED;
		}
	}

	private void shootArrow(ItemStack stack) {
		List<ItemStack> stackList = this.getChargedProjectilesFromStack(stack);
		for (int i = 0; i < stackList.size(); ++i) {
			ItemStack chosenProjectile = stackList.get(i);
			Vector3d vector3d1 = goalOwner.getUpVector(1.0F);
			ArrowEntity arrow = new ArrowEntity(this.goalOwner.getEntityWorld(), goalOwner);
			FireworkRocketEntity rocket = new FireworkRocketEntity(this.goalOwner.getEntityWorld(), chosenProjectile, this.goalOwner, goalOwner.getPosX(), goalOwner.getPosYEye() - (double) 0.15F, goalOwner.getPosZ(), true);
			ProjectileEntity projectile = chosenProjectile.getItem() == Items.FIREWORK_ROCKET ? rocket : arrow;
			Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), 1.0F, true);
			Vector3d vector3d = this.goalOwner.getLook(1.0F);
			Vector3f vector3f = new Vector3f(vector3d);
			vector3f.transform(quaternion);
			this.goalOwner.getEntityWorld().addEntity(projectile);
			projectile.shoot((double) vector3f.getX(), (double) vector3f.getY(), (double) vector3f.getZ(), 1.6F, 0.0F);
			CompoundNBT compoundnbt = stack.getTag();
			if (compoundnbt != null) {
				ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 9);
				listnbt.clear();
				compoundnbt.put("ChargedProjectiles", listnbt);
			}
		}

	}

	private List<ItemStack> getChargedProjectilesFromStack(ItemStack stack) {
		List<ItemStack> list = Lists.newArrayList();
		CompoundNBT compoundnbt = stack.getTag();
		if (compoundnbt != null && compoundnbt.contains("ChargedProjectiles", 9)) {
			ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 10);
			if (listnbt != null) {
				for (int i = 0; i < listnbt.size(); ++i) {
					CompoundNBT compoundnbt1 = listnbt.getCompound(i);
					list.add(ItemStack.read(compoundnbt1));
				}
			}
		}

		return list;
	}

	@Override
	public void resetTask() {
		if (this.goalOwner.isHandActive()) {
			this.goalOwner.resetActiveHand();
			this.goalOwner.setCharging(false);
		}
	}

	enum CrossbowState {
		UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK
	}
}
