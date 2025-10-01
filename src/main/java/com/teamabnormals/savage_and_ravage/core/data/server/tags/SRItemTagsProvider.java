package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRItemTags;
import com.teamabnormals.savage_and_ravage.core.registry.SRBlocks;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class SRItemTagsProvider extends ItemTagsProvider {

	public SRItemTagsProvider(PackOutput output, CompletableFuture<Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> lookup, ExistingFileHelper helper) {
		super(output, provider, lookup, SavageAndRavage.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(ItemTags.ARROWS).add(SRItems.MISCHIEF_ARROW.get());
		this.tag(ItemTags.PIGLIN_LOVED).add(SRItems.GRIEFER_HELMET.get(), SRItems.GRIEFER_CHESTPLATE.get(), SRItems.GRIEFER_LEGGINGS.get(), SRItems.GRIEFER_BOOTS.get(), SRItems.BLAST_PROOF_PLATING.get(), SRBlocks.BLAST_PROOF_PLATES.get().asItem(), SRBlocks.BLAST_PROOF_STAIRS.get().asItem(), SRBlocks.BLAST_PROOF_SLAB.get().asItem());
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);

		this.tag(Items.ARMORS_HELMETS).add(SRItems.GRIEFER_HELMET.get(), SRItems.MASK_OF_DISHONESTY.get());
		this.tag(Items.ARMORS_CHESTPLATES).add(SRItems.GRIEFER_CHESTPLATE.get());
		this.tag(Items.ARMORS_LEGGINGS).add(SRItems.GRIEFER_LEGGINGS.get());
		this.tag(Items.ARMORS_BOOTS).add(SRItems.GRIEFER_BOOTS.get());

		this.tag(SRItemTags.EXPLOSION_IMMUNE).add(SRItems.CREEPER_SPORES.get(), SRItems.GRIEFER_HELMET.get(), SRItems.GRIEFER_CHESTPLATE.get(), SRItems.GRIEFER_LEGGINGS.get(), SRItems.GRIEFER_BOOTS.get(), SRBlocks.BLAST_PROOF_PLATES.get().asItem(), SRBlocks.BLAST_PROOF_STAIRS.get().asItem(), SRBlocks.BLAST_PROOF_SLAB.get().asItem());
	}
}