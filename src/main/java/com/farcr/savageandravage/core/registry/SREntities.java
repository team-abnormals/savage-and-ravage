package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.client.render.CreepieRenderer;
import com.farcr.savageandravage.client.render.NoModelRenderer;
import com.farcr.savageandravage.client.render.RunePrisonRenderer;
import com.farcr.savageandravage.client.render.SkeletonVillagerRenderer;
import com.farcr.savageandravage.client.render.SporeBombRenderer;
import com.farcr.savageandravage.common.entity.BurningBannerEntity;
import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.common.entity.RunePrisonEntity;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.farcr.savageandravage.common.entity.block.SporeBombEntity;
import com.farcr.savageandravage.core.SavageAndRavage;
import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SREntities {
	public static final RegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER;
	public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, SavageAndRavage.MODID);
	
    public static final RegistryObject<EntityType<CreepieEntity>> CREEPIE 						= HELPER.createLivingEntity("creepie", CreepieEntity::new, EntityClassification.CREATURE, 0.90F, 1.90F);
    public static final RegistryObject<EntityType<SkeletonVillagerEntity>> SKELETON_VILLAGER 	= HELPER.createLivingEntity("skeleton_villager", SkeletonVillagerEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
    public static final RegistryObject<EntityType<CreeperSporeCloudEntity>> CREEPER_SPORE_CLOUD = ENTITIES.register("creeper_spore_cloud", () -> EntityType.Builder.<CreeperSporeCloudEntity>create(CreeperSporeCloudEntity::new, EntityClassification.MISC).immuneToFire().size(0.25F, 0.25F).build("savageandravage:creeper_spore_cloud"));
    public static final RegistryObject<EntityType<SporeBombEntity>> SPORE_BOMB 					= ENTITIES.register("spore_bomb", () -> EntityType.Builder.<SporeBombEntity>create(SporeBombEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).build("savageandravage:spore_bomb"));
    public static final RegistryObject<EntityType<BurningBannerEntity>> BURNING_BANNER 			= ENTITIES.register("burning_banner", () -> EntityType.Builder.<BurningBannerEntity>create(BurningBannerEntity::new, EntityClassification.MISC).immuneToFire().build("savageandravage:burning_banner"));
    public static final RegistryObject<EntityType<RunePrisonEntity>> RUNE_PRISON 				= ENTITIES.register("rune_prison", () -> EntityType.Builder.<RunePrisonEntity>create(RunePrisonEntity::new, EntityClassification.MISC).immuneToFire().size(1.35F, 0.7F).build("savageandravage:rune_prison"));
    
    @OnlyIn(Dist.CLIENT)
    public static void registerRendering() {
        RenderingRegistry.registerEntityRenderingHandler(CREEPIE.get(), CreepieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SKELETON_VILLAGER.get(), SkeletonVillagerRenderer::new);
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
    	if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.MUSHROOM && biome.getCategory() != Biome.Category.NONE) {
    		biome.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(SKELETON_VILLAGER.get(), 5, 5, 5)); //Rationalisation for this is that it used to be a pillager patrol
    	}
    }
}
