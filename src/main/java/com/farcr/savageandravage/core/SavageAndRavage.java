package com.farcr.savageandravage.core;

import com.farcr.savageandravage.core.registry.SRBlocks;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("savageandravage")
public class SavageAndRavage
{
    //private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "savageandravage";

    public SavageAndRavage() {
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        SRBlocks.BLOCKS.register(modEventBus);
        SRItems.ITEMS.register(modEventBus);
        SREntities.ENTITIES.register(modEventBus);
        //SRBlocks.PAINTINGS.register(modEventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
        
        modEventBus.addListener(this::commonSetup);
    	DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
        	modEventBus.addListener(this::clientSetup);
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        //SRData.registerBlockData();
        //SRFeatures.generateFeatures();
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
    	//SRData.setRenderLayers();
    	SREntities.registerRendering();
    	SREntities.addEntitySpawns();
        //SRData.registerBlockColors();
    }
}
