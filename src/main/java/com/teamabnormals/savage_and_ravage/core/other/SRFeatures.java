package com.teamabnormals.savage_and_ravage.core.other;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.savage_and_ravage.common.levelgen.feature.EnclosureFeature;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
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

	public static void addToJigsawPatterns() {
		for (String biome : new String[]{"plains", "snowy", "savanna", "desert", "taiga"})
			DataUtil.addToJigsawPattern(new ResourceLocation("village/" + biome + "/zombie/villagers"), access -> StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":village/skeleton_villager").apply(StructureTemplatePool.Projection.RIGID), 10);
		DataUtil.addToJigsawPattern(new ResourceLocation("pillager_outpost/features"), access -> StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/feature_targets_arrow").apply(StructureTemplatePool.Projection.RIGID), 2);
	}

	public static final class SRTemplatePools {
		public static final ResourceKey<StructureTemplatePool> ENCLOSURES = createKey("enclosure/enclosures");
		public static final ResourceKey<StructureTemplatePool> PILLAGERS = createKey("pillager_outpost/pillagers");
		public static final ResourceKey<StructureTemplatePool> VINDICATORS = createKey("pillager_outpost/vindicators");
		public static final ResourceKey<StructureTemplatePool> NOTE_BLOCKS = createKey("pillager_outpost/note_blocks");

		public static void bootstrap(BootstapContext<StructureTemplatePool> context) {
			HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
			HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);

			Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);

			context.register(ENCLOSURES, new StructureTemplatePool(empty, ImmutableList.of(Pair.of(StructurePoolElement.feature(features.getOrThrow(SRPlacedFeatures.CREEPER_ENCLOSURE)), 1)), StructureTemplatePool.Projection.RIGID));
			context.register(PILLAGERS, new StructureTemplatePool(empty, ImmutableList.of(Pair.of(StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/pillager"), 1)), StructureTemplatePool.Projection.RIGID));
			context.register(VINDICATORS, new StructureTemplatePool(empty, ImmutableList.of(Pair.of(StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/vindicator"), 1)), StructureTemplatePool.Projection.RIGID));
			context.register(NOTE_BLOCKS, new StructureTemplatePool(empty, noteBlocks(), StructureTemplatePool.Projection.RIGID));
		}

		private static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> noteBlocks() {
			ImmutableList.Builder<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> builder = ImmutableList.builder();
			for (int i = 0; i <= 24; i++)
				builder.add(Pair.of(StructurePoolElement.legacy(SavageAndRavage.MOD_ID + ":pillager_outpost/note_blocks/note_block" + i), 1));
			return builder.build();
		}

		public static ResourceKey<StructureTemplatePool> createKey(String name) {
			return ResourceKey.create(Registries.TEMPLATE_POOL, SavageAndRavage.location(name));
		}
	}

	public static final class SRConfiguredFeatures {
		public static final ResourceKey<ConfiguredFeature<?, ?>> CREEPER_ENCLOSURE = createKey("creeper_enclosure");

		public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
			register(context, CREEPER_ENCLOSURE, SRFeatures.CREEPER_ENCLOSURE.get(), NoneFeatureConfiguration.INSTANCE);
		}

		public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
			return ResourceKey.create(Registries.CONFIGURED_FEATURE, SavageAndRavage.location(name));
		}

		public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
			context.register(key, new ConfiguredFeature<>(feature, config));
		}

	}

	public static final class SRPlacedFeatures {
		public static final ResourceKey<PlacedFeature> CREEPER_ENCLOSURE = createKey("creeper_enclosure");

		public static void bootstrap(BootstapContext<PlacedFeature> context) {
			register(context, CREEPER_ENCLOSURE, SRConfiguredFeatures.CREEPER_ENCLOSURE);
		}

		public static ResourceKey<PlacedFeature> createKey(String name) {
			return ResourceKey.create(Registries.PLACED_FEATURE, SavageAndRavage.location(name));
		}

		public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
			context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
		}

		public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
			register(context, key, feature, List.of(modifiers));
		}
	}
}