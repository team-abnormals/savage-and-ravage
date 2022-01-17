package com.teamabnormals.savage_and_ravage.common.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.damagesource.DamageSource;

/**
 * @author Ocelot
 */
public class FrostbiteMobEffect extends MobEffect {
	public FrostbiteMobEffect() {
		super(MobEffectCategory.HARMFUL, 0x58DFA9);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "84F1D6E1-7D97-4E82-A866-7D82BEBD5A57", -0.8F, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		entity.hurt(DamageSource.MAGIC, 0.5F);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		int i = 20 >> amplifier;
		if (i > 0) {
			return duration % i == 0;
		} else {
			return true;
		}
	}
}
