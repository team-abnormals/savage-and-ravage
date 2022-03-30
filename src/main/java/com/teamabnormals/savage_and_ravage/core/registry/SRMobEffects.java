package com.teamabnormals.savage_and_ravage.core.registry;

import com.teamabnormals.savage_and_ravage.common.effect.WeightMobEffect;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SRMobEffects {
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SavageAndRavage.MOD_ID);

	public static final RegistryObject<MobEffect> WEIGHT = MOB_EFFECTS.register("weight", WeightMobEffect::new);
}
