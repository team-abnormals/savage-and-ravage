package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.common.EffectGrowing;
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
    //ForgeRegistries.POTIONS is quite badly named, MCP moment.
    public static final DeferredRegister<Potion> POTIONS = new DeferredRegister<>(ForgeRegistries.POTION_TYPES, SavageAndRavage.MODID);
    //Same for this, potion types???

    public static RegistryObject<Effect> GROWING = EFFECTS.register("growth", EffectGrowing::new);

    public static final RegistryObject<Potion> GROWING_NORMAL  = POTIONS.register("growing", () -> new Potion(new EffectInstance(GROWING.get(), 600)));
    public static final RegistryObject<Potion> GROWING_SHORT   = POTIONS.register("growing_short", () -> new Potion(new EffectInstance(GROWING.get(), 300)));

    public static void registerBrewingRecipes(){
        PotionBrewing.addMix(Potions.AWKWARD, Items.GOLDEN_APPLE, GROWING_NORMAL.get());
        PotionBrewing.addMix(GROWING_NORMAL.get(), Items.REDSTONE, GROWING_SHORT.get());
    }
}
