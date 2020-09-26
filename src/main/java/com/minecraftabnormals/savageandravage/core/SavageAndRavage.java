package com.minecraftabnormals.savageandravage.core;

import com.minecraftabnormals.savageandravage.core.other.SRCompat;
import com.minecraftabnormals.savageandravage.core.registry.SREffects;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.minecraftabnormals.savageandravage.core.registry.SRParticles;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import com.minecraftabnormals.savageandravage.core.registry.SRStructures;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
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
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
    	REGISTRY_HELPER.getDeferredBlockRegister().register(modEventBus);
    	REGISTRY_HELPER.getDeferredItemRegister().register(modEventBus);
    	REGISTRY_HELPER.getDeferredEntityRegister().register(modEventBus);
    	REGISTRY_HELPER.getDeferredTileEntityRegister().register(modEventBus);
    	
        SREntities.ENTITIES.register(modEventBus);
        SRParticles.PARTICLES.register(modEventBus);
        SRSounds.SOUNDS.register(modEventBus);
        SREffects.EFFECTS.register(modEventBus);
		SREffects.POTIONS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SRConfig.COMMON_SPEC); 
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SRConfig.CLIENT_SPEC); 
        modEventBus.addListener(this::commonSetup);
    	DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
        	modEventBus.addListener(this::clientSetup);
    		modEventBus.addListener(EventPriority.LOWEST, this::registerItemColors);
        });
    }
    
    @OnlyIn(Dist.CLIENT)
	private void registerItemColors(ColorHandlerEvent.Item event) {
		REGISTRY_HELPER.processSpawnEggColors(event);
	}

    private void commonSetup(final FMLCommonSetupEvent event) {
    	DeferredWorkQueue.runLater(() -> {
    		SREffects.registerBrewingRecipes();
    		SREntities.registerEntitySpawns();
    		SREntities.registerAttributes();
    		SREntities.registerWaveMembers();
    		SRCompat.registerFlammables();
    		SRCompat.registerDispenserBehaviors();
			SRStructures.registerPools();
    	});
	}
    
    private void clientSetup(final FMLClientSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			SREntities.registerRendering();
			SRItems.registerItemProperties();
		});
    }
}
