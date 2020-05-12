package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.core.SavageAndRavage;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRSounds 
{
	  public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, SavageAndRavage.MODID);
	  
	  public static final RegistryObject<SoundEvent> CREEPIE_HURT = createSound("entity.creepie.hurt");
      public static final RegistryObject<SoundEvent> CREEPIE_DEATH =  createSound("entity.creepie.death");
      public static final RegistryObject<SoundEvent> CREEPIE_PRIMED =  createSound("entity.creepie.primed");
      public static final RegistryObject<SoundEvent> RUNES_ACTIVATED = createSound("block.runed_gloomy_tiles.activated");
      
      public static RegistryObject<SoundEvent> createSound(String name) {
  		return SRSounds.SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(SavageAndRavage.MODID, name)));
  	}
}
