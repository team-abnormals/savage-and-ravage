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
        this.navigator = entityIn.getNavigator();
        this.minDist = minimumDistance;
        this.maxDist = maximumDistance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(entityIn.getNavigator() instanceof GroundPathNavigator) && !(entityIn.getNavigator() instanceof FlyingPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobOwnerGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        LivingEntity livingentity = ((IOwnableMob) this.ownedMob).getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.ownedMob.getDistanceSq(livingentity) <= (double) (this.minDist * this.minDist) || this.ownedMob.getDistanceSq(livingentity) >= (double) (this.maxDist * this.maxDist)) {
            return false;
        } else if (this.ownedMob.getAttackTarget() != null) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.navigator.noPath()) {
            return false;
        } else if ((this.ownedMob.getDistanceSq(this.owner) <= (double) (this.minDist * this.minDist)) || (this.ownedMob.getDistanceSq(this.owner) >= (double) (this.maxDist * this.maxDist))) {
            return false;
        } else return this.ownedMob.getAttackTarget() == null;
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.ownedMob.getPathPriority(PathNodeType.WATER);
        this.ownedMob.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.navigator.clearPath();
        this.ownedMob.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.ownedMob.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float) this.ownedMob.getVerticalFaceSpeed());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.ownedMob.getLeashed() && !this.ownedMob.isPassenger()) {
                this.navigator.tryMoveToEntityLiving(this.owner, this.followSpeed);
            }
        }
    }
}