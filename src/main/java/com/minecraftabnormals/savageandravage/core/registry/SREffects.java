package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.common.effect.FrostbiteEffect;
import com.minecraftabnormals.savageandravage.common.effect.GrowingEffect;
import com.minecraftabnormals.savageandravage.common.effect.ShrinkingEffect;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SREffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, SavageAndRavage.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, SavageAndRavage.MODID);

    public static final RegistryObject<Effect> GROWING = EFFECTS.register("growing", GrowingEffect::new);
    public static final RegistryObject<Effect> SHRINKING = EFFECTS.register("shrinking", ShrinkingEffect::new);
    public static final RegistryObject<Effect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);

    public static final RegistryObject<Potion> GROWTH_NORMAL = POTIONS.register("growth", () -> new Potion(new EffectInstance(GROWING.get(), 600)));
    public static final RegistryObject<Potion> GROWTH_LONG = POTIONS.register("growth_long", () -> new Potion(new EffectInstance(GROWING.get(), 1800)));
    public static final RegistryObject<Potion> YOUTH_NORMAL = POTIONS.register("youth", () -> new Potion(new EffectInstance(SHRINKING.get(), 600)));
    public static final RegistryObject<Potion> YOUTH_LONG = POTIONS.register("youth_long", () -> new Potion(new EffectInstance(SHRINKING.get(), 1800)));

    private static final Method ADD_MIX_METHOD = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "func_193357_a", Potion.class, Item.class, Potion.class);

    public static void registerBrewingRecipes() {
        addMix(Potions.AWKWARD, Items.GOLDEN_APPLE, GROWTH_NORMAL.get());
        addMix(GROWTH_NORMAL.get(), Items.REDSTONE, GROWTH_LONG.get());
        addMix(GROWTH_NORMAL.get(), Items.FERMENTED_SPIDER_EYE, YOUTH_NORMAL.get());
        addMix(GROWTH_LONG.get(), Items.FERMENTED_SPIDER_EYE, YOUTH_LONG.get());
        addMix(YOUTH_NORMAL.get(), Items.REDSTONE, YOUTH_LONG.get());
    }

    private static void addMix(Potion input, Item reactant, Potion result) {
        try {
            ADD_MIX_METHOD.invoke(null, input, reactant, result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to add mix for " + result.getRegistryName() + " from " + reactant.getRegistryName(), e);
        }
    }
}
