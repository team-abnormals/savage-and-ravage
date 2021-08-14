package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.savageandravage.client.render.*;
import com.minecraftabnormals.savageandravage.client.render.layer.TotemShieldLayer;
import com.minecraftabnormals.savageandravage.common.entity.*;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SREntities {

	public static final EntitySubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getEntitySubHelper();
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<EntityType<SkeletonVillagerEntity>> SKELETON_VILLAGER = HELPER.createLivingEntity("skeleton_villager", SkeletonVillagerEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<CreepieEntity>> CREEPIE = HELPER.createLivingEntity("creepie", CreepieEntity::new, EntityClassification.CREATURE, 0.5F, 0.90F);
	public static final RegistryObject<EntityType<GrieferEntity>> GRIEFER = HELPER.createLivingEntity("griefer", GrieferEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<IceologerEntity>> ICEOLOGER = ENTITIES.register("iceologer", () -> EntityType.Builder.of(IceologerEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":iceologer"));
	public static final RegistryObject<EntityType<ExecutionerEntity>> EXECUTIONER = ENTITIES.register("executioner", () -> EntityType.Builder.of(ExecutionerEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":executioner"));
	public static final RegistryObject<EntityType<TricksterEntity>> TRICKSTER = HELPER.createLivingEntity("trickster", TricksterEntity::new, EntityClassification.MONSTER, 0.6F, 1.89F);

	public static final RegistryObject<EntityType<BurningBannerEntity>> BURNING_BANNER = ENTITIES.register("burning_banner", () -> EntityType.Builder.<BurningBannerEntity>of(BurningBannerEntity::new, EntityClassification.MISC).fireImmune().sized(1.0F, 2.0F).noSummon().build(SavageAndRavage.MOD_ID + ":burning_banner"));
	public static final RegistryObject<EntityType<SporeCloudEntity>> SPORE_CLOUD = ENTITIES.register("spore_cloud", () -> EntityType.Builder.<SporeCloudEntity>of(SporeCloudEntity::new, EntityClassification.MISC).fireImmune().sized(0.25F, 0.25F).build(SavageAndRavage.MOD_ID + ":creeper_spore_cloud"));
	public static final RegistryObject<EntityType<SporeBombEntity>> SPORE_BOMB = ENTITIES.register("spore_bomb", () -> EntityType.Builder.<SporeBombEntity>of(SporeBombEntity::new, EntityClassification.MISC).fireImmune().sized(0.98F, 0.98F).build(SavageAndRavage.MOD_ID + ":spore_bomb"));
	public static final RegistryObject<EntityType<MischiefArrowEntity>> MISCHIEF_ARROW = HELPER.createEntity("mischief_arrow", MischiefArrowEntity::new, MischiefArrowEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<IceChunkEntity>> ICE_CHUNK = ENTITIES.register("ice_chunk", () -> EntityType.Builder.<IceChunkEntity>of(IceChunkEntity::new, EntityClassification.MISC).sized(2.2F, 1.0F).clientTrackingRange(8).updateInterval(Integer.MAX_VALUE).build(SavageAndRavage.MOD_ID + ":ice_chunk"));
	public static final RegistryObject<EntityType<IceCloudEntity>> ICE_CLOUD = ENTITIES.register("ice_cloud", () -> EntityType.Builder.<IceCloudEntity>of(IceCloudEntity::new, EntityClassification.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":ice_cloud"));
	public static final RegistryObject<EntityType<RunePrisonEntity>> RUNE_PRISON = ENTITIES.register("rune_prison", () -> EntityType.Builder.<RunePrisonEntity>of(RunePrisonEntity::new, EntityClassification.MISC).fireImmune().sized(1.35F, 0.7F).build(SavageAndRavage.MOD_ID + ":rune_prison"));
	public static final RegistryObject<EntityType<ConfusionBoltEntity>> CONFUSION_BOLT = ENTITIES.register("confusion_bolt", () -> EntityType.Builder.<ConfusionBoltEntity>of(ConfusionBoltEntity::new, EntityClassification.MISC).fireImmune().sized(1.0F, 1.0F).build(SavageAndRavage.MOD_ID + ":confusion_bolt"));


	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SREntities.SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(SREntities.EXECUTIONER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(SREntities.ICEOLOGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceologerEntity::canIceologerSpawn);
	}

	public static void registerWaveMembers() {
		Raid.WaveMember.create("GRIEFER", SREntities.GRIEFER.get(), new int[]{0, 1, 0, 1, 2, 2, 3, 2});
		Raid.WaveMember.create("EXECUTIONER", SREntities.EXECUTIONER.get(), new int[]{0, 0, 1, 0, 0, 1, 2, 2});
		Raid.WaveMember.create("TRICKSTER", SREntities.TRICKSTER.get(), new int[]{0, 0, 0, 0, 0, 1, 1, 2});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SKELETON_VILLAGER.get(), AbstractSkeletonEntity.createAttributes().build());
		event.put(CREEPIE.get(), CreepieEntity.registerAttributes().build());
		event.put(GRIEFER.get(), GrieferEntity.registerAttributes().build());
		event.put(ICEOLOGER.get(), IceologerEntity.registerAttributes().build());
		event.put(EXECUTIONER.get(), ExecutionerEntity.registerAttributes().build());
		event.put(TRICKSTER.get(), TricksterEntity.registerAttributes().build());
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	public static void registerRenderLayers() {
		EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
		EntityRenderer<?> renderer = manager.renderers.get(EntityType.EVOKER);
		if (renderer instanceof EvokerRenderer) {
			EvokerRenderer<EvokerEntity> livingRenderer = (EvokerRenderer<EvokerEntity>) renderer;
			livingRenderer.addLayer(new TotemShieldLayer<>(livingRenderer, new IllagerModel<>(2.0F, 0.0F, 64, 64)));
		}
	}

	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(SKELETON_VILLAGER.get(), SkeletonVillagerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(CREEPIE.get(), CreepieRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(GRIEFER.get(), GrieferRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ICEOLOGER.get(), IceologerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EXECUTIONER.get(), ExecutionerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(TRICKSTER.get(), TricksterRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BURNING_BANNER.get(), BurningBannerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SPORE_CLOUD.get(), NoModelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SPORE_BOMB.get(), SporeBombRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MISCHIEF_ARROW.get(), MischiefArrowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ICE_CHUNK.get(), IceChunkRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ICE_CLOUD.get(), NoModelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(RUNE_PRISON.get(), RunePrisonRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(CONFUSION_BOLT.get(), NoModelRenderer::new);
	}
}
