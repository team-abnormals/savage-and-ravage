package com.teamabnormals.savage_and_ravage.core.registry;

import com.teamabnormals.savage_and_ravage.common.entity.decoration.BurningBanner;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.ConfusionBolt;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Creepie;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Executioner;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Griefer;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.IceChunk;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.IceCloud;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Iceologer;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.MischiefArrow;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.RunePrison;
import com.teamabnormals.savage_and_ravage.common.entity.monster.SkeletonVillager;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.SporeCloud;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Trickster;
import com.teamabnormals.savage_and_ravage.common.entity.item.SporeBomb;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
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
public class SREntityTypes {
	public static final EntitySubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getEntitySubHelper();
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<EntityType<SkeletonVillager>> SKELETON_VILLAGER = HELPER.createLivingEntity("skeleton_villager", SkeletonVillager::new, MobCategory.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<Creepie>> CREEPIE = HELPER.createLivingEntity("creepie", Creepie::new, MobCategory.CREATURE, 0.5F, 0.90F);
	public static final RegistryObject<EntityType<Griefer>> GRIEFER = HELPER.createLivingEntity("griefer", Griefer::new, MobCategory.MONSTER, 0.6F, 1.99F);
	public static final RegistryObject<EntityType<Iceologer>> ICEOLOGER = ENTITIES.register("iceologer", () -> EntityType.Builder.of(Iceologer::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":iceologer"));
	public static final RegistryObject<EntityType<Executioner>> EXECUTIONER = ENTITIES.register("executioner", () -> EntityType.Builder.of(Executioner::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":executioner"));
	public static final RegistryObject<EntityType<Trickster>> TRICKSTER = HELPER.createLivingEntity("trickster", Trickster::new, MobCategory.MONSTER, 0.6F, 1.89F);

	public static final RegistryObject<EntityType<BurningBanner>> BURNING_BANNER = ENTITIES.register("burning_banner", () -> EntityType.Builder.<BurningBanner>of(BurningBanner::new, MobCategory.MISC).fireImmune().sized(1.0F, 2.0F).noSummon().build(SavageAndRavage.MOD_ID + ":burning_banner"));
	public static final RegistryObject<EntityType<SporeCloud>> SPORE_CLOUD = ENTITIES.register("spore_cloud", () -> EntityType.Builder.<SporeCloud>of(SporeCloud::new, MobCategory.MISC).fireImmune().sized(0.25F, 0.25F).build(SavageAndRavage.MOD_ID + ":creeper_spore_cloud"));
	public static final RegistryObject<EntityType<SporeBomb>> SPORE_BOMB = ENTITIES.register("spore_bomb", () -> EntityType.Builder.<SporeBomb>of(SporeBomb::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).build(SavageAndRavage.MOD_ID + ":spore_bomb"));
	public static final RegistryObject<EntityType<MischiefArrow>> MISCHIEF_ARROW = HELPER.createEntity("mischief_arrow", MischiefArrow::new, MischiefArrow::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<IceChunk>> ICE_CHUNK = ENTITIES.register("ice_chunk", () -> EntityType.Builder.<IceChunk>of(IceChunk::new, MobCategory.MISC).sized(2.2F, 1.0F).clientTrackingRange(8).updateInterval(Integer.MAX_VALUE).build(SavageAndRavage.MOD_ID + ":ice_chunk"));
	public static final RegistryObject<EntityType<IceCloud>> ICE_CLOUD = ENTITIES.register("ice_cloud", () -> EntityType.Builder.<IceCloud>of(IceCloud::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).build(SavageAndRavage.MOD_ID + ":ice_cloud"));
	public static final RegistryObject<EntityType<RunePrison>> RUNE_PRISON = ENTITIES.register("rune_prison", () -> EntityType.Builder.<RunePrison>of(RunePrison::new, MobCategory.MISC).fireImmune().sized(1.35F, 0.7F).build(SavageAndRavage.MOD_ID + ":rune_prison"));
	public static final RegistryObject<EntityType<ConfusionBolt>> CONFUSION_BOLT = ENTITIES.register("confusion_bolt", () -> EntityType.Builder.<ConfusionBolt>of(ConfusionBolt::new, MobCategory.MISC).fireImmune().sized(1.0F, 1.0F).build(SavageAndRavage.MOD_ID + ":confusion_bolt"));

	public static void registerEntitySpawns() {
		SpawnPlacements.register(SKELETON_VILLAGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(CREEPIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(GRIEFER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(ICEOLOGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Iceologer::canIceologerSpawn);
		SpawnPlacements.register(EXECUTIONER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		SpawnPlacements.register(TRICKSTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
	}

	public static void registerWaveMembers() {
		Raid.RaiderType.create("GRIEFER", GRIEFER.get(), new int[]{0, 1, 0, 1, 2, 2, 3, 2});
		Raid.RaiderType.create("EXECUTIONER", EXECUTIONER.get(), new int[]{0, 0, 1, 0, 0, 1, 2, 2});
		Raid.RaiderType.create("TRICKSTER", TRICKSTER.get(), new int[]{0, 0, 0, 0, 0, 1, 1, 2});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SKELETON_VILLAGER.get(), AbstractSkeleton.createAttributes().build());
		event.put(CREEPIE.get(), Creepie.registerAttributes().build());
		event.put(GRIEFER.get(), Griefer.registerAttributes().build());
		event.put(ICEOLOGER.get(), Iceologer.registerAttributes().build());
		event.put(EXECUTIONER.get(), Executioner.registerAttributes().build());
		event.put(TRICKSTER.get(), Trickster.registerAttributes().build());
	}
}
