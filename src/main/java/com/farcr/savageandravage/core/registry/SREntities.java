package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.client.render.CreeperSporeRenderer;
import com.farcr.savageandravage.client.render.CreepieRenderer;
import com.farcr.savageandravage.client.render.SkeletonVillagerRenderer;
import com.farcr.savageandravage.client.render.SporeBombRenderer;
import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.farcr.savageandravage.common.entity.block.SporeBombEntity;
import com.farcr.savageandravage.core.SavageAndRavage;

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
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, SavageAndRavage.MODID);
    public static RegistryObject<EntityType<CreepieEntity>> CREEPIE = ENTITIES.register("creepie", () -> EntityType.Builder.<CreepieEntity>create(CreepieEntity::new, EntityClassification.MONSTER).size(0.51F, 1.02F).build("savageandravage:creepie"));
    public static RegistryObject<EntityType<SkeletonVillagerEntity>> SKELETON_VILLAGER = ENTITIES.register("skeleton_villager", () -> EntityType.Builder.<SkeletonVillagerEntity>create(SkeletonVillagerEntity::new, EntityClassification.MONSTER).size(0.6F, 1.99F).build("savageandravage:skeleton_villager"));
    public static RegistryObject<EntityType<CreeperSporeCloudEntity>> CREEPER_SPORE_CLOUD = ENTITIES.register("creeper_spore_cloud", () -> EntityType.Builder.<CreeperSporeCloudEntity>create(CreeperSporeCloudEntity::new, EntityClassification.MISC).size(0.6F, 1.99F).build("savageandravage:creeper_spore_cloud"));
    public static RegistryObject<EntityType<SporeBombEntity>> SPORE_BOMB = ENTITIES.register("spore_bomb", () -> EntityType.Builder.<SporeBombEntity>create(SporeBombEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).build("savageandravage:spore_bomb"));    
    
    @OnlyIn(Dist.CLIENT)
    public static void registerRendering() {
        RenderingRegistry.registerEntityRenderingHandler((EntityType<? extends CreepieEntity>)CREEPIE.get(), CreepieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler((EntityType<? extends SkeletonVillagerEntity>)SKELETON_VILLAGER.get(), SkeletonVillagerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler((EntityType<? extends CreeperSporeCloudEntity>)CREEPER_SPORE_CLOUD.get(), CreeperSporeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler((EntityType<? extends SporeBombEntity>)SPORE_BOMB.get(), SporeBombRenderer::new);
    }
    
    public static void addEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        ForgeRegistries.BIOMES.getValues().forEach(SREntities::addSpawns);
	}

    public static void addSpawns(Biome biome) {
    	if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.MUSHROOM && biome.getCategory() != Biome.Category.NONE) {
    		biome.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(SKELETON_VILLAGER.get(), 5, 5, 5));
    	}
    }
}
