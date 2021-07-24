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
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}


	@Override
	public boolean canUse() {
		if (this.defendingEntity.getOwnerId() != null) {
			LivingEntity livingentity = this.defendingEntity.getOwner();
			if (livingentity == null) {
				return false;
			} else {
				this.attacker = livingentity.getLastHurtByMob();
				int i = livingentity.getLastHurtByMobTimestamp();
				return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.defendingEntity.shouldAttackEntity(this.attacker, livingentity);
			}
		} else {
			return false;
		}
	}


	@Override
	public void start() {
		this.mob.setTarget(this.attacker);
		LivingEntity livingentity = this.defendingEntity.getOwner();
		if (livingentity != null) {
			this.timestamp = livingentity.getLastHurtByMobTimestamp();
		}

		super.start();
	}
}