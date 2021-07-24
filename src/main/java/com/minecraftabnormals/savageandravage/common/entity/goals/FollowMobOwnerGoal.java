package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.savageandravage.common.entity.IOwnableMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;

import java.util.EnumSet;

public class FollowMobOwnerGoal extends Goal {
	private final MobEntity ownedMob;
	private LivingEntity owner;
	private final double followSpeed;
	private final PathNavigator navigator;
	private int timeToRecalcPath;
	private final float minDist;
	private final float maxDist;
	private float oldWaterCost;

	public FollowMobOwnerGoal(MobEntity entityIn, double followSpeed, float minimumDistance, float maximumDistance) {
		this.ownedMob = entityIn;
		this.followSpeed = followSpeed;
		this.navigator = entityIn.getNavigation();
		this.minDist = minimumDistance;
		this.maxDist = maximumDistance;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		if (!(entityIn.getNavigation() instanceof GroundPathNavigator) && !(entityIn.getNavigation() instanceof FlyingPathNavigator)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowMobOwnerGoal");
		}
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		LivingEntity livingentity = ((IOwnableMob) this.ownedMob).getOwner();
		if (livingentity == null) {
			return false;
		} else if (livingentity.isSpectator()) {
			return false;
		} else if (this.ownedMob.distanceToSqr(livingentity) <= (double) (this.minDist * this.minDist) || this.ownedMob.distanceToSqr(livingentity) >= (double) (this.maxDist * this.maxDist)) {
			return false;
		} else if (this.ownedMob.getTarget() != null) {
			return false;
		} else {
			this.owner = livingentity;
			return true;
		}
	}

	@Override
	public boolean canContinueToUse() {
		if (this.navigator.isDone()) {
			return false;
		} else if ((this.ownedMob.distanceToSqr(this.owner) <= (double) (this.minDist * this.minDist)) || (this.ownedMob.distanceToSqr(this.owner) >= (double) (this.maxDist * this.maxDist))) {
			return false;
		} else return this.ownedMob.getTarget() == null;
	}

	@Override
	public void start() {
		this.timeToRecalcPath = 0;
		this.oldWaterCost = this.ownedMob.getPathfindingMalus(PathNodeType.WATER);
		this.ownedMob.setPathfindingMalus(PathNodeType.WATER, 0.0F);
	}

	@Override
	public void stop() {
		this.owner = null;
		this.navigator.stop();
		this.ownedMob.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
	}

	@Override
	public void tick() {
		this.ownedMob.getLookControl().setLookAt(this.owner, 10.0F, (float) this.ownedMob.getMaxHeadXRot());
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = 10;
			if (!this.ownedMob.isLeashed() && !this.ownedMob.isPassenger()) {
				this.navigator.moveTo(this.owner, this.followSpeed);
			}
		}
	}
}