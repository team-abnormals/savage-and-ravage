package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.savageandravage.common.entity.IOwnableMob;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class MobOwnerHurtByTargetGoal extends TargetGoal {

    private final IOwnableMob defendingEntity;
    private LivingEntity attacker;
    private int timestamp;

    public MobOwnerHurtByTargetGoal(MobEntity defendingEntityIn) {
        super(defendingEntityIn, false);
        this.defendingEntity = (IOwnableMob) defendingEntityIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
    }


    @Override
    public boolean shouldExecute() {
        if (this.defendingEntity.getOwnerId() != null) {
            LivingEntity livingentity = this.defendingEntity.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getRevengeTarget();
                int i = livingentity.getRevengeTimer();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && this.defendingEntity.shouldAttackEntity(this.attacker, livingentity);
            }
        } else {
            return false;
        }
    }


    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacker);
        LivingEntity livingentity = this.defendingEntity.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getRevengeTimer();
        }

        super.startExecuting();
    }
}