package com.teamabnormals.savage_and_ravage.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;

public class SREntityTypeTags {
	public static final Tag.Named<EntityType<?>> CREEPER_IMMUNE = entityTypeTag("creeper_immune");

	private static Tag.Named<EntityType<?>> entityTypeTag(String name) {
		return TagUtil.entityTypeTag(SavageAndRavage.MOD_ID, name);
	}
}
