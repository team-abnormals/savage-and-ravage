package com.teamabnormals.savage_and_ravage.core.data.server.modifiers;

import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifierProvider;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.CriteriaModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.DisplayInfoModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.EffectsChangedModifier;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.ParentModifier;
import com.teamabnormals.blueprint.core.api.conditions.ConfigValueCondition;
import com.teamabnormals.savage_and_ravage.core.SRConfig;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.registry.SRCriteriaTriggers;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import com.teamabnormals.savage_and_ravage.core.registry.SRMobEffects;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;

public class SRAdvancementModifierProvider extends AdvancementModifierProvider {
	private static final EntityType<?>[] MOBS_TO_KILL = new EntityType[]{SREntityTypes.SKELETON_VILLAGER.get(), SREntityTypes.CREEPIE.get(), SREntityTypes.ICEOLOGER.get(), SREntityTypes.EXECUTIONER.get(), SREntityTypes.TRICKSTER.get()};

	public SRAdvancementModifierProvider(DataGenerator generator) {
		super(generator, SavageAndRavage.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		MobEffectsPredicate predicate = MobEffectsPredicate.effects();
		SRMobEffects.MOB_EFFECTS.getEntries().forEach(mobEffect -> predicate.and(mobEffect.get()));
		this.entry("nether/all_effects").selects("nether/all_effects").addModifier(new EffectsChangedModifier("all_effects", false, predicate));
		this.entry("adventure/ol_betsy").selects(new ResourceLocation("adventure/ol_betsy")).addModifier(new ParentModifier(new ResourceLocation("adventure/shoot_arrow")));

		CriteriaModifier.Builder killAMob = CriteriaModifier.builder(this.modId);
		CriteriaModifier.Builder killAllMobs = CriteriaModifier.builder(this.modId);
		ArrayList<String> names = Lists.newArrayList();
		for (EntityType<?> entityType : MOBS_TO_KILL) {
			String name = ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath();
			KilledTrigger.TriggerInstance triggerInstance = KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityType));
			killAMob.addCriterion(name, triggerInstance);
			killAllMobs.addCriterion(name, triggerInstance);
			names.add(name);
		}

		ICondition noBadOmenOnDeath = new ConfigValueCondition(new ResourceLocation(SavageAndRavage.MOD_ID, "config"), SRConfig.COMMON.noBadOmenOnDeath, "no_bad_omen_on_death", Maps.newHashMap(), false);
		this.entry("adventure/kill_a_mob").selects("adventure/kill_a_mob").addModifier(killAMob.addIndexedRequirements(0, false, names.toArray(new String[0])).build());
		this.entry("adventure/kill_all_mobs").selects("adventure/kill_all_mobs").addModifier(killAllMobs.requirements(RequirementsStrategy.AND).build());
		this.entry("adventure/voluntary_exile").selects("adventure/voluntary_exile")
				.addModifier(new ParentModifier(new ResourceLocation("adventure/trade")))
				.addModifier(CriteriaModifier.builder(this.modId).addCriterion("voluntary_exile", SRCriteriaTriggers.BURN_OMINOUS_BANNER.createInstance()).build(), noBadOmenOnDeath)
				.addModifier(DisplayInfoModifier.builder().description(Component.translatable("advancements." + this.modId + ".adventure.voluntary_exile.description")).build(), noBadOmenOnDeath);
	}
}