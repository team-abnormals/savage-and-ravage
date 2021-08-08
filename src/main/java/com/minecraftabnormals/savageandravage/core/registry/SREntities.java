package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.DataProcessors;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.SyncType;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedData;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedDataManager;
import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.savageandravage.client.render.*;
import com.minecraftabnormals.savageandravage.client.render.layer.TotemShieldLayer;
import com.minecraftabnormals.savageandravage.common.entity.*;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.other.SRCompat;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SREntities {

	public static final EntitySubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getEntitySubHelper();
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<EntityType<CreepieEntity>> CREEPIE = HELPER.createLivingEntity("creepie", CreepieEntity::new, EntityClassification.CREATURE, 0.5F, 0.90F);
	public static final RegistryObject<EntityType<GrieferEntity>> GRIEFER = HELPER.createLivingEntity("griefer", GrieferEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<SporeCloudEntity>> SPORE_CLOUD = ENTITIES.register("spore_cloud", () -> EntityType.Builder.<SporeCloudEntity>of(SporeCloudEntity::new, EntityClassification.MISC).fireImmune().sized(0.25F, 0.25F).build(SavageAndRavage.MOD_ID + ":creeper_spore_cloud"));
	public static final RegistryObject<EntityType<SporeBombEntity>> SPORE_BOMB = ENTITIES.register("spore_bomb", () -> EntityType.Builder.<SporeBombEntity>of(SporeBombEntity::new, EntityClassification.MISC).fireImmune().sized(0.98F, 0.98F).build(SavageAndRavage.MOD_ID + ":spore_bomb"));
	public static final RegistryObject<EntityType<MischiefArrowEntity>> MISCHIEF_ARROW = HELPER.createEntity("mischief_arrow", MischiefArrowEntity::new, MischiefArrowEntity::new, EntityClassification.MISC, 0.5F, 0.5F);

	public static final RegistryObject<EntityType<SkeletonVillagerEntity>> SKELETON_VILLAGER = HELPER.createLivingEntity("skeleton_villager", SkeletonVillagerEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<BurningBannerEntity>> BURNING_BANNER = ENTITIES.register("burning_banner", () -> EntityType.Builder.<BurningBannerEntity>of(BurningBannerEntity::new, EntityClassification.MISC).fireImmune().sized(1.0F, 2.0F).noSummon().build(SavageAndRavage.MOD_ID + ":burning_banner"));
	public static final RegistryObject<EntityType<RunePrisonEntity>> RUNE_PRISON = ENTITIES.register("rune_prison", () -> EntityType.Builder.<RunePrisonEntity>of(RunePrisonEntity::new, EntityClassification.MISC).fireImmune().sized(1.35F, 0.7F).build(SavageAndRavage.MOD_ID + ":rune_prison"));
	public static final RegistryObject<EntityType<ExecutionerEntity>> EXECUTIONER = ENTITIES.register("executioner", () -> EntityType.Builder.of(ExecutionerEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":executioner"));
	public static final RegistryObject<EntityType<TricksterEntity>> TRICKSTER = HELPER.createLivingEntity("trickster", TricksterEntity::new, EntityClassification.MONSTER, 0.6F, 1.89F);

	public static final RegistryObject<EntityType<IceologerEntity>> ICEOLOGER = ENTITIES.register("iceologer", () -> EntityType.Builder.of(IceologerEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":iceologer"));
	public static final RegistryObject<EntityType<IceChunkEntity>> ICE_CHUNK = ENTITIES.register("ice_chunk", () -> EntityType.Builder.<IceChunkEntity>of(IceChunkEntity::new, EntityClassification.MISC).sized(2.2F, 1.0F).clientTrackingRange(8).updateInterval(Integer.MAX_VALUE).build(SavageAndRavage.MOD_ID + ":ice_chunk"));
	public static final RegistryObject<EntityType<IceCloudEntity>> ICE_CLOUD = ENTITIES.register("ice_cloud", () -> EntityType.Builder.<IceCloudEntity>of(IceCloudEntity::new, EntityClassification.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":ice_cloud"));

	public static final TrackedData<Integer> TOTEM_SHIELD_TIME = TrackedData.Builder.create(DataProcessors.INT, () -> -1).enableSaving().build();
	public static final TrackedData<Integer> TOTEM_SHIELD_COOLDOWN = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().build();
	public static final TrackedData<Boolean> MARK_INVISIBLE = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enableSaving().setSyncType(SyncType.TO_CLIENTS).build();
	public static final TrackedData<Boolean> INVISIBLE_DUE_TO_MASK = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enableSaving().setSyncType(SyncType.TO_CLIENTS).build();
	public static final TrackedData<Optional<Vector3d>> PREVIOUS_POSITION = TrackedData.Builder.create(SRCompat.OPTIONAL_VECTOR3D, Optional::empty).setSyncType(SyncType.NOPE).build();
	public static final TrackedData<Integer> ILLEGAL_MASK_TICKS = TrackedData.Builder.create(DataProcessors.INT, () -> 0).setSyncType(SyncType.NOPE).build();

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SREntities.SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(SREntities.EXECUTIONER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(SREntities.ICEOLOGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceologerEntity::canIceologerSpawn);
	}

	public static void registerTrackedData() {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "evoker_shield_time"), TOTEM_SHIELD_TIME);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "evoker_shield_cooldown"), TOTEM_SHIELD_COOLDOWN);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "mark_invisible"), MARK_INVISIBLE);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "invisible_due_to_mask"), INVISIBLE_DUE_TO_MASK);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "previous_position"), PREVIOUS_POSITION);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "illegal_mask_ticks"), ILLEGAL_MASK_TICKS);
	}

	public static void registerWaveMembers() {
		Raid.WaveMember.create("GRIEFER", SREntities.GRIEFER.get(), new int[]{0, 1, 0, 1, 2, 2, 3, 2});
		Raid.WaveMember.create("EXECUTIONER", SREntities.EXECUTIONER.get(), new int[]{0, 0, 1, 0, 0, 1, 2, 2});
		Raid.WaveMember.create("TRICKSTER", SREntities.TRICKSTER.get(), new int[]{0, 0, 1, 0, 0, 1, 2, 2});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(CREEPIE.get(), CreepieEntity.registerAttributes().build());
		event.put(GRIEFER.get(), GrieferEntity.registerAttributes().build());
		event.put(SKELETON_VILLAGER.get(), AbstractSkeletonEntity.createAttributes().build());
		event.put(ICEOLOGER.get(), IceologerEntity.registerAttributes().build());
		event.put(EXECUTIONER.get(), ExecutionerEntity.registerAttributes().build());
		event.put(TRICKSTER.get(), TricksterEntity.registerAttributes().build());
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderLayers() {
		EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
		EntityRenderer<?> renderer = manager.renderers.get(EntityType.EVOKER);
		if (renderer instanceof EvokerRenderer) {
			EvokerRenderer<EvokerEntity> livingRenderer = (EvokerRenderer<EvokerEntity>) renderer;
			livingRenderer.addLayer(new TotemShieldLayer<>(livingRenderer, new IllagerModel<>(2.0F, 0.0F, 64, 64)));
		}
	}

	public static void registerRendering() {
		RenderingRegistry.registerEntityRenderingHandler(CREEPIE.get(), CreepieRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SKELETON_VILLAGER.get(), SkeletonVillagerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(GRIEFER.get(), GrieferRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SPORE_CLOUD.get(), NoModelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(SPORE_BOMB.get(), SporeBombRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BURNING_BANNER.get(), BurningBannerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(RUNE_PRISON.get(), RunePrisonRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MISCHIEF_ARROW.get(), MischiefArrowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ICEOLOGER.get(), IceologerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ICE_CHUNK.get(), IceChunkRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ICE_CLOUD.get(), NoModelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EXECUTIONER.get(), ExecutionerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(TRICKSTER.get(), TricksterRenderer::new);
	}
}
