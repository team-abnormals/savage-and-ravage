package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.Blocks;

public class SRNoteBlocks {
	public static void registerNoteBlocks() {
		DataUtil.registerNoteBlockInstrument(new DataUtil.CustomNoteBlockInstrument(SavageAndRavage.MOD_ID, source -> source.getBlockState().is(Blocks.TARGET), SRSounds.BLOCK_NOTE_BLOCK_HIT_MARKER.get()));
		DataUtil.registerNoteBlockInstrument(new DataUtil.CustomNoteBlockInstrument(SavageAndRavage.MOD_ID, source -> source.getBlockState().is(SRTags.GLOOMY_TILES), SRSounds.BLOCK_NOTE_BLOCK_HARPSICHORD.get()));
		DataUtil.registerNoteBlockInstrument(new DataUtil.CustomNoteBlockInstrument(SavageAndRavage.MOD_ID, source -> source.getBlockState().is(SRTags.BLAST_PROOF), SRSounds.BLOCK_NOTE_BLOCK_ORCHESTRAL_HIT.get()));
	}
}
