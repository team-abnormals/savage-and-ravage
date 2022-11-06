package com.teamabnormals.savage_and_ravage.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SREntityTypeTags {
	public static final TagKey<EntityType<?>> CREEPER_IMMUNE = entityTypeTag("creeper_immune");
	public static final TagKey<EntityType<?>> CREEPER_LIKE = entityTypeTag("creeper_like");

	private static TagKey<EntityType<?>> entityTypeTag(String name) {
		return TagUtil.entityTypeTag(SavageAndRavage.MOD_ID, name);
	}
}
