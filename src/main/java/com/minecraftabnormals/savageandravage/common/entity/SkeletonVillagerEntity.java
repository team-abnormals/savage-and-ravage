package com.minecraftabnormals.savageandravage.common.entity;

import com.google.common.collect.Maps;
import com.minecraftabnormals.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.Map;

public class SkeletonVillagerEntity extends AbstractSkeleton implements CrossbowAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(SkeletonVillagerEntity.class, EntityDataSerializers.BOOLEAN);
	private final ImprovedCrossbowGoal<SkeletonVillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<>(this, 1.0D, 8.0F, 5.0D);
	private final MeleeAttackGoal aiMelee = new MeleeAttackGoal(this, 1.2D, false) {
		@Override
		public void stop() {
			super.stop();
			SkeletonVillagerEntity.this.setAggressive(false);
		}

		@Override
		public void start() {
			super.start();
			SkeletonVillagerEntity.this.setAggressive(true);
		}
	};

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	public SkeletonVillagerEntity(EntityType<? extends SkeletonVillagerEntity> type, Level worldIn) {
		super(type, worldIn);
		this.reassessWeaponGoal();
	}

	@Override
	public double getMyRidingOffset() {
		return -0.4D;
	}

	@Override
	public void reassessWeaponGoal() {
		this.goalSelector.removeGoal(this.aiMelee);
		this.goalSelector.removeGoal(this.aiCrossBow);
		ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
		if (itemstack.getItem() instanceof CrossbowItem) {
			this.goalSelector.addGoal(3, this.aiCrossBow);
		} else {
			super.reassessWeaponGoal();
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, SkeletonVillagerEntity.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SKELETON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SKELETON_DEATH;
	}

	@Override
	protected SoundEvent getStepSound() {
		return SoundEvents.SKELETON_STEP;
	}

	@Override
	public void setChargingCrossbow(boolean isCharging) {
		this.entityData.set(DATA_CHARGING_STATE, isCharging);
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(SRItems.SKELETON_VILLAGER_SPAWN_EGG.get());
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		this.populateDefaultEquipmentSlots(difficultyIn);
		this.populateDefaultEquipmentEnchantments(difficultyIn);
		if (worldIn.getRandom().nextInt(100) == 0) {
			Spider spider = EntityType.SPIDER.create(this.level);
			if (spider != null) {
				spider.copyPosition(this);
				worldIn.addFreshEntity(spider);
				this.startRiding(spider);
			}
		}
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		ItemStack itemstack = new ItemStack(Items.CROSSBOW);
		if (this.random.nextInt(300) == 0) {
			Map<Enchantment, Integer> map = Maps.newHashMap();
			map.put(Enchantments.PIERCING, 1);
			EnchantmentHelper.setEnchantments(map, itemstack);
		}
		this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_CHARGING_STATE, false);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		this.performCrossbowAttack(this, 1.6F);
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGING_STATE);
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Override
	public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, Projectile p_230284_3_, float p_230284_4_) {
		this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
	}
}