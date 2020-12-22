/*package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.*;
import net.minecraft.world.gen.feature.structure.PillagerOutpostPieces;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRStructures {

    @SuppressWarnings("deprecation")
    public static void registerPools() {
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosures"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure1"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure2"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure3"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure4"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/enclosure5"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosure_features"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure_cage1"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure_cage2"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure_cage3"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        if (ModList.get().isLoaded("quark")) {
            JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosure_loot"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure_chest1"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure_chest2"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure_chest3"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        } else {
            JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosure_loot"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure_chest2"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/enclosure_chest3"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        }
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/creepers"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creeper1"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creeper2"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creeper3"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/creeper4"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creeper5"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creeper6"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/creepies"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creepie1"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creepie2"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creepie3"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + "pillager_outpost/creepie4"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creepie5"), 1), Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/creepie6"), 1)), JigsawPattern.PlacementBehaviour.RIGID));
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/griefers"), new ResourceLocation("empty"), ImmutableList.of(Pair.of(new SingleJigsawPiece(SavageAndRavage.MODID + ":pillager_outpost/griefer"), 1)), JigsawPattern.PlacementBehaviour.RIGID));

        PillagerOutpostPieces.func_236999_a_();
        for (int i = 1; i <= 5; i++) {
            addToPool(new ResourceLocation("pillager_outpost/features"), new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost/enclosure"+i), 1);
        }
    }

    /**
     * @author bageldotjpg
     *
     * This is a hack that only works for this specific situation, reuse not recommended
     * *//*
    //@SuppressWarnings("deprecation")
    private static void addToPool(ResourceLocation pool, ResourceLocation toAdd, int weight) {
        JigsawPattern old = JigsawManager.REGISTRY.get(pool);
        List<JigsawPiece> shuffled = old.getShuffledPieces(new Random());
        List<Pair<JigsawPiece, Integer>> newPieces = new ArrayList<>();
        for (JigsawPiece p : shuffled) {
            newPieces.add(new Pair<>(p, 2));
        }
        newPieces.add(new Pair<>(new SingleJigsawPiece(toAdd.toString()), weight));
        ResourceLocation something = old.getFallback();
        JigsawManager.REGISTRY.register(new JigsawPattern(pool, something, newPieces, JigsawPattern.PlacementBehaviour.RIGID));
    }

}*/
