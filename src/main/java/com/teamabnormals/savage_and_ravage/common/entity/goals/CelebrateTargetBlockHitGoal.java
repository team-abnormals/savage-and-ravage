package com.teamabnormals.savage_and_ravage.common.entity.goals;

import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.savage_and_ravage.core.other.SRDataProcessors;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.raid.Raider;

import java.util.EnumSet;

public class CelebrateTargetBlockHitGoal extends Goal {
	private final Raider mob;

	public CelebrateTargetBlockHitGoal(Raider mob) {
		this.mob = mob;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	public boolean canUse() {
		return this.mob.isAlive() && this.mob.getTarget() == null && TrackedDataManager.INSTANCE.getValue(this.mob, SRDataProcessors.CELEBRATION_TIME) > 0;
	}

	public void start() {
		this.mob.setCelebrating(true);
		super.start();
	}

	public void stop() {
		this.mob.setCelebrating(false);
		TrackedDataManager.INSTANCE.setValue(this.mob, SRDataProcessors.CELEBRATION_TIME, 0);
		super.stop();
	}

	public void tick() {
		if (!this.mob.isSilent() && this.mob.getRandom().nextInt(100) == 0) {
			this.mob.playSound(this.mob.getCelebrateSound(), 1.0F, (this.mob.getRandom().nextFloat() - this.mob.getRandom().nextFloat()) * 0.2F + 1.0F);
		}

		if (!this.mob.isPassenger() && this.mob.getRandom().nextInt(25) == 0) {
			this.mob.getJumpControl().jump();
		}
		super.tick();
	}
}
