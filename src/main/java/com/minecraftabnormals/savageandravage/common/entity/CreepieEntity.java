package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.abnormals_core.core.api.IAgeableEntity;
import com.minecraftabnormals.savageandravage.common.entity.goals.CreepieSwellGoal;
import com.minecraftabnormals.savageandravage.common.entity.goals.FollowMobOwnerGoal;
import com.minecraftabnormals.savageandravage.common.entity.goals.MobOwnerHurtByTargetGoal;
import com.minecraftabnormals.savageandravage.common.entity.goals.MobOwnerHurtTargetGoal;
import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.minecraftabnormals.savageandravage.core.registry.SRParticles;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@OnlyIn(value = Dist.CLIENT, _interface = IChargeableMob.class)
public class CreepieEntity extends MonsterEntity implements IChargeableMob, IOwnableMob, IAgeableEntity {
	private static final DataParameter<Integer> STATE = EntityDataManager.defineId(CreepieEntity.class, DataSerializers.INT);
	private static final DataParameter<Boolean> POWERED = EntityDataManager.defineId(CreepieEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IGNITED = EntityDataManager.defineId(CreepieEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.defineId(CreepieEntity.class, DataSerializers.OPTIONAL_UUID);
	private static final DataParameter<Integer> CONVERSION_TIME = EntityDataManager.defineId(CreepieEntity.class, DataSerializers.INT);
	public boolean attackPlayersOnly;
	public int lastActiveTime;
	public int timeSinceIgnited;
	public int fuseTime = 30;
	private int growingAge = -24000;
	private int forcedAgeTimer;
	private float explosionRadius;
	private boolean hasStartedConverting = false;

	public CreepieEntity(EntityType<? extends CreepieEntity> type, World worldIn) {
		super(type, worldIn);
		this.explosionRadius = 1.2f;
		this.xpReward = 0;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new CreepieSwellGoal(this));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new FollowMobOwnerGoal(this, 1.0D, 2.0F, 20.0F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(2, new MobOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new MobOwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<MobEntity>(this, MobEntity.class, true) {
			@Override
			public boolean canUse() {
				return super.canUse()
						&& ((CreepieEntity) mob).getOwnerId() == null
						&& !(this.target instanceof CreepieEntity)
						&& !(this.target instanceof CreeperEntity)
						&& !((CreepieEntity) mob).attackPlayersOnly;
			}
		});
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true) {
			@Override
			public boolean canUse() {
				return super.canUse() && ((CreepieEntity) mob).getOwnerId() == null;
			}
		});
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 5.0)
				.add(Attributes.MOVEMENT_SPEED, 0.35D);
	}

	@Override
	public int getMaxFallDistance() {
		return this.getTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return this.getOwner() == null;
	}

	@Override
	public boolean isPreventingPlayerRest(PlayerEntity playerIn) {
		return this.getOwner() == null;
	}

	public boolean causeFallDamage(float distance, float damageMultiplier) {
		boolean flag = super.causeFallDamage(distance, damageMultiplier);
		this.timeSinceIgnited = (int) ((float) this.timeSinceIgnited + distance * 1.5F);
		if (this.timeSinceIgnited > this.fuseTime - 5) {
			this.timeSinceIgnited = this.fuseTime - 5;
		}

		return flag;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(STATE, -1);
		this.entityData.define(POWERED, false);
		this.entityData.define(IGNITED, false);
		this.entityData.define(CONVERSION_TIME, -1);
		this.entityData.define(OWNER_UUID, Optional.empty());
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		if (this.getOwnerId() != null) {
			compound.putUUID("OwnerUUID", this.getOwnerId());
		}
		compound.putInt("Age", this.getGrowingAge());
		compound.putInt("ConversionTime", this.getConversionTime());
		compound.putShort("Fuse", (short) this.fuseTime);
		compound.putByte("ExplosionRadius", (byte) this.explosionRadius);
		compound.putBoolean("Ignited", this.hasIgnited());
		if (this.entityData.get(POWERED)) {
			compound.putBoolean("Powered", true);
		}
		compound.putBoolean("AttackPlayersOnly", this.attackPlayersOnly);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Fuse", 99)) {
			this.fuseTime = compound.getShort("Fuse");
		}
		this.explosionRadius = compound.getByte("ExplosionRadius");
		if (compound.contains("Age", 99)) {
			this.setGrowingAge(compound.getInt("Age"));
		}
		if (compound.getBoolean("Ignited")) this.ignite();
		this.entityData.set(POWERED, compound.getBoolean("Powered"));
		if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1) {
			this.startConversion(compound.getInt("ConversionTime"));
		}
		if (compound.hasUUID("OwnerUUID")) {
			this.setOwnerId(compound.getUUID("OwnerUUID"));
		}
		this.attackPlayersOnly = compound.getBoolean("AttackPlayersOnly");
	}

	@Override
	public boolean isPowered() {
		return this.entityData.get(POWERED);
	}

	public void setCharged(boolean charged) {
		this.entityData.set(POWERED, charged);
	}


	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(Items.CREEPER_SPAWN_EGG);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.8F;
	}

	public int getGrowingAge() {
		return this.growingAge;
	}

	private void ageUp(int growthSeconds) {
		int i = this.getGrowingAge();
		i = i + growthSeconds * 20;
		if (i > 0) {
			i = 0;
		}

		this.setGrowingAge(i);
		if (this.forcedAgeTimer == 0) {
			this.forcedAgeTimer = 40;
		}
	}

	public void setGrowingAge(int age) {
		int i = this.growingAge;
		this.growingAge = age;
		if (i < 0 && age >= 0) {
			this.startConversion(this.random.nextInt(80) + 160); //10 seconds before it converts
		}
	}

	/**
	 * Creates an explosion as determined by this creeper's power and explosion radius.
	 */
	protected void explode() {
		if (!this.level.isClientSide()) {
			Explosion.Mode mode = SRConfig.COMMON.creepieExplosionsDestroyBlocks.get() ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
			float chargedModifier = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius * chargedModifier, mode);
			this.remove();
			this.spawnLingeringCloud();
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.level.isClientSide()) {
			if (this.forcedAgeTimer > 0) {
				if (this.forcedAgeTimer % 4 == 0) {
					this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
				}
				this.forcedAgeTimer--;
			}
		}
		if (this.isAlive()) {
			int i = this.getGrowingAge();
			if (i < 0) {
				++i;
				this.setGrowingAge(i);
			} else if (i > 0) {
				--i;
				this.setGrowingAge(i);
			}
		}

	}

	@Override
	protected float getVoicePitch() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SRSounds.ENTITY_CREEPIE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SRSounds.ENTITY_CREEPIE_DEATH.get();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() {
		if (this.isAlive()) {
			this.lastActiveTime = this.timeSinceIgnited;
			if (this.hasIgnited()) {
				this.setCreeperState(1);
			}
			int i = this.getCreeperState();
			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound(SRSounds.ENTITY_CREEPIE_PRIMED.get(), this.getSoundVolume(), this.getVoicePitch());
			}
			this.timeSinceIgnited += i;
			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
				this.explode();
			}
			if (this.isConverting()) {
				if (this.hasStartedConverting) {
					this.playSound(SRSounds.ENTITY_CREEPIE_CONVERT.get(), 1.0F, 1.0F);
					this.hasStartedConverting = false;
				}
				this.setConversionTime(this.getConversionTime() - 1);
				if (this.getConversionTime() <= 0) {
					this.finishConversion(this.level);
				}
				if (this.level.isClientSide()) {
					this.level.addParticle(SRParticles.CREEPER_SPORES.get(), this.getX() - 0.5d + (double) (this.random.nextFloat()), this.getY() + 0.5d, this.getZ() - 0.5d + (double) (this.random.nextFloat()), 0.0D, (this.random.nextFloat() / 5.0F), 0.0D);
				}
			}
		}
		super.tick();
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		return true;
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() == Items.BONE_MEAL) {
			if (this.getGrowingAge() < 0) {
				this.consumeItemFromStack(player, itemstack);
				this.ageUp((int) ((-this.getGrowingAge() / 20) * 0.1F));
				return ActionResultType.SUCCESS;
			}
		}
		if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
			this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
			if (!this.level.isClientSide()) {
				this.ignite();
				itemstack.hurtAndBreak(1, player, (p_213625_1_) -> p_213625_1_.broadcastBreakEvent(hand));
			}

			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.FAIL;
		}
	}

	/**
	 * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
	 */
	@OnlyIn(Dist.CLIENT)
	public float getCreeperFlashIntensity(float partialTicks) {
		return MathHelper.lerp(partialTicks, (float) this.lastActiveTime, (float) this.timeSinceIgnited) / (float) (this.fuseTime - 2);
	}

	/**
	 * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
	 */
	public int getCreeperState() {
		return this.entityData.get(STATE);
	}

	/**
	 * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
	 */
	public void setCreeperState(int state) {
		this.entityData.set(STATE, state);
	}

	public boolean hasIgnited() {
		return this.entityData.get(IGNITED);
	}

	public void ignite() {
		this.entityData.set(IGNITED, true);
	}

	protected void spawnLingeringCloud() {
		Collection<EffectInstance> collection = this.getActiveEffects();
		if (!collection.isEmpty()) {
			AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
			areaeffectcloudentity.setRadius(1.0F);
			areaeffectcloudentity.setRadiusOnUse(-0.5F);
			areaeffectcloudentity.setWaitTime(10);
			areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
			areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());

			for (EffectInstance effectinstance : collection) {
				areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
			}

			this.level.addFreshEntity(areaeffectcloudentity);
		}

	}

	private void consumeItemFromStack(PlayerEntity player, ItemStack stack) {
		if (!player.isCreative()) {
			stack.shrink(1);
		}
	}

	@Override
	public boolean canBeLeashed(PlayerEntity player) {
		return (!this.isLeashed() && this.getOwnerId() != null);
	}

	@Override
	@Nullable
	public UUID getOwnerId() {
		return this.entityData.get(OWNER_UUID).orElse(null);
	}

	@Override
	public void setOwnerId(@Nullable UUID ownerId) {
		this.entityData.set(OWNER_UUID, Optional.ofNullable(ownerId));
	}

	@Override
	@Nullable
	public LivingEntity getOwner() {
		if (!this.level.isClientSide()) { //TODO: this is experimental, if anything breaks by being only on the client, a packet is needed
			UUID uuid = this.getOwnerId();
			if (uuid == null)
				return null;
			Entity entity = ((ServerWorld) this.level).getEntity(uuid);
			return entity instanceof LivingEntity ? (LivingEntity) entity : null;
		}
		return null;
	}

	@Override
	public boolean canAttack(LivingEntity target) {
		return target != this.getOwner() && super.canAttack(target);
	}

	@Override
	public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
		boolean shouldAttack = true;
		if (target instanceof IOwnableMob) {
			shouldAttack = ((IOwnableMob) target).getOwner() != owner;
		} else if (target instanceof TameableEntity) {
			shouldAttack = ((TameableEntity) target).getOwner() != owner;
		} else if (target instanceof PlayerEntity && owner instanceof PlayerEntity) {
			shouldAttack = ((PlayerEntity) owner).canHarmPlayer((PlayerEntity) target);
		}
		return shouldAttack;
	}

	public boolean isConverting() {
		return this.getEntityData().get(CONVERSION_TIME) > -1;
	}

	private void startConversion(int conversionTime) {
		this.setConversionTime(conversionTime);
		hasStartedConverting = true; //Sound playing is done in tick because after summoning, this is called before the position is set
	}

	public int getConversionTime() {
		return this.getEntityData().get(CONVERSION_TIME);
	}

	private void setConversionTime(int conversionTimeIn) {
		this.entityData.set(CONVERSION_TIME, conversionTimeIn);
	}

	private LivingEntity finishConversion(World world) {
		CreeperEntity creeperEntity = EntityType.CREEPER.create(this.level);
		if (creeperEntity == null)
			return null;

		creeperEntity.copyPosition(this);
		if (!this.level.isClientSide())
			creeperEntity.finalizeSpawn((ServerWorld) world, this.level.getCurrentDifficultyAt(creeperEntity.blockPosition()), SpawnReason.CONVERSION, null, null);
		creeperEntity.setNoAi(this.isNoAi());
		if (this.hasCustomName()) {
			creeperEntity.setCustomName(this.getCustomName());
			creeperEntity.setCustomNameVisible(this.isCustomNameVisible());
		}

		if (this.isPowered())
			creeperEntity.getEntityData().set(CreeperEntity.DATA_IS_POWERED, true);

		if (this.isPersistenceRequired()) {
			creeperEntity.setPersistenceRequired();
		}
		if (this.isLeashed()) {
			if (this.getLeashHolder() != null) creeperEntity.setLeashedTo(this.getLeashHolder(), true);
			this.dropLeash(true, false);
		}

		if (this.getVehicle() != null) {
			creeperEntity.startRiding(this.getVehicle());
		}
		creeperEntity.setInvulnerable(this.isInvulnerable());
		creeperEntity.setHealth(creeperEntity.getMaxHealth());
		this.dead = true;
		this.remove();
		this.level.addFreshEntity(creeperEntity);
		this.playSound(SRSounds.ENTITY_CREEPIE_GROW.get(), 1.0F, 1.0F);
		return creeperEntity;
	}

	@Override
	public Team getTeam() {
		LivingEntity owner = this.getOwner();
		if (owner != null)
			return owner.getTeam();
		return super.getTeam();
	}

	@Override
	public boolean isAlliedTo(Entity entityIn) {
		LivingEntity owner = this.getOwner();
		if (entityIn == owner)
			return true;

		if (owner != null)
			return owner.isAlliedTo(entityIn);

		return super.isAlliedTo(entityIn);
	}

	@Override
	public boolean hasGrowthProgress() {
		return true;
	}

	@Override
	public void resetGrowthProgress() {
		this.setGrowingAge(-24000);
	}

	@Override
	public boolean canAge(boolean isGrowing) {
		return isGrowing;
	}

	@Override
	public LivingEntity attemptAging(boolean isGrowing) {
		if (isGrowing) {
			this.growingAge = 0;
			return this.finishConversion(this.level);
		}
		return this;
	}
}
