package com.teamabnormals.savage_and_ravage.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifiers;
import com.teamabnormals.blueprint.core.util.modification.ConfiguredModifier;
import com.teamabnormals.blueprint.core.util.modification.ModifierDataProvider;
import com.teamabnormals.blueprint.core.util.modification.TargetedModifier;
import com.teamabnormals.blueprint.core.util.modification.targeting.ConditionedModifierTargetSelector;
import com.teamabnormals.blueprint.core.util.modification.targeting.ModifierTargetSelectorRegistry;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class SRAdvancementModifiersProvider {
	public static ModifierDataProvider<Advancement.Builder, Void, DeserializationContext> createDataProvider(DataGenerator dataGenerator) {
		return AdvancementModifiers.createDataProvider(dataGenerator, "Advancement Modifiers", SavageAndRavage.MOD_ID,
				createModifierEntry("adventure/ol_betsy", Collections.singletonList(createParentModifier(new ResourceLocation("adventure/shoot_arrow")))),
				createModifierEntry("adventure/voluntary_exile", Collections.singletonList(createParentModifier(new ResourceLocation("adventure/trade"))))
		);
	}

	private static ModifierDataProvider.ProviderEntry<Advancement.Builder, Void, DeserializationContext> createModifierEntry(List<ResourceLocation> targets, String name, List<ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?>> modifiers) {
		return new ModifierDataProvider.ProviderEntry<>(
				new TargetedModifier<>(new ConditionedModifierTargetSelector<>(ModifierTargetSelectorRegistry.NAMES.withConfiguration(targets)), modifiers),
				new ResourceLocation(SavageAndRavage.MOD_ID, name)
		);
	}

	private static ModifierDataProvider.ProviderEntry<Advancement.Builder, Void, DeserializationContext> createModifierEntry(String target, List<ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?>> modifiers) {
		return createModifierEntry(Collections.singletonList(new ResourceLocation(target)), target, modifiers);
	}

	private static ConfiguredModifier<Advancement.Builder, ?, Void, DeserializationContext, ?> createParentModifier(ResourceLocation parent) {
		return new ConfiguredModifier<>(AdvancementModifiers.PARENT_MODIFIER, parent);
	}
}