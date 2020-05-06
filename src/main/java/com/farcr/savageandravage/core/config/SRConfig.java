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
	public static boolean QuarkPoisonPotatoCompat;
	
	public static void bakeConfig() {
		CreepieGoBigWhenBoom = COMMON.CreepieGoBigWhenBoom.get();
		CreeperNoDestroyBlocks = COMMON.CreeperNoDestroyBlocks.get();
		CreepersSpawnCreepiesWhenBoom = COMMON.CreepersSpawnCreepiesWhenBoom.get();
		QuarkPoisonPotatoCompat = COMMON.QuarkPoisonPotatoCompat.get();
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
		public final ForgeConfigSpec.BooleanValue QuarkPoisonPotatoCompat;

		public CommonConfig(ForgeConfigSpec.Builder builder) 
		{
			CreeperNoDestroyBlocks = builder
					.translation(SavageAndRavage.MODID + ".config.CreeperNoDestroyBlocks")
					.define("Creepers do not destroy blocks", true);
			CreepersSpawnCreepiesWhenBoom = builder
					.translation(SavageAndRavage.MODID + ".config.CreepersSpawnCreepiesWhenBoom")
					.define("Creepies spawn after creeper explosions", true);
			QuarkPoisonPotatoCompat = builder
					.comment("If true, creepies can be fed a poisonous potato to stunt their growth when Quark is installed.")
					.translation(SavageAndRavage.MODID + "config.QuarkPoisonPotatoCompat")
					.define("Quark Poisonous Potato Compat",true);
			CreepieGoBigWhenBoom = builder
					.comment("If true, creepies grow significantly larger when they explode.")
					.translation(SavageAndRavage.MODID + ".config.CreepieGoBigWhenBoom")
					.define("Creepie go big when boom", false);
		}
	}
}
