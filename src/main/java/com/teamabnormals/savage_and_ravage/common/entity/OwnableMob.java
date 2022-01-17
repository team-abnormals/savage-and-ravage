package com.teamabnormals.savage_and_ravage.common.entity;

import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface OwnableMob {

	@Nullable
	LivingEntity getOwner();

	@Nullable
	UUID getOwnerId();

	void setOwnerId(@Nullable UUID uuid);

	boolean shouldAttackEntity(LivingEntity target, LivingEntity owner);
}
