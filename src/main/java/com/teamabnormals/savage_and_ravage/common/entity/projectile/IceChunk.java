package com.teamabnormals.savage_and_ravage.common.entity.projectile;

import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import com.teamabnormals.savage_and_ravage.core.registry.SRMobEffects;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * @author Ocelot
 */
public class IceChunk extends Entity implements IEntityAdditionalSpawnData {
	public static final int HOVER_TIME = 80;
	public static final int HOVER_DISTANCE = 3;

	private UUID casterEntityUUID;
	private int casterEntity;
	private UUID targetEntityUUID;
	private int targetEntity;
	private int hoverTicks;

	public IceChunk(EntityType<IceChunk> entityType, Level world) {
		super(entityType, world);
	}

	public IceChunk(Level world, @Nullable Entity caster, @Nullable Entity target) {
		this(SREntityTypes.ICE_CHUNK.get(), world);
		if (target != null)
			this.absMoveTo(target.getX(), target.getY(1) + HOVER_DISTANCE, target.getZ(), this.getYRot(), this.getXRot());
		this.setCaster(caster);
		this.setTarget(target);
	}

	private void onImpact(HitResult result) {
		if (!this.level.isClientSide()) {
			BlockState state = Blocks.PACKED_ICE.defaultBlockState();
			SoundType soundtype = state.getSoundType(this.level, this.blockPosition(), null);
			this.playSound(soundtype.getBreakSound(), (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			((ServerLevel) this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), this.getX(), this.getY() + this.getBbHeight() / 2.0, this.getZ(), 256, this.getBbWidth() / 2.0, this.getBbHeight() / 2.0, this.getBbWidth() / 2.0, 1);
		}
		this.discard();
	}

	private void onImpactEntity(EntityHitResult result) {
		Entity entity = result.getEntity();
		entity.hurt(DamageSource.indirectMagic(this, this.getCaster()), 8.0f);
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).addEffect(new MobEffectInstance(SRMobEffects.FROSTBITE.get(), 160, 0, false, false, true));
		}
	}

	@Nullable
	public Entity getCaster() {
		if (this.casterEntityUUID != null && this.level instanceof ServerLevel) {
			return ((ServerLevel) this.level).getEntity(this.casterEntityUUID);
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
		if (this.targetEntityUUID != null && this.level instanceof ServerLevel) {
			return ((ServerLevel) this.level).getEntity(this.targetEntityUUID);
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
				this.setDeltaMovement(Vec3.ZERO);
			this.setTarget(null);
		}

		if (!this.level.isClientSide()) {
			HitResult result = ProjectileUtil.getHitResult(this, this::canHitEntity);
			List<Entity> intersecting = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox(), this::canHitEntity);
			if (result.getType() != HitResult.Type.MISS || !intersecting.isEmpty()) {
				intersecting.forEach(e -> this.onImpactEntity(new EntityHitResult(e)));
				if (result.getType() == HitResult.Type.ENTITY && intersecting.isEmpty())
					this.onImpactEntity((EntityHitResult) result);
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
	protected void readAdditionalSaveData(CompoundTag nbt) {
		this.casterEntityUUID = nbt.hasUUID("Caster") ? nbt.getUUID("Caster") : null;
		this.targetEntityUUID = nbt.hasUUID("Target") ? nbt.getUUID("Target") : null;
		this.hoverTicks = nbt.getInt("HoverTicks");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		if (this.casterEntityUUID != null)
			nbt.putUUID("Caster", this.casterEntityUUID);
		if (this.targetEntityUUID != null)
			nbt.putUUID("Target", this.targetEntityUUID);
		nbt.putInt("HoverTicks", this.hoverTicks);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buf) {
		Entity target = this.getTarget();
		buf.writeVarInt(target == null ? 0 : target.getId());
		buf.writeVarInt(this.hoverTicks);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buf) {
		this.targetEntity = buf.readVarInt();
		this.hoverTicks = buf.readVarInt();
	}
}
