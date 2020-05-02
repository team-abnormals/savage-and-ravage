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
	
	public static void bakeConfig() {
		CreepieGoBigWhenBoom = COMMON.CreepieGoBigWhenBoom.get();
		CreeperNoDestroyBlocks = COMMON.CreeperNoDestroyBlocks.get();
		CreepersSpawnCreepiesWhenBoom = COMMON.CreepersSpawnCreepiesWhenBoom.get();
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

		public CommonConfig(ForgeConfigSpec.Builder builder) 
		{
			CreepieGoBigWhenBoom = builder
					.comment("This option makes creepies grow particularly larger when they explode")
					.translation(SavageAndRavage.MODID + ".config.CreepieGoBigWhenBoom")
					.define("Make Creepie go big when boom?", false);
			
			CreeperNoDestroyBlocks = builder
					.comment("This option makes creepers not destroy blocks.")
					.translation(SavageAndRavage.MODID + ".config.CreeperNoDestroyBlocks")
					.define("Make Creepers not destroy blocks?", true);
			
			CreepersSpawnCreepiesWhenBoom = builder
					.comment("This option makes creepies spawn whenever a creeper explodes.")
					.translation(SavageAndRavage.MODID + ".config.CreepersSpawnCreepiesWhenBoom")
					.define("Make Creepies spawn whenever a creeper explodes?", true);
		}
	}
}
