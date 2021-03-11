package com.minecraftabnormals.savageandravage.common.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.minecraftabnormals.savageandravage.core.other.SRLoot;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.registry.Registry;

public class RandomDifficultyChance implements ILootCondition {
	private final float defaultChance;
	private final float peacefulChance;
	private final float easyChance;
	private final float normalChance;
	private final float hardChance;

	public RandomDifficultyChance(float defaultChance, float peacefulChance, float easyChance, float normalChance, float hardChance) {
		this.peacefulChance = peacefulChance;
		this.defaultChance = defaultChance;
		this.easyChance = easyChance;
		this.normalChance = normalChance;
		this.hardChance = hardChance;
	}

	@Override
	public LootConditionType func_230419_b_() {
		return Registry.LOOT_CONDITION_TYPE.getOrDefault(SRLoot.RANDOM_DIFFICULTY_CHANCE);
	}

	@Override
	public boolean test(LootContext lootContext) {
		float chance = this.defaultChance;
		switch (lootContext.getWorld().getDifficulty()) {
			case PEACEFUL:
				if (this.peacefulChance >= 0) chance = this.peacefulChance;
				break;
			case EASY:
				if (this.easyChance >= 0) chance = this.easyChance;
				break;
			case NORMAL:
				if (this.normalChance >= 0) chance = this.normalChance;
				break;
			case HARD:
				if (this.hardChance >= 0) chance = this.hardChance;

		}
		return lootContext.getRandom().nextFloat() < chance;
	}

	public static class Serializer implements ILootSerializer<RandomDifficultyChance> {
		public void serialize(JsonObject json, RandomDifficultyChance condition, JsonSerializationContext context) {
			json.addProperty("default_chance", condition.defaultChance);
			if (condition.peacefulChance >= 0)
				json.addProperty("peaceful", condition.peacefulChance);
			if (condition.easyChance >= 0)
				json.addProperty("easy", condition.easyChance);
			if (condition.normalChance >= 0)
				json.addProperty("normal", condition.normalChance);
			if (condition.hardChance >= 0)
				json.addProperty("hard", condition.hardChance);
		}

		public RandomDifficultyChance deserialize(JsonObject json, JsonDeserializationContext context) {
			if (json.has("default_chance")) {
				return new RandomDifficultyChance(JSONUtils.getFloat(json, "default_chance"), getFloatOrMinus1(json, "peaceful"), getFloatOrMinus1(json, "easy"), getFloatOrMinus1(json, "normal"), getFloatOrMinus1(json, "hard"));
			}
			throw new JsonSyntaxException("Missing 'default_chance', expected to find a string");
		}

		private static float getFloatOrMinus1(JsonObject json, String fieldName) {
			if (json.has(fieldName))
				return JSONUtils.getFloat(json, fieldName);
			return -1;
		}
	}
}
