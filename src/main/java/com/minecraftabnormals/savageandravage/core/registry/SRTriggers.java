package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.abnormals_core.common.advancement.EmptyTrigger;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

/**
 * @author - SmellyModder(Luke Tonon)
 */
@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SRTriggers {
	public static final EmptyTrigger BURN_OMINOUS_BANNER = CriteriaTriggers.register(new EmptyTrigger(new ResourceLocation(SavageAndRavage.MOD_ID, "burn_ominous_banner")));
}
