package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRBiomeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class SRBiomeTagsProvider extends BiomeTagsProvider {

	public SRBiomeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, SavageAndRavage.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(SRBiomeTags.HAS_ICEOLOGER).addTag(Tags.Biomes.IS_SNOWY);
		TagAppender<Biome> hasCommonSkeletonVillager = this.tag(SRBiomeTags.HAS_COMMON_SKELETON_VILLAGER);
		MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD.usedBiomes().forEach((biome) -> {
			if (biome != Biomes.SNOWY_PLAINS && biome != Biomes.ICE_SPIKES && biome != Biomes.MUSHROOM_FIELDS && biome != Biomes.DEEP_DARK)
				hasCommonSkeletonVillager.add(biome);
		});
		this.tag(SRBiomeTags.HAS_RARE_SKELETON_VILLAGER).add(Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES);
		this.tag(SRBiomeTags.HAS_WEIRD_SKELETON_VILLAGER).add(Biomes.OLD_GROWTH_PINE_TAIGA);
		this.tag(SRBiomeTags.HAS_SKELETON_VILLAGER).addTag(SRBiomeTags.HAS_COMMON_SKELETON_VILLAGER).addTag(SRBiomeTags.HAS_RARE_SKELETON_VILLAGER).addTag(SRBiomeTags.HAS_WEIRD_SKELETON_VILLAGER);
	}
}