package com.farcr.savageandravage.common.entity.goals;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;

public class CreepieSwellGoal extends CreeperSwellGoal {
    private final CreepieEntity swellingCreepie;
    private LivingEntity creeperAttackTarget;

    public CreepieSwellGoal(CreepieEntity entityIn) {
        super(entityIn);
        swellingCreepie = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity livingEntity = this.swellingCreepie.getAttackTarget();
        return this.swellingCreepie.getCreeperState() > 0 || livingEntity != null && this.swellingCreepie.getDistanceSq(livingEntity) < 4.0D;
    }
    
    @Override
    public void startExecuting() {
        this.creeperAttackTarget = this.swellingCreepie.getAttackTarget();
    }
    
    @Override
    public void tick() {
        if (this.creeperAttackTarget == null) {
            this.swellingCreepie.setCreeperState(-1);
        }
        else if (this.swellingCreepie.getDistanceSq(this.creeperAttackTarget) > 6.25D) {
            this.swellingCreepie.setCreeperState(-1);
        }
        else if (!this.swellingCreepie.getEntitySenses().canSee(this.creeperAttackTarget)) {
            this.swellingCreepie.setCreeperState(-1);

        }
        else {
            this.swellingCreepie.setCreeperState(1);
            this.swellingCreepie.getNavigator().tryMoveToEntityLiving(creeperAttackTarget,0.8D);
        }
    }
}
