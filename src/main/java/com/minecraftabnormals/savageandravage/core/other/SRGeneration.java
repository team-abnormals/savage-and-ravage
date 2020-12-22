package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.savageandravage.common.entity.IceologerEntity;
import com.minecraftabnormals.savageandravage.common.generation.EnclosureFeature;
import com.minecraftabnormals.savageandravage.common.generation.EnclosurePlacement;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SRGeneration {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SavageAndRavage.MOD_ID);
	public static final DeferredRegister<Placement<?>> PLACEMENTS = DeferredRegister.create(ForgeRegistries.DECORATORS, SavageAndRavage.MOD_ID);

	public static final RegistryObject<Feature<NoFeatureConfig>> CREEPER_ENCLOSURE = FEATURES.register("creeper_enclosure", () -> new EnclosureFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Placement<NoPlacementConfig>> CREEPER_ENCLOSURE_PLACEMENT = PLACEMENTS.register("creeper_enclosure_placement", () -> new EnclosurePlacement(NoPlacementConfig.CODEC));

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SREntities.SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.EXECUTIONER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.ICEOLOGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceologerEntity::canIceologerSpawn);
	}

	//TODO add target decoration to pool without replacing?

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
			if(event.getGeneration().getStructures().stream().map(Supplier::get).anyMatch(s -> s.equals(StructureFeatures.PILLAGER_OUTPOST))) {
				event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Configured.CREEPER_ENCLOSURE);
			}
		}
	}

	public static final class Configured {
		public static final ConfiguredFeature<?, ?> CREEPER_ENCLOSURE = SRGeneration.CREEPER_ENCLOSURE.get().withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(CREEPER_ENCLOSURE_PLACEMENT.get().configure(NoPlacementConfig.INSTANCE));}

	public static boolean canHostilesSpawn(ResourceLocation biomeName) {
		Biome biome = ForgeRegistries.BIOMES.getValue(biomeName);
		if(biome != null) return
				!(biome.getCategory() == Biome.Category.MUSHROOM ||
						biome.getCategory() == Biome.Category.NONE ||
						biome == ForgeRegistries.BIOMES.getValue(new ResourceLocation("biomesoplenty", "rainbow_valley")));
		return true;
	}
}
