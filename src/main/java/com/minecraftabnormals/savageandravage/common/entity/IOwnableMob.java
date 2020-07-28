package com.minecraftabnormals.savageandravage.common.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;

public interface IOwnableMob {
    LivingEntity getOwner();
    UUID getOwnerId();
    void setOwnerId(@Nullable UUID uuid);
    boolean shouldAttackEntity(LivingEntity target, LivingEntity owner);
}
