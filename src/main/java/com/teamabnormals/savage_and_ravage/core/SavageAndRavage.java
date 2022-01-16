package com.teamabnormals.savage_and_ravage.core;

import com.teamabnormals.savage_and_ravage.client.model.CreepieModel;
import com.teamabnormals.savage_and_ravage.client.model.ExecutionerModel;
import com.teamabnormals.savage_and_ravage.client.model.GrieferArmorModel;
import com.teamabnormals.savage_and_ravage.client.model.GrieferModel;
import com.teamabnormals.savage_and_ravage.client.model.IceologerModel;
import com.teamabnormals.savage_and_ravage.client.model.MaskOfDishonestyModel;
import com.teamabnormals.savage_and_ravage.client.model.RunePrisonModel;
import com.teamabnormals.savage_and_ravage.client.model.SkeletonVillagerModel;
import com.teamabnormals.savage_and_ravage.client.model.TricksterModel;
import com.teamabnormals.savage_and_ravage.client.model.VillagerArmorModel;
import com.teamabnormals.savage_and_ravage.client.render.BurningBannerRenderer;
import com.teamabnormals.savage_and_ravage.client.render.CreepieRenderer;
import com.teamabnormals.savage_and_ravage.client.render.ExecutionerRenderer;
import com.teamabnormals.savage_and_ravage.client.render.GrieferRenderer;
import com.teamabnormals.savage_and_ravage.client.render.IceChunkRenderer;
import com.teamabnormals.savage_and_ravage.client.render.IceologerRenderer;
import com.teamabnormals.savage_and_ravage.client.render.MischiefArrowRenderer;
import com.teamabnormals.savage_and_ravage.client.render.NoModelRenderer;
import com.teamabnormals.savage_and_ravage.client.render.RunePrisonRenderer;
import com.teamabnormals.savage_and_ravage.client.render.SkeletonVillagerRenderer;
import com.teamabnormals.savage_and_ravage.client.render.SporeBombRenderer;
import com.teamabnormals.savage_and_ravage.client.render.TricksterRenderer;
import com.teamabnormals.savage_and_ravage.client.render.layer.TotemShieldLayer;
import com.teamabnormals.savage_and_ravage.core.other.SRCompat;
import com.teamabnormals.savage_and_ravage.core.other.SRDataProcessors;
import com.teamabnormals.savage_and_ravage.core.other.SRDataSerializers;
import com.teamabnormals.savage_and_ravage.core.other.SRFeatures;
import com.teamabnormals.savage_and_ravage.core.other.SRModelLayers;
import com.teamabnormals.savage_and_ravage.core.registry.SRAttributes;
import com.teamabnormals.savage_and_ravage.core.registry.SREffects;
import com.teamabnormals.savage_and_ravage.core.registry.SREntities;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import com.teamabnormals.savage_and_ravage.core.registry.SRParticles;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SavageAndRavage.MOD_ID)
public class SavageAndRavage {
	public static final String MOD_ID = "savage_and_ravage";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

	public SavageAndRavage() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext context = ModLoadingContext.get();

		SRDataProcessors.registerTrackedData();

		REGISTRY_HELPER.register(bus);
		SREntities.ENTITIES.register(bus);
		SRParticles.PARTICLES.register(bus);
		SREffects.EFFECTS.register(bus);
		SRFeatures.FEATURES.register(bus);
		SRAttributes.ATTRIBUTES.register(bus);
		SRDataSerializers.SERIALIZERS.register(bus);
		MinecraftForge.EVENT_BUS.register(this);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::registerLayers);
			bus.addListener(this::registerLayerDefinitions);
			bus.addListener(this::registerModels);
			bus.addListener(this::registerRenderers);
		});

		context.registerConfig(ModConfig.Type.COMMON, SRConfig.COMMON_SPEC);
		context.registerConfig(ModConfig.Type.CLIENT, SRConfig.CLIENT_SPEC);
		DataUtil.registerConfigCondition(SavageAndRavage.MOD_ID, SRConfig.COMMON, SRConfig.CLIENT);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SREntities.registerEntitySpawns();
			SREntities.registerWaveMembers();
			SRFeatures.registerPools();
			SRCompat.registerCompat();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(SRItems::registerItemProperties);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerLayers(EntityRenderersEvent.AddLayers event) {
		EntityRenderer<?> renderer = event.getRenderer(EntityType.EVOKER);
		if (renderer instanceof EvokerRenderer evokerRenderer) {
			evokerRenderer.addLayer(new TotemShieldLayer<Evoker, IllagerModel<Evoker>>(evokerRenderer, event.getEntityModels()));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void registerModels(ModelRegistryEvent event) {
		ForgeModelBakery.addSpecialModel(IceChunkRenderer.MODEL_LOCATION);
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
		event.registerEntityRenderer(SREntities.SKELETON_VILLAGER.get(), SkeletonVillagerRenderer::new);
		event.registerEntityRenderer(SREntities.CREEPIE.get(), CreepieRenderer::new);
		event.registerEntityRenderer(SREntities.GRIEFER.get(), GrieferRenderer::new);
		event.registerEntityRenderer(SREntities.ICEOLOGER.get(), IceologerRenderer::new);
		event.registerEntityRenderer(SREntities.EXECUTIONER.get(), ExecutionerRenderer::new);
		event.registerEntityRenderer(SREntities.TRICKSTER.get(), TricksterRenderer::new);
		event.registerEntityRenderer(SREntities.BURNING_BANNER.get(), BurningBannerRenderer::new);
		event.registerEntityRenderer(SREntities.SPORE_CLOUD.get(), NoModelRenderer::new);
		event.registerEntityRenderer(SREntities.SPORE_BOMB.get(), SporeBombRenderer::new);
		event.registerEntityRenderer(SREntities.MISCHIEF_ARROW.get(), MischiefArrowRenderer::new);
		event.registerEntityRenderer(SREntities.ICE_CHUNK.get(), IceChunkRenderer::new);
		event.registerEntityRenderer(SREntities.ICE_CLOUD.get(), NoModelRenderer::new);
		event.registerEntityRenderer(SREntities.RUNE_PRISON.get(), RunePrisonRenderer::new);
		event.registerEntityRenderer(SREntities.CONFUSION_BOLT.get(), NoModelRenderer::new);
	}
}
