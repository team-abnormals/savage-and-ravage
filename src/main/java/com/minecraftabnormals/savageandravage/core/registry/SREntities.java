package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.DataProcessors;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedData;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedDataManager;
import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.savageandravage.client.render.*;
import com.minecraftabnormals.savageandravage.common.entity.*;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SREntities {

	public static final EntitySubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getEntitySubHelper();
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<EntityType<CreepieEntity>> CREEPIE = HELPER.createLivingEntity("creepie", CreepieEntity::new, EntityClassification.CREATURE, 0.5F, 0.90F);
	public static final RegistryObject<EntityType<GrieferEntity>> GRIEFER = HELPER.createLivingEntity("griefer", GrieferEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<SporeCloudEntity>> SPORE_CLOUD = ENTITIES.register("spore_cloud", () -> EntityType.Builder.<SporeCloudEntity>create(SporeCloudEntity::new, EntityClassification.MISC).immuneToFire().size(0.25F, 0.25F).build(SavageAndRavage.MOD_ID + ":creeper_spore_cloud"));
	public static final RegistryObject<EntityType<SporeBombEntity>> SPORE_BOMB = ENTITIES.register("spore_bomb", () -> EntityType.Builder.<SporeBombEntity>create(SporeBombEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).build(SavageAndRavage.MOD_ID + ":spore_bomb"));
	public static final RegistryObject<EntityType<MischiefArrowEntity>> MISCHIEF_ARROW = HELPER.createEntity("mischief_arrow", MischiefArrowEntity::new, MischiefArrowEntity::new, EntityClassification.MISC, 0.5F, 0.5F);

	public static final RegistryObject<EntityType<SkeletonVillagerEntity>> SKELETON_VILLAGER = HELPER.createLivingEntity("skeleton_villager", SkeletonVillagerEntity::new, EntityClassification.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<BurningBannerEntity>> BURNING_BANNER = ENTITIES.register("burning_banner", () -> EntityType.Builder.<BurningBannerEntity>create(BurningBannerEntity::new, EntityClassification.MISC).immuneToFire().size(1.0F, 2.0F).disableSummoning().build(SavageAndRavage.MOD_ID + ":burning_banner"));
	public static final RegistryObject<EntityType<RunePrisonEntity>> RUNE_PRISON = ENTITIES.register("rune_prison", () -> EntityType.Builder.<RunePrisonEntity>create(RunePrisonEntity::new, EntityClassification.MISC).immuneToFire().size(1.35F, 0.7F).build(SavageAndRavage.MOD_ID + ":rune_prison"));
	public static final RegistryObject<EntityType<ExecutionerEntity>> EXECUTIONER = ENTITIES.register("executioner", () -> EntityType.Builder.create(ExecutionerEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F).trackingRange(8).build(SavageAndRavage.MOD_ID + ":executioner"));
	public static final RegistryObject<EntityType<TricksterEntity>> TRICKSTER = HELPER.createLivingEntity("trickster", TricksterEntity::new, EntityClassification.MONSTER, 0.6F, 1.89F);

	public static final RegistryObject<EntityType<IceologerEntity>> ICEOLOGER = ENTITIES.register("iceologer", () -> EntityType.Builder.create(IceologerEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F).trackingRange(8).build(SavageAndRavage.MOD_ID + ":iceologer"));
	public static final RegistryObject<EntityType<IceChunkEntity>> ICE_CHUNK = ENTITIES.register("ice_chunk", () -> EntityType.Builder.<IceChunkEntity>create(IceChunkEntity::new, EntityClassification.MISC).size(2.2F, 1.0F).trackingRange(8).func_233608_b_(Integer.MAX_VALUE).build(SavageAndRavage.MOD_ID + ":ice_chunk"));
	public static final RegistryObject<EntityType<IceCloudEntity>> ICE_CLOUD = ENTITIES.register("ice_cloud", () -> EntityType.Builder.<IceCloudEntity>create(IceCloudEntity::new, EntityClassification.MISC).size(1.0F, 1.0F).trackingRange(8).build(SavageAndRavage.MOD_ID + ":ice_cloud"));

	public static final TrackedData<Integer> EVOKER_SHIELD_TIME = TrackedData.Builder.create(DataProcessors.INT, () -> -1).enableSaving().build();
	public static final TrackedData<Integer> EVOKER_SHIELD_COOLDOWN = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().build();

	public static void registerEntitySpawns() {
		EntitySpawnPlacementRegistry.register(SREntities.SKELETON_VILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.EXECUTIONER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
		EntitySpawnPlacementRegistry.register(SREntities.ICEOLOGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceologerEntity::canIceologerSpawn);
	}

	public static void registerTrackedData() {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "evoker_shield_time"), EVOKER_SHIELD_TIME);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "evoker_shield_cooldown"), EVOKER_SHIELD_COOLDOWN);
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

	public static void registerAttributes() {
		GlobalEntityTypeAttributes.put(CREEPIE.get(), CreepieEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(GRIEFER.get(), GrieferEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(SKELETON_VILLAGER.get(), AbstractSkeletonEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(ICEOLOGER.get(), IceologerEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(EXECUTIONER.get(), ExecutionerEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(TRICKSTER.get(), TricksterEntity.registerAttributes().create());
	}

	public static void registerWaveMembers() {
		Raid.WaveMember.create("GRIEFER", SREntities.GRIEFER.get(), new int[]{0, 1, 0, 1, 2, 2, 3, 2});
		Raid.WaveMember.create("EXECUTIONER", SREntities.EXECUTIONER.get(), new int[]{0, 0, 1, 0, 0, 1, 2, 2});
		Raid.WaveMember.create("TRICKSTER", SREntities.TRICKSTER.get(), new int[]{0, 0, 1, 0, 0, 1, 2, 2});
	}
}
