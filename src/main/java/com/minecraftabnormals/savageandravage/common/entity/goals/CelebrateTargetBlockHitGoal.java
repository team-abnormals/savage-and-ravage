package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.savageandravage.core.other.SRDataProcessors;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.AbstractRaiderEntity;

import java.util.EnumSet;

public class CelebrateTargetBlockHitGoal extends Goal {
    private final AbstractRaiderEntity mob;

    public CelebrateTargetBlockHitGoal(AbstractRaiderEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return this.mob.isAlive() && this.mob.getTarget() == null && ((IDataManager) this.mob).getValue(SRDataProcessors.CELEBRATION_TIME) > 0;
    }

    public void start() {
        this.mob.setCelebrating(true);
        super.start();
    }

    public void stop() {
        this.mob.setCelebrating(false);
        ((IDataManager) this.mob).setValue(SRDataProcessors.CELEBRATION_TIME, 0);
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
