package com.teamabnormals.savage_and_ravage.core.other;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.savage_and_ravage.common.levelgen.feature.EnclosureFeature;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;

public class SRFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> CREEPER_ENCLOSURE = FEATURES.register("creeper_enclosure", () -> new EnclosureFeature(NoneFeatureConfiguration.CODEC));

	public static void registerPools() {
		Pools.register(new StructureTemplatePool(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/enclosures"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.feature(SRPlacedFeatures.CREEPER_ENCLOSURE), 1)), StructureTemplatePool.Projection.RIGID));
		Pools.register(new StructureTemplatePool(new ResourceLocation(SavageAndRavage.MOD_ID, "pillager_outpost/pillagers"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/pillager"), 1)), StructureTemplatePool.Projection.RIGID));
		Pools.register(new StructureTemplatePool(new ResourceLocation(SavageAndRavage.MOD_ID, "pillager_outpost/vindicators"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/vindicator"), 1)), StructureTemplatePool.Projection.RIGID));
		for (String biome : new String[]{"plains", "snowy", "savanna", "desert", "taiga"})
			DataUtil.addToJigsawPattern(new ResourceLocation("village/" + biome + "/zombie/villagers"), StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":village/skeleton_villager").apply(StructureTemplatePool.Projection.RIGID), 10);
		DataUtil.addToJigsawPattern(new ResourceLocation("pillager_outpost/features"), StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/feature_targets_arrow").apply(StructureTemplatePool.Projection.RIGID), 2);
		Pools.register(new StructureTemplatePool(new ResourceLocation(SavageAndRavage.MOD_ID, "pillager_outpost/note_blocks"), new ResourceLocation("empty"), noteBlocks(), StructureTemplatePool.Projection.RIGID));
	}

	private static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> noteBlocks() {
		ImmutableList.Builder<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> builder = ImmutableList.builder();
		for (int i = 0; i <= 24; i++)
			builder.add(Pair.of(StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/note_blocks/note_block" + i), 1));
		return builder.build();
	}

	public static final class SRConfiguredFeatures {
		public static final Holder<ConfiguredFeature<?, ?>> CREEPER_ENCLOSURE = register("creeper_enclosure", SRFeatures.CREEPER_ENCLOSURE.get(), NoneFeatureConfiguration.INSTANCE);

		public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<?, ?>> register(String name, F feature, FC config) {
			return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(SavageAndRavage.MOD_ID, name), new ConfiguredFeature<>(feature, config));
		}
	}

	public static final class SRPlacedFeatures {
		public static final Holder<PlacedFeature> CREEPER_ENCLOSURE = register("creeper_enclosure", SRConfiguredFeatures.CREEPER_ENCLOSURE);

		public static Holder<PlacedFeature> register(String name, Holder<? extends ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
			return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(SavageAndRavage.MOD_ID, name), new PlacedFeature(Holder.hackyErase(configuredFeature), List.of(placementModifiers)));
		}
	}
}