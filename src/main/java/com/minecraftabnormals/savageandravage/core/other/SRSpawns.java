package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.savageandravage.common.entity.IceologerEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID)
public class SRSpawns {

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SREntities.SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.EXECUTIONER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.ICEOLOGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceologerEntity::canIceologerSpawn);
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		ResourceLocation biome = event.getName();
		if (biome != null) {
			RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome);
			if (BiomeDictionary.hasType(key, BiomeDictionary.Type.OVERWORLD) && canHostilesSpawn(biome)) {
				event.getSpawns().withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(SREntities.SKELETON_VILLAGER.get(), 5, 1, 1));
			}
			if (event.getCategory() == Biome.Category.ICY || event.getCategory() == Biome.Category.EXTREME_HILLS) {
				event.getSpawns().withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(SREntities.ICEOLOGER.get(), 8, 1, 1));
			}
		}
	}

	public static boolean canHostilesSpawn(ResourceLocation biomeName) {
		Biome biome = ForgeRegistries.BIOMES.getValue(biomeName);
		if(biome != null) return
				!(biome.getCategory() == Biome.Category.MUSHROOM ||
						biome.getCategory() == Biome.Category.NONE ||
						biome == ForgeRegistries.BIOMES.getValue(new ResourceLocation("biomesoplenty", "rainbow_valley")));
		return true;
	}
}
