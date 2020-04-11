package com.farcr.savageandravage.core;

import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.common.item.SRSpawnEgg;
import com.farcr.savageandravage.common.structure.PillagerOutpostReplacer;
import com.farcr.savageandravage.core.events.SREvents;
import com.farcr.savageandravage.core.registry.SRBlocks;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;
import com.farcr.savageandravage.core.registry.SRParticles;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
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
        SRParticles.PARTICLES.register(modEventBus);
        //SRBlocks.PAINTINGS.register(modEventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SREvents.class);
        
        modEventBus.addListener(this::commonSetup);
    	DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
        	modEventBus.addListener(this::clientSetup);
    		modEventBus.addListener(EventPriority.LOWEST, this::registerItemColors);
        });
    }
    
    /**
     * @author SmellyModder(Luke Tonon)
     */
	private void registerItemColors(ColorHandlerEvent.Item event) 
	{
	   for(RegistryObject<Item> items : SRItems.SPAWN_EGGS) 
	   {	
	    if (ObfuscationReflectionHelper.getPrivateValue(RegistryObject.class, items, "value") != null) {
	    Item item = items.get();
	    if (item instanceof SRSpawnEgg) 
	    {
	       event.getItemColors().register((itemColor, itemsIn) -> 
	       {
	         return ((SRSpawnEgg) item).getColor(itemsIn);
	       }, item);
	    }
	  }
	 }
	}

    private void commonSetup(final FMLCommonSetupEvent event) {
        //SRData.registerBlockData();
        //SRFeatures.generateFeatures();
		DispenserBlock.registerDispenseBehavior(SRItems.CREEPER_SPORES.get(), new ProjectileDispenseBehavior() {
			@Override
			protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				CreeperSporeCloudEntity creeperSporeCloud = new CreeperSporeCloudEntity(worldIn, position.getX(), position.getY(), position.getZ());
				return creeperSporeCloud;
			}
		});
		PillagerOutpostReplacer.inject();
	}
    
    private void clientSetup(final FMLClientSetupEvent event) {
    	//SRData.setRenderLayers();
    	SREntities.registerRendering();
    	SREntities.addEntitySpawns();
        //SRData.registerBlockColors();
    }
}
