package com.minecraftabnormals.savageandravage.core.other;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationManager;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationPredicates;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeSpawnsModifier;
import com.minecraftabnormals.savageandravage.common.entity.IceologerEntity;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.common.generation.EnclosureFeature;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRGeneration {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<Feature<NoFeatureConfig>> CREEPER_ENCLOSURE = FEATURES.register("creeper_enclosure", () -> new EnclosureFeature(NoFeatureConfig.field_236558_a_));

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SREntities.SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.EXECUTIONER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.ICEOLOGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceologerEntity::canIceologerSpawn);
	}

	public static void registerPools() {
		JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/enclosures"	), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242845_a(CREEPER_ENCLOSURE.get().withConfiguration(NoFeatureConfig.field_236559_b_)), 1)), JigsawPattern.PlacementBehaviour.RIGID));
	}

	//TODO add target decoration to pool

	public static void registerBiomeModifications() {
		BiomeModificationManager manager = BiomeModificationManager.INSTANCE;
		manager.addModifier(BiomeSpawnsModifier.createSpawnAdder((k, b) -> BiomeDictionary.hasType(k, BiomeDictionary.Type.OVERWORLD) && canHostilesSpawn(b.getRegistryName()), EntityClassification.MONSTER, SREntities.SKELETON_VILLAGER::get, 5, 1, 1));
		manager.addModifier(BiomeSpawnsModifier.createSpawnAdder((k, b) -> BiomeDictionary.hasType(k, BiomeDictionary.Type.OVERWORLD) && BiomeModificationPredicates.forCategory(Biome.Category.ICY, Biome.Category.EXTREME_HILLS).test(k, b), EntityClassification.MONSTER, SREntities.ICEOLOGER::get, 8, 1, 1));
	}

	public static boolean canHostilesSpawn(ResourceLocation biomeName) {
		Biome biome = ForgeRegistries.BIOMES.getValue(biomeName);
		boolean canHostilesSpawn = true;
		if(biome != null) {
			if (biome.getCategory() != Biome.Category.MUSHROOM && biome.getCategory() != Biome.Category.NONE ) {
				canHostilesSpawn = false;
			} else canHostilesSpawn = biome == ForgeRegistries.BIOMES.getValue(new ResourceLocation("biomesoplenty", "rainbow_valley"));
		}
		return canHostilesSpawn;
	}
}