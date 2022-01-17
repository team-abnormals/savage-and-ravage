package com.teamabnormals.savage_and_ravage.common.entity.goals;

import com.teamabnormals.savage_and_ravage.common.entity.OwnableMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

public class MobOwnerHurtTargetGoal extends TargetGoal {
	private final OwnableMob defendingEntity;
	private LivingEntity attacker;

	public MobOwnerHurtTargetGoal(Mob defendingEntityIn) {
		super(defendingEntityIn, false);
		this.defendingEntity = (OwnableMob) defendingEntityIn;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		if (this.defendingEntity.getOwnerId() != null) {
			LivingEntity owner = this.defendingEntity.getOwner();
			if (owner == null) {
				return false;
			} else {
				this.attacker = owner instanceof Mob ? ((Mob) owner).getTarget() : owner.getLastHurtMob();
				return this.canAttack(this.attacker, TargetingConditions.DEFAULT) && this.defendingEntity.shouldAttackEntity(this.attacker, owner);
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