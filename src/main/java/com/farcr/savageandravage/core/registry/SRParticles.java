package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.common.particles.CreeperSporeParticle;
import com.farcr.savageandravage.core.SavageAndRavage;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRParticles 
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES = new DeferredRegister<>(ForgeRegistries.PARTICLE_TYPES, SavageAndRavage.MODID);
	
	public static final RegistryObject<BasicParticleType> CREEPER_SPORES = createParticleType("creeper_spores", true);
	
	private static RegistryObject<BasicParticleType> createParticleType(String name, boolean alwaysShow) {
		RegistryObject<BasicParticleType> particleType = PARTICLES.register(name, () -> new BasicParticleType(alwaysShow));
		return particleType;
	}
	
	@EventBusSubscriber(modid = SavageAndRavage.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegisterParticleFactories {
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerParticleTypes(ParticleFactoryRegisterEvent event) {
			Minecraft.getInstance().particles.registerFactory(CREEPER_SPORES.get(), CreeperSporeParticle.Factory::new);
		}
	}
}
