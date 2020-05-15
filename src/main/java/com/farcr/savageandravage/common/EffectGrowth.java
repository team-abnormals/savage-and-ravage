package com.farcr.savageandravage.common;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
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
        if((entity instanceof AgeableEntity) && entity.isChild()){
            canGrow = true;
        }
        else if(entity instanceof CreepieEntity){
            canGrow = true;
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
