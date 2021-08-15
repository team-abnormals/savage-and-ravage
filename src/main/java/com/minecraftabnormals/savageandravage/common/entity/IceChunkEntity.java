package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.core.registry.SREffects;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * @author Ocelot
 */
public class IceChunkEntity extends Entity implements IEntityAdditionalSpawnData {

	public static final int HOVER_TIME = 80;
	public static final int HOVER_DISTANCE = 3;

	private UUID casterEntityUUID;
	private int casterEntity;
	private UUID targetEntityUUID;
	private int targetEntity;
	private int hoverTicks;

	public IceChunkEntity(EntityType<IceChunkEntity> entityType, World world) {
		super(entityType, world);
	}

	public IceChunkEntity(World world, @Nullable Entity caster, @Nullable Entity target) {
		this(SREntities.ICE_CHUNK.get(), world);
		if (target != null)
			this.absMoveTo(target.getX(), target.getY(1) + HOVER_DISTANCE, target.getZ(), this.yRot, this.xRot);
		this.setCaster(caster);
		this.setTarget(target);
	}

	private void onImpact(RayTraceResult result) {
		if (!this.level.isClientSide()) {
			BlockState state = Blocks.PACKED_ICE.defaultBlockState();
			SoundType soundtype = state.getSoundType(this.level, this.blockPosition(), null);
			this.playSound(soundtype.getBreakSound(), (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			((ServerWorld) this.level).sendParticles(new BlockParticleData(ParticleTypes.BLOCK, state), this.getX(), this.getY() + this.getBbHeight() / 2.0, this.getZ(), 256, this.getBbWidth() / 2.0, this.getBbHeight() / 2.0, this.getBbWidth() / 2.0, 1);
		}
		this.remove();
	}

	private void onImpactEntity(EntityRayTraceResult result) {
		Entity entity = result.getEntity();
		entity.hurt(DamageSource.indirectMagic(this, this.getCaster()), 8.0f);
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).addEffect(new EffectInstance(SREffects.FROSTBITE.get(), 160, 0, false, false, true));
		}
	}

	@Nullable
	public Entity getCaster() {
		if (this.casterEntityUUID != null && this.level instanceof ServerWorld) {
			return ((ServerWorld) this.level).getEntity(this.casterEntityUUID);
		} else {
			return this.casterEntity != 0 ? this.level.getEntity(this.casterEntity) : null;
		}
	}

	public void setCaster(@Nullable Entity caster) {
		this.casterEntity = caster == null ? 0 : caster.getId();
		this.casterEntityUUID = caster == null ? null : caster.getUUID();
	}

	@Nullable
	public Entity getTarget() {
		if (this.targetEntityUUID != null && this.level instanceof ServerWorld) {
			return ((ServerWorld) this.level).getEntity(this.targetEntityUUID);
		} else {
			return this.targetEntity != 0 ? this.level.getEntity(this.targetEntity) : null;
		}
	}

	public void setTarget(@Nullable Entity target) {
		this.targetEntity = target == null ? 0 : target.getId();
		this.targetEntityUUID = target == null ? null : target.getUUID();
	}

	@Override
	public void tick() {
		super.tick();

		this.hoverTicks++;
		Entity target = this.getTarget();
		if (this.hoverTicks < HOVER_TIME) {
			if (target != null) {
				this.setPos(target.getX(), target.getY(1) + HOVER_DISTANCE, target.getZ());
			}
		} else if (this.hoverTicks >= HOVER_TIME + 20) {
			if (target != null)
				this.setDeltaMovement(Vector3d.ZERO);
			this.setTarget(null);
		}

		if (!this.level.isClientSide()) {
			RayTraceResult result = ProjectileHelper.getHitResult(this, this::canHitEntity);
			List<Entity> intersecting = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox(), this::canHitEntity);
			if (result.getType() != RayTraceResult.Type.MISS || !intersecting.isEmpty()) {
				intersecting.forEach(e -> this.onImpactEntity(new EntityRayTraceResult(e)));
				if (result.getType() == RayTraceResult.Type.ENTITY && intersecting.isEmpty())
					this.onImpactEntity((EntityRayTraceResult) result);
				this.onImpact(result);
			}
		}

		if (target == null) {
			this.setDeltaMovement(this.getDeltaMovement().add(0, -0.05, 0));

			this.setPos(this.getX() + this.getDeltaMovement().x(), this.getY() + this.getDeltaMovement().y(), this.getZ() + this.getDeltaMovement().z());
		}
	}

	protected boolean canHitEntity(Entity entity) {
		if (!entity.isSpectator() && entity.isAlive() && entity.isPickable() && !entity.noPhysics) {
			Entity caster = this.getCaster();
			return caster == null || !caster.isPassengerOfSameVehicle(entity);
		} else {
			return false;
		}
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT nbt) {
		this.casterEntityUUID = nbt.hasUUID("Caster") ? nbt.getUUID("Caster") : null;
		this.targetEntityUUID = nbt.hasUUID("Target") ? nbt.getUUID("Target") : null;
		this.hoverTicks = nbt.getInt("HoverTicks");
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
		if (this.casterEntityUUID != null)
			nbt.putUUID("Caster", this.casterEntityUUID);
		if (this.targetEntityUUID != null)
			nbt.putUUID("Target", this.targetEntityUUID);
		nbt.putInt("HoverTicks", this.hoverTicks);
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(PacketBuffer buf) {
		Entity target = this.getTarget();
		buf.writeVarInt(target == null ? 0 : target.getId());
		buf.writeVarInt(this.hoverTicks);
	}

	@Override
	public void readSpawnData(PacketBuffer buf) {
		this.targetEntity = buf.readVarInt();
		this.hoverTicks = buf.readVarInt();
	}
}
