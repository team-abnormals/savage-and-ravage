package com.teamabnormals.savage_and_ravage.core;

import com.teamabnormals.blueprint.core.annotations.ConfigKey;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import org.apache.commons.lang3.tuple.Pair;

public class SRConfig {

	public static class Common {
		public final BooleanValue creeperExplosionsDestroyBlocks;
		public final BooleanValue creeperExplosionsSpawnCreepies;
		@ConfigKey("creepers_drop_spores_after_explosion_death")
		public final BooleanValue creepersDropSporesAfterExplosionDeath;
		public final BooleanValue creepieExplosionsDestroyBlocks;
		public final BooleanValue evokersUseTotems;
		public final BooleanValue reducedVexHealth;
		public final BooleanValue improvedIllagerBehavior;
		@ConfigKey("no_bad_omen_on_death")
		public final BooleanValue noBadOmenOnDeath;
		public final BooleanValue poisonPotatoCompat;
		public final DoubleValue poisonPotatoChance;
		public final BooleanValue poisonPotatoEffect;

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
			improvedIllagerBehavior = builder
					.comment("Replaces the vanilla AI of Illagers with the improved AI. Disabling it will revert to the vanilla AI, which can be useful for mob or experience farms that depend on specific Illager behavior")
					.define("Illagers use the new improved AI", true);
			builder.pop();
			builder.pop();

			builder.push("compat");
			builder.push("poisonous_potato");
			this.poisonPotatoCompat = builder.comment("If baby mobs can be fed a poisonous potato to stunt their growth when Quark is installed").define("Poisonous Potato compat", true);
			this.poisonPotatoChance = builder.comment("The chance to stunt baby mob growth when feeding a poisonous potato").defineInRange("Poisonous Potato stunt chance", 0.1, 0, 1);
			this.poisonPotatoEffect = builder.comment("If growth stunting should give baby mobs poison").define("Poisonous Potato effect", true);
			builder.pop();
			builder.pop();
		}
	}

	public static class Client {
		public final BooleanValue creepieSprout;

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
