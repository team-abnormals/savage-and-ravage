package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import com.minecraftabnormals.savageandravage.common.entity.GrieferEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;

public class AvoidGrieferOwnedCreepiesGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

	public AvoidGrieferOwnedCreepiesGoal(CreatureEntity p_i46404_1_, Class<T> p_i46404_2_, float p_i46404_3_, double p_i46404_4_, double p_i46404_6_) {
		super(p_i46404_1_, p_i46404_2_, p_i46404_3_, p_i46404_4_, p_i46404_6_);
	}

	@Override
	public boolean canUse() {
		return super.canUse() && this.toAvoid instanceof CreepieEntity && ((CreepieEntity) this.toAvoid).getOwner() instanceof GrieferEntity;
	}
}
