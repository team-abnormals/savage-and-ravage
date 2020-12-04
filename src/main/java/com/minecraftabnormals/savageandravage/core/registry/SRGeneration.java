package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.common.generation.EnclosureFeature;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SRGeneration {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SavageAndRavage.MODID);

    public static final RegistryObject<Feature<NoFeatureConfig>> CREEPER_ENCLOSURE = FEATURES.register("creeper_enclosure", () -> new EnclosureFeature(NoFeatureConfig.field_236558_a_));

    public static void registerGeneration() {
        ForgeRegistries.BIOMES.getValues().stream().filter(b -> b.hasStructure(Structure.field_236366_b_)).forEach(SRGeneration::generate);
        //addToPool(new ResourceLocation("pillager_outpost/features"), new ResourceLocation(SavageAndRavage.MODID,"pillager_outpost/targets"),1);
        //TODO design targets decoration
    }

    public static void generate(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SRGeneration.CREEPER_ENCLOSURE.get().withConfiguration(new NoFeatureConfig()).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(8))));
    }

    /**
     * @author bageldotjpg
     *
     * This is a hack that only works for this specific situation, reuse not recommended
     * */
    @SuppressWarnings("deprecation")
    private static void addToPool(ResourceLocation pool, ResourceLocation toAdd, int weight) {
        JigsawPattern old = JigsawManager.REGISTRY.get(pool);
        List<JigsawPiece> shuffled = old.getShuffledPieces(new Random());
        List<Pair<JigsawPiece, Integer>> newPieces = new ArrayList<>();
        for (JigsawPiece p : shuffled) {
            newPieces.add(new Pair<>(p, 1)); //better way to do weighting?
        }
        newPieces.add(new Pair<>(new SingleJigsawPiece(toAdd.toString()), weight));
        ResourceLocation something = old.getFallback();
        JigsawManager.REGISTRY.register(new JigsawPattern(pool, something, newPieces, JigsawPattern.PlacementBehaviour.RIGID));
    }

}
