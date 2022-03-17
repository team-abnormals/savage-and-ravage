package com.teamabnormals.savage_and_ravage.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SRItemTags {
	public static final TagKey<Item> EXPLOSION_IMMUNE = itemTag("explosion_immune");

	private static TagKey<Item> itemTag(String name) {
		return TagUtil.itemTag(SavageAndRavage.MOD_ID, name);
	}
}
