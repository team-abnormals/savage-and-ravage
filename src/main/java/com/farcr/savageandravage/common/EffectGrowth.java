package com.farcr.savageandravage.common;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectGrowth extends Effect {

    public EffectGrowth() {
        super(EffectType.BENEFICIAL, 16768259);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
