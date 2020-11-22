package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.client.particle.CreeperSporeParticle;
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

@EventBusSubscriber(modid = SavageAndRavage.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SRParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SavageAndRavage.MODID);

    public static final RegistryObject<BasicParticleType> CREEPER_SPORES 	= PARTICLES.register("creeper_spores", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> RUNE          	= PARTICLES.register("rune", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> SNOWFLAKE 		= PARTICLES.register("snowflake", () -> new BasicParticleType(true));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        // Bug: This should be in the deferred work queue, but then the particle manager freaks out
    	ParticleManager manager = Minecraft.getInstance().particles;
        if (CREEPER_SPORES.isPresent()) manager.registerFactory(CREEPER_SPORES.get(), CreeperSporeParticle.Factory::new);
        if (RUNE.isPresent()) manager.registerFactory(RUNE.get(), RuneParticle.Factory::new);
        if (SNOWFLAKE.isPresent()) manager.registerFactory(SNOWFLAKE.get(), SpellParticle.Factory::new);
    }
}
