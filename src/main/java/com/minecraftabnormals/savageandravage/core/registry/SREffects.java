package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.common.effect.GrowingEffect;
import com.minecraftabnormals.savageandravage.common.effect.ShrinkingEffect;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.*;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SREffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, SavageAndRavage.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, SavageAndRavage.MODID);

    public static final RegistryObject<Effect> GROWING = EFFECTS.register("growing", GrowingEffect::new);
    public static final RegistryObject<Effect> SHRINKING = EFFECTS.register("shrinking", ShrinkingEffect::new);

    public static final RegistryObject<Potion> GROWTH_NORMAL = POTIONS.register("growth", () -> new Potion(new EffectInstance(GROWING.get(), 600)));
    public static final RegistryObject<Potion> GROWTH_LONG = POTIONS.register("growth_long", () -> new Potion(new EffectInstance(GROWING.get(), 1800)));
    public static final RegistryObject<Potion> YOUTH_NORMAL = POTIONS.register("youth", () -> new Potion(new EffectInstance(SHRINKING.get(), 600)));
    public static final RegistryObject<Potion> YOUTH_LONG = POTIONS.register("youth_long", () -> new Potion(new EffectInstance(SHRINKING.get(), 1800)));

    public static void registerBrewingRecipes() {
        addMix(Potions.AWKWARD, Ingredient.fromItems(Items.GOLDEN_APPLE), GROWTH_NORMAL.get());
        addMix(GROWTH_NORMAL.get(), Ingredient.fromItems(Items.REDSTONE), GROWTH_LONG.get());
        addMix(GROWTH_NORMAL.get(), Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE), YOUTH_NORMAL.get());
        addMix(GROWTH_LONG.get(), Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE), YOUTH_LONG.get());
        addMix(YOUTH_NORMAL.get(), Ingredient.fromItems(Items.REDSTONE), YOUTH_LONG.get());
    }

    private static void addMix(Potion input, Ingredient ingredient, Potion result) {
        BrewingRecipeRegistry.addRecipe(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), input)), ingredient, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), result));
        BrewingRecipeRegistry.addRecipe(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), input)), ingredient, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), result));
        BrewingRecipeRegistry.addRecipe(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), input)), ingredient, PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), result));
    }
}
