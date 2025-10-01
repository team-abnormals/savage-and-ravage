package com.teamabnormals.savage_and_ravage.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.savage_and_ravage.core.data.server.SRDatapackBuiltinEntriesProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.modifiers.SRAdvancementModifierProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.modifiers.SRLootModifierProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.tags.SRBiomeTagsProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.tags.SRBlockTagsProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.tags.SREntityTypeTagsProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.tags.SRItemTagsProvider;
import com.teamabnormals.savage_and_ravage.core.other.SRCompat;
import com.teamabnormals.savage_and_ravage.core.other.SRDataProcessors;
import com.teamabnormals.savage_and_ravage.core.other.SRDataSerializers;
import com.teamabnormals.savage_and_ravage.core.other.SRFeatures;
import com.teamabnormals.savage_and_ravage.core.registry.*;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.concurrent.CompletableFuture;

@Mod(SavageAndRavage.MOD_ID)
public class SavageAndRavage {
	public static final String MOD_ID = "savage_and_ravage";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

	public SavageAndRavage() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext context = ModLoadingContext.get();
		MinecraftForge.EVENT_BUS.register(this);

		SRDataProcessors.registerTrackedData();

		REGISTRY_HELPER.register(bus);
		SREntityTypes.ENTITIES.register(bus);
		SRParticleTypes.PARTICLES.register(bus);
		SRMobEffects.MOB_EFFECTS.register(bus);
		SRFeatures.FEATURES.register(bus);
		SRAttributes.ATTRIBUTES.register(bus);
		SRDataSerializers.SERIALIZERS.register(bus);
		SRLootConditions.LOOT_CONDITION_TYPES.register(bus);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			SRItems.setupTabEditors();
			SRBlocks.setupTabEditors();
		});

		context.registerConfig(ModConfig.Type.COMMON, SRConfig.COMMON_SPEC);
		context.registerConfig(ModConfig.Type.CLIENT, SRConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SRFeatures.addToJigsawPatterns();
			SREntityTypes.registerWaveMembers();
			SRCompat.registerCompat();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(SRItems::registerItemProperties);
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<Provider> provider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		boolean server = event.includeServer();
		SRBlockTagsProvider blockTags = new SRBlockTagsProvider(output, provider, helper);
		generator.addProvider(server, blockTags);
		generator.addProvider(server, new SRItemTagsProvider(output, provider, blockTags.contentsGetter(), helper));
		generator.addProvider(server, new SREntityTypeTagsProvider(output, provider, helper));
		generator.addProvider(server, new SRBiomeTagsProvider(output, provider, helper));
		generator.addProvider(server, new SRAdvancementModifierProvider(output, provider));
		generator.addProvider(server, new SRLootModifierProvider(output, provider));
		generator.addProvider(server, new SRDatapackBuiltinEntriesProvider(output, provider));
	}

	public static ResourceLocation location(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
