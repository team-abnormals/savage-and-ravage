package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.blueprint.core.data.server.tags.BlueprintBiomeTagsProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintBiomeTags;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRBiomeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SRBiomeTagsProvider extends BlueprintBiomeTagsProvider {

	public SRBiomeTagsProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
		super(SavageAndRavage.MOD_ID, generator, fileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(SRBiomeTags.HAS_ICEOLOGER).addTag(BlueprintBiomeTags.IS_ICY).add(Biomes.SNOWY_TAIGA, Biomes.GROVE, Biomes.SNOWY_SLOPES, Biomes.JAGGED_PEAKS, Biomes.FROZEN_PEAKS);
		this.tag(SRBiomeTags.HAS_SKELETON_VILLAGER).addTag(BlueprintBiomeTags.IS_OVERWORLD);
	}
}