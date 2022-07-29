package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRItemTags;
import com.teamabnormals.savage_and_ravage.core.registry.SRBlocks;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SRItemTagsProvider extends ItemTagsProvider {

	public SRItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
		super(generator, blockTags, SavageAndRavage.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(ItemTags.ARROWS).add(SRItems.MISCHIEF_ARROW.get());
		this.tag(ItemTags.PIGLIN_LOVED).add(SRItems.GRIEFER_HELMET.get(), SRItems.GRIEFER_CHESTPLATE.get(), SRItems.GRIEFER_LEGGINGS.get(), SRItems.GRIEFER_BOOTS.get(), SRItems.BLAST_PROOF_PLATING.get(), SRBlocks.BLAST_PROOF_PLATES.get().asItem(), SRBlocks.BLAST_PROOF_STAIRS.get().asItem(), SRBlocks.BLAST_PROOF_SLAB.get().asItem(), SRBlocks.BLAST_PROOF_VERTICAL_SLAB.get().asItem());
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);

		this.tag(SRItemTags.EXPLOSION_IMMUNE).add(SRItems.CREEPER_SPORES.get(), SRItems.GRIEFER_HELMET.get(), SRItems.GRIEFER_CHESTPLATE.get(), SRItems.GRIEFER_LEGGINGS.get(), SRItems.GRIEFER_BOOTS.get(), SRBlocks.BLAST_PROOF_PLATES.get().asItem(), SRBlocks.BLAST_PROOF_STAIRS.get().asItem(), SRBlocks.BLAST_PROOF_SLAB.get().asItem(), SRBlocks.BLAST_PROOF_VERTICAL_SLAB.get().asItem());

		this.copy(BlueprintBlockTags.VERTICAL_SLABS, BlueprintItemTags.VERTICAL_SLABS);
	}
}