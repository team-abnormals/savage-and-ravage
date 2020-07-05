package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.core.SavageAndRavage;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SavageAndRavage.MODID);
	  
    public static final RegistryObject<SoundEvent> CREEPIE_HURT = createSound("entity.creepie.hurt");
    public static final RegistryObject<SoundEvent> CREEPIE_DEATH =  createSound("entity.creepie.death");
    public static final RegistryObject<SoundEvent> CREEPIE_PRIMED =  createSound("entity.creepie.primed");
    public static final RegistryObject<SoundEvent> CREEPIE_BEGIN_CONVERSION =  createSound("entity.creepie.convert");
    public static final RegistryObject<SoundEvent> CREEPIE_GROW =  createSound("entity.creepie.grow");
    public static final RegistryObject<SoundEvent> RUNES_ACTIVATED = createSound("block.runed_gloomy_tiles.activated");
    public static final RegistryObject<SoundEvent> GROWTH_MODIFICATION_SUCCESS = createSound("entity.generic.growth_success");
    public static final RegistryObject<SoundEvent> GROWTH_MODIFICATION_FAILURE = createSound("entity.generic.growth_failure");

    public static RegistryObject<SoundEvent> createSound(String name) {
  		return SRSounds.SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(SavageAndRavage.MODID, name)));
  	}
}
