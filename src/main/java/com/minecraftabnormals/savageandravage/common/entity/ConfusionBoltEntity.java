package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.savageandravage.core.other.SREvents;
import com.minecraftabnormals.savageandravage.core.registry.*;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.Random;

public class ConfusionBoltEntity extends ThrowableEntity {
	private static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.defineId(ConfusionBoltEntity.class, DataSerializers.INT);

	public ConfusionBoltEntity(EntityType<? extends ConfusionBoltEntity> type, World world) {
		super(type, world);
		this.setNoGravity(true);
	}

	public ConfusionBoltEntity(World world, LivingEntity thrower, int ticksTillRemove) {
		super(SREntities.CONFUSION_BOLT.get(), thrower, world);
		this.setNoGravity(true);
		this.entityData.set(TICKS_TILL_REMOVE, ticksTillRemove);
	}

	public ConfusionBoltEntity(World world, double x, double y, double z, int ticksTillRemove) {
		super(SREntities.CONFUSION_BOLT.get(), x, y, z, world);
		this.setNoGravity(true);
		this.entityData.set(TICKS_TILL_REMOVE, ticksTillRemove);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(TICKS_TILL_REMOVE, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		this.entityData.set(TICKS_TILL_REMOVE, compound.getInt("TicksTillRemove"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		compound.putInt("TicksTillRemove", this.entityData.get(TICKS_TILL_REMOVE));
	}

	@Override
	public void tick() {
		Vector3d deltaMovement = this.getDeltaMovement();
		super.tick();
		this.setDeltaMovement(deltaMovement); //Undoes tampering by superclass
		spawnGaussianParticles(this.level, this.getBoundingBox(), SRParticles.CONFUSION_BOLT.getId().toString(), 5);
		this.entityData.set(TICKS_TILL_REMOVE, this.entityData.get(TICKS_TILL_REMOVE) - 1);
		if (this.entityData.get(TICKS_TILL_REMOVE) <= 0)
			this.remove();
		//Normal projectile hit detection is bad
		if (!this.level.isClientSide()) {
			RayTraceResult result = ProjectileHelper.getHitResult(this, this::canHitEntity);
			if (result.getType() == RayTraceResult.Type.MISS && this.isAlive()) {
				List<Entity> intersecting = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox(), this::canHitEntity);
				if (!intersecting.isEmpty())
					this.onHit(new EntityRayTraceResult(intersecting.get(0)));
			}
		}
	}

	public static void spawnGaussianParticles(World world, AxisAlignedBB box, String name, int loops) {
		Random random = world.getRandom();
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
	protected void onHit(RayTraceResult result) {
		this.playSound(SRSounds.GENERIC_PUFF_OF_SMOKE.get(), 5.0F, 1.0F);
		spawnGaussianParticles(this.level, this.getBoundingBox().inflate(0.5D), SREvents.POOF_KEY, 25);
		super.onHit(result);
		this.remove();
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult result) {
		super.onHitEntity(result);
		Entity entity = result.getEntity();
		Entity owner = this.getOwner();
		if (owner != null && entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			Vector3d oldPos = owner.position();
			teleport(owner, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
			teleport(livingEntity, oldPos.x(), oldPos.y(), oldPos.z());
			if (livingEntity.isAffectedByPotions()) {
				livingEntity.addEffect(new EffectInstance(SREffects.WEIGHT.get(), 140, 2));
				livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 140, 1));
				livingEntity.addEffect(new EffectInstance(Effects.BLINDNESS, 30));
			}
			livingEntity.playSound(SRSounds.GENERIC_PUFF_OF_SMOKE.get(), 5.0F, 1.0F);
			spawnGaussianParticles(this.level, livingEntity.getBoundingBox().inflate(0.5D), SREvents.POOF_KEY, 25);
			if (owner instanceof ITracksHits)
				((ITracksHits) owner).onTrackedHit(this, entity);
		}
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		if (!this.level.isClientSide) {
			BlockPos.Mutable pos = result.getBlockPos().mutable();
			if (this.level.getBlockState(pos).getBlock() == SRBlocks.GLOOMY_TILES.get()) {
				this.level.setBlock(pos, SRBlocks.RUNED_GLOOMY_TILES.get().defaultBlockState(), 2);
				for (Direction direction : Direction.values()) {
					pos.move(direction);
					if (!this.level.getBlockState(pos).isSolidRender(this.level, pos))
						for (int i = 0; i < 3; i++)
							NetworkUtil.spawnParticle(SRParticles.RUNE.getId().toString(), this.level.dimension(), pos.getX() + random.nextDouble(), pos.getY() + 0.25, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
					pos.move(direction.getOpposite());
				}
			}
		}
	}

	private void teleport(Entity entity, double x, double y, double z) {
		Vector3d originalPos = entity.position();
		NetworkUtil.teleportEntity(entity, x, y, z);
		entity.lookAt(EntityAnchorArgument.Type.EYES, originalPos);
	}

	@Override
	protected float getGravity() {
		return 0.0F;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
