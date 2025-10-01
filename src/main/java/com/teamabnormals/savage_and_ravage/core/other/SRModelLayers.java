package com.teamabnormals.savage_and_ravage.core.other;

import com.teamabnormals.savage_and_ravage.client.model.*;
import com.teamabnormals.savage_and_ravage.client.renderer.entity.*;
import com.teamabnormals.savage_and_ravage.client.renderer.entity.layers.TotemShieldLayer;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SRModelLayers {
	public static final ModelLayerLocation CREEPIE = register("creepie");
	public static final ModelLayerLocation CREEPIE_ARMOR = register("creepie", "armor");
	public static final ModelLayerLocation EXECUTIONER = register("executioner");
	public static final ModelLayerLocation GRIEFER = register("griefer");
	public static final ModelLayerLocation GRIEFER_ARMOR = register("griefer_armor", "armor");
	public static final ModelLayerLocation ICEOLOGER = register("iceologer");
	public static final ModelLayerLocation ILLAGER_ARMOR = register("illager", "armor");
	public static final ModelLayerLocation MASK_OF_DISHONESTY = register("mask_of_dishonesty", "armor");
	public static final ModelLayerLocation RUNE_PRISON = register("rune_prison");
	public static final ModelLayerLocation SKELETON_VILLAGER = register("skeleton_villager");
	public static final ModelLayerLocation TRICKSTER = register("trickster");
	public static final ModelLayerLocation VILLAGER_INNER_ARMOR = register("villager_armor", "inner_armor");
	public static final ModelLayerLocation VILLAGER_OUTER_ARMOR = register("villager_armor", "outer_armor");

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {
		EntityRenderer<?> renderer = event.getRenderer(EntityType.EVOKER);
		if (renderer instanceof EvokerRenderer evokerRenderer) {
			evokerRenderer.addLayer(new TotemShieldLayer<Evoker, IllagerModel<Evoker>>(evokerRenderer, event.getEntityModels()));
		}
	}

	@SubscribeEvent
	public static void registerModels(ModelEvent.RegisterAdditional event) {
		event.register(IceChunkRenderer.MODEL_LOCATION);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
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

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
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


	public static ModelLayerLocation register(String name) {
		return register(name, "main");
	}

	public static ModelLayerLocation register(String name, String layer) {
		return new ModelLayerLocation(SavageAndRavage.location(name), layer);
	}
}
