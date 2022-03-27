package com.teamabnormals.savage_and_ravage.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.advancement.modification.modifiers.ParentModifier;
import com.teamabnormals.blueprint.core.util.modification.AdvancementModifierProvider;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

public class SRAdvancementModifiersProvider extends AdvancementModifierProvider {

	public SRAdvancementModifiersProvider(DataGenerator dataGenerator) {
		super(dataGenerator, SavageAndRavage.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		this.registerEntry("adventure/ol_betsy", new ParentModifier(new ResourceLocation("adventure/shoot_arrow")), new ResourceLocation("adventure/ol_betsy"));
		this.registerEntry("adventure/voluntary_exile", new ParentModifier(new ResourceLocation("adventure/trade")), new ResourceLocation("adventure/voluntary_exile"));
	}
}