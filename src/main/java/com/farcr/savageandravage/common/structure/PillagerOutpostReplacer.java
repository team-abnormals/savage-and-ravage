package com.farcr.savageandravage.common.structure;

import com.farcr.savageandravage.core.SavageAndRavage;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.*;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.UnaryOperator;

public final class PillagerOutpostReplacer extends JigsawPatternRegistry {
    private static final IStructureProcessorType PROCESSOR = Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(SavageAndRavage.MODID, "pillager_outpost"), Processor::new);

    private final JigsawPatternRegistry registry;

    private PillagerOutpostReplacer(final JigsawPatternRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void register(final JigsawPattern pattern) {
        if (pattern != JigsawPattern.EMPTY) {
            this.registry.register(this.map(pattern));
        }
    }

    private JigsawPattern map(final JigsawPattern pattern) {
        //func_214947_b gets the name
        if (pattern.func_214947_b().equals(new ResourceLocation("pillager_outpost/watchtower"))) {
            final List<JigsawPiece> jigsawPieces = ObfuscationReflectionHelper.getPrivateValue(JigsawPattern.class, pattern, "field_214953_e");
            jigsawPieces.set(0, new SingleJigsawPiece("savageandravage:pillager_outpost/watchtower", ImmutableList.of(new Processor()), JigsawPattern.PlacementBehaviour.RIGID));
        }
        if (pattern.func_214947_b().equals(new ResourceLocation("pillager_outpost/watchtower_overgrown"))) {
            final List<JigsawPiece> jigsawPieces = ObfuscationReflectionHelper.getPrivateValue(JigsawPattern.class, pattern, "field_214953_e");
            jigsawPieces.set(0, new SingleJigsawPiece("savageandravage:pillager_outpost/watchtower_overgrown", ImmutableList.of(new Processor()), JigsawPattern.PlacementBehaviour.RIGID));
        }
        if (pattern.func_214947_b().equals(new ResourceLocation("pillager_outpost/base_plate"))) {
            final List<JigsawPiece> jigsawPieces = ObfuscationReflectionHelper.getPrivateValue(JigsawPattern.class, pattern, "field_214953_e");
            jigsawPieces.set(0, new SingleJigsawPiece("savageandravage:pillager_outpost/base_plate", ImmutableList.of(new Processor()), JigsawPattern.PlacementBehaviour.RIGID));
        }
        return pattern;
    }

    @Override
    public JigsawPattern get(final ResourceLocation name) {
        return this.registry.get(name);
    }

    public static void inject() {
        PillagerOutpostReplacer.<JigsawManager, JigsawPatternRegistry>replace(JigsawManager.class, null, "field_214891_a", PillagerOutpostReplacer::new);
    }

    @SuppressWarnings("unchecked")
    private static <U, T> void replace(final Class<U> owner, @Nullable final Object instance, final String name, final UnaryOperator<T> operator) {
        final Field registryField = ObfuscationReflectionHelper.findField(owner, name);
        FieldUtils.removeFinalModifier(registryField);
        try {
            registryField.set(instance, operator.apply((T) registryField.get(instance)));
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class Processor extends StructureProcessor
    {
        public Processor()
        {

        }

        public Processor(final Dynamic<?> dynamic)
        {

        }


        @Override
        protected IStructureProcessorType getType() {
            return PillagerOutpostReplacer.PROCESSOR;
        }

        @Override
        protected <T> Dynamic<T> serialize0(final DynamicOps<T> ops) {
            return new Dynamic<>(ops, ops.emptyMap());
        }
    }
}
