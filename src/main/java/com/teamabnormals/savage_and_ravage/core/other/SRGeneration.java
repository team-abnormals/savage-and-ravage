package com.teamabnormals.savage_and_ravage.core.other;

import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SRGeneration {

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		ResourceLocation name = event.getName();
		BiomeCategory category = event.getCategory();
		MobSpawnSettingsBuilder spawns = event.getSpawns();

		if (name == null) return;

		ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, name);

		if (BiomeDictionary.getTypes(key).contains(BiomeDictionary.Type.OVERWORLD)) {
			if ((category.equals(BiomeCategory.ICY) || category.equals(BiomeCategory.MOUNTAIN)) && !DataUtil.matchesKeys(name, Biomes.MEADOW)) {
				spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SREntityTypes.ICEOLOGER.get(), 5, 1, 1));
			}

			if (!category.equals(BiomeCategory.MUSHROOM) && !category.equals(BiomeCategory.NONE)) {
				spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SREntityTypes.SKELETON_VILLAGER.get(), 5, 1, 1));
			}
		}
	}
}