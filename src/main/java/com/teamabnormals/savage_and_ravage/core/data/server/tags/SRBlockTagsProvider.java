package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRBlockTags;
import com.teamabnormals.savage_and_ravage.core.registry.SRBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SRBlockTagsProvider extends BlockTagsProvider {

	public SRBlockTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SavageAndRavage.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(BlockTags.ENDERMAN_HOLDABLE).add(SRBlocks.SPORE_BOMB.get());
		this.tag(BlockTags.FLOWER_POTS).add(SRBlocks.POTTED_CREEPER_SPORES.get());
		this.tag(BlockTags.GUARDED_BY_PIGLINS).add(SRBlocks.BLAST_PROOF_PLATES.get(), SRBlocks.BLAST_PROOF_STAIRS.get(), SRBlocks.BLAST_PROOF_SLAB.get(), SRBlocks.BLAST_PROOF_VERTICAL_SLAB.get());
		this.tag(BlockTags.SLABS).add(SRBlocks.GLOOMY_TILE_SLAB.get(), SRBlocks.BLAST_PROOF_SLAB.get());
		this.tag(BlockTags.STAIRS).add(SRBlocks.GLOOMY_TILE_STAIRS.get(), SRBlocks.BLAST_PROOF_STAIRS.get());
		this.tag(BlockTags.WALLS).add(SRBlocks.GLOOMY_TILE_WALL.get());
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SRBlocks.GLOOMY_TILES.get(), SRBlocks.CHISELED_GLOOMY_TILES.get(), SRBlocks.GLOOMY_TILE_STAIRS.get(), SRBlocks.GLOOMY_TILE_SLAB.get(), SRBlocks.GLOOMY_TILE_WALL.get(), SRBlocks.GLOOMY_TILE_VERTICAL_SLAB.get(), SRBlocks.RUNED_GLOOMY_TILES.get(), SRBlocks.BLAST_PROOF_PLATES.get(), SRBlocks.BLAST_PROOF_STAIRS.get(), SRBlocks.BLAST_PROOF_SLAB.get(), SRBlocks.BLAST_PROOF_VERTICAL_SLAB.get());
		this.tag(BlockTags.NEEDS_IRON_TOOL).add(SRBlocks.BLAST_PROOF_PLATES.get(), SRBlocks.BLAST_PROOF_STAIRS.get(), SRBlocks.BLAST_PROOF_SLAB.get(), SRBlocks.BLAST_PROOF_VERTICAL_SLAB.get());

		this.tag(SRBlockTags.ORCHESTRAL_NOTE_BLOCKS).add(SRBlocks.BLAST_PROOF_PLATES.get(), SRBlocks.BLAST_PROOF_STAIRS.get(), SRBlocks.BLAST_PROOF_SLAB.get(), SRBlocks.BLAST_PROOF_VERTICAL_SLAB.get());
		this.tag(SRBlockTags.HARPSICHORD_NOTE_BLOCKS).add(SRBlocks.GLOOMY_TILES.get(), SRBlocks.CHISELED_GLOOMY_TILES.get(), SRBlocks.GLOOMY_TILE_STAIRS.get(), SRBlocks.GLOOMY_TILE_SLAB.get(), SRBlocks.GLOOMY_TILE_WALL.get(), SRBlocks.GLOOMY_TILE_VERTICAL_SLAB.get(), SRBlocks.RUNED_GLOOMY_TILES.get());

		this.tag(BlueprintBlockTags.VERTICAL_SLABS).add(SRBlocks.GLOOMY_TILE_VERTICAL_SLAB.get(), SRBlocks.BLAST_PROOF_VERTICAL_SLAB.get());
	}
}