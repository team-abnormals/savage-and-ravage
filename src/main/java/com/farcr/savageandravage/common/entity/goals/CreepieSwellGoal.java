package com.farcr.savageandravage.common.entity.goals;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.monster.CreeperEntity;

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
        //return this.swellingCreepie.getCreeperState() > 0 || livingEntity != null && this.swellingCreepie.getDistanceSq(livingEntity) < 1.69D;
        /** this makes the distance from the player that the creepie starts to explode smaller. It's temporarily commented out so I can see whether it's actually moving while exploding clearer */
        return this.swellingCreepie.getCreeperState() > 0 || livingEntity != null && this.swellingCreepie.getDistanceSq(livingEntity) < 9.0D;
    }
    
    @Override
    public void startExecuting() {
        //this.swellingCreepie.getNavigator().
        //this.swellingCreepie.setAIMoveSpeed(this.swellingCreepie.getAIMoveSpeed()-0.1F);
        /**This makes the creepie move slower while exploding. I commented this out so I could know that it wasn't causing it to stop moving due to the value being too high.*/
        this.creeperAttackTarget = this.swellingCreepie.getAttackTarget();
    }
    
    @Override
    public void tick() {
        if (this.creeperAttackTarget == null) {
            this.swellingCreepie.setCreeperState(-1);
            //this.swellingCreepie.setAIMoveSpeed(this.swellingCreepie.getAIMoveSpeed()+0.1F);
        }
        //else if (this.swellingCreepie.getDistanceSq(this.creeperAttackTarget) > 6.25D) {
        /** this makes the distance from the player that the creepie stops exploding smaller. It's temporarily commented out so I can see whether it's actually moving while exploding clearer **/
        else if(this.swellingCreepie.getDistanceSq(this.creeperAttackTarget)>49.0D) {
            this.swellingCreepie.setCreeperState(-1);
            //this.swellingCreepie.setAIMoveSpeed(this.swellingCreepie.getAIMoveSpeed()+0.1F);
        }
        else if (!this.swellingCreepie.getEntitySenses().canSee(this.creeperAttackTarget)) {
            this.swellingCreepie.setCreeperState(-1);
            //this.swellingCreepie.setAIMoveSpeed(this.swellingCreepie.getAIMoveSpeed()+0.1F);

        }
        else {
            this.swellingCreepie.setCreeperState(1);
        }
    }
}
