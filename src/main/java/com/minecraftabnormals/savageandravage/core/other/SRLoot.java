package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.savageandravage.common.loot.condition.RandomDifficultyChance;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class SRLoot {
    public static final ResourceLocation CREEPER_EXPLOSION_DROPS = new ResourceLocation(SavageAndRavage.MOD_ID, "entities/conditional/creeper_explosion_drops");
    public static final ResourceLocation PILLAGER_RAID_DROPS = new ResourceLocation(SavageAndRavage.MOD_ID, "entities/conditional/pillager_raid_drops");
    public static final ResourceLocation EVOKER_TOTEM_REPLACEMENT = new ResourceLocation(SavageAndRavage.MOD_ID, "entities/conditional/evoker_totem_replacement");

    //TODO add to AC
    public static final ResourceLocation RANDOM_DIFFICULTY_CHANCE = new ResourceLocation(SavageAndRavage.MOD_ID, "random_difficulty_chance");

    public static void registerLootConditions() {
        Registry.register(Registry.LOOT_CONDITION_TYPE, RANDOM_DIFFICULTY_CHANCE, new LootConditionType(new RandomDifficultyChance.Serializer()));
    }
}