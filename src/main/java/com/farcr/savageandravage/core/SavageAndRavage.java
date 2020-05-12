package com.farcr.savageandravage.core;

import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.core.config.SRConfig;
import com.farcr.savageandravage.core.registry.SRBlocks;
import com.farcr.savageandravage.core.registry.SREffects;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;
import com.farcr.savageandravage.core.registry.SRParticles;
import com.farcr.savageandravage.core.registry.SRSounds;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = SavageAndRavage.MODID)
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
		//SRBlocks.PAINTINGS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SRConfig.COMMON_SPEC); 
        modEventBus.addListener(this::commonSetup);
    	DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
        	modEventBus.addListener(this::clientSetup);
    		modEventBus.addListener(EventPriority.LOWEST, this::registerItemColors);
        });
    }
    
    /**
     * @author SmellyModder(Luke Tonon)
     */
    @OnlyIn(Dist.CLIENT)
	private void registerItemColors(ColorHandlerEvent.Item event) {
		REGISTRY_HELPER.processSpawnEggColors(event);
	}

    private void commonSetup(final FMLCommonSetupEvent event) {
        //SRData.registerBlockData();
        //SRFeatures.generateFeatures();
    	SRBlocks.registerFlammables();
		DispenserBlock.registerDispenseBehavior(SRItems.CREEPER_SPORES.get(), new ProjectileDispenseBehavior() {
			@Override
			protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				return new CreeperSporeCloudEntity(worldIn, position.getX(), position.getY(), position.getZ());
			}
		});
		SREffects.registerBrewingRecipes();
	}
    
    private void clientSetup(final FMLClientSetupEvent event) {
    	//SRData.setRenderLayers();
    	SREntities.registerRendering();
    	SREntities.addEntitySpawns();
        //SRData.registerBlockColors();
    }
}
