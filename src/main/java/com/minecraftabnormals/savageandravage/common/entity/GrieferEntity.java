package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.common.item.CreeperSporesItem;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class GrieferEntity extends AbstractIllagerEntity implements IRangedAttackMob {
	private static final DataParameter<Boolean> KICKING = EntityDataManager.defineId(GrieferEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> APESHIT_MODE = EntityDataManager.defineId(GrieferEntity.class, DataSerializers.BOOLEAN);
	private final EntityPredicate distance = (new EntityPredicate()).range(10.0D);

	public GrieferEntity(EntityType<? extends GrieferEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public int creeperSporeStacks;
	public int kickTicks;
	public int kickCoolDown;

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new AbstractRaiderEntity.FindTargetGoal(this, 10.0F));
		this.goalSelector.addGoal(2, new GrieferEntity.KickGoal(this));
		this.goalSelector.addGoal(3, new GrieferEntity.MeleePhaseGoal(this, 0.9D, true));
		this.goalSelector.addGoal(3, new GrieferEntity.AttackWithSporesGoal(this, 0.9D, 200));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true) {
			@Override
			public boolean canUse() {
				return !(mob.getMainHandItem().getItem() instanceof CreeperSporesItem) && super.canUse();
			}
		});
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 15.0F));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.35F)
				.add(Attributes.MAX_HEALTH, 25.0D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 32.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(KICKING, false);
		this.entityData.define(APESHIT_MODE, false);
	}

	public AbstractIllagerEntity.ArmPose getArmPose() {
		return this.isCelebrating() ? AbstractIllagerEntity.ArmPose.CELEBRATING : AbstractIllagerEntity.ArmPose.NEUTRAL;
	}

	// temporary sounds
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.PILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.PILLAGER_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.PILLAGER_HURT;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(SRItems.GRIEFER_SPAWN_EGG.get());
	}

	// Just the pillagers ymca sound for now
	@Override
	public SoundEvent getCelebrateSound() {
		return SoundEvents.PILLAGER_CELEBRATE;
	}

	@Override
	public void aiStep() {
		if (this.kickTicks > 0) {
			--this.kickTicks;
		}

		if (this.kickCoolDown > 0) {
			--this.kickCoolDown;
		}

		super.aiStep();
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		if (this.isKicking()) {
			((LivingEntity) entityIn).knockback(1.5F, MathHelper.sin(this.yRot * ((float) Math.PI / 180F)), (-MathHelper.cos(this.yRot * ((float) Math.PI / 180F))));
			this.kickTicks = 10;
			this.level.broadcastEntityEvent(this, (byte) 4);
			this.lookAt(entityIn, 90.0F, 90.0F);
		}
		return super.doHurtTarget(entityIn);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 4) {
			this.kickTicks = 10;
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
		super.actuallyHurt(damageSrc, damageAmount);
		if (this.isApeshit() && creeperSporeStacks <= 0 && damageSrc.getEntity() == this.getTarget() && this.getTarget() != null)
			creeperSporeStacks = MathHelper.ceil(this.lastHurt / 2);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.kickTicks = compound.getInt("KickTicks");
		this.creeperSporeStacks = compound.getInt("CreeperSporeStacks");
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("KickTicks", this.kickTicks);
		compound.putInt("CreeperSporeStacks", this.creeperSporeStacks);
	}

	public int getKickTicks() {
		return this.kickTicks;
	}

	public void kick(LivingEntity entity) {
		this.setKicking(true);
		this.doHurtTarget(entity);
		this.kickCoolDown = 30;
	}

	public boolean isKicking() {
		return this.entityData.get(KICKING);
	}

	public boolean isApeshit() {
		return this.entityData.get(APESHIT_MODE);
	}

	public void setKicking(boolean shouldSetKicking) {
		this.entityData.set(KICKING, shouldSetKicking);
	}

	public void becomeApeshit(boolean shouldBecomeApeshit) {
		this.entityData.set(APESHIT_MODE, shouldBecomeApeshit);
	}

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		this.populateDefaultEquipmentSlots(difficultyIn);
		this.creeperSporeStacks = 3 + random.nextInt(3);
		return spawnDataIn;
	}

	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(SRItems.CREEPER_SPORES.get()));
		this.giveArmorOnRandom(EquipmentSlotType.HEAD, new ItemStack(SRItems.GRIEFER_HELMET.get()));
		this.giveArmorOnRandom(EquipmentSlotType.CHEST, new ItemStack(SRItems.GRIEFER_CHESTPLATE.get()));
		this.giveArmorOnRandom(EquipmentSlotType.LEGS, new ItemStack(SRItems.GRIEFER_LEGGINGS.get()));
		this.giveArmorOnRandom(EquipmentSlotType.FEET, new ItemStack(SRItems.GRIEFER_BOOTS.get()));
	}

	private void giveArmorOnRandom(EquipmentSlotType slot, ItemStack stack) {
		ItemStack itemstack = this.getItemBySlot(slot);
		float chance = this.isPatrolLeader() ? 1.00F : 0.100F; // feedback on chance would be epic
		if (itemstack.isEmpty() && this.random.nextFloat() < chance) {
			this.setItemSlot(slot, stack);
		}
	}

	@Override
	public void applyRaidBuffs(int arg0, boolean arg1) {
		// TODO : put something here
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		if (this.level.getNearbyEntities(CreepieEntity.class, distance, this, this.getBoundingBox().inflate(10.0D, 4.0D, 10.0D)).size() < 5 && this.getMainHandItem().getItem() instanceof CreeperSporesItem) {
			SporeCloudEntity creeperSpores = new SporeCloudEntity(this.level, this);
			double distance = target.getY() - 1;
			double d1 = target.getX() - this.getX();
			double d2 = distance - creeperSpores.getY();
			double d3 = target.getZ() - this.getZ();
			float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
			creeperSpores.shoot(d1, d2 + (double) f, d3, 1.6F, 12.0F);
			creeperSpores.setCloudSize(creeperSpores.level.random.nextInt(50) == 0 ? 0 : 1 + creeperSpores.level.random.nextInt(3));
			this.swing(getUsedItemHand());
			this.playSound(SRSounds.ENTITY_CREEPER_SPORES_THROW.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level.addFreshEntity(creeperSpores);
			this.lookAt(target, 30.0F, 30.0F);
			this.creeperSporeStacks--;
		}
	}

	public static class KickGoal extends Goal {
		private final GrieferEntity griefer;

		public KickGoal(GrieferEntity griefer) {
			this.griefer = griefer;
		}

		@Override
		public boolean canUse() {
			LivingEntity entity = griefer.getTarget();
			return griefer.getTarget() != null && entity != null && entity.distanceTo(griefer) <= 2.5D && !griefer.isKicking() && griefer.kickTicks <= 0 && griefer.kickCoolDown == 0 && griefer.creeperSporeStacks > 0;
		}

		@Override
		public void start() {
			LivingEntity entity = griefer.getTarget();
			if (entity != null)
				griefer.kick(entity);
		}

		@Override
		public void stop() {
			griefer.kickTicks = 0;
			griefer.setKicking(false);
		}

	}

	public static class MeleePhaseGoal extends MeleeAttackGoal {

		private final GrieferEntity griefer;

		public MeleePhaseGoal(GrieferEntity griefer, double speedIn, boolean useLongMemory) {
			super(griefer, speedIn, useLongMemory);
			this.griefer = griefer;
		}

		@Override
		public boolean canUse() {
			return griefer.creeperSporeStacks == 0 && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return griefer.creeperSporeStacks == 0 && super.canContinueToUse();
		}

		@Override
		public void start() {
			griefer.becomeApeshit(true);
			if (griefer.creeperSporeStacks == 0) {
				griefer.setItemInHand(griefer.getUsedItemHand(), ItemStack.EMPTY);
			}
			super.start();
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
			double distance = this.getAttackReachSqr(enemy);
			int i = griefer.level.random.nextInt(3);
			if (distToEnemySqr <= distance && this.ticksUntilNextAttack <= 0) {
				this.ticksUntilNextAttack = 60;
				switch (i) {
					case 0:
						this.griefer.swing(Hand.MAIN_HAND);
						break;

					case 1:
						this.griefer.swing(Hand.OFF_HAND);
						break;

					case 2:
						this.griefer.kick(enemy);
						break;
				}
				this.griefer.lookAt(enemy, 30.0F, 30.0F);
				this.griefer.doHurtTarget(enemy);
				enemy.knockback(1.5F, MathHelper.sin(griefer.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(griefer.yRot * ((float) Math.PI / 180F)));
			}
		}
	}

	public static class AttackWithSporesGoal extends Goal {
		private final GrieferEntity griefer;
		private int rangedAttackTime = -1;
		private final double entityMoveSpeed;
		private final int attackIntervalMin;
		private final int maxRangedAttackTime;
		private final float attackRadius;
		private boolean strafingClockwise;
		private int seeTime;
		private int strafeTime;

		public AttackWithSporesGoal(GrieferEntity attacker, double moveSpeed, int maxAttackTime) {
			this(attacker, moveSpeed, maxAttackTime, maxAttackTime);
		}

		public AttackWithSporesGoal(GrieferEntity attacker, double moveSpeed, int p_i1650_4_, int maxAttackTime) {
			this.griefer = attacker;
			this.entityMoveSpeed = moveSpeed;
			this.attackIntervalMin = p_i1650_4_;
			this.maxRangedAttackTime = maxAttackTime;
			this.attackRadius = (float) 15.0D;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			LivingEntity livingentity = this.griefer.getTarget();
			return livingentity != null && livingentity.isAlive() && this.griefer.creeperSporeStacks > 0;
		}

		@Override
		public boolean canContinueToUse() {
			return this.canUse() || !this.griefer.getNavigation().isDone();
		}

		@Override
		public void start() {
			if (griefer.isApeshit())
				griefer.becomeApeshit(false);

			if (griefer.creeperSporeStacks > 0)
				griefer.setItemInHand(Hand.MAIN_HAND, new ItemStack(SRItems.CREEPER_SPORES.get()));
		}

		@Override
		public void stop() {
			this.griefer.setAggressive(false);
			this.griefer.setTarget(null);
			this.griefer.getNavigation();
			this.rangedAttackTime = -1;
			if (griefer.isKicking())
				this.griefer.setKicking(false);
		}

		private boolean isWalkable() {
			PathNavigator pathnavigator = this.griefer.getNavigation();
			NodeProcessor nodeprocessor = pathnavigator.getNodeEvaluator();
			return nodeprocessor.getBlockPathType(this.griefer.level, MathHelper.floor(this.griefer.getX() + 1.0D), MathHelper.floor(this.griefer.getY()), MathHelper.floor(this.griefer.getZ() + 1.0D)) == PathNodeType.WALKABLE;
		}

		@Override
		public void tick() {
			LivingEntity attackTarget = griefer.getTarget();
			if (attackTarget != null) {
				double distance = this.griefer.distanceTo(attackTarget);
				boolean canSee = this.griefer.getSensing().canSee(attackTarget);
				this.griefer.setAggressive(true);
				if (canSee) {
					++this.seeTime;
				} else {
					--this.seeTime;
				}
				if (distance <= 15.0D) {
					this.griefer.getNavigation().stop();
				} else {
					this.griefer.getNavigation().moveTo(attackTarget, this.entityMoveSpeed);
				}
				if (this.griefer.getRandom().nextDouble() < 0.3D && strafeTime <= 0) {
					this.strafingClockwise = !this.strafingClockwise;
				}
				if (strafeTime > 0) {
					if (this.isWalkable()) {
						this.griefer.getMoveControl().strafe(0.0F, (float) (this.strafingClockwise ? this.entityMoveSpeed : -this.entityMoveSpeed));
					} else {
						this.griefer.setZza(1.0F);
						this.griefer.setXxa(0.0F);
						this.griefer.getNavigation().stop();
					}
				}
				this.griefer.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
				this.griefer.lookAt(attackTarget, 30.0F, 30.0F);
				if (--this.rangedAttackTime == 0 || this.seeTime == 3) {
					if (!canSee) {
						return;
					}
					float f = MathHelper.sqrt(distance) / this.attackRadius;
					float distanceFactor = MathHelper.clamp(f, 0.1F, 1.0F);
					this.griefer.performRangedAttack(attackTarget, distanceFactor);
					this.strafeTime = 50;
					this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
				} else if (this.rangedAttackTime < 0) {
					float f2 = MathHelper.sqrt(distance) / this.attackRadius;
					this.rangedAttackTime = MathHelper.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
				}
			}
		}
	}
}