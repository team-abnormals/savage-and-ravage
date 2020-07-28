package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.common.effect.GrowingEffect;
import com.minecraftabnormals.savageandravage.common.effect.ShrinkingEffect;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;

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
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, SavageAndRavage.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, SavageAndRavage.MODID);

    public static final RegistryObject<Effect> GROWING = EFFECTS.register("growing", GrowingEffect::new);
    public static final RegistryObject<Effect> SHRINKING = EFFECTS.register("shrinking", ShrinkingEffect::new);

    public static final RegistryObject<Potion> GROWTH_NORMAL  = POTIONS.register("growth", () -> new Potion(new EffectInstance(GROWING.get(), 600)));
    public static final RegistryObject<Potion> GROWTH_LONG   = POTIONS.register("growth_long", () -> new Potion(new EffectInstance(GROWING.get(), 1800)));
    public static final RegistryObject<Potion> YOUTH_NORMAL  = POTIONS.register("youth", () -> new Potion(new EffectInstance(SHRINKING.get(), 600)));
    public static final RegistryObject<Potion> YOUTH_LONG   = POTIONS.register("youth_long", () -> new Potion(new EffectInstance(SHRINKING.get(), 1800)));

    public static void registerBrewingRecipes(){
        PotionBrewing.addMix(Potions.AWKWARD, Items.GOLDEN_APPLE, GROWTH_NORMAL.get());
        PotionBrewing.addMix(GROWTH_NORMAL.get(), Items.REDSTONE, GROWTH_LONG.get());
        PotionBrewing.addMix(GROWTH_NORMAL.get(), Items.FERMENTED_SPIDER_EYE, YOUTH_NORMAL.get());
        PotionBrewing.addMix(GROWTH_LONG.get(), Items.FERMENTED_SPIDER_EYE, YOUTH_LONG.get());
        PotionBrewing.addMix(YOUTH_NORMAL.get(), Items.REDSTONE, YOUTH_LONG.get());
    }
}
