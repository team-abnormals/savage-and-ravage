package com.farcr.savageandravage.common.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IOwnableMob {
    LivingEntity getOwner();
    UUID getOwnerId();
    void setOwnerId(@Nullable UUID uuid);
    boolean shouldAttackEntity(LivingEntity target, LivingEntity owner);
    void writeAdditional(CompoundNBT compound);
    void readAdditional(CompoundNBT compound);
}
