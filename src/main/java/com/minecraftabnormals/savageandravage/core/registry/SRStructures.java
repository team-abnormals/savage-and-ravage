package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.*;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRStructures {

    public static void registerPools() {
        //Made custom pool instead of adding to features in order to avoid 2 enclosures, and to set the y elevation exactly
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosure_plates"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/feature_enclosure"), 1), Pair.of(new SingleJigsawPiece("empty"), 5)), JigsawPattern.PlacementBehaviour.RIGID));
        //TODO redo this when list of decorations is known
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosure_features"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/furnace"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/crafting_tables"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/chests"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosure_walls"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/wall1"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/wall2"), 1), Pair.of(new SingleJigsawPiece("empty"), 5)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosure_spawns"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/creeper"), 3), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/creepie"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
    }

}
