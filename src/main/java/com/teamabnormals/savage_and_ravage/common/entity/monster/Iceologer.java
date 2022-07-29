package com.teamabnormals.savage_and_ravage.common.entity.monster;

import com.teamabnormals.savage_and_ravage.common.entity.projectile.IceChunk;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.IceCloud;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import com.teamabnormals.savage_and_ravage.core.registry.SRSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author Ocelot
 */
public class Iceologer extends SpellcasterIllager {
	private UUID iceChunkEntityUUID;
	private int iceChunkEntity;

	public double prevChasingPosX;
	public double prevChasingPosY;
	public double prevChasingPosZ;
	public double chasingPosX;
	public double chasingPosY;
	public double chasingPosZ;
	public float prevCameraYaw;
	public float cameraYaw;

	public Iceologer(EntityType<Iceologer> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new SpellcasterCastingSpellGoal());
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6D, 1.0D));
		this.goalSelector.addGoal(5, new AttackIceChunkGoal());
		this.goalSelector.addGoal(6, new AttackIceCloudGoal());
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
		this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.ATTACK_KNOCKBACK).add(Attributes.MOVEMENT_SPEED, 0.5D);
	}

	private void updateCape() {
		this.prevChasingPosX = this.chasingPosX;
		this.prevChasingPosY = this.chasingPosY;
		this.prevChasingPosZ = this.chasingPosZ;
		double d0 = this.getX() - this.chasingPosX;
		double d1 = this.getY() - this.chasingPosY;
		double d2 = this.getZ() - this.chasingPosZ;
		if (d0 > 10.0D) {
			this.chasingPosX = this.getX();
//            this.prevChasingPosX = this.chasingPosX;
		}

		if (d2 > 10.0D) {
			this.chasingPosZ = this.getZ();
//            this.prevChasingPosZ = this.chasingPosZ;
		}

		if (d1 > 10.0D) {
			this.chasingPosY = this.getY();
//            this.prevChasingPosY = this.chasingPosY;
		}

		if (d0 < -10.0D) {
			this.chasingPosX = this.getX();
//            this.prevChasingPosX = this.chasingPosX;
		}

		if (d2 < -10.0D) {
			this.chasingPosZ = this.getZ();
//            this.prevChasingPosZ = this.chasingPosZ;
		}

		if (d1 < -10.0D) {
			this.chasingPosY = this.getY();
//            this.prevChasingPosY = this.chasingPosY;
		}

		this.chasingPosX += d0 * 0.25D;
		this.chasingPosZ += d2 * 0.25D;
		this.chasingPosY += d1 * 0.25D;
	}

	@Override
	public void tick() {
		super.tick();
		this.updateCape();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		this.prevCameraYaw = this.cameraYaw;
		float f;
		if (this.onGround && !this.isDeadOrDying() && !this.isSwimming()) {
			f = Math.min(0.1F, Mth.sqrt((float) this.getDeltaMovement().horizontalDistanceSqr()));
		} else {
			f = 0.0F;
		}
		this.cameraYaw += (f - this.cameraYaw) * 0.4F;
	}

	@Override
	public void applyRaidBuffs(int wave, boolean p_213660_2_) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SRSounds.ENTITY_ICEOLOGER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SRSounds.ENTITY_ICEOLOGER_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SRSounds.ENTITY_ICEOLOGER_HURT.get();
	}

	@Override
	public SoundEvent getCelebrateSound() {
		return SRSounds.ENTITY_ICEOLOGER_CELEBRATE.get();
	}

	@Override
	public SoundEvent getCastingSoundEvent() {
		return SRSounds.ENTITY_ICEOLOGER_CAST_SPELL.get();
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(SRItems.ICEOLOGER_SPAWN_EGG.get());
	}

	@Nullable
	public IceChunk getIceChunk() {
		if (this.iceChunkEntityUUID != null && this.level instanceof ServerLevel) {
			Entity entity = ((ServerLevel) this.level).getEntity(this.iceChunkEntityUUID);
			return entity instanceof IceChunk ? (IceChunk) entity : null;
		} else {
			if (this.iceChunkEntity == 0)
				return null;

			Entity entity = this.level.getEntity(this.iceChunkEntity);
			return entity instanceof IceChunk ? (IceChunk) entity : null;
		}
	}

	public void setIceChunk(@Nullable IceChunk target) {
		if (target != null) {
			this.iceChunkEntity = target.getId();
			this.iceChunkEntityUUID = target.getUUID();
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.ILLAGER;
	}

	@Override
	public boolean canBeLeader() {
		return true;
	}

	public static boolean canIceologerSpawn(EntityType<? extends Iceologer> patrollerType, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return worldIn.getBlockState(pos).is(Blocks.SNOW) && checkAnyLightMonsterSpawnRules(patrollerType, worldIn, reason, pos, random);
	}

	class AttackIceChunkGoal extends SpellcasterUseSpellGoal {
		private AttackIceChunkGoal() {
		}

		@Override
		public boolean canUse() {
			return Iceologer.this.getIceChunk() == null && super.canUse();
		}

		@Override
		protected int getCastingTime() {
			return 40;
		}

		@Override
		protected int getCastingInterval() {
			return 100;
		}

		@Override
		protected void performSpellCasting() {
			LivingEntity target = Iceologer.this.getTarget();
			if (Iceologer.this.getIceChunk() == null) {
				IceChunk iceChunk = new IceChunk(Iceologer.this.level, Iceologer.this, target);
				Iceologer.this.setIceChunk(iceChunk);
				Iceologer.this.level.addFreshEntity(iceChunk);
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SRSounds.GENERIC_PREPARE_ATTACK.get();
		}

		@Override
		protected IllagerSpell getSpell() {
			return IllagerSpell.FANGS;
		}
	}

	class AttackIceCloudGoal extends SpellcasterUseSpellGoal {
		private AttackIceCloudGoal() {
		}

		@Override
		protected int getCastingTime() {
			return 10;
		}

		@Override
		protected int getCastingInterval() {
			return 80;
		}

		@Override
		protected void performSpellCasting() {
			LivingEntity target = Iceologer.this.getTarget();
			if (target != null) {
				IceCloud iceCloud = new IceCloud(Iceologer.this.getX(), Iceologer.this.getY(), Iceologer.this.getZ(), target.getX(), target.getY(), target.getZ(), Iceologer.this.level);
				iceCloud.setOwner(Iceologer.this);
				Iceologer.this.level.addFreshEntity(iceCloud);
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SRSounds.GENERIC_PREPARE_ATTACK.get();
		}

		@Override
		protected IllagerSpell getSpell() {
			return IllagerSpell.SUMMON_VEX;
		}
	}
}
