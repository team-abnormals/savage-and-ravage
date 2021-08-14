package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.savageandravage.common.block.RunedGloomyTilesBlock;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.minecraftabnormals.savageandravage.core.registry.SREffects;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRParticles;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class ConfusionBoltEntity extends ThrowableEntity {
	private static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.defineId(ConfusionBoltEntity.class, DataSerializers.INT);

	public ConfusionBoltEntity(EntityType<? extends ConfusionBoltEntity> type, World world) {
		super(type, world);
	}

	public ConfusionBoltEntity(World world, LivingEntity thrower, int ticksTillRemove) {
		super(SREntities.CONFUSION_BOLT.get(), thrower, world);
		this.entityData.set(TICKS_TILL_REMOVE, ticksTillRemove);
	}

	public ConfusionBoltEntity(World world, double x, double y, double z, int ticksTillRemove) {
		super(SREntities.CONFUSION_BOLT.get(), x, y, z, world);
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
		super.tick();
		spawnGaussianParticles(this.random, this.getBoundingBox(), SRParticles.CONFUSION_BOLT.get(), 20);
		this.entityData.set(TICKS_TILL_REMOVE, this.entityData.get(TICKS_TILL_REMOVE) - 1);
		if (this.entityData.get(TICKS_TILL_REMOVE) <= 0)
			this.remove();
	}

	public static void spawnGaussianParticles(Random random, AxisAlignedBB box, IParticleData type, int loops) {
		for (int i = 0; i < loops; i++) {
			double randomPositionX = box.min(Direction.Axis.X) + ((0.5 + (random.nextGaussian() * 0.25)) * box.getXsize());
			double randomPositionY = box.min(Direction.Axis.Y) + ((0.5 + (random.nextGaussian() * 0.25)) * box.getYsize());
			double randomPositionZ = box.min(Direction.Axis.Z) + ((0.5 + (random.nextGaussian() * 0.25)) * box.getZsize());
			ResourceLocation particleID = ForgeRegistries.PARTICLE_TYPES.getKey((ParticleType<?>) type);
			if (particleID != null)
				NetworkUtil.spawnParticle(particleID.toString(), randomPositionX, randomPositionY, randomPositionZ, 0.0f, 0.0f, 0.0f);

		}
	}

	@Override
	protected void onHit(RayTraceResult result) {
		if (!(result instanceof EntityRayTraceResult) || EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(((EntityRayTraceResult) result).getEntity())) {
			this.playSound(SRSounds.GENERIC_PUFF_OF_SMOKE.get(), 5.0F, 1.0F);
			spawnGaussianParticles(this.random, this.getBoundingBox().inflate(0.5D), ParticleTypes.POOF, 25);
			super.onHit(result);
			this.remove();
		}
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult result) {
		Entity entity = result.getEntity();
		Entity owner = this.getOwner();
		super.onHitEntity(result);
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
			if (owner instanceof ITracksHits && RunedGloomyTilesBlock.shouldTrigger(entity, false))
				((ITracksHits) owner).onTrackedHit(this, entity);
		}
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		BlockPos.Mutable pos = result.getBlockPos().mutable();
		if (this.level.getBlockState(pos).getBlock() == SRBlocks.GLOOMY_TILES.get()) {
			this.level.setBlock(pos, SRBlocks.RUNED_GLOOMY_TILES.get().defaultBlockState(), 2);
			pos.move(Direction.UP);
			if (!this.level.getBlockState(pos).isSolidRender(this.level, pos)) {
				for (int i = 0; i < 3; i++)
					NetworkUtil.spawnParticle(SRParticles.RUNE.getId().toString(), pos.getX() + random.nextDouble(), pos.getY() + 0.25, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
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
