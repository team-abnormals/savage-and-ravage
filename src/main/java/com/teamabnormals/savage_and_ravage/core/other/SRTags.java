package com.teamabnormals.savage_and_ravage.core.other;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.tags.ItemTags;

public class SRTags {
	public static final Named<Item> BLAST_PROOF_ITEMS = ItemTags.bind(SavageAndRavage.MOD_ID + ":blast_proof_items");
	public static final Named<EntityType<?>> CREEPER_PROOF_TYPES = EntityTypeTags.bind(SavageAndRavage.MOD_ID + ":creeper_proof_types");

	public static final Named<Block> BLAST_PROOF = BlockTags.bind(SavageAndRavage.MOD_ID + ":blast_proof");
	public static final Named<Block> GLOOMY_TILES = BlockTags.bind(SavageAndRavage.MOD_ID + ":gloomy_tiles");
}
