package com.teamabnormals.savage_and_ravage.common.entity.monster;

import com.teamabnormals.savage_and_ravage.common.entity.OwnableMob;
import com.teamabnormals.savage_and_ravage.common.entity.ai.goal.CreepieSwellGoal;
import com.teamabnormals.savage_and_ravage.common.entity.ai.goal.FollowMobOwnerGoal;
import com.teamabnormals.savage_and_ravage.common.entity.ai.goal.MobOwnerHurtByTargetGoal;
import com.teamabnormals.savage_and_ravage.common.entity.ai.goal.MobOwnerHurtTargetGoal;
import com.teamabnormals.savage_and_ravage.core.SRConfig;
import com.teamabnormals.savage_and_ravage.core.registry.SRParticles;
import com.teamabnormals.savage_and_ravage.core.registry.SRSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.scores.Team;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@OnlyIn(value = Dist.CLIENT, _interface = PowerableMob.class)
public class Creepie extends Monster implements PowerableMob, OwnableMob {
	private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(Creepie.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(Creepie.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IGNITED = SynchedEntityData.defineId(Creepie.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(Creepie.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Integer> CONVERSION_TIME = SynchedEntityData.defineId(Creepie.class, EntityDataSerializers.INT);
	public boolean attackPlayersOnly;
	public int lastActiveTime;
	public int timeSinceIgnited;
	public int fuseTime = 30;
	private int growingAge = -24000;
	private int forcedAgeTimer;
	private float explosionRadius;
	private boolean hasStartedConverting = false;

	public Creepie(EntityType<? extends Creepie> type, Level worldIn) {
		super(type, worldIn);
		this.explosionRadius = 1.2f;
		this.xpReward = 0;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new CreepieSwellGoal(this));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new FollowMobOwnerGoal(this, 1.0D, 2.0F, 20.0F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(2, new MobOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new MobOwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (target) -> this.getOwnerId() == null && !(target instanceof Creepie) && !(target instanceof Creeper) && !this.attackPlayersOnly));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 5, false, false, (target) -> this.getOwnerId() == null));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMobAttributes()
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
	public boolean isPreventingPlayerRest(Player playerIn) {
		return this.getOwner() == null;
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		boolean flag = super.causeFallDamage(distance, damageMultiplier, source);
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
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getOwnerId() != null) {
			compound.putUUID("OwnerUUID", this.getOwnerId());
		}
		compound.putInt("Age", this.getGrowingAge());
		compound.putInt("ConversionTime", this.getConversionTime());
		compound.putShort("Fuse", (short) this.fuseTime);
		compound.putFloat("ExplosionRadius", this.explosionRadius);
		compound.putBoolean("Ignited", this.hasIgnited());
		if (this.entityData.get(POWERED)) {
			compound.putBoolean("Powered", true);
		}
		compound.putBoolean("AttackPlayersOnly", this.attackPlayersOnly);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Fuse", 99))
			this.fuseTime = compound.getShort("Fuse");
		if (compound.contains("ExplosionRadius", 99))
			this.explosionRadius = compound.getFloat("ExplosionRadius");
		if (compound.contains("Age", 99))
			this.setGrowingAge(compound.getInt("Age"));
		if (compound.getBoolean("Ignited")) this.ignite();
		this.entityData.set(POWERED, compound.getBoolean("Powered"));
		if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1)
			this.startConversion(compound.getInt("ConversionTime"));
		if (compound.hasUUID("OwnerUUID"))
			this.setOwnerId(compound.getUUID("OwnerUUID"));
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
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(Items.CREEPER_SPAWN_EGG);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
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
			Explosion.BlockInteraction mode = SRConfig.COMMON.creepieExplosionsDestroyBlocks.get() && ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
			float chargedModifier = this.isPowered() ? 2.0F : 1.0F;
			this.dead = true;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius * chargedModifier, mode);
			this.discard();
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
	public float getVoicePitch() {
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
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() == Items.BONE_MEAL) {
			if (this.getGrowingAge() < 0) {
				this.consumeItemFromStack(player, itemstack);
				this.ageUp((int) ((-this.getGrowingAge() / 20) * 0.1F));
				return InteractionResult.SUCCESS;
			}
		}
		if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
			this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
			if (!this.level.isClientSide()) {
				this.ignite();
				itemstack.hurtAndBreak(1, player, (p_213625_1_) -> p_213625_1_.broadcastBreakEvent(hand));
			}

			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.FAIL;
		}
	}

	/**
	 * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
	 */
	@OnlyIn(Dist.CLIENT)
	public float getCreeperFlashIntensity(float partialTicks) {
		return Mth.lerp(partialTicks, (float) this.lastActiveTime, (float) this.timeSinceIgnited) / (float) (this.fuseTime - 2);
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
		Collection<MobEffectInstance> collection = this.getActiveEffects();
		if (!collection.isEmpty()) {
			AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
			areaeffectcloudentity.setRadius(1.0F);
			areaeffectcloudentity.setRadiusOnUse(-0.5F);
			areaeffectcloudentity.setWaitTime(10);
			areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
			areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());

			for (MobEffectInstance effectinstance : collection) {
				areaeffectcloudentity.addEffect(new MobEffectInstance(effectinstance));
			}

			this.level.addFreshEntity(areaeffectcloudentity);
		}

	}

	private void consumeItemFromStack(Player player, ItemStack stack) {
		if (!player.isCreative()) {
			stack.shrink(1);
		}
	}

	@Override
	public boolean canBeLeashed(Player player) {
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
			Entity entity = ((ServerLevel) this.level).getEntity(uuid);
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
		if (target instanceof OwnableMob) {
			shouldAttack = ((OwnableMob) target).getOwner() != owner;
		} else if (target instanceof TamableAnimal) {
			shouldAttack = ((TamableAnimal) target).getOwner() != owner;
		} else if (target instanceof Player && owner instanceof Player) {
			shouldAttack = ((Player) owner).canHarmPlayer((Player) target);
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

	private LivingEntity finishConversion(Level world) {
		Creeper creeperEntity = EntityType.CREEPER.create(this.level);
		if (creeperEntity == null)
			return null;

		creeperEntity.copyPosition(this);
		if (!this.level.isClientSide())
			creeperEntity.finalizeSpawn((ServerLevel) world, this.level.getCurrentDifficultyAt(creeperEntity.blockPosition()), MobSpawnType.CONVERSION, null, null);
		creeperEntity.setNoAi(this.isNoAi());
		if (this.hasCustomName()) {
			creeperEntity.setCustomName(this.getCustomName());
			creeperEntity.setCustomNameVisible(this.isCustomNameVisible());
		}

		if (this.isPowered())
			creeperEntity.getEntityData().set(Creeper.DATA_IS_POWERED, true);

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
		this.discard();
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
}
