package com.farcr.savageandravage.common.effect;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.core.registry.other.SREvents;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.server.ServerWorld;

public class GrowingEffect extends Effect {

    public GrowingEffect() {
        super(EffectType.BENEFICIAL, 8247444);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        boolean canGrow = false;
        if(entity instanceof SlimeEntity && ((SlimeEntity)entity).getSlimeSize()<3) canGrow = true;
        else if(SREvents.checkBooflo(entity,false)) canGrow = true;
        else if(entity.isChild()){
            if((entity instanceof AgeableEntity && !(entity instanceof ParrotEntity)) || entity instanceof CreepieEntity || entity instanceof ZombieEntity) canGrow = true;
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
