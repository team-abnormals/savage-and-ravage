package com.minecraftabnormals.savageandravage.common.entity.goals;

import net.minecraft.block.TargetBlock;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AttackTargetBlockRandomlyGoal extends TargetGoal {
    private int focusTime;
    private BlockPos nearestTargetPos;

    public AttackTargetBlockRandomlyGoal(MobEntity mobIn, boolean checkSight) {
        super(mobIn, checkSight);
    }

    public AttackTargetBlockRandomlyGoal(MobEntity mobIn, boolean checkSight, boolean nearbyOnlyIn) {
        super(mobIn, checkSight, nearbyOnlyIn);
    }

    @Override
    public boolean shouldExecute() {
        return this.goalOwner.getRNG().nextFloat()<0.002F;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.focusTime >= 0;
    }

    @Override
    public void startExecuting() {
        World world = this.goalOwner.world;
        BlockPos.Mutable checkingPos = new BlockPos.Mutable();
        this.focusTime = 200 + this.goalOwner.getRNG().nextInt(70);
        for(double x=this.goalOwner.getPosX()-15; x<=this.goalOwner.getPosX()+15; x++) {
            for(double y=this.goalOwner.getPosY()-5; y<=this.goalOwner.getPosY()+5; y++) {
                for(double z=this.goalOwner.getPosZ()-15; z<=this.goalOwner.getPosZ()+15; z++) {
                    checkingPos.setPos(x, y, z);
                    if (world.getBlockState(checkingPos).getBlock() instanceof TargetBlock) {
                        this.nearestTargetPos = checkingPos.toImmutable();
                    }
                }
            }

        }
    }

    @Override
    public void tick() {
        this.focusTime--;
        this.goalOwner.getLookController().setLookPosition(nearestTargetPos.getX(), nearestTargetPos.getY(), nearestTargetPos.getZ());
    }
}
