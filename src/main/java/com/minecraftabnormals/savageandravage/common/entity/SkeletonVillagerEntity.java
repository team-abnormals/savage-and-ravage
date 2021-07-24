package com.minecraftabnormals.savageandravage.common.entity;

import com.google.common.collect.Maps;
import com.minecraftabnormals.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class SkeletonVillagerEntity extends AbstractSkeletonEntity implements ICrossbowUser {
	private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.defineId(SkeletonVillagerEntity.class, DataSerializers.BOOLEAN);
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
	public CreatureAttribute getMobType() {
		return CreatureAttribute.UNDEAD;
	}

	public SkeletonVillagerEntity(EntityType<? extends SkeletonVillagerEntity> type, World worldIn) {
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
		ItemStack itemstack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, Items.CROSSBOW));
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
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, SkeletonVillagerEntity.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
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
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(SRItems.SKELETON_VILLAGER_SPAWN_EGG.get());
	}

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		this.populateDefaultEquipmentSlots(difficultyIn);
		this.populateDefaultEquipmentEnchantments(difficultyIn);
		if (worldIn.getRandom().nextInt(100) == 0) {
			SpiderEntity spider = EntityType.SPIDER.create(this.level);
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
		this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
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
	public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
		this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
	}
}