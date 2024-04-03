package com.teamabnormals.savage_and_ravage.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.savage_and_ravage.client.model.*;
import com.teamabnormals.savage_and_ravage.client.renderer.entity.*;
import com.teamabnormals.savage_and_ravage.client.renderer.entity.layers.TotemShieldLayer;
import com.teamabnormals.savage_and_ravage.core.data.server.SRDatapackBuiltinEntriesProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.modifiers.SRAdvancementModifierProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.modifiers.SRLootModifierProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.tags.SRBiomeTagsProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.tags.SRBlockTagsProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.tags.SREntityTypeTagsProvider;
import com.teamabnormals.savage_and_ravage.core.data.server.tags.SRItemTagsProvider;
import com.teamabnormals.savage_and_ravage.core.other.*;
import com.teamabnormals.savage_and_ravage.core.registry.*;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
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
			bus.addListener(this::registerLayers);
			bus.addListener(this::registerLayerDefinitions);
			bus.addListener(this::registerModels);
			bus.addListener(this::registerRenderers);
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

		boolean includeServer = event.includeServer();
		SRBlockTagsProvider blockTags = new SRBlockTagsProvider(output, provider, helper);
		generator.addProvider(includeServer, blockTags);
		generator.addProvider(includeServer, new SRItemTagsProvider(output, provider, blockTags.contentsGetter(), helper));
		generator.addProvider(includeServer, new SREntityTypeTagsProvider(output, provider, helper));
		generator.addProvider(includeServer, new SRBiomeTagsProvider(output, provider, helper));
		generator.addProvider(includeServer, new SRAdvancementModifierProvider(output, provider));
		generator.addProvider(includeServer, new SRLootModifierProvider(output, provider));
		generator.addProvider(includeServer, new SRDatapackBuiltinEntriesProvider(output, provider));
	}

	@OnlyIn(Dist.CLIENT)
	private void registerLayers(EntityRenderersEvent.AddLayers event) {
		EntityRenderer<?> renderer = event.getRenderer(EntityType.EVOKER);
		if (renderer instanceof EvokerRenderer evokerRenderer) {
			evokerRenderer.addLayer(new TotemShieldLayer<Evoker, IllagerModel<Evoker>>(evokerRenderer, event.getEntityModels()));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void registerModels(ModelEvent.RegisterAdditional event) {
		event.register(IceChunkRenderer.MODEL_LOCATION);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(SRModelLayers.CREEPIE, () -> CreepieModel.createBodyLayer(CubeDeformation.NONE));
		event.registerLayerDefinition(SRModelLayers.CREEPIE_ARMOR, () -> CreepieModel.createBodyLayer(new CubeDeformation(2.0F)));
		event.registerLayerDefinition(SRModelLayers.EXECUTIONER, ExecutionerModel::createBodyLayer);
		event.registerLayerDefinition(SRModelLayers.GRIEFER, GrieferModel::createBodyLayer);
		event.registerLayerDefinition(SRModelLayers.GRIEFER_ARMOR, GrieferArmorModel::createArmorLayer);
		event.registerLayerDefinition(SRModelLayers.ICEOLOGER, IceologerModel::createBodyLayer);
		event.registerLayerDefinition(SRModelLayers.ILLAGER_ARMOR, () -> TotemShieldLayer.createBodyLayer(new CubeDeformation(2.0F)));
		event.registerLayerDefinition(SRModelLayers.MASK_OF_DISHONESTY, MaskOfDishonestyModel::createArmorLayer);
		event.registerLayerDefinition(SRModelLayers.RUNE_PRISON, RunePrisonModel::createBodyLayer);
		event.registerLayerDefinition(SRModelLayers.SKELETON_VILLAGER, SkeletonVillagerModel::createBodyLayer);
		event.registerLayerDefinition(SRModelLayers.TRICKSTER, TricksterModel::createBodyLayer);
		event.registerLayerDefinition(SRModelLayers.VILLAGER_INNER_ARMOR, VillagerArmorModel::createInnerArmorLayer);
		event.registerLayerDefinition(SRModelLayers.VILLAGER_OUTER_ARMOR, VillagerArmorModel::createOuterArmorLayer);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SREntityTypes.SKELETON_VILLAGER.get(), SkeletonVillagerRenderer::new);
		event.registerEntityRenderer(SREntityTypes.CREEPIE.get(), CreepieRenderer::new);
		event.registerEntityRenderer(SREntityTypes.GRIEFER.get(), GrieferRenderer::new);
		event.registerEntityRenderer(SREntityTypes.ICEOLOGER.get(), IceologerRenderer::new);
		event.registerEntityRenderer(SREntityTypes.EXECUTIONER.get(), ExecutionerRenderer::new);
		event.registerEntityRenderer(SREntityTypes.TRICKSTER.get(), TricksterRenderer::new);
		event.registerEntityRenderer(SREntityTypes.BURNING_BANNER.get(), BurningBannerRenderer::new);
		event.registerEntityRenderer(SREntityTypes.SPORE_CLOUD.get(), NoModelRenderer::new);
		event.registerEntityRenderer(SREntityTypes.SPORE_BOMB.get(), SporeBombRenderer::new);
		event.registerEntityRenderer(SREntityTypes.MISCHIEF_ARROW.get(), MischiefArrowRenderer::new);
		event.registerEntityRenderer(SREntityTypes.ICE_CHUNK.get(), IceChunkRenderer::new);
		event.registerEntityRenderer(SREntityTypes.ICE_CLOUD.get(), NoModelRenderer::new);
		event.registerEntityRenderer(SREntityTypes.RUNE_PRISON.get(), RunePrisonRenderer::new);
		event.registerEntityRenderer(SREntityTypes.CONFUSION_BOLT.get(), NoModelRenderer::new);
	}
}
