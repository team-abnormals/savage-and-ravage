package com.teamabnormals.savage_and_ravage.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifierProvider;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.EffectsChangedModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.ParentModifier;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.registry.SRMobEffects;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

public class SRAdvancementModifiersProvider extends AdvancementModifierProvider {

	public SRAdvancementModifiersProvider(DataGenerator dataGenerator) {
		super(dataGenerator, SavageAndRavage.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		this.entry("adventure/ol_betsy").selects(new ResourceLocation("adventure/ol_betsy")).addModifier(new ParentModifier(new ResourceLocation("adventure/shoot_arrow")));
		this.entry("adventure/voluntary_exile").selects(new ResourceLocation("adventure/voluntary_exile")).addModifier(new ParentModifier(new ResourceLocation("adventure/trade")));
		this.entry("nether/all_effects").selects(new ResourceLocation(("nether/all_effects"))).addModifier(new EffectsChangedModifier("all_effects", false, MobEffectsPredicate.effects().and(SRMobEffects.WEIGHT.get())));
	}
}