package com.farcr.savageandravage.common;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectGrowth extends Effect {

    public EffectGrowth() {
        super(EffectType.BENEFICIAL, 16768259);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        Boolean flag = false;
        if((entity instanceof AgeableEntity)){
            if(((AgeableEntity)entity).isChild()) {
                flag = true;
            }
        }
        else if(entity instanceof CreepieEntity){
            flag = true;
        }
        if(flag){
            double d0 = (double) (1 >> 16 & 255) / 255.0D;
            double d1 = (double) (1 >> 8 & 255) / 255.0D;
            double d2 = (double) (1 >> 0 & 255) / 255.0D;
            entity.world.addParticle(ParticleTypes.HAPPY_VILLAGER, entity.getPosXRandom(0.5D), entity.getPosYRandom(), entity.getPosZRandom(0.5D), 1, 1, 1);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
