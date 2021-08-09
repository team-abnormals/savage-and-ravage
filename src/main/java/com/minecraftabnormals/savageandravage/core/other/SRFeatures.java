package com.minecraftabnormals.savageandravage.core.other;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.abnormals_core.common.world.modification.*;
import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.savageandravage.common.world.gen.feature.EnclosureFeature;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class SRFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<Feature<NoFeatureConfig>> CREEPER_ENCLOSURE = FEATURES.register("creeper_enclosure", () -> new EnclosureFeature(NoFeatureConfig.CODEC));

	public static void registerPools() {
		JigsawPatternRegistry.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/enclosures"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.feature(CREEPER_ENCLOSURE.get().configured(NoFeatureConfig.INSTANCE)), 1)), JigsawPattern.PlacementBehaviour.RIGID));
		JigsawPatternRegistry.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "pillager_outpost/pillagers"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/pillager"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
		JigsawPatternRegistry.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MOD_ID, "pillager_outpost/vindicators"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(JigsawPiece.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/vindicator"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
		for (String biome : new String[]{"plains", "snowy", "savanna", "desert", "taiga"})
			DataUtil.addToJigsawPattern(new ResourceLocation("village/" + biome + "/zombie/villagers"), JigsawPiece.legacy(SavageAndRavage.MOD_ID + ":village/skeleton_villager").apply(JigsawPattern.PlacementBehaviour.RIGID), 10);
	}

	public static void registerBiomeModifications() {
		BiomeModificationManager manager = BiomeModificationManager.INSTANCE;
		manager.addModifier(CustomSpawnsModifier.createSpawnAdder((key, biome) -> BiomeDictionary.hasType(key, BiomeDictionary.Type.OVERWORLD) && !BiomeModificationPredicates.forCategory(Biome.Category.MUSHROOM, Biome.Category.NONE).test(key, biome), EntityClassification.MONSTER, SREntities.SKELETON_VILLAGER::get, 5, 1, 1));
		manager.addModifier(BiomeSpawnsModifier.createSpawnAdder((key, biome) -> BiomeDictionary.hasType(key, BiomeDictionary.Type.OVERWORLD) && BiomeModificationPredicates.forCategory(Biome.Category.ICY, Biome.Category.EXTREME_HILLS).test(key, biome), EntityClassification.MONSTER, SREntities.ICEOLOGER::get, 8, 1, 1));
	}

	//All this is hacky and won't be needed after AC refactors this system
	static class CustomSpawnsModifier extends BiomeModifier {
		private final BiomeSpawnsModifier modifier;

		public CustomSpawnsModifier(BiPredicate<RegistryKey<Biome>, Biome> shouldModify, EntityClassification classification, Supplier<EntityType<?>> typeSupplier, int weight, int minCount, int maxCount) {
			super(shouldModify, (context) -> context.event.getSpawns().addSpawn(classification, new MobSpawnInfo.Spawners(typeSupplier.get(), weight, minCount, maxCount)));
			this.modifier = BiomeSpawnsModifier.createSpawnAdder(shouldModify, classification, typeSupplier, weight, minCount, maxCount);
		}

		@Override
		public boolean test(BiomeModificationContext context) {
			ResourceLocation name = context.event.getName();
			return this.modifier.test(context) && (name == null || !name.equals(new ResourceLocation("biomesoplenty", "rainbow_hills")));
		}

		public static CustomSpawnsModifier createSpawnAdder(BiPredicate<RegistryKey<Biome>, Biome> shouldModify, EntityClassification classification, Supplier<EntityType<?>> typeSupplier, int weight, int minCount, int maxCount) {
			return new CustomSpawnsModifier(shouldModify, classification, typeSupplier, weight, minCount, maxCount);
		}
	}
}