package com.minecraftabnormals.savageandravage.common.entity.goals;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class ImprovedCrossbowGoal<T extends CreatureEntity & IRangedAttackMob & ICrossbowUser> extends Goal {

	private final T entity;
	private ImprovedCrossbowGoal.CrossbowState crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
	private final double speedChanger;
	private final float radiusSq;
	private int seeTime;
	private int wait;
	private final double blocksUntilBackupSq;

	public ImprovedCrossbowGoal(T entity, double speedChanger, float radius, double blocksUntilBackup) {
		this.entity = entity;
		this.speedChanger = speedChanger;
		this.radiusSq = radius * radius;
		this.blocksUntilBackupSq = blocksUntilBackup;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	private boolean hasCrossbowOnMainHand() {
		return this.entity.getHeldItemMainhand().getItem() instanceof CrossbowItem;
	}

	private boolean isAttackTargetStillExisting() {
		return this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isAlive();
	}

	@Override
	public boolean shouldExecute() {
		return this.isAttackTargetStillExisting() && this.hasCrossbowOnMainHand();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.isAttackTargetStillExisting() && (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.hasCrossbowOnMainHand();
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.entity.setAggroed(false);
		this.entity.setAttackTarget(null);
		this.seeTime = 0;
		if (this.entity.isHandActive()) {
			this.entity.resetActiveHand();
			this.entity.setCharging(false);
		}
	}
	
	private boolean isWalkable() {
		PathNavigator pathnavigator = this.entity.getNavigator();
        float f = (float)this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float f1 = (float) 0.25D * f;
        float f2 = entity.moveForward;
        float f3 = entity.moveStrafing;
        float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);
        if (f4 < 1.0F) {
           f4 = 1.0F;
        }

        f4 = f1 / f4;
        f2 = f2 * f4;
        f3 = f3 * f4;
        float f5 = MathHelper.sin(this.entity.rotationYaw * ((float)Math.PI / 180F));
        float f6 = MathHelper.cos(this.entity.rotationYaw * ((float)Math.PI / 180F));
        float f7 = f2 * f6 - f3 * f5;
        float f8 = f3 * f6 + f2 * f5;
		if (pathnavigator != null) {
			NodeProcessor nodeprocessor = pathnavigator.getNodeProcessor();
			if (nodeprocessor != null && nodeprocessor.getPathNodeType(this.entity.world, MathHelper.floor(this.entity.getPosX() + (double)f7), MathHelper.floor(this.entity.getPosY()), MathHelper.floor(this.entity.getPosZ() + (double)f8)) == PathNodeType.WALKABLE) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void tick() {
		LivingEntity target = this.entity.getAttackTarget();
		this.entity.setAggroed(true); // Minecraft doesn't think shooting an arrow at another entity is aggression.
		if (target == null)
			return;

		boolean canSeeEnemy = this.entity.getEntitySenses().canSee(target);
		if (canSeeEnemy) {
			++this.seeTime;
		} else {
			this.seeTime = 0;
		}

		double distanceSq = target.getDistanceSq(entity);
		double distance = target.getDistance(entity);
		if (distance <= blocksUntilBackupSq && !(target instanceof AbstractVillagerEntity)) {
			this.entity.faceEntity(target, 30.0F, 30.0F);
			if (isWalkable())
				this.entity.getMoveHelper().strafe(entity.isHandActive() ? -0.5F : -3.0F, 0); // note: when an entity is "charging" their crossbow they set an active hand
		}
		ItemStack activeStack = this.entity.getActiveItemStack();
		boolean shouldMoveTowardsEnemy = (distanceSq > (double) this.radiusSq || this.seeTime < 5) && this.wait == 0;
		if (shouldMoveTowardsEnemy) {
			this.entity.getNavigator().tryMoveToEntityLiving(target, this.isCrossbowUncharged() ? this.speedChanger : this.speedChanger * 0.5D);
		} else {
			this.entity.getNavigator().clearPath();
		}

		this.entity.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
		if (this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.UNCHARGED && !CrossbowItem.isCharged(activeStack)) {
			if (canSeeEnemy) {
				this.entity.setActiveHand(ProjectileHelper.getHandWith(this.entity, Items.CROSSBOW));
				this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.CHARGING;
				this.entity.setCharging(true);
			}
		} else if (this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.CHARGING) {
			if (!this.entity.isHandActive()) {
				this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
			}

			int i = this.entity.getItemInUseMaxCount();
			if (i >= CrossbowItem.getChargeTime(activeStack) || CrossbowItem.isCharged(activeStack)) {
				this.entity.stopActiveHand();
				this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.CHARGED;
				this.wait = 20 + this.entity.getRNG().nextInt(20);
				if (entity.getHeldItemOffhand().getItem() instanceof FireworkRocketItem) {
					entity.setActiveHand(Hand.OFF_HAND);
				}
				this.entity.setCharging(false);
			}
		} else if (this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.CHARGED) {
			--this.wait;
			if (this.wait == 0) {
				this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK;
			}
		} else if (this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK && canSeeEnemy) {
			this.entity.attackEntityWithRangedAttack(target, 1.0F);
			CrossbowItem.setCharged(this.entity.getHeldItem(ProjectileHelper.getHandWith(this.entity, Items.CROSSBOW)), false);
			this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
		}
	}

	private boolean isCrossbowUncharged() {
		return this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
	}

	enum CrossbowState {
		UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK
	}
}
