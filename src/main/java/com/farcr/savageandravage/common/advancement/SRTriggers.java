package com.farcr.savageandravage.common.advancement;

import com.farcr.savageandravage.core.SavageAndRavage;
import com.teamabnormals.abnormals_core.common.advancement.EmptyTrigger;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

/**
 * @author - SmellyModder(Luke Tonon)
 */
@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID)
public class SRTriggers {
    public static final EmptyTrigger BURN_BANNER = CriteriaTriggers.register(new EmptyTrigger(prefix("burn_banner")));

    private static ResourceLocation prefix(String name) {
        return new ResourceLocation(SavageAndRavage.MODID, name);
    }

}
