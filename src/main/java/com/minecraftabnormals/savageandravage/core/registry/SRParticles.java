package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.common.particles.CreeperSporeParticle;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
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

@EventBusSubscriber(modid = SavageAndRavage.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SRParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SavageAndRavage.MODID);

    public static final RegistryObject<BasicParticleType> CREEPER_SPORES = PARTICLES.register("creeper_spores", () -> new BasicParticleType(true));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        // Bug: This should be in the deferred work queue, but then the particle manager freaks out
        if (CREEPER_SPORES.isPresent())
            Minecraft.getInstance().particles.registerFactory(CREEPER_SPORES.get(), CreeperSporeParticle.Factory::new);
    }
}
