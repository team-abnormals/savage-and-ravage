package com.teamabnormals.savage_and_ravage.core.other;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRBiomeTags;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.RemoveSpawnsBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SRBiomeModifiers {

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		removeSpawn(context, "witch", SRBiomeTags.HAS_ICEOLOGER, EntityType.WITCH);
		addSpawn(context, "iceologer", SRBiomeTags.HAS_ICEOLOGER, new MobSpawnSettings.SpawnerData(SREntityTypes.ICEOLOGER.get(), 5, 1, 1));

		addSpawn(context, "skeleton_villager", SRBiomeTags.HAS_COMMON_SKELETON_VILLAGER, new MobSpawnSettings.SpawnerData(SREntityTypes.SKELETON_VILLAGER.get(), 5, 1, 1));
		addSpawn(context, "skeleton_villager_snowy", SRBiomeTags.HAS_RARE_SKELETON_VILLAGER, new MobSpawnSettings.SpawnerData(SREntityTypes.SKELETON_VILLAGER.get(), 1, 1, 1));
		addSpawn(context, "skeleton_villager_weird", SRBiomeTags.HAS_WEIRD_SKELETON_VILLAGER, new MobSpawnSettings.SpawnerData(SREntityTypes.SKELETON_VILLAGER.get(), 25, 1, 1));
	}

	private static void addSpawn(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, MobSpawnSettings.SpawnerData... spawns) {
		register(context, "add_spawn/" + name, () -> new AddSpawnsBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), List.of(spawns)));
	}

	private static void removeSpawn(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, EntityType<?>... types) {
		register(context, "remove_spawn/" + name, () -> new RemoveSpawnsBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), HolderSet.direct(Stream.of(types).map(type -> ForgeRegistries.ENTITY_TYPES.getHolder(type).get()).collect(Collectors.toList()))));
	}

	private static void register(BootstapContext<BiomeModifier> context, String name, Supplier<? extends BiomeModifier> modifier) {
		context.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, SavageAndRavage.location(name)), modifier.get());
	}
}