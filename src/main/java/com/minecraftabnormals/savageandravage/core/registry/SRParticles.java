package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.client.particle.CreeperSporeSprinklesParticle;
import com.minecraftabnormals.savageandravage.client.particle.CreeperSporesParticle;
import com.minecraftabnormals.savageandravage.client.particle.RuneParticle;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SRParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<BasicParticleType> CREEPER_SPORES = PARTICLES.register("creeper_spores", () -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> CREEPER_SPORE_SPRINKLES = PARTICLES.register("creeper_spore_sprinkles", () -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> RUNE = PARTICLES.register("rune", () -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> CONFUSION_BOLT = PARTICLES.register("confusion_bolt", () -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> SNOWFLAKE = PARTICLES.register("snowflake", () -> new BasicParticleType(true));

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
		ParticleManager manager = Minecraft.getInstance().particleEngine;
		if (CREEPER_SPORES.isPresent()) manager.register(CREEPER_SPORES.get(), CreeperSporesParticle.Factory::new);
		if (CREEPER_SPORE_SPRINKLES.isPresent()) manager.register(CREEPER_SPORE_SPRINKLES.get(), CreeperSporeSprinklesParticle.Factory::new);
		if (RUNE.isPresent()) manager.register(RUNE.get(), RuneParticle.Factory::new);
		if (CONFUSION_BOLT.isPresent()) manager.register(CONFUSION_BOLT.get(), SpellParticle.Factory::new);
		if (SNOWFLAKE.isPresent()) manager.register(SNOWFLAKE.get(), SpellParticle.Factory::new);
	}
}
