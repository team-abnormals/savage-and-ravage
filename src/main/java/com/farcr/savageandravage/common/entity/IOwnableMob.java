package com.farcr.savageandravage.common.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.extensions.IForgeEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IOwnableMob extends IForgeEntity {
    LivingEntity getOwner();
    UUID getOwnerId();
    void setOwnerId(@Nullable UUID uuid);
    boolean shouldAttackEntity(LivingEntity target, LivingEntity owner);
    void writeAdditional(CompoundNBT compound);
    void readAdditional(CompoundNBT compound);
}
