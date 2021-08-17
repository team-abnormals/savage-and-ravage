package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;

public class SRTags {
	public static final INamedTag<Item> BLAST_PROOF_ITEMS = ItemTags.bind(SavageAndRavage.MOD_ID + ":blast_proof_items");
	public static final INamedTag<Block> BLAST_PROOF = BlockTags.bind(SavageAndRavage.MOD_ID + ":blast_proof");
	public static final INamedTag<Block> GLOOMY_TILES = BlockTags.bind(SavageAndRavage.MOD_ID + ":gloomy_tiles");
}
