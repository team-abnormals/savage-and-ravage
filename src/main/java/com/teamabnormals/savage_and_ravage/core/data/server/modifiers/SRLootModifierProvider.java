package com.teamabnormals.savage_and_ravage.core.data.server.modifiers;

import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.common.loot.modification.LootModifierProvider;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolEntriesModifier;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolsModifier;
import com.teamabnormals.blueprint.core.api.conditions.ConfigValueCondition;
import com.teamabnormals.blueprint.core.util.modification.selection.ConditionedResourceSelector;
import com.teamabnormals.blueprint.core.util.modification.selection.selectors.NamesResourceSelector;
import com.teamabnormals.savage_and_ravage.core.SRConfig;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;
import java.util.Collections;

public class SRLootModifierProvider extends LootModifierProvider {
	public SRLootModifierProvider(DataGenerator dataGenerator) {
		super(dataGenerator, SavageAndRavage.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		this.entry("chests/pillager_outpost").selects(BuiltInLootTables.PILLAGER_OUTPOST).addModifier(new LootPoolsModifier(Collections.singletonList(LootPool.lootPool().name(this.modId + ":outpost_emeralds").setRolls(UniformGenerator.between(1.0F, 2.0F)).when(LootItemRandomChanceCondition.randomChance(0.305F)).add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))).build()), false));
		this.entry("entities/pillager").selects("entities/pillager").addModifier(new LootPoolsModifier(Arrays.asList(LootPool.lootPool().name(this.modId + ":pillager_arrows").setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))).build(), LootPool.lootPool().name(this.modId + ":pillager_raid_drops").setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(new ResourceLocation(this.modId, "entities/raider_drops"))).build()), false));
		this.entry("entities/vindicator").selects("entities/vindicator").addModifier(new LootPoolsModifier(Collections.singletonList(LootPool.lootPool().name(this.modId + ":vindicator_raid_drops").setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(new ResourceLocation(this.modId, "entities/raider_drops"))).build()), false));
		this.entry("entities/evoker").selects("entities/evoker").addModifier(new LootPoolEntriesModifier(true, 0, AlternativesEntry.alternatives(LootItem.lootTableItem(SRItems.CONCH_OF_CONJURING.get()).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.025F)), LootItem.lootTableItem(Items.TOTEM_OF_UNDYING)).build()));
		this.entry("entities/creeper").selector(new ConditionedResourceSelector(new NamesResourceSelector("entities/creeper"), new ConfigValueCondition(new ResourceLocation(SavageAndRavage.MOD_ID, "config"), SRConfig.COMMON.creepersDropSporesAfterExplosionDeath, "creepers_drop_spores_after_explosion_death", Maps.newHashMap(), false))).addModifier(new LootPoolsModifier(Collections.singletonList(LootPool.lootPool().name(this.modId + ":creeper_explosion_drops").setRolls(ConstantValue.exactly(1.0F)).when(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isExplosion(true))).add(LootItem.lootTableItem(SRItems.CREEPER_SPORES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))).build()), false));
	}
}