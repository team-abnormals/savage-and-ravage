package com.farcr.savageandravage.common.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IOwnableMob {
    LivingEntity getOwner();
    UUID getOwnerId();
    void setOwnerId(@Nullable UUID uuid);
    boolean shouldAttackEntity(LivingEntity target, LivingEntity owner);
}
