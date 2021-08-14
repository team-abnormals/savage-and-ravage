package com.minecraftabnormals.savageandravage.core;

import com.minecraftabnormals.abnormals_core.core.annotations.ConfigKey;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class SRConfig {

	public static class Common {

		@ConfigKey("creeper_explosions_destroy_blocks")
		public final ConfigValue<Boolean> creeperExplosionsDestroyBlocks;

		@ConfigKey("creeper_explosions_spawn_creepies")
		public final ConfigValue<Boolean> creeperExplosionsSpawnCreepies;

		@ConfigKey("creepers_drop_spores_after_explosion_death")
		public final ConfigValue<Boolean> creepersDropSporesAfterExplosionDeath;

		@ConfigKey("creepie_explosions_destroy_blocks")
		public final ConfigValue<Boolean> creepieExplosionsDestroyBlocks;

		@ConfigKey("evokers_use_totems")
		public final ConfigValue<Boolean> evokersUseTotems;

		@ConfigKey("reduced_vex_health")
		public final ConfigValue<Boolean> reducedVexHealth;

		@ConfigKey("no_bad_omen_on_death")
		public final ConfigValue<Boolean> noBadOmenOnDeath;

		public Common(ForgeConfigSpec.Builder builder) {
			builder.push("mobs");

			builder.push("creepers");
			creeperExplosionsDestroyBlocks = builder.define("Creeper explosions destroy blocks", true);
			creeperExplosionsSpawnCreepies = builder.define("Creeper explosions spawn creepies", false);
			creepersDropSporesAfterExplosionDeath = builder.define("Creepers drop Creeper Spores after they die from an explosion", true);
			creepieExplosionsDestroyBlocks = builder.define("Creepie explosions destroy blocks", false);
			builder.pop();

			builder.push("illagers");
			noBadOmenOnDeath = builder
					.comment("Illagers with banners will no longer give Bad Omen when you kill them", "Instead, you will have to place and burn the banner that they drop with flint and steel or a fire charge")
					.define("Illagers no longer give Bad Omen on death", false);
			evokersUseTotems = builder
					.comment("When killed by projectiles, evokers use a totem of undying", "This restores 2 health and gives them a projectile-proof shield for 30 seconds", "When their shield runs out, evokers cannot use totems for another 90 seconds")
					.define("Evokers use totems", true);
			reducedVexHealth = builder.define("Vex health is reduced to 2", true);
			builder.pop();

			builder.pop();
		}
	}

	public static class Client {
		@ConfigKey("creepie_sprout")
		public final ConfigValue<Boolean> creepieSprout;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("mobs");
			builder.push("creepers");
			this.creepieSprout = builder.define("Creepies have a sprout on their head", true);
			builder.pop();
			builder.pop();
		}
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
