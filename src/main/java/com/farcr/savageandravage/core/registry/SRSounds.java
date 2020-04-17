package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.util.RegistryUtils;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRSounds 
{
	  public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, SavageAndRavage.MODID);
	  
	  public static RegistryObject<SoundEvent> CREEPIE_HURT = RegistryUtils.createSound("entity.creepie.hurt");
      public static RegistryObject<SoundEvent> CREEPIE_DEATH =  RegistryUtils.createSound("entity.creepie.death");
      public static RegistryObject<SoundEvent> CREEPIE_PRIMED =  RegistryUtils.createSound("entity.creepie.primed");
}
