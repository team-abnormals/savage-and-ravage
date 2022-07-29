package com.teamabnormals.savage_and_ravage.core.registry;

import com.teamabnormals.savage_and_ravage.client.particle.*;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SRParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<SimpleParticleType> CREEPER_SPORES = PARTICLES.register("creeper_spores", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> CREEPER_SPORE_SPRINKLES = PARTICLES.register("creeper_spore_sprinkles", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> RUNE = PARTICLES.register("rune", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> CONFUSION_BOLT = PARTICLES.register("confusion_bolt", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> SNOWFLAKE = PARTICLES.register("snowflake", () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> CLEAVER_SWEEP = PARTICLES.register("cleaver_sweep", () -> new SimpleParticleType(true));

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
		event.register(CREEPER_SPORES.get(), CreeperSporesParticle.Factory::new);
		event.register(CREEPER_SPORE_SPRINKLES.get(), CreeperSporeSprinklesParticle.Factory::new);
		event.register(RUNE.get(), RuneParticle.Factory::new);
		event.register(CONFUSION_BOLT.get(), ConfusionBoltParticle.Factory::new);
		event.register(SNOWFLAKE.get(), SpellParticle.Provider::new);
		event.register(CLEAVER_SWEEP.get(), CleaverSweepParticle.Factory::new);
	}
}