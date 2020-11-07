package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.savageandravage.common.entity.IOwnableMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class ConditionalNearestAttackableTargetGoal extends NearestAttackableTargetGoal<PlayerEntity> {

    public ConditionalNearestAttackableTargetGoal(MobEntity goalOwnerIn, boolean checkSight) {
        super(goalOwnerIn, PlayerEntity.class, checkSight);
    }

    @Override
    public boolean shouldExecute() {
        if (((IOwnableMob) this.goalOwner).getOwnerId() != null || (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0)) {
            return false;
        } else {
            this.findNearestTarget();
            return this.nearestTarget != null;
        }
    }
}
