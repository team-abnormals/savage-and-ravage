package com.teamabnormals.savageandravage.core.registry;

import com.teamabnormals.savageandravage.core.SavageAndRavage;
import com.teamabnormals.blueprint.common.advancement.EmptyTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SRTriggers {
	public static final EmptyTrigger BURN_OMINOUS_BANNER = CriteriaTriggers.register(new EmptyTrigger(new ResourceLocation(SavageAndRavage.MOD_ID, "burn_ominous_banner")));
}
