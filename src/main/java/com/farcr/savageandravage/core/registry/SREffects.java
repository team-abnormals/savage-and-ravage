package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.common.EffectBaby;
import com.farcr.savageandravage.common.EffectGrowth;
import com.farcr.savageandravage.core.SavageAndRavage;

import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SREffects {
    public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, SavageAndRavage.MODID);
    //ForgeRegistries.POTIONS is badly named, MCP moment.
    public static final DeferredRegister<Potion> POTIONS = new DeferredRegister<>(ForgeRegistries.POTION_TYPES, SavageAndRavage.MODID);
    //Same for this, potion types???

    public static final RegistryObject<Effect> GROWING = EFFECTS.register("growth", EffectGrowth::new);
    public static final RegistryObject<Effect> SHRINKING = EFFECTS.register("shrinking", EffectBaby::new);

    public static final RegistryObject<Potion> GROWING_NORMAL  = POTIONS.register("growing", () -> new Potion(new EffectInstance(GROWING.get(), 600)));
    public static final RegistryObject<Potion> GROWING_LONG   = POTIONS.register("growing_long", () -> new Potion(new EffectInstance(GROWING.get(), 1800)));
    public static final RegistryObject<Potion> YOUTH_NORMAL  = POTIONS.register("youth", () -> new Potion(new EffectInstance(SHRINKING.get(), 600)));
    public static final RegistryObject<Potion> YOUTH_LONG   = POTIONS.register("youth_long", () -> new Potion(new EffectInstance(SHRINKING.get(), 1800)));

    public static void registerBrewingRecipes(){
        PotionBrewing.addMix(Potions.AWKWARD, Items.GOLDEN_APPLE, GROWING_NORMAL.get());
        PotionBrewing.addMix(GROWING_NORMAL.get(), Items.REDSTONE, GROWING_LONG.get());
        PotionBrewing.addMix(GROWING_NORMAL.get(), Items.FERMENTED_SPIDER_EYE, YOUTH_NORMAL.get());
        PotionBrewing.addMix(GROWING_LONG.get(), Items.FERMENTED_SPIDER_EYE, YOUTH_LONG.get());
        PotionBrewing.addMix(YOUTH_NORMAL.get(), Items.REDSTONE, YOUTH_LONG.get());
    }
}
