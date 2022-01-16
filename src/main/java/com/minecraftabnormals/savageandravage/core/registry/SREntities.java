package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.common.entity.BurningBannerEntity;
import com.minecraftabnormals.savageandravage.common.entity.ConfusionBoltEntity;
import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import com.minecraftabnormals.savageandravage.common.entity.ExecutionerEntity;
import com.minecraftabnormals.savageandravage.common.entity.GrieferEntity;
import com.minecraftabnormals.savageandravage.common.entity.IceChunkEntity;
import com.minecraftabnormals.savageandravage.common.entity.IceCloudEntity;
import com.minecraftabnormals.savageandravage.common.entity.IceologerEntity;
import com.minecraftabnormals.savageandravage.common.entity.MischiefArrowEntity;
import com.minecraftabnormals.savageandravage.common.entity.RunePrisonEntity;
import com.minecraftabnormals.savageandravage.common.entity.SkeletonVillagerEntity;
import com.minecraftabnormals.savageandravage.common.entity.SporeCloudEntity;
import com.minecraftabnormals.savageandravage.common.entity.TricksterEntity;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SREntities {
	public static final EntitySubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getEntitySubHelper();
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<EntityType<SkeletonVillagerEntity>> SKELETON_VILLAGER = HELPER.createLivingEntity("skeleton_villager", SkeletonVillagerEntity::new, MobCategory.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<CreepieEntity>> CREEPIE = HELPER.createLivingEntity("creepie", CreepieEntity::new, MobCategory.CREATURE, 0.5F, 0.90F);
	public static final RegistryObject<EntityType<GrieferEntity>> GRIEFER = HELPER.createLivingEntity("griefer", GrieferEntity::new, MobCategory.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<IceologerEntity>> ICEOLOGER = ENTITIES.register("iceologer", () -> EntityType.Builder.of(IceologerEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":iceologer"));
	public static final RegistryObject<EntityType<ExecutionerEntity>> EXECUTIONER = ENTITIES.register("executioner", () -> EntityType.Builder.of(ExecutionerEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":executioner"));
	public static final RegistryObject<EntityType<TricksterEntity>> TRICKSTER = HELPER.createLivingEntity("trickster", TricksterEntity::new, MobCategory.MONSTER, 0.6F, 1.89F);

	public static final RegistryObject<EntityType<BurningBannerEntity>> BURNING_BANNER = ENTITIES.register("burning_banner", () -> EntityType.Builder.<BurningBannerEntity>of(BurningBannerEntity::new, MobCategory.MISC).fireImmune().sized(1.0F, 2.0F).noSummon().build(SavageAndRavage.MOD_ID + ":burning_banner"));
	public static final RegistryObject<EntityType<SporeCloudEntity>> SPORE_CLOUD = ENTITIES.register("spore_cloud", () -> EntityType.Builder.<SporeCloudEntity>of(SporeCloudEntity::new, MobCategory.MISC).fireImmune().sized(0.25F, 0.25F).build(SavageAndRavage.MOD_ID + ":creeper_spore_cloud"));
	public static final RegistryObject<EntityType<SporeBombEntity>> SPORE_BOMB = ENTITIES.register("spore_bomb", () -> EntityType.Builder.<SporeBombEntity>of(SporeBombEntity::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).build(SavageAndRavage.MOD_ID + ":spore_bomb"));
	public static final RegistryObject<EntityType<MischiefArrowEntity>> MISCHIEF_ARROW = HELPER.createEntity("mischief_arrow", MischiefArrowEntity::new, MischiefArrowEntity::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<IceChunkEntity>> ICE_CHUNK = ENTITIES.register("ice_chunk", () -> EntityType.Builder.<IceChunkEntity>of(IceChunkEntity::new, MobCategory.MISC).sized(2.2F, 1.0F).clientTrackingRange(8).updateInterval(Integer.MAX_VALUE).build(SavageAndRavage.MOD_ID + ":ice_chunk"));
	public static final RegistryObject<EntityType<IceCloudEntity>> ICE_CLOUD = ENTITIES.register("ice_cloud", () -> EntityType.Builder.<IceCloudEntity>of(IceCloudEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":ice_cloud"));
	public static final RegistryObject<EntityType<RunePrisonEntity>> RUNE_PRISON = ENTITIES.register("rune_prison", () -> EntityType.Builder.<RunePrisonEntity>of(RunePrisonEntity::new, MobCategory.MISC).fireImmune().sized(1.35F, 0.7F).build(SavageAndRavage.MOD_ID + ":rune_prison"));
	public static final RegistryObject<EntityType<ConfusionBoltEntity>> CONFUSION_BOLT = ENTITIES.register("confusion_bolt", () -> EntityType.Builder.<ConfusionBoltEntity>of(ConfusionBoltEntity::new, MobCategory.MISC).fireImmune().sized(1.0F, 1.0F).build(SavageAndRavage.MOD_ID + ":confusion_bolt"));


	public static void registerEntitySpawns() {
		SpawnPlacements.register(SREntities.SKELETON_VILLAGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(SREntities.CREEPIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(SREntities.GRIEFER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(SREntities.ICEOLOGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IceologerEntity::canIceologerSpawn);
		SpawnPlacements.register(SREntities.EXECUTIONER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(SREntities.TRICKSTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
	}

	public static void registerWaveMembers() {
		Raid.RaiderType.create("GRIEFER", SREntities.GRIEFER.get(), new int[]{0, 1, 0, 1, 2, 2, 3, 2});
		Raid.RaiderType.create("EXECUTIONER", SREntities.EXECUTIONER.get(), new int[]{0, 0, 1, 0, 0, 1, 2, 2});
		Raid.RaiderType.create("TRICKSTER", SREntities.TRICKSTER.get(), new int[]{0, 0, 0, 0, 0, 1, 1, 2});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SKELETON_VILLAGER.get(), AbstractSkeleton.createAttributes().build());
		event.put(CREEPIE.get(), CreepieEntity.registerAttributes().build());
		event.put(GRIEFER.get(), GrieferEntity.registerAttributes().build());
		event.put(ICEOLOGER.get(), IceologerEntity.registerAttributes().build());
		event.put(EXECUTIONER.get(), ExecutionerEntity.registerAttributes().build());
		event.put(TRICKSTER.get(), TricksterEntity.registerAttributes().build());
	}
}
