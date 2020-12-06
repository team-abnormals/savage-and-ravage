package com.minecraftabnormals.savageandravage.common.effect;

import com.minecraftabnormals.abnormals_core.core.api.IAgeableEntity;
import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.server.ServerWorld;

public class ShrinkingEffect extends Effect {

    public ShrinkingEffect() {
        super(EffectType.NEUTRAL, 10400767);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        boolean canShrink = false;
        if (entity instanceof IAgeableEntity && ((IAgeableEntity)entity).canAge(false)) canShrink = true;
        else if (entity instanceof SlimeEntity && ((SlimeEntity) entity).getSlimeSize() > 1) canShrink = true;
        else if (entity.isChild()) canShrink =
            (entity instanceof AgeableEntity && !(entity instanceof ParrotEntity)) ||
            entity instanceof CreepieEntity ||
            entity instanceof ZombieEntity ||
            entity instanceof ZoglinEntity ||
            entity instanceof PiglinEntity;
        if (canShrink && entity.getRNG().nextInt(3) == 0) {
            if (entity.isServerWorld())
                ((ServerWorld) entity.world).spawnParticle(ParticleTypes.TOTEM_OF_UNDYING, entity.getPosXRandom(0.3D), entity.getPosYRandom(), entity.getPosZRandom(0.3D), 1, 0.3D, 0.3D, 0.3D, 0.2D);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}

