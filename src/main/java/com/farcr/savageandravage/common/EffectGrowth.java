package com.farcr.savageandravage.common;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.server.ServerWorld;

public class EffectGrowth extends Effect {

    public EffectGrowth() {
        super(EffectType.BENEFICIAL, 16768259);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        boolean canGrow = false;
        //TODO add case for booflo
        if(entity instanceof SlimeEntity && ((SlimeEntity)entity).getSlimeSize()<3){
           canGrow = true;
        }
        else if(entity.isChild()){
            canGrow = true;
            if(!(entity instanceof AgeableEntity || entity instanceof CreepieEntity || entity instanceof ZombieEntity)) canGrow = false;
        }
        if(canGrow && entity.getRNG().nextInt(3)==0){
            if(entity.isServerWorld()) ((ServerWorld) entity.world).spawnParticle(ParticleTypes.HAPPY_VILLAGER, entity.getPosXRandom(0.3D), entity.getPosYRandom(), entity.getPosZRandom(0.3D), 1, 0.3D, 0.3D, 0.3D, 1.0D);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
