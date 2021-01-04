package com.minecraftabnormals.savageandravage.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class SRConfig {

	public static class Common {
		public final ConfigValue<Boolean> creeperExplosionsDestroyBlocks;
		public final ConfigValue<Boolean> creeperExplosionsSpawnCreepies;
		public final ConfigValue<Boolean> creepersDropSporesAfterExplosionDeath;
		public final ConfigValue<Boolean> creepieExplosionsDestroyBlocks;

		public Common(ForgeConfigSpec.Builder builder) {
			creeperExplosionsDestroyBlocks = builder
					.translation(makeTranslation("creeperExplosionsDestroyBlocks"))
					.define("Creeper explosions destroy blocks", false);
			creeperExplosionsSpawnCreepies = builder
					.translation(makeTranslation("creeperExplosionsSpawnCreepies"))
					.define("Creeper explosions spawn creepies", true);
			creepersDropSporesAfterExplosionDeath = builder
					.translation(makeTranslation("creepersDropSporesAfterExplosionDeath"))
					.define("Creepers drop Creeper Spores after they die from an explosion", true);
			creepieExplosionsDestroyBlocks = builder
					.translation(makeTranslation("creepieExplosionsDestroyBlocks"))
					.comment("(Not officially supported)")
					.define("Creepie explosions destroy blocks", false);
		}
	}

	public static class Client {
		public final ConfigValue<Boolean> creepiesGrowLargerOnExplosion;
		public final ConfigValue<Boolean> creepieSprout;

		public Client(ForgeConfigSpec.Builder builder) {
			this.creepiesGrowLargerOnExplosion = builder
					.comment("If true, creepies grow significantly larger when they explode")
					.translation(makeTranslation("creepiesGrowLargerOnExplosion"))
					.define("Creepies grow larger on explosion", false);

			this.creepieSprout = builder
					.comment("If true, creepies have a sprout on their head")
					.translation(makeTranslation("creepieSprout"))
					.define("Creepie sprout", true);
		}
	}

	private static String makeTranslation(String name) {
		return SavageAndRavage.MOD_ID + ".config." + name;
	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	static {
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = commonSpecPair.getRight();
		COMMON = commonSpecPair.getLeft();

		Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = clientSpecPair.getRight();
		CLIENT = clientSpecPair.getLeft();
	}
}
