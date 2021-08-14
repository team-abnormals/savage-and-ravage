package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CreepieSwellGoal extends Goal {

	private final CreepieEntity swellingCreepie;
	private LivingEntity creepieAttackTarget;

	public CreepieSwellGoal(CreepieEntity entityIn) {
		this.swellingCreepie = entityIn;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		LivingEntity livingEntity = this.swellingCreepie.getTarget();
		return this.swellingCreepie.getCreeperState() > 0 || livingEntity != null && this.swellingCreepie.distanceToSqr(livingEntity) < 4.0D;
	}

	@Override
	public void start() {
		this.creepieAttackTarget = this.swellingCreepie.getTarget();
	}

	@Override
	public void stop() {
		this.creepieAttackTarget = null;
	}

	@Override
	public void tick() {
		if (this.creepieAttackTarget == null) {
			this.swellingCreepie.setCreeperState(-1);
		} else if (this.swellingCreepie.distanceToSqr(this.creepieAttackTarget) > 6.25D) {
			this.swellingCreepie.setCreeperState(-1);
		} else if (!this.swellingCreepie.getSensing().canSee(this.creepieAttackTarget)) {
			this.swellingCreepie.setCreeperState(-1);

		} else {
			this.swellingCreepie.setCreeperState(1);
			this.swellingCreepie.getNavigation().moveTo(creepieAttackTarget, 0.8D);
			// FIXME work out why this is necessary so we can solve the problem with performant
		}
	}
}
