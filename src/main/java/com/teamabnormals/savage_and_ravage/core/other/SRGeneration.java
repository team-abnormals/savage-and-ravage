package com.teamabnormals.savage_and_ravage.core.other;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBiomeTags;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRBiomeTags;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SRGeneration {

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		ResourceLocation name = event.getName();
		MobSpawnSettingsBuilder spawns = event.getSpawns();
		Biome biome = ForgeRegistries.BIOMES.getValue(name);

		if (isTagged(biome, SRBiomeTags.HAS_ICEOLOGER)) {
			spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SREntityTypes.ICEOLOGER.get(), 5, 1, 1));
		}

		if (isTagged(biome, SRBiomeTags.HAS_SKELETON_VILLAGER) && !isTagged(biome, BlueprintBiomeTags.WITHOUT_MONSTER_SPAWNS)) {
			spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SREntityTypes.SKELETON_VILLAGER.get(), 5, 1, 1));
		}
	}

	private static boolean isTagged(Biome biome, TagKey<Biome> tagKey) {
		return ForgeRegistries.BIOMES.tags().getTag(tagKey).contains(biome);
	}
}