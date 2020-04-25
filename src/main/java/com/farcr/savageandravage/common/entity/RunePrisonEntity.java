package com.farcr.savageandravage.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;

public class RunePrisonEntity extends Entity {

    public RunePrisonEntity(EntityType<? extends RunePrisonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public void tick(){
        super.tick();
        
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return null;
    }
}
