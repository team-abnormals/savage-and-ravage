package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRBlockTags;
import com.teamabnormals.savage_and_ravage.core.registry.SRBlocks;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class SRBlockTagsProvider extends BlockTagsProvider {

	public SRBlockTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, SavageAndRavage.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(BlockTags.ENDERMAN_HOLDABLE).add(SRBlocks.SPORE_BOMB.get());
		this.tag(BlockTags.FLOWER_POTS).add(SRBlocks.POTTED_CREEPER_SPORES.get());
		this.tag(BlockTags.GUARDED_BY_PIGLINS).add(SRBlocks.BLAST_PROOF_PLATES.get(), SRBlocks.BLAST_PROOF_STAIRS.get(), SRBlocks.BLAST_PROOF_SLAB.get());
		this.tag(BlockTags.SLABS).add(SRBlocks.GLOOMY_TILE_SLAB.get(), SRBlocks.BLAST_PROOF_SLAB.get());
		this.tag(BlockTags.STAIRS).add(SRBlocks.GLOOMY_TILE_STAIRS.get(), SRBlocks.BLAST_PROOF_STAIRS.get());
		this.tag(BlockTags.WALLS).add(SRBlocks.GLOOMY_TILE_WALL.get());
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SRBlocks.GLOOMY_TILES.get(), SRBlocks.CHISELED_GLOOMY_TILES.get(), SRBlocks.GLOOMY_TILE_STAIRS.get(), SRBlocks.GLOOMY_TILE_SLAB.get(), SRBlocks.GLOOMY_TILE_WALL.get(), SRBlocks.RUNED_GLOOMY_TILES.get(), SRBlocks.BLAST_PROOF_PLATES.get(), SRBlocks.BLAST_PROOF_STAIRS.get(), SRBlocks.BLAST_PROOF_SLAB.get());
		this.tag(BlockTags.NEEDS_IRON_TOOL).add(SRBlocks.BLAST_PROOF_PLATES.get(), SRBlocks.BLAST_PROOF_STAIRS.get(), SRBlocks.BLAST_PROOF_SLAB.get());

		this.tag(SRBlockTags.ORCHESTRAL_NOTE_BLOCKS).add(SRBlocks.BLAST_PROOF_PLATES.get(), SRBlocks.BLAST_PROOF_STAIRS.get(), SRBlocks.BLAST_PROOF_SLAB.get());
		this.tag(SRBlockTags.HARPSICHORD_NOTE_BLOCKS).add(SRBlocks.GLOOMY_TILES.get(), SRBlocks.CHISELED_GLOOMY_TILES.get(), SRBlocks.GLOOMY_TILE_STAIRS.get(), SRBlocks.GLOOMY_TILE_SLAB.get(), SRBlocks.GLOOMY_TILE_WALL.get(), SRBlocks.RUNED_GLOOMY_TILES.get());
	}
}