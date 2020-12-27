package com.minecraftabnormals.savageandravage.core.other;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationManager;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationPredicates;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeSpawnsModifier;
import com.minecraftabnormals.savageandravage.common.entity.IceologerEntity;
import com.minecraftabnormals.savageandravage.common.generation.EnclosureFeature;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRGeneration {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SavageAndRavage.MOD_ID);
	//public static final DeferredRegister<Placement<?>> PLACEMENTS = DeferredRegister.create(ForgeRegistries.DECORATORS, SavageAndRavage.MOD_ID);

	public static final RegistryObject<Feature<NoFeatureConfig>> CREEPER_ENCLOSURE = FEATURES.register("creeper_enclosure", () -> new EnclosureFeature(NoFeatureConfig.field_236558_a_));
	//public static final RegistryObject<Placement<NoPlacementConfig>> CREEPER_ENCLOSURE_PLACEMENT = PLACEMENTS.register("creeper_enclosure_placement", () -> new EnclosurePlacement(NoPlacementConfig.CODEC));

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SREntities.SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.EXECUTIONER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.ICEOLOGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceologerEntity::canIceologerSpawn);
	}

	//TODO work out why structure voids are getting replaced with air blocks that act like structure voids !
	public static void registerPools() {
		JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/enclosures"	), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242845_a(CREEPER_ENCLOSURE.get().withConfiguration(NoFeatureConfig.field_236559_b_)), 1)), JigsawPattern.PlacementBehaviour.RIGID));
		JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/cages"	), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/decorations/cages/cage1"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/decorations/cages/cage2"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/decorations/cages/cage3"), 1)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));
		JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/chests"	), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/decorations/chests/chest1"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/decorations/chests/chest2"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/decorations/chests/chest3"), 1)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));
		//JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/creepers"	), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepers/creeper1"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepers/creeper2"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepers/creeper3"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepers/creeper4"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepers/creeper5"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepers/creeper6"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
		//JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/creepies"	), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepies/creepie1"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepies/creepie2"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepies/creepie3"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepies/creepie4"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepies/creepie5"), 1), Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/creepies/creepie6"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
		//JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/griefers"	), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.func_242859_b(SavageAndRavage.MOD_ID + ":enclosure/spawns/griefer"), 1)), JigsawPattern.PlacementBehaviour.RIGID));

	}

	//TODO add target decoration to pool, redstone feature on spore bomb cage

	public static void registerBiomeModifications() {
		BiomeModificationManager manager = BiomeModificationManager.INSTANCE;
		manager.addModifier(BiomeSpawnsModifier.createSpawnAdder((k, b) -> BiomeDictionary.hasType(k, BiomeDictionary.Type.OVERWORLD) && canHostilesSpawn(b.getRegistryName()), EntityClassification.MONSTER, SREntities.SKELETON_VILLAGER::get, 5, 1, 1));
		manager.addModifier(BiomeSpawnsModifier.createSpawnAdder((k, b) -> BiomeDictionary.hasType(k, BiomeDictionary.Type.OVERWORLD) && BiomeModificationPredicates.forCategory(Biome.Category.ICY, Biome.Category.EXTREME_HILLS).test(k, b), EntityClassification.MONSTER, SREntities.ICEOLOGER::get, 8, 1, 1));
		//manager.addModifier(BiomeFeatureModifier.createFeatureAdder((k, b) -> b.getGenerationSettings().hasStructure(Structure.PILLAGER_OUTPOST), GenerationStage.Decoration.SURFACE_STRUCTURES, () -> SRGeneration.CREEPER_ENCLOSURE.get().withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(CREEPER_ENCLOSURE_PLACEMENT.get().configure(NoPlacementConfig.INSTANCE))));
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