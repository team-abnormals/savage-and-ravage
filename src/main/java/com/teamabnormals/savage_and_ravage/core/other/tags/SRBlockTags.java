package com.teamabnormals.savage_and_ravage.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class SRBlockTags {
	public static final Tag.Named<Block> ORCHESTRAL_NOTE_BLOCKS = blockTag("orchestral_note_blocks");
	public static final Tag.Named<Block> HARPSICHORD_NOTE_BLOCKS = blockTag("harpsichord_note_blocks");

	private static Tag.Named<Block> blockTag(String name) {
		return TagUtil.blockTag(SavageAndRavage.MOD_ID, name);
	}
}
