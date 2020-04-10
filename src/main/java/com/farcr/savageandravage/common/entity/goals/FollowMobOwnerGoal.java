package com.farcr.savageandravage.common.entity.goals;

import com.farcr.savageandravage.common.entity.IOwnableMob;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

public class FollowMobOwnerGoal extends Goal {
    private final MobEntity ownedMob;
    private LivingEntity owner;
    private final IWorldReader world;
    private final double followSpeed;
    private final PathNavigator navigator;
    private int timeToRecalcPath;
    private final float maxDist;
    private final float minDist;
    private float oldWaterCost;
    private final boolean canFly;

    public FollowMobOwnerGoal(MobEntity entityIn, double followSpeed, float minimumDistance, float maximumDistance, boolean canFly) {
        this.ownedMob = entityIn;
        this.world = entityIn.world;
        this.followSpeed = followSpeed;
        this.navigator = entityIn.getNavigator();
        this.minDist = minimumDistance;
        this.maxDist = maximumDistance;
        this.canFly = canFly;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(entityIn.getNavigator() instanceof GroundPathNavigator) && !(entityIn.getNavigator() instanceof FlyingPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        LivingEntity livingentity = ((IOwnableMob)this.ownedMob).getOwner();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.ownedMob.getDistanceSq(livingentity) < (double)(this.minDist * this.minDist)) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        if (this.navigator.noPath()) {
            return false;

        } else {
            return !(this.ownedMob.getDistanceSq(this.owner) <= (double)(this.maxDist * this.maxDist));
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.ownedMob.getPathPriority(PathNodeType.WATER);
        this.ownedMob.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.owner = null;
        this.navigator.clearPath();
        this.ownedMob.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.ownedMob.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float)this.ownedMob.getVerticalFaceSpeed());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.ownedMob.getLeashed() && !this.ownedMob.isPassenger()) {
                if (this.ownedMob.getDistanceSq(this.owner) >= 144.0D) {
                    this.teleportToOwner();
                } else {
                    this.navigator.tryMoveToEntityLiving(this.owner, this.followSpeed);
                }

            }
        }
    }

    private void teleportToOwner() {
        BlockPos blockpos = new BlockPos(this.owner);

        for(int i = 0; i < 10; ++i) {
            int j = this.inclusiveRandomInt(-3, 3);
            int k = this.inclusiveRandomInt(-1, 1);
            int l = this.inclusiveRandomInt(-3, 3);
            boolean flag = this.possiblyTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private boolean possiblyTeleportTo(int x, int y, int z) {
        if (Math.abs((double)x - this.owner.getPosX()) < 2.0D && Math.abs((double)z - this.owner.getPosZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.ownedMob.setLocationAndAngles((double)((float)x + 0.5F), (double)y, (double)((float)z + 0.5F), this.ownedMob.rotationYaw, this.ownedMob.rotationPitch);
            this.navigator.clearPath();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos blockPositionIn) {
        PathNodeType pathnodetype = WalkNodeProcessor.func_227480_b_(this.world, blockPositionIn.getX(), blockPositionIn.getY(), blockPositionIn.getZ());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.world.getBlockState(blockPositionIn.down());
            if (!this.canFly && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = blockPositionIn.subtract(new BlockPos(this.ownedMob));
                return this.world.hasNoCollisions(this.ownedMob, this.ownedMob.getBoundingBox().offset(blockpos));
            }
        }
    }

    private int inclusiveRandomInt(int lowerBound, int upperBound) {
        return this.ownedMob.getRNG().nextInt( upperBound - lowerBound + 1) + lowerBound;
    }
}
