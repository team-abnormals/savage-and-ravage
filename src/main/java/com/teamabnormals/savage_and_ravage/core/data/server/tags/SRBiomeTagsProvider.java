package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRBiomeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SRBiomeTagsProvider extends BiomeTagsProvider {

	public SRBiomeTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SavageAndRavage.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(SRBiomeTags.HAS_ICEOLOGER).addTag(Tags.Biomes.IS_SNOWY);
		TagAppender<Biome> hasCommonSkeletonVillager = this.tag(SRBiomeTags.HAS_COMMON_SKELETON_VILLAGER);
		MultiNoiseBiomeSource.Preset.OVERWORLD.possibleBiomes().forEach((biome) -> {
			if (biome != Biomes.SNOWY_PLAINS && biome != Biomes.ICE_SPIKES && biome != Biomes.MUSHROOM_FIELDS && biome != Biomes.DEEP_DARK)
				hasCommonSkeletonVillager.add(biome);
		});
		this.tag(SRBiomeTags.HAS_RARE_SKELETON_VILLAGER).add(Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES);
		this.tag(SRBiomeTags.HAS_WEIRD_SKELETON_VILLAGER).add(Biomes.OLD_GROWTH_PINE_TAIGA);
		this.tag(SRBiomeTags.HAS_SKELETON_VILLAGER).addTag(SRBiomeTags.HAS_COMMON_SKELETON_VILLAGER).addTag(SRBiomeTags.HAS_RARE_SKELETON_VILLAGER).addTag(SRBiomeTags.HAS_WEIRD_SKELETON_VILLAGER);
	}
}