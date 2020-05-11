package com.farcr.savageandravage.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectGrowing extends Effect {

    public EffectGrowing() {
        super(EffectType.BENEFICIAL, 16768259);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {

    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
