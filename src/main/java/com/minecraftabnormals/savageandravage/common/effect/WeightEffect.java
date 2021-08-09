package com.minecraftabnormals.savageandravage.common.effect;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class WeightEffect extends Effect {
	public WeightEffect() {
		super(EffectType.HARMFUL, 5926018); //TODO change colour
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
