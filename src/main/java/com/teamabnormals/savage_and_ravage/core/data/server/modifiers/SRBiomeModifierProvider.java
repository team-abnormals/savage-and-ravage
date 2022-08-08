package com.teamabnormals.savage_and_ravage.core.data.server.modifiers;

import com.mojang.serialization.JsonOps;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRBiomeTags;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.RemoveSpawnsBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;

public class SRBiomeModifierProvider {

	public static JsonCodecProvider<BiomeModifier> create(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		RegistryAccess access = RegistryAccess.builtinCopy();
		Registry<Biome> biomeRegistry = access.registryOrThrow(Registry.BIOME_REGISTRY);
		HashMap<ResourceLocation, BiomeModifier> modifiers = new HashMap<>();

		addModifier(modifiers, "remove_monster/witch", new RemoveSpawnsBiomeModifier(tag(biomeRegistry, SRBiomeTags.HAS_ICEOLOGER), HolderSet.direct(List.of(ForgeRegistries.ENTITY_TYPES.getHolder(EntityType.WITCH).get()))));
		addModifier(modifiers, "add_monster/iceologer", new AddSpawnsBiomeModifier(tag(biomeRegistry, SRBiomeTags.HAS_ICEOLOGER), List.of(new MobSpawnSettings.SpawnerData(SREntityTypes.ICEOLOGER.get(), 5, 1, 1))));

		addModifier(modifiers, "add_monster/skeleton_villager", new AddSpawnsBiomeModifier(tag(biomeRegistry, SRBiomeTags.HAS_COMMON_SKELETON_VILLAGER), List.of(new MobSpawnSettings.SpawnerData(SREntityTypes.SKELETON_VILLAGER.get(), 5, 1, 1))));
		addModifier(modifiers, "add_monster/skeleton_villager_snowy", new AddSpawnsBiomeModifier(tag(biomeRegistry, SRBiomeTags.HAS_RARE_SKELETON_VILLAGER), List.of(new MobSpawnSettings.SpawnerData(SREntityTypes.SKELETON_VILLAGER.get(), 1, 1, 1))));
		addModifier(modifiers, "add_monster/skeleton_villager_weird", new AddSpawnsBiomeModifier(tag(biomeRegistry, SRBiomeTags.HAS_WEIRD_SKELETON_VILLAGER), List.of(new MobSpawnSettings.SpawnerData(SREntityTypes.SKELETON_VILLAGER.get(), 25, 1, 1))));

		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, SavageAndRavage.MOD_ID, RegistryOps.create(JsonOps.INSTANCE, access), ForgeRegistries.Keys.BIOME_MODIFIERS, modifiers);
	}

	private static HolderSet<Biome> tag(Registry<Biome> biomeRegistry, TagKey<Biome> tagKey) {
		return new HolderSet.Named<>(biomeRegistry, tagKey);
	}

	private static void addModifier(HashMap<ResourceLocation, BiomeModifier> modifiers, String name, BiomeModifier modifier) {
		modifiers.put(new ResourceLocation(SavageAndRavage.MOD_ID, name), modifier);
	}

}