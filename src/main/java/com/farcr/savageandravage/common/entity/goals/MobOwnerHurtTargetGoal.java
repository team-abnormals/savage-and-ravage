package com.farcr.savageandravage.common.entity.goals;

import com.farcr.savageandravage.common.entity.IOwnableMob;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

@SuppressWarnings("unused")
public class MobOwnerHurtTargetGoal extends TargetGoal {
    private final IOwnableMob defendingEntity;
    private LivingEntity attacker;
	private int timestamp;

    public MobOwnerHurtTargetGoal(MobEntity defendingEntityIn) {
        super(defendingEntityIn, false);
        this.defendingEntity = (IOwnableMob)defendingEntityIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        if (this.defendingEntity.getOwnerId()!=null) {
            LivingEntity owner = this.defendingEntity.getOwner();
            if (owner == null) {
                return false;
            } else {
                this.attacker = owner instanceof MobEntity ? ((MobEntity)owner).getAttackTarget() : owner.getLastAttackedEntity();
                int i = owner.getLastAttackedEntityTime();
                return /*i != this.timestamp &&*/ this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && this.defendingEntity.shouldAttackEntity(this.attacker, owner);
            }
        } else {
            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacker);
        LivingEntity livingentity = this.defendingEntity.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}