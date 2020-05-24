package com.farcr.savageandravage.common.entity.goals;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CreepieSwellGoal extends Goal {
    private final CreepieEntity swellingCreepie;
    private LivingEntity creepieAttackTarget;

    public CreepieSwellGoal(CreepieEntity entityIn) {
        this.swellingCreepie = entityIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    @Override
    public boolean shouldExecute() {
        LivingEntity livingEntity = this.swellingCreepie.getAttackTarget();
        return this.swellingCreepie.getCreeperState() > 0 || livingEntity != null && this.swellingCreepie.getDistanceSq(livingEntity) < 4.0D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.creepieAttackTarget = this.swellingCreepie.getAttackTarget();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.creepieAttackTarget = null;
    }

    @Override
    public void tick() {
        if (this.creepieAttackTarget == null) {
            this.swellingCreepie.setCreeperState(-1);
        }
        else if (this.swellingCreepie.getDistanceSq(this.creepieAttackTarget) > 6.25D) {
            this.swellingCreepie.setCreeperState(-1);
        }
        else if (!this.swellingCreepie.getEntitySenses().canSee(this.creepieAttackTarget)) {
            this.swellingCreepie.setCreeperState(-1);

        }
        else {
            this.swellingCreepie.setCreeperState(1);
            this.swellingCreepie.getNavigator().tryMoveToEntityLiving(creepieAttackTarget,0.8D);
            //TODO work out why this is necessary so we can solve the problem with performant
        }
    }
}
