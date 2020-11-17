package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

/**
 * @author Ocelot
 */
public class IceologerEntity extends SpellcastingIllagerEntity {

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

	public IceologerEntity(EntityType<IceologerEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new CastingASpellGoal());
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
		this.goalSelector.addGoal(5, new AttackIceChunkGoal());
		this.goalSelector.addGoal(6, new AttackIceCloudGoal());
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
		this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return LivingEntity.registerAttributes().createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D).createMutableAttribute(Attributes.ATTACK_KNOCKBACK);
	}

	private void updateCape() {
		this.prevChasingPosX = this.chasingPosX;
		this.prevChasingPosY = this.chasingPosY;
		this.prevChasingPosZ = this.chasingPosZ;
		double d0 = this.getPosX() - this.chasingPosX;
		double d1 = this.getPosY() - this.chasingPosY;
		double d2 = this.getPosZ() - this.chasingPosZ;
		if (d0 > 10.0D) {
			this.chasingPosX = this.getPosX();
//            this.prevChasingPosX = this.chasingPosX;
		}

		if (d2 > 10.0D) {
			this.chasingPosZ = this.getPosZ();
//            this.prevChasingPosZ = this.chasingPosZ;
		}

		if (d1 > 10.0D) {
			this.chasingPosY = this.getPosY();
//            this.prevChasingPosY = this.chasingPosY;
		}

		if (d0 < -10.0D) {
			this.chasingPosX = this.getPosX();
//            this.prevChasingPosX = this.chasingPosX;
		}

		if (d2 < -10.0D) {
			this.chasingPosZ = this.getPosZ();
//            this.prevChasingPosZ = this.chasingPosZ;
		}

		if (d1 < -10.0D) {
			this.chasingPosY = this.getPosY();
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
	public void livingTick() {
		super.livingTick();

		this.prevCameraYaw = this.cameraYaw;
		float f;
		if (this.onGround && !this.getShouldBeDead() && !this.isSwimming()) {
			f = Math.min(0.1F, MathHelper.sqrt(horizontalMag(this.getMotion())));
		} else {
			f = 0.0F;
		}
		this.cameraYaw += (f - this.cameraYaw) * 0.4F;
	}

	@Override
	public void applyWaveBonus(int wave, boolean p_213660_2_) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SRSounds.ICEOLOGER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SRSounds.ICEOLOGER_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SRSounds.ICEOLOGER_HURT.get();
	}

	@Override
	public SoundEvent getRaidLossSound() {
		return SRSounds.ICEOLOGER_CELEBRATE.get();
	}

	@Override
	public SoundEvent getSpellSound() {
		return SRSounds.ICEOLOGER_CAST_SPELL.get();
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(SRItems.ICEOLOGER_SPAWN_EGG.get());
	}

	@Nullable
	public IceChunkEntity getIceChunk() {
		if (this.iceChunkEntityUUID != null && this.world instanceof ServerWorld) {
			Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.iceChunkEntityUUID);
			return entity instanceof IceChunkEntity ? (IceChunkEntity) entity : null;
		} else {
			if (this.iceChunkEntity == 0)
				return null;

			Entity entity = this.world.getEntityByID(this.iceChunkEntity);
			return entity instanceof IceChunkEntity ? (IceChunkEntity) entity : null;
		}
	}

	public void setIceChunk(@Nullable IceChunkEntity target) {
		if (target != null) {
			this.iceChunkEntity = target.getEntityId();
			this.iceChunkEntityUUID = target.getUniqueID();
		}
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ILLAGER;
	}

	@Override
	public boolean canBeLeader() {
		return true;
	}

	public static boolean canIceologerSpawn(EntityType<? extends IceologerEntity> patrollerType, IWorld worldIn, SpawnReason reason, BlockPos pos, Random random) {
		return worldIn.getBlockState(pos).isIn(Blocks.SNOW) && canMonsterSpawn(patrollerType, worldIn, reason, pos, random);
	}

	class AttackIceChunkGoal extends UseSpellGoal {
		private AttackIceChunkGoal() {
		}

		@Override
		public boolean shouldExecute() {
			return IceologerEntity.this.getIceChunk() == null && super.shouldExecute();
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
		protected void castSpell() {
			LivingEntity target = IceologerEntity.this.getAttackTarget();
			if (IceologerEntity.this.getIceChunk() == null) {
				IceChunkEntity iceChunk = new IceChunkEntity(IceologerEntity.this.world, IceologerEntity.this, target);
				IceologerEntity.this.setIceChunk(iceChunk);
				IceologerEntity.this.world.addEntity(iceChunk);
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
		}

		@Override
		protected SpellType getSpellType() {
			return SpellType.FANGS;
		}
	}

	class AttackIceCloudGoal extends UseSpellGoal {
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
		protected void castSpell() {
			LivingEntity target = IceologerEntity.this.getAttackTarget();
			if (target != null) {
				IceCloudEntity iceCloud = new IceCloudEntity(IceologerEntity.this.getPosX(), IceologerEntity.this.getPosY(), IceologerEntity.this.getPosZ(), target.getPosX(), target.getPosY(), target.getPosZ(), IceologerEntity.this.world);
				iceCloud.setShooter(IceologerEntity.this);
				IceologerEntity.this.world.addEntity(iceCloud);
			}
		}

		@Override
		protected SoundEvent getSpellPrepareSound() {
			return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
		}

		@Override
		protected SpellType getSpellType() {
			return SpellType.SUMMON_VEX;
		}
	}
}
