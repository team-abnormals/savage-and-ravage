package com.minecraftabnormals.savageandravage.common.effect;

import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import com.minecraftabnormals.savageandravage.core.other.SREvents;
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

public class GrowingEffect extends Effect {

    public GrowingEffect() {
        super(EffectType.BENEFICIAL, 8247444);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        if (entity.world.isRemote())
            return;

        boolean canGrow = false;
        if (entity instanceof SlimeEntity && ((SlimeEntity) entity).getSlimeSize() < 3) {
            canGrow = true;
        } else if (SREvents.checkBooflo(entity, false)) {
            canGrow = true;
        } else if (entity.isChild() && ((entity instanceof AgeableEntity && !(entity instanceof ParrotEntity)) || entity instanceof CreepieEntity || entity instanceof ZombieEntity || entity instanceof ZoglinEntity || entity instanceof PiglinEntity)) {
            canGrow = true;
        }

        if (canGrow && entity.getRNG().nextInt(3) == 0)
            ((ServerWorld) entity.world).spawnParticle(ParticleTypes.HAPPY_VILLAGER, entity.getPosXRandom(0.3D), entity.getPosYRandom(), entity.getPosZRandom(0.3D), 1, 0.3D, 0.3D, 0.3D, 1.0D);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
