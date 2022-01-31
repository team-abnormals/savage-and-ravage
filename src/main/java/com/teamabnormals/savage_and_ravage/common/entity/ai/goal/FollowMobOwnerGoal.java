package com.teamabnormals.savage_and_ravage.common.entity.ai.goal;

import com.teamabnormals.savage_and_ravage.common.entity.OwnableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;

public class FollowMobOwnerGoal extends Goal {
	private final Mob ownedMob;
	private LivingEntity owner;
	private final double followSpeed;
	private final PathNavigation navigator;
	private int timeToRecalcPath;
	private final float minDist;
	private final float maxDist;
	private float oldWaterCost;

	public FollowMobOwnerGoal(Mob entityIn, double followSpeed, float minimumDistance, float maximumDistance) {
		this.ownedMob = entityIn;
		this.followSpeed = followSpeed;
		this.navigator = entityIn.getNavigation();
		this.minDist = minimumDistance;
		this.maxDist = maximumDistance;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		if (!(entityIn.getNavigation() instanceof GroundPathNavigation) && !(entityIn.getNavigation() instanceof FlyingPathNavigation)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowMobOwnerGoal");
		}
	}

	public boolean canUse() {
		LivingEntity livingentity = ((OwnableMob) this.ownedMob).getOwner();
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
		this.oldWaterCost = this.ownedMob.getPathfindingMalus(BlockPathTypes.WATER);
		this.ownedMob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	@Override
	public void stop() {
		this.owner = null;
		this.navigator.stop();
		this.ownedMob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
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