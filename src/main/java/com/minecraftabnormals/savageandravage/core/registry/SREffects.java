package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.abnormals_core.common.potion.AbnormalsEffect;
import com.minecraftabnormals.savageandravage.common.effect.FrostbiteEffect;
import com.minecraftabnormals.savageandravage.common.effect.WeightEffect;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SREffects {
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, SavageAndRavage.MOD_ID);

	public static final RegistryObject<Effect> CONFUSION = EFFECTS.register("confusion", () -> new AbnormalsEffect(EffectType.HARMFUL, 3928018));
	public static final RegistryObject<Effect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);
	public static final RegistryObject<Effect> WEIGHT = EFFECTS.register("weight", WeightEffect::new);
}
