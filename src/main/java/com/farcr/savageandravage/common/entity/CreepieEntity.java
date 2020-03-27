package com.farcr.savageandravage.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class CreepieEntity extends MonsterEntity {
    public CreepieEntity(EntityType<? extends CreepieEntity> type, World worldIn){
        super(type, worldIn);
    }
    
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CREEPER_HURT;
     }

     protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
     }
//something will be here soon
}
