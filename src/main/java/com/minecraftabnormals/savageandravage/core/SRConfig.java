package com.minecraftabnormals.savageandravage.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class SRConfig {

    public static class Common {
        public final ConfigValue<Boolean> creeperExplosionsDestroyBlocks;
        public final ConfigValue<Boolean> creeperExplosionsSpawnCreepies;
        public final ConfigValue<Boolean> creepersDropSporesAfterExplosionDeath;

        public Common(ForgeConfigSpec.Builder builder) {
            creeperExplosionsDestroyBlocks = builder
                    .translation(makeTranslation("creeperExplosionsDestroyBlocks"))
                    .define("Creeper explosions destroy blocks", false);
            creeperExplosionsSpawnCreepies = builder
                    .translation(makeTranslation("creeperExplosionsSpawnCreepies"))
                    .define("Creeper explosions spawn Creepies", true);
            creepersDropSporesAfterExplosionDeath = builder
                    .translation(makeTranslation("creepersDropSporesAfterExplosionDeath"))
                    .define("Creepers drop Creeper Spores after they die from an explosion", true);
        }
    }

    public static class Client {
        public final ConfigValue<Boolean> vindicatorRemodel;
        public final ConfigValue<Boolean> creepiesGrowLargerOnExplosion;

        public Client(ForgeConfigSpec.Builder builder) {
            this.creepiesGrowLargerOnExplosion = builder
                    .comment("If true, creepies grow significantly larger when they explode.")
                    .translation(makeTranslation("creepieGoBigWhenBoom"))
                    .define("Creepies grow larger on explosion", false);
            this.vindicatorRemodel = builder
                    .translation(makeTranslation("vindicatorRemodel"))
                    .define("Enable Vindicator Remodel", true);
        }
    }

    private static String makeTranslation(String name) {
        return SavageAndRavage.MODID + ".config." + name;
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
