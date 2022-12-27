package com.teamabnormals.savage_and_ravage.common.entity.projectile;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.savage_and_ravage.common.entity.TracksHits;
import com.teamabnormals.savage_and_ravage.core.other.SREvents;
import com.teamabnormals.savage_and_ravage.core.registry.*;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class ConfusionBolt extends ThrowableProjectile {
	private static final EntityDataAccessor<Integer> TICKS_TILL_REMOVE = SynchedEntityData.defineId(ConfusionBolt.class, EntityDataSerializers.INT);

	public ConfusionBolt(EntityType<? extends ConfusionBolt> type, Level world) {
		super(type, world);
		this.setNoGravity(true);
	}

	public ConfusionBolt(Level world, LivingEntity thrower, int ticksTillRemove) {
		super(SREntityTypes.CONFUSION_BOLT.get(), thrower, world);
		this.setNoGravity(true);
		this.entityData.set(TICKS_TILL_REMOVE, ticksTillRemove);
	}

	public ConfusionBolt(Level world, double x, double y, double z, int ticksTillRemove) {
		super(SREntityTypes.CONFUSION_BOLT.get(), x, y, z, world);
		this.setNoGravity(true);
		this.entityData.set(TICKS_TILL_REMOVE, ticksTillRemove);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(TICKS_TILL_REMOVE, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.entityData.set(TICKS_TILL_REMOVE, compound.getInt("TicksTillRemove"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("TicksTillRemove", this.entityData.get(TICKS_TILL_REMOVE));
	}

	@Override
	public void tick() {
		Vec3 deltaMovement = this.getDeltaMovement();
		super.tick();
		this.setDeltaMovement(deltaMovement); //Undoes tampering by superclass
		spawnGaussianParticles(this.level, this.random, this.getBoundingBox(), SRParticleTypes.CONFUSION_BOLT.getId().toString(), 5);
		this.entityData.set(TICKS_TILL_REMOVE, this.entityData.get(TICKS_TILL_REMOVE) - 1);
		if (this.entityData.get(TICKS_TILL_REMOVE) <= 0)
			this.discard();
		//Normal projectile hit detection is bad
		if (!this.level.isClientSide()) {
			HitResult result = ProjectileUtil.getHitResult(this, this::canHitEntity);
			if (result.getType() == HitResult.Type.MISS && this.isAlive()) {
				List<Entity> intersecting = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox(), this::canHitEntity);
				if (!intersecting.isEmpty())
					this.onHit(new EntityHitResult(intersecting.get(0)));
			}
		}
	}

	public static void spawnGaussianParticles(Level world, RandomSource random, AABB box, String name, int loops) {
		if (!world.isClientSide) {
			for (int i = 0; i < loops; i++) {
				double x = box.min(Direction.Axis.X) + ((0.5 + (random.nextGaussian() * 0.25)) * box.getXsize());
				double y = box.min(Direction.Axis.Y) + ((0.5 + (random.nextGaussian() * 0.25)) * box.getYsize());
				double z = box.min(Direction.Axis.Z) + ((0.5 + (random.nextGaussian() * 0.25)) * box.getZsize());
				NetworkUtil.spawnParticle(name, world.dimension(), x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void onHit(HitResult result) {
		this.playSound(SRSounds.GENERIC_PUFF_OF_SMOKE.get(), 5.0F, 1.0F);
		spawnGaussianParticles(this.level, this.random, this.getBoundingBox().inflate(0.5D), SREvents.POOF_KEY, 25);
		super.onHit(result);
		this.discard();
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		Entity entity = result.getEntity();
		Entity owner = this.getOwner();
		if (owner != null && entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			Vec3 oldPos = owner.position();
			teleport(owner, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
			teleport(livingEntity, oldPos.x(), oldPos.y(), oldPos.z());
			if (livingEntity.isAffectedByPotions()) {
				livingEntity.addEffect(new MobEffectInstance(SRMobEffects.WEIGHT.get(), 140, 2));
				livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 140, 1));
				livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30));
			}
			livingEntity.playSound(SRSounds.GENERIC_PUFF_OF_SMOKE.get(), 5.0F, 1.0F);
			spawnGaussianParticles(this.level, this.random, livingEntity.getBoundingBox().inflate(0.5D), SREvents.POOF_KEY, 25);
			if (owner instanceof TracksHits)
				((TracksHits) owner).onTrackedHit(this, entity);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level.isClientSide) {
			BlockPos.MutableBlockPos pos = result.getBlockPos().mutable();
			if (this.level.getBlockState(pos).getBlock() == SRBlocks.GLOOMY_TILES.get()) {
				this.level.setBlock(pos, SRBlocks.RUNED_GLOOMY_TILES.get().defaultBlockState(), 2);
				for (Direction direction : Direction.values()) {
					pos.move(direction);
					if (!this.level.getBlockState(pos).isSolidRender(this.level, pos))
						for (int i = 0; i < 3; i++)
							NetworkUtil.spawnParticle(SRParticleTypes.RUNE.getId().toString(), this.level.dimension(), pos.getX() + random.nextDouble(), pos.getY() + 0.25, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
					pos.move(direction.getOpposite());
				}
			}
		}
	}

	private void teleport(Entity entity, double x, double y, double z) {
		Vec3 originalPos = entity.position();
		NetworkUtil.teleportEntity(entity, x, y, z);
		if (!(entity instanceof Mob && ((Mob) entity).isNoAi()))
			entity.lookAt(EntityAnchorArgument.Anchor.EYES, originalPos);
	}

	@Override
	protected float getGravity() {
		return 0.0F;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
