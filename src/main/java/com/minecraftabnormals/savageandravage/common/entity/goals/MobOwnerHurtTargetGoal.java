package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.savageandravage.common.entity.IOwnableMob;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class MobOwnerHurtTargetGoal extends TargetGoal {
	private final IOwnableMob defendingEntity;
	private LivingEntity attacker;

	public MobOwnerHurtTargetGoal(MobEntity defendingEntityIn) {
		super(defendingEntityIn, false);
		this.defendingEntity = (IOwnableMob) defendingEntityIn;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		if (this.defendingEntity.getOwnerId() != null) {
			LivingEntity owner = this.defendingEntity.getOwner();
			if (owner == null) {
				return false;
			} else {
				this.attacker = owner instanceof MobEntity ? ((MobEntity) owner).getTarget() : owner.getLastHurtMob();
				return this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.defendingEntity.shouldAttackEntity(this.attacker, owner);
			}
		} else {
			return false;
		}
	}

	@Override
	public void start() {
		super.start();
		this.mob.setTarget(this.attacker);
	}
}