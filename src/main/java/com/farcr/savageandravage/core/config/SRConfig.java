package com.farcr.savageandravage.core.config;

import org.apache.commons.lang3.tuple.Pair;

import com.farcr.savageandravage.core.SavageAndRavage;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

@EventBusSubscriber(modid = SavageAndRavage.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SRConfig 
{
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final CommonConfig COMMON;
	static {
		final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}
	
	public static boolean CreepieGoBigWhenBoom;
	public static boolean CreeperNoDestroyBlocks;
	public static boolean CreepersSpawnCreepiesWhenBoom;
	public static boolean PoisonPotatoCompatEnabled;
	public static boolean PoisonEffect;
	public static double PoisonChance;


	public static void bakeConfig() {
		CreepieGoBigWhenBoom = COMMON.CreepieGoBigWhenBoom.get();
		CreeperNoDestroyBlocks = COMMON.CreeperNoDestroyBlocks.get();
		CreepersSpawnCreepiesWhenBoom = COMMON.CreepersSpawnCreepiesWhenBoom.get();
		PoisonPotatoCompatEnabled = COMMON.PoisonPotatoCompatEnabled.get();
		PoisonEffect = COMMON.PoisonEffect.get();
		PoisonChance = COMMON.PoisonChance.get();
	}

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
		if (configEvent.getConfig().getSpec() == SRConfig.COMMON_SPEC) {
			bakeConfig();
		}
	}
	
	public static class CommonConfig 
	{

		public final ForgeConfigSpec.BooleanValue CreepieGoBigWhenBoom;
		public final ForgeConfigSpec.BooleanValue CreeperNoDestroyBlocks;
		public final ForgeConfigSpec.BooleanValue CreepersSpawnCreepiesWhenBoom;
		public final ForgeConfigSpec.BooleanValue PoisonPotatoCompatEnabled;
		public final ForgeConfigSpec.BooleanValue PoisonEffect;
		public final ForgeConfigSpec.DoubleValue PoisonChance;

		public CommonConfig(ForgeConfigSpec.Builder builder) 
		{
			CreeperNoDestroyBlocks = builder
					.translation(SavageAndRavage.MODID + ".config.CreeperNoDestroyBlocks")
					.define("Creepers do not destroy blocks", true);
			CreepersSpawnCreepiesWhenBoom = builder
					.translation(SavageAndRavage.MODID + ".config.CreepersSpawnCreepiesWhenBoom")
					.define("Creepies spawn after creeper explosions", true);
			builder.push("Quark Poisonous Potato Compat");
			PoisonPotatoCompatEnabled = builder
					.comment("If true, creepies can be fed a poisonous potato to stunt their growth when Quark is installed.")
					.translation(SavageAndRavage.MODID + "config.QuarkPoisonPotatoCompat")
					.define("Compat enabled",true);
			PoisonEffect = builder
					.comment("If false, the feature will still work but not give creepies poison.")
					.translation(SavageAndRavage.MODID + "config.PoisonEffect")
					.define("Poison effect",true);
			PoisonChance = builder
					.comment("This affects the chance to stunt a creepie's growth when a poisonous potato is fed.")
					.translation(SavageAndRavage.MODID + "config.PoisonChance")
					.defineInRange("Poison chance",0.1,0,1);
			builder.pop();
			CreepieGoBigWhenBoom = builder
					.comment("If true, creepies grow significantly larger when they explode.")
					.translation(SavageAndRavage.MODID + ".config.CreepieGoBigWhenBoom")
					.define("Creepie go big when boom", false);
		}
	}
}
