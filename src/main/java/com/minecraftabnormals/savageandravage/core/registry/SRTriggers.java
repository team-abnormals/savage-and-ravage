package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.teamabnormals.abnormals_core.common.advancement.EmptyTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID)
public class SRTriggers {

    public static final EmptyTrigger BURN_BANNER = CriteriaTriggers.register(new EmptyTrigger(new ResourceLocation(SavageAndRavage.MODID, "burn_banner")));

}
