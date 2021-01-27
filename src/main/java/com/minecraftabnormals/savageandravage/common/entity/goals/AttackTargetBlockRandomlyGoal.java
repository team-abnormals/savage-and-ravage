package com.minecraftabnormals.savageandravage.common.entity.goals;

import net.minecraft.block.TargetBlock;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AttackTargetBlockRandomlyGoal<T extends MobEntity & ICrossbowUser> extends Goal {
    private final T goalOwner;
    private int focusTime;
    private int timeTillNextShoot;
    private BlockPos nearestTargetPos;
    private ImprovedCrossbowGoal.CrossbowState state;

    public AttackTargetBlockRandomlyGoal(T mobIn) {
        this.goalOwner = mobIn;
    }

    @Override
    public boolean shouldExecute() {
        if (this.goalOwner.getRNG().nextFloat() < 0.005F) {
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
        return this.focusTime >= 0;
    }

    @Override
    public void startExecuting() {
        this.timeTillNextShoot = goalOwner.getRNG().nextInt(15)+15;
    }

    @Override
    public void tick() {
        this.focusTime--;
        this.timeTillNextShoot--;
        this.goalOwner.getLookController().setLookPosition(nearestTargetPos.getX(), nearestTargetPos.getY(), nearestTargetPos.getZ());
        //TODO Use CrossbowState then ProjectileItem#shoot
        if (timeTillNextShoot <= 0) {
            goalOwner.setCharging(true);
            timeTillNextShoot = goalOwner.getRNG().nextInt(25)+40;
        }
    }
}
