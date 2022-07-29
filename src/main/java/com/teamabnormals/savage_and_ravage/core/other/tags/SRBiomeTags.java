package com.teamabnormals.savage_and_ravage.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class SRBiomeTags {
	public static final TagKey<Biome> HAS_ICEOLOGER = biomeTag("has_monster/iceologer");
	public static final TagKey<Biome> HAS_SKELETON_VILLAGER = biomeTag("has_monster/skeleton_villager");
	public static final TagKey<Biome> HAS_COMMON_SKELETON_VILLAGER = biomeTag("has_monster/skeleton_villager/common");
	public static final TagKey<Biome> HAS_RARE_SKELETON_VILLAGER = biomeTag("has_monster/skeleton_villager/rare");

	private static TagKey<Biome> biomeTag(String name) {
		return TagUtil.biomeTag(SavageAndRavage.MOD_ID, name);
	}
}
