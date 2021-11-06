package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedDataManager;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.savageandravage.common.block.RunedGloomyTilesBlock;
import com.minecraftabnormals.savageandravage.core.other.SRDataProcessors;
import com.minecraftabnormals.savageandravage.core.other.SRDataSerializers;
import com.minecraftabnormals.savageandravage.core.other.SREvents;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.minecraftabnormals.savageandravage.core.registry.SRParticles;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class TricksterEntity extends SpellcastingIllagerEntity implements ITracksHits {
	private static final DataParameter<Integer> PRISON_CHARGING_TIME = EntityDataManager.defineId(TricksterEntity.class, DataSerializers.INT);
	private static final DataParameter<Optional<Vector3d>> PRISON_POS = EntityDataManager.defineId(TricksterEntity.class, SRDataSerializers.OPTIONAL_VECTOR3D);
	private static final int chargeTime = 4;
	private static final int prisonTime = 60;
	private final Set<Entity> trackedSpellEntities = new HashSet<>();

	public TricksterEntity(EntityType<? extends SpellcastingIllagerEntity> type, World p_i48551_2_) {
		super(type, p_i48551_2_);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new CastingASpellGoal());
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
		this.goalSelector.addGoal(5, new CreatePrisonGoal());
		this.goalSelector.addGoal(6, new ThrowBoltGoal());
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IronGolemEntity.class, 8.0F, 0.6D, 1.0D));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, 10, true, false, (target) -> {
			return TrackedDataManager.INSTANCE.getValue(this, SRDataProcessors.TOTEM_SHIELD_TIME) <= 0;
		}).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, 10, true, false, (target) -> {
			return TrackedDataManager.INSTANCE.getValue(this, SRDataProcessors.TOTEM_SHIELD_TIME) <= 0;
		}).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (target) -> {
			return TrackedDataManager.INSTANCE.getValue(this, SRDataProcessors.TOTEM_SHIELD_TIME) <= 0;
		}).setUnseenMemoryTicks(300));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
	}


	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(PRISON_CHARGING_TIME, -1);
		this.entityData.define(PRISON_POS, Optional.empty());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		if (compound.contains("PrisonChargingTime", 3))
			this.entityData.set(PRISON_CHARGING_TIME, compound.getInt("PrisonChargingTime"));
		if (compound.contains("PrisonX", 6) && compound.contains("PrisonY", 6) && compound.contains("PrisonZ", 6))
			this.entityData.set(PRISON_POS, Optional.of(new Vector3d(compound.getInt("PrisonX"), compound.getInt("PrisonY"), compound.getInt("PrisonX"))));
	}


	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("PrisonChargingTime", this.entityData.get(PRISON_CHARGING_TIME));
		if (this.entityData.get(PRISON_CHARGING_TIME) > 0) {
			this.entityData.get(PRISON_POS).ifPresent(pos -> {
				compound.putDouble("PrisonX", pos.x);
				compound.putDouble("PrisonY", pos.y);
				compound.putDouble("PrisonZ", pos.z);
			});
		}
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.775F;
	}

	@Override
	public void tick() {
		super.tick();
		//Rune particles at hands
		if (level.random.nextInt(14) == 0 && this.isCastingSpell()) {
			//Handles entity rotation
			float f = this.yBodyRot * ((float) Math.PI / 180F) + MathHelper.cos((float) this.tickCount * 0.6662F) * 0.25F;
			float f1 = MathHelper.cos(f);
			float f2 = MathHelper.sin(f);
			//Spawns particles at hands
			IParticleData particle = this.getCurrentSpell() == SpellType.FANGS ? SRParticles.RUNE.get() : ParticleTypes.ENTITY_EFFECT;
			this.level.addParticle(particle, this.getX() + (double) f1 * 0.8D, this.getY() + 1.5D, this.getZ() + (double) f2 * 0.6D, 0.0, 0.0, 0.0);
			this.level.addParticle(particle, this.getX() - (double) f1 * 0.8D, this.getY() + 1.5D, this.getZ() - (double) f2 * 0.6D, 0.0, 0.0, 0.0);
		}
		//Dealing with the prison spell charge up
		this.entityData.get(PRISON_POS).ifPresent(pos -> {
			int time = this.entityData.get(PRISON_CHARGING_TIME);
			if (time > 0) {
				int loops = 10 * ((chargeTime - time)) + 1; //Adds more loops (therefore more particles) as the spell progresses
				for (int i = 0; i < loops; i++) {
					/* progress - as the charge goes down the particles get closer to the edge instead of the middle.
					 * This value increases mid-tick, because of the second part of the expression*/
					double progress = (((double) time) / chargeTime) - ((((double) 1) / chargeTime) * (((double) i) / loops));
					/* coefficient - the value to multiply random.nextDouble() by - it decreases as progress increases
					 * because the adjustment value goes up, accounting for the difference.
					 * When both applied to a rand function, coefficient and adjustment mean that for higher progress
					 * values, the function will generate higher values*/
					double coefficient = 0.65625D - (0.34375D * progress);
					double adjustment = 0.34375D - (0.34375D * progress);
					double x = pos.x + (random.nextInt(2) == 0 ? 1 : -1) * 0.65625D * random.nextDouble(); //Generating the x pos at a random position in the 'prison outline'
					//Generating the corresponding z pos - if the x value is 'near the edges' (far from the middle) this can be anything, but if it is close to the middle it is constrained to edge values
					double z = pos.z + (random.nextInt(2) == 0 ? 1 : -1) * (Math.abs(pos.x - x) < 0.34375 ? ((coefficient * random.nextDouble()) + adjustment) : 0.65625D * random.nextDouble());
					this.level.addParticle(SRParticles.RUNE.get(), x, pos.y + 0.8125D, z, 0.0, 0.0, 0.0);
				}
				/* These doubles throughout are not arbitrary - they're calculated by dividing pixel values on
				 * the prison by 16 (which works because it is never scaled). 0.65625 is half the total length and 0.34375
				 * is the distance between the middle and the start of the 'outer ring'*/
				this.entityData.set(PRISON_CHARGING_TIME, time - 1);
			} else if (time == 0) {
				RunePrisonEntity runePrison = new RunePrisonEntity(this.level, null, prisonTime, false, this);
				runePrison.moveTo(pos.x, pos.y + 0.5, pos.z, 0.0F, 0.0F);
				this.level.addFreshEntity(runePrison);
				this.trackedSpellEntities.add(runePrison);
				this.entityData.set(PRISON_POS, Optional.empty());
				this.entityData.set(PRISON_CHARGING_TIME, -1);
			}
		});
		this.trackedSpellEntities.removeIf(Objects::isNull);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		IDataManager data = (IDataManager) this;
		if (source.getDirectEntity() instanceof ProjectileEntity && this.getHealth() - amount <= 0 && data.getValue(SRDataProcessors.TOTEM_SHIELD_COOLDOWN) <= 0) {
			this.setHealth(2.0F);
			data.setValue(SRDataProcessors.TOTEM_SHIELD_COOLDOWN, 1800);
			if (!this.level.isClientSide()) {
				this.level.broadcastEntityEvent(this, (byte) 35);
				for (int i = 0; i < 64; i++) {
					if (this.teleport())
						return true;
				}
			}
			return false;
		}
		return super.hurt(source, amount);
	}

	protected boolean teleport() {
		if (this.isAlive()) {
			double randomX = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
			double randomZ = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
			BlockState state = this.level.getBlockState(new BlockPos.Mutable(randomX, this.getY() - 1, randomZ));
			if (state.getMaterial().blocksMotion() && !state.getFluidState().is(FluidTags.LAVA)) {
				AxisAlignedBB oldBox = this.getBoundingBox().inflate(0.5D);
				BlockPos oldPos = this.blockPosition();
				boolean successful = this.randomTeleport(randomX, this.getY(), randomZ, true);
				if (successful) {
					this.level.playSound(null, oldPos, SRSounds.GENERIC_PUFF_OF_SMOKE.get(), this.getSoundSource(), 10.0F, 1.0F);
					this.level.playSound(null, this.blockPosition(), SRSounds.GENERIC_PUFF_OF_SMOKE.get(), this.getSoundSource(), 10.0F, 1.0F);
					this.level.playSound(null, oldPos, SRSounds.ENTITY_TRICKSTER_LAUGH.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
					ConfusionBoltEntity.spawnGaussianParticles(this.level, oldBox, SREvents.POOF_KEY, 50);
					ConfusionBoltEntity.spawnGaussianParticles(this.level, this.getBoundingBox().inflate(0.5D), SREvents.POOF_KEY, 50);
					if (ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
						BlockPos.Mutable searchPos = new BlockPos.Mutable();
						for (int x = oldPos.getX() - 2; x <= oldPos.getX() + 2; x++) {
							for (int y = oldPos.getY() - 2; y <= oldPos.getY() + 2; y++) {
								for (int z = oldPos.getZ() - 2; z <= oldPos.getZ() + 2; z++) {
									searchPos.set(x, y, z);
									if (this.level.getBlockState(searchPos).getBlock() == SRBlocks.GLOOMY_TILES.get()) {
										this.level.setBlock(searchPos, SRBlocks.RUNED_GLOOMY_TILES.get().defaultBlockState(), 2);
										searchPos.move(Direction.UP);
										if (!this.level.isClientSide && !this.level.getBlockState(searchPos).isSolidRender(this.level, searchPos)) {
											for (int i = 0; i < 3; i++)
												NetworkUtil.spawnParticle(SRParticles.RUNE.getId().toString(), this.level.dimension(), x + random.nextDouble(), y + 1.25, z + random.nextDouble(), 0.0D, 0.0D, 0.0D);
										}
									}
								}
							}
						}
					}
				}
				return successful;
			}
		}
		return false;
	}


	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return SRSounds.ENTITY_TRICKSTER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SRSounds.ENTITY_TRICKSTER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SRSounds.ENTITY_TRICKSTER_DEATH.get();
	}

	@Override
	public void playAmbientSound() {
		if (this.entityData.get(PRISON_CHARGING_TIME) < 0 && this.trackedSpellEntities.isEmpty()) //Prevents overlap
			super.playAmbientSound();
	}


	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		super.playStepSound(pos, blockIn);
		this.playSound(SRSounds.ENTITY_TRICKSTER_STEP.get(), 0.5F, 1.0F);
	}

	@Override
	protected SoundEvent getCastingSoundEvent() {
		return SRSounds.ENTITY_TRICKSTER_CAST_SPELL.get();
	}

	@Override
	public void applyRaidBuffs(int wave, boolean p_213660_2_) {
	}

	@Override
	public SoundEvent getCelebrateSound() {
		return SRSounds.ENTITY_TRICKSTER_CELEBRATE.get();
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(SRItems.TRICKSTER_SPAWN_EGG.get());
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.MAX_HEALTH, 24.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	@Nullable
	@Override
	public void onTrackedHit(Entity hitter, Entity hit) {
		if (RunedGloomyTilesBlock.shouldTrigger(hit, false)) {
			if (trackedSpellEntities.contains(hitter)) {
				this.level.playSound(null, this.blockPosition(), SRSounds.ENTITY_TRICKSTER_LAUGH.get(), SoundCategory.HOSTILE, 1.0f, 1.0f);
				trackedSpellEntities.remove(hitter);
			}
		}
	}

	class CreatePrisonGoal extends UseSpellGoal {

		@Override
		protected void performSpellCasting() {
			LivingEntity target = TricksterEntity.this.getTarget();
			if (target != null) {
				TricksterEntity.this.entityData.set(PRISON_POS, Optional.of(target.position()));
				TricksterEntity.this.entityData.set(PRISON_CHARGING_TIME, chargeTime);
			}
		}

		@Override
		protected int getCastingTime() {
			return 40;
		}

		@Override
		protected int getCastingInterval() {
			return 100;
		}

		@Nullable
		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SRSounds.GENERIC_PREPARE_ATTACK.get();
		}

		@Override
		protected SpellType getSpell() {
			return SpellType.FANGS;
		}
	}

	class ThrowBoltGoal extends UseSpellGoal {

		@Override
		protected void performSpellCasting() {
			World world = TricksterEntity.this.level;
			LivingEntity target = TricksterEntity.this.getTarget();
			if (target != null) {
				ConfusionBoltEntity bolt = new ConfusionBoltEntity(world, TricksterEntity.this, 240);
				Vector3d pos = TricksterEntity.this.position();
				Vector3d targetPos = target.position();
				bolt.setPos(bolt.getX(), bolt.getY() - 0.5, bolt.getZ());
				bolt.setDeltaMovement(new Vector3d(targetPos.x - pos.x, targetPos.y - pos.y, targetPos.z - pos.z).normalize().scale(0.25));
				world.addFreshEntity(bolt);
				TricksterEntity.this.trackedSpellEntities.add(bolt);
			}
		}

		@Override
		protected int getCastingTime() {
			return 80;
		}

		@Override
		protected int getCastingInterval() {
			return 340;
		}

		@Nullable
		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SRSounds.GENERIC_PREPARE_ATTACK.get();
		}

		@Override
		protected SpellType getSpell() {
			return SpellType.SUMMON_VEX;
		}
	}
}
