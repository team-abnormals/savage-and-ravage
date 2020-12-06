package com.minecraftabnormals.savageandravage.core;

import com.minecraftabnormals.abnormals_core.core.util.registry.RegistryHelper;
import com.minecraftabnormals.savageandravage.client.render.IceChunkRenderer;
import com.minecraftabnormals.savageandravage.core.other.SRCompat;
import com.minecraftabnormals.savageandravage.core.registry.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SavageAndRavage.MODID)
public class SavageAndRavage {

    public static final String MODID = "savageandravage";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MODID);

    public SavageAndRavage() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRY_HELPER.getBlockSubHelper().register(bus);
        REGISTRY_HELPER.getItemSubHelper().register(bus);
        REGISTRY_HELPER.getEntitySubHelper().register(bus);
        REGISTRY_HELPER.getSoundSubHelper().register(bus);

        SREntities.ENTITIES.register(bus);
        SRParticles.PARTICLES.register(bus);
        SREffects.EFFECTS.register(bus);
        SREffects.POTIONS.register(bus);
        SRAttributes.ATTRIBUTES.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SRConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SRConfig.CLIENT_SPEC);
        bus.addListener(this::commonSetup);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(this::clientSetup);
            bus.addListener(this::registerModels);
        });
    }

    private void registerModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(IceChunkRenderer.MODEL_LOCATION);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            SREffects.registerBrewingRecipes();
            SREntities.registerEntitySpawns();
            SREntities.registerAttributes();
            SREntities.registerWaveMembers();
            SRCompat.registerFlammables();
            SRCompat.registerDispenserBehaviors();
        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            SREntities.registerRendering();
            SRItems.registerItemProperties();
        });
    }
}
