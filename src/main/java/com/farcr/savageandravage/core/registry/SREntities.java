package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.client.render.CreepieRenderer;
import com.farcr.savageandravage.client.render.GrieferRenderer;
import com.farcr.savageandravage.client.render.NoModelRenderer;
import com.farcr.savageandravage.client.render.RunePrisonRenderer;
import com.farcr.savageandravage.client.render.SkeletonVillagerRenderer;
import com.farcr.savageandravage.client.render.SporeBombRenderer;
import com.farcr.savageandravage.common.entity.BurningBannerEntity;
import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.common.entity.GrieferEntity;
import com.farcr.savageandravage.common.entity.RunePrisonEntity;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.farcr.savageandravage.common.entity.block.SporeBombEntity;
import com.farcr.savageandravage.core.SavageAndRavage;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SREntities {
	public static final RegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER;
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SavageAndRavage.MODID);
	
    public static final RegistryObject<EntityType<CreepieEntity>> CREEPIE 						= HELPER.createLivingEntity("creepie", CreepieEntity::new, EntityClassification.CREATURE, 0.85F, 1.90F);
    public static final RegistryObject<EntityType<SkeletonVillagerEntity>> SKELETON_VILLAGER 	= HELPER.createLivingEntity("skeleton_villager", SkeletonVillagerEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
    public static final RegistryObject<EntityType<GrieferEntity>> GRIEFER 	                    = HELPER.createLivingEntity("griefer", GrieferEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
    public static final RegistryObject<EntityType<CreeperSporeCloudEntity>> CREEPER_SPORE_CLOUD = ENTITIES.register("creeper_spore_cloud", () -> EntityType.Builder.<CreeperSporeCloudEntity>create(CreeperSporeCloudEntity::new, EntityClassification.MISC).immuneToFire().size(0.25F, 0.25F).build("savageandravage:creeper_spore_cloud"));
    public static final RegistryObject<EntityType<SporeBombEntity>> SPORE_BOMB 					= ENTITIES.register("spore_bomb", () -> EntityType.Builder.<SporeBombEntity>create(SporeBombEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).build("savageandravage:spore_bomb"));
    public static final RegistryObject<EntityType<BurningBannerEntity>> BURNING_BANNER 			= ENTITIES.register("burning_banner", () -> EntityType.Builder.<BurningBannerEntity>create(BurningBannerEntity::new, EntityClassification.MISC).immuneToFire().build("savageandravage:burning_banner"));
    public static final RegistryObject<EntityType<RunePrisonEntity>> RUNE_PRISON 				= ENTITIES.register("rune_prison", () -> EntityType.Builder.<RunePrisonEntity>create(RunePrisonEntity::new, EntityClassification.MISC).immuneToFire().size(1.35F, 0.7F).build("savageandravage:rune_prison"));
    
    @OnlyIn(Dist.CLIENT)
    public static void registerRendering() {
        RenderingRegistry.registerEntityRenderingHandler(CREEPIE.get(), CreepieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SKELETON_VILLAGER.get(), SkeletonVillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GRIEFER.get(), GrieferRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(CREEPER_SPORE_CLOUD.get(), NoModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SPORE_BOMB.get(), SporeBombRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(BURNING_BANNER.get(), NoModelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(RUNE_PRISON.get(), RunePrisonRenderer::new);
    }
    
    public static void addEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        ForgeRegistries.BIOMES.getValues().forEach(SREntities::addSpawns);
	}

    public static void addSpawns(Biome biome) {
    	if (BiomeDictionary.hasType(biome, Type.OVERWORLD) && biome.getCategory() != Biome.Category.MUSHROOM && biome.getCategory() != Biome.Category.NONE) {
    		biome.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(SKELETON_VILLAGER.get(), 5, 5, 5)); //Rationalisation for this is that it used to be a pillager patrol
    	}
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class registerStuffAfterEntityType
    {
    	@SubscribeEvent(priority = EventPriority.LOWEST)
        public static void imstuff(final RegistryEvent.Register<EntityType<?>> event) 
    	{
    		GlobalEntityTypeAttributes.put(CREEPIE.get(), CreepieEntity.func_234200_m_().func_233813_a_());
    		GlobalEntityTypeAttributes.put(GRIEFER.get(), GrieferEntity.func_234296_eI_().func_233813_a_());
    		GlobalEntityTypeAttributes.put(SKELETON_VILLAGER.get(), SkeletonEntity.func_234275_m_().func_233813_a_());
    		Raid.WaveMember.create("griefer", SREntities.GRIEFER.get(), new int[]{0, 2, 1, 2, 3, 3, 4, 3});
    	}
    }
}
