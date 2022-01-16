package com.teamabnormals.savageandravage.core.other;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.savageandravage.common.world.gen.feature.EnclosureFeature;
import com.teamabnormals.savageandravage.core.SavageAndRavage;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class SRFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> CREEPER_ENCLOSURE = FEATURES.register("creeper_enclosure", () -> new EnclosureFeature(NoneFeatureConfiguration.CODEC));

	public static void registerPools() {
		Pools.register(new StructureTemplatePool(new ResourceLocation(SavageAndRavage.MOD_ID, "enclosure/enclosures"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(StructurePoolElement.feature(CREEPER_ENCLOSURE.get().configured(NoneFeatureConfiguration.INSTANCE).placed()), 1)), StructureTemplatePool.Projection.RIGID));
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
}