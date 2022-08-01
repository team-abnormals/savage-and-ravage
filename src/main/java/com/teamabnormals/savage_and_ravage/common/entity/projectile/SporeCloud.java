package com.teamabnormals.savage_and_ravage.common.entity.projectile;

import com.teamabnormals.savage_and_ravage.common.entity.monster.Creepie;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import com.teamabnormals.savage_and_ravage.core.registry.SRParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class SporeCloud extends ThrowableProjectile implements IEntityAdditionalSpawnData {
	private AreaEffectCloud cloudEntity;
	private UUID cloudId;
	private int cloudSize;
	private boolean charged = false;
	private boolean spawnCloudInstantly;
	private boolean creepiesAttackPlayersOnly;
	private boolean hit;

	public SporeCloud(EntityType<? extends SporeCloud> type, Level world) {
		super(type, world);
	}

	public SporeCloud(Level world, LivingEntity thrower) {
		super(SREntityTypes.SPORE_CLOUD.get(), thrower, world);
	}

	public SporeCloud(Level world, double x, double y, double z) {
		super(SREntityTypes.SPORE_CLOUD.get(), x, y, z, world);
	}

	private void spawnAreaEffectCloud(double x, double y, double z) {
		if (this.cloudId != null)
			return;

		this.setPos(x, y, z);
		AreaEffectCloud aoe = new AreaEffectCloud(this.level, x, y, z);
		Entity thrower = this.getOwner();
		if (thrower instanceof LivingEntity)
			aoe.setOwner((LivingEntity) thrower);
		aoe.setParticle(SRParticleTypes.CREEPER_SPORES.get());
		aoe.setRadius(this.cloudSize + 1.3F);
		aoe.setRadiusOnUse(-0.05F);
		aoe.setDuration((this.cloudSize * 20) + 60);
		aoe.setRadiusPerTick(-aoe.getRadius() / (float) aoe.getDuration());
		this.level.addFreshEntity(aoe);
		this.setCloudEntity(aoe);
		this.level.broadcastEntityEvent(this, (byte) 3);
	}

	public void setCloudEntity(@Nullable AreaEffectCloud entity) {
		this.cloudEntity = entity;
		this.cloudId = entity == null ? null : entity.getUUID();
	}

	@Nullable
	private AreaEffectCloud getCloudEntity() {
		if (this.cloudId != null && this.level instanceof ServerLevel) {
			Entity entity = ((ServerLevel) this.level).getEntity(this.cloudId);
			return entity instanceof AreaEffectCloud ? (AreaEffectCloud) entity : null;
		}
		return null;
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);

		if (this.cloudId != null)
			nbt.putUUID("CloudEntity", this.cloudId);
		if (this.charged)
			nbt.putBoolean("Charged", true);
		nbt.putInt("CloudSize", this.cloudSize);
		nbt.putBoolean("SpawnCloudInstantly", this.spawnCloudInstantly);
		nbt.putBoolean("AttackPlayersOnly", this.creepiesAttackPlayersOnly);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.cloudId = nbt.hasUUID("CloudEntity") ? nbt.getUUID("CloudEntity") : null;
		this.charged = nbt.getBoolean("Charged");
		this.cloudSize = nbt.getInt("CloudSize");
		this.spawnCloudInstantly = nbt.getBoolean("SpawnCloudInstantly");
		this.creepiesAttackPlayersOnly = nbt.getBoolean("AttackPlayersOnly");
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void onHit(HitResult result) {
		Vec3 hitVec = result.getLocation();
		if (!this.level.isClientSide()) {
			this.spawnAreaEffectCloud(hitVec.x(), hitVec.y(), hitVec.z());
		} else for (int i = 0; i < 16; i++) {
			this.level.addParticle(SRParticleTypes.CREEPER_SPORE_SPRINKLES.get(), this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
		}
		this.hit = true;
		if (result instanceof BlockHitResult)
			this.onHitBlock((BlockHitResult) result);
		if (result instanceof EntityHitResult)
			this.onHitEntity((EntityHitResult) result);
	}

	@Override
	public void handleEntityEvent(byte id) {
		super.handleEntityEvent(id);
		if (id == 3)
			this.hit = true;
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide() && this.spawnCloudInstantly)
			this.spawnAreaEffectCloud(this.getX(), this.getY(), this.getZ());

		if (this.cloudId != null || this.hit)
			this.setDeltaMovement(0, 0, 0);

		if (this.level.isClientSide()) {
			if (!this.hit)
				this.level.addParticle(SRParticleTypes.CREEPER_SPORES.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
		} else if (this.cloudId != null) {
			AreaEffectCloud aoe = this.getCloudEntity();
			if (aoe == null) {
				if (this.cloudEntity == null || !this.cloudEntity.isAlive())
					this.discard();
				return;
			}

			Creepie creepie = SREntityTypes.CREEPIE.get().create(this.level);
			if (creepie != null) {
				if (this.charged) {
					creepie.setCharged(true);
				}
				creepie.attackPlayersOnly = this.creepiesAttackPlayersOnly();
				if (!creepie.attackPlayersOnly) creepie.setPersistenceRequired();
				Entity thrower = this.getOwner();
				if (thrower instanceof LivingEntity && !(thrower.isInvisible()))
					creepie.setOwnerId(thrower.getUUID());
				BlockPos nextPosition = null;
				if (aoe.tickCount % 20 == 0) {
					for (int i = 0; i < 10; i++) {
						double xPos = aoe.getRandomX(0.1D);
						double zPos = aoe.getRandomZ(0.2D);
						creepie.moveTo(xPos, aoe.getY(), zPos, 0.0F, 0.0F);
						AABB box = creepie.getBoundingBox();
						if (BlockPos.betweenClosedStream(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ), Mth.ceil(box.maxX), Mth.ceil(box.maxY), Mth.ceil(box.maxZ)).distinct().noneMatch(pos -> {
							if (this.level.getBlockState(pos).isSuffocating(this.level, pos)) {
								for (AABB blockBox : this.level.getBlockState(pos).getShape(this.level, pos).toAabbs()) {
									blockBox = new AABB(blockBox.minX + pos.getX(), blockBox.minY + pos.getY(), blockBox.minZ + pos.getZ(), blockBox.maxX + pos.getX(), blockBox.maxY + pos.getY(), blockBox.maxZ + pos.getZ());
									if (blockBox.intersects(creepie.getBoundingBox())) {
										return true;
									}
								}
							}
							return false;
						})) {
							nextPosition = new BlockPos(xPos, aoe.getY(), zPos);
							break;
						}
					}
					if (nextPosition != null) {
						this.level.addFreshEntity(creepie);
					}
				}

				if (!aoe.isAlive())
					this.discard();
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public void setCloudSize(int cloudSize) {
		this.cloudSize = cloudSize;
	}

	public void setSpawnCloudInstantly(boolean spawnCloudInstantly) {
		this.spawnCloudInstantly = spawnCloudInstantly;
	}

	public void setCharged(boolean charged) {
		this.charged = charged;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buf) {
		buf.writeBoolean(this.cloudId != null);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buf) {
		this.hit = buf.readBoolean();
	}

	public boolean creepiesAttackPlayersOnly() {
		return creepiesAttackPlayersOnly;
	}

	public void creepiesAttackPlayersOnly(boolean creepiesAttackPlayersOnly) {
		this.creepiesAttackPlayersOnly = creepiesAttackPlayersOnly;
	}
}
