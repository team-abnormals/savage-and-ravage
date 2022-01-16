package com.teamabnormals.savageandravage.common.entity.goals;

import com.teamabnormals.savageandravage.common.entity.IOwnableMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

public class MobOwnerHurtByTargetGoal extends TargetGoal {

	private final IOwnableMob defendingEntity;
	private LivingEntity attacker;
	private int timestamp;

	public MobOwnerHurtByTargetGoal(Mob defendingEntityIn) {
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
				return i != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT) && this.defendingEntity.shouldAttackEntity(this.attacker, livingentity);
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