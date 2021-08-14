package com.minecraftabnormals.savageandravage.common.entity.goals;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.TargetBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
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

//TODO needs actual shooting, firework compat, and pathfinding
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
	public boolean canUse() {
		if (this.goalOwner.getRandom().nextFloat() < 0.005F && this.goalOwner.getTarget() == null && this.goalOwner.getMainHandItem().getItem() instanceof CrossbowItem) {
			World world = this.goalOwner.level;
			BlockPos.Mutable searchPos = new BlockPos.Mutable();
			for (double x = this.goalOwner.getX() - 16; x <= this.goalOwner.getX() + 16; x++) {
				for (double y = this.goalOwner.getY() - 8; y <= this.goalOwner.getY() + 8; y++) {
					for (double z = this.goalOwner.getZ() - 16; z <= this.goalOwner.getZ() + 16; z++) {
						searchPos.set(x, y, z);
						if (world.getBlockState(searchPos).getBlock() instanceof TargetBlock) {
							this.nearestTargetPos = searchPos.immutable();
							this.focusTime = 200 + this.goalOwner.getRandom().nextInt(70);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.focusTime > 0 && this.goalOwner.getTarget() == null;
	}



	@Override
	public void start() {
		this.timeTillNextShoot = goalOwner.getRandom().nextInt(15) + 15;
	}

	@Override
	public void tick() {
		this.focusTime--;
		this.timeTillNextShoot--;
		this.goalOwner.getNavigation().stop();
		ItemStack useStack = this.goalOwner.getUseItem();
		this.goalOwner.getLookControl().setLookAt(this.nearestTargetPos.getX(), this.nearestTargetPos.getY() + 1, this.nearestTargetPos.getZ());
		if (this.timeTillNextShoot <= 0 && this.state == CrossbowState.UNCHARGED) {
			this.goalOwner.startUsingItem(ProjectileHelper.getWeaponHoldingHand(this.goalOwner, i -> i instanceof CrossbowItem));
			this.state = CrossbowState.CHARGING;
			this.goalOwner.setChargingCrossbow(true);
			this.timeTillNextShoot = goalOwner.getRandom().nextInt(25) + 40;
		} else if (this.state == CrossbowState.CHARGING) {
			if (!this.goalOwner.isUsingItem()) {
				this.state = CrossbowState.UNCHARGED;
			}

			int i = this.goalOwner.getTicksUsingItem();
			if (i >= CrossbowItem.getChargeDuration(useStack) || CrossbowItem.isCharged(useStack)) {
				this.goalOwner.stopUsingItem();
				this.state = CrossbowState.CHARGED;
				this.goalOwner.setChargingCrossbow(false);
			}
		} else if (this.state == CrossbowState.CHARGED && this.goalOwner.getTarget() == null && this.timeTillNextShoot == 0) {
			Hand hand = ProjectileHelper.getWeaponHoldingHand(this.goalOwner, i -> i instanceof CrossbowItem);
			ItemStack heldStack = this.goalOwner.getItemInHand(hand);
			this.shootArrow(heldStack);
			CrossbowItem.setCharged(heldStack, false);
			this.state = CrossbowState.UNCHARGED;
		}
	}

	private void shootArrow(ItemStack stack) {
		for (ItemStack projectileStack : this.getChargedProjectilesFromStack(stack)) {
			Vector3d vector3d1 = this.goalOwner.getUpVector(1.0F);
			ArrowEntity arrow = new ArrowEntity(this.goalOwner.level, this.goalOwner);
			FireworkRocketEntity rocket = new FireworkRocketEntity(this.goalOwner.level, projectileStack, this.goalOwner, this.goalOwner.getX(), this.goalOwner.getEyeY() - (double) 0.15F, this.goalOwner.getZ(), true);
			ProjectileEntity projectile = projectileStack.getItem() == Items.FIREWORK_ROCKET ? rocket : arrow;
			Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), 1.0F, true);
			Vector3d vector3d = this.goalOwner.getViewVector(1.0F);
			Vector3f vector3f = new Vector3f(vector3d);
			if (projectile == arrow) {
				arrow.setShotFromCrossbow(true);
				int pierce = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, this.goalOwner.getMainHandItem());
				arrow.setPierceLevel((byte) pierce);
			}
			vector3f.transform(quaternion);
			this.goalOwner.level.addFreshEntity(projectile);
			projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 1.6F, 0.0F);
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
		CompoundNBT nbt = stack.getTag();
		if (nbt != null && nbt.contains("ChargedProjectiles", 9)) {
			ListNBT listnbt = nbt.getList("ChargedProjectiles", 10);
			for (int i = 0; i < listnbt.size(); ++i) {
				CompoundNBT compoundnbt1 = listnbt.getCompound(i);
				list.add(ItemStack.of(compoundnbt1));
			}
		}

		return list;
	}

	@Override
	public void stop() {
		if (this.goalOwner.isUsingItem()) {
			this.goalOwner.stopUsingItem();
			this.goalOwner.setChargingCrossbow(false);
		}
	}

	enum CrossbowState {
		UNCHARGED, CHARGING, CHARGED
	}
}
