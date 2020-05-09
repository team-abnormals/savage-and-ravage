package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.common.particles.CreeperSporeParticle;
import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.util.RegistryUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRParticles 
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES = new DeferredRegister<>(ForgeRegistries.PARTICLE_TYPES, SavageAndRavage.MODID);
	
	public static final RegistryObject<BasicParticleType> CREEPER_SPORES = RegistryUtils.createParticle("creeper_spores", true);
	
	@EventBusSubscriber(modid = SavageAndRavage.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegisterParticleFactories {
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerParticleTypes(ParticleFactoryRegisterEvent event) {
	        if(checkForNonNullWithReflectionCauseForgeIsBaby(CREEPER_SPORES)) {
	        	Minecraft.getInstance().particles.registerFactory(CREEPER_SPORES.get(), CreeperSporeParticle.Factory::new);
	        }
		}
	}
	
	/*
	 * @author SmellyModder(Luke Tonon)
	 */
	private static boolean checkForNonNullWithReflectionCauseForgeIsBaby(RegistryObject<BasicParticleType> registryObject) {
	    return ObfuscationReflectionHelper.getPrivateValue(RegistryObject.class, registryObject, "value") != null;
	}
}
