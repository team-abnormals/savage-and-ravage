package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRParticles;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class SporeCloudEntity extends ThrowableEntity implements IEntityAdditionalSpawnData {

	private AreaEffectCloudEntity cloudEntity;
	private UUID cloudId;
	private int cloudSize;
	private boolean charged = false;
	private boolean spawnCloudInstantly;
	private boolean creepiesAttackPlayersOnly;
	private boolean hit;

	public SporeCloudEntity(EntityType<? extends SporeCloudEntity> type, World world) {
		super(type, world);
	}

	public SporeCloudEntity(World world, LivingEntity thrower) {
		super(SREntities.SPORE_CLOUD.get(), thrower, world);
	}

	public SporeCloudEntity(World world, double x, double y, double z) {
		super(SREntities.SPORE_CLOUD.get(), x, y, z, world);
	}

	private void spawnAreaEffectCloud(double x, double y, double z) {
		if (this.cloudId != null)
			return;

		this.setPos(x, y, z);
		AreaEffectCloudEntity aoe = new AreaEffectCloudEntity(this.level, x, y, z);
		Entity thrower = this.getOwner();
		if (thrower instanceof LivingEntity)
			aoe.setOwner((LivingEntity) thrower);
		aoe.setParticle(SRParticles.CREEPER_SPORES.get());
		aoe.setRadius(this.cloudSize + 1.3F);
		aoe.setRadiusOnUse(-0.05F);
		aoe.setDuration((this.cloudSize * 20) + 60);
		aoe.setRadiusPerTick(-aoe.getRadius() / (float) aoe.getDuration());
		this.level.addFreshEntity(aoe);
		this.setCloudEntity(aoe);
		this.level.broadcastEntityEvent(this, (byte) 3);
	}

	public void setCloudEntity(@Nullable AreaEffectCloudEntity entity) {
		this.cloudEntity = entity;
		this.cloudId = entity == null ? null : entity.getUUID();
	}

	@Nullable
	private AreaEffectCloudEntity getCloudEntity() {
		if (this.cloudId != null && this.level instanceof ServerWorld) {
			Entity entity = ((ServerWorld) this.level).getEntity(this.cloudId);
			return entity instanceof AreaEffectCloudEntity ? (AreaEffectCloudEntity) entity : null;
		}
		return null;
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
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
	protected void readAdditionalSaveData(CompoundNBT nbt) {
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
	protected void onHit(RayTraceResult result) {
		Vector3d hitVec = result.getLocation();
		if (!this.level.isClientSide()) {
			this.spawnAreaEffectCloud(hitVec.x(), hitVec.y(), hitVec.z());
		} else for (int i=0; i<16; i++) {
			this.level.addParticle(SRParticles.CREEPER_SPORE_SPRINKLES.get(), this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
		}
		this.hit = true;
		if (result instanceof BlockRayTraceResult)
			this.onHitBlock((BlockRayTraceResult) result);
		if (result instanceof EntityRayTraceResult)
			this.onHitEntity((EntityRayTraceResult) result);
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
				this.level.addParticle(SRParticles.CREEPER_SPORES.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
		} else if (this.cloudId != null) {
			AreaEffectCloudEntity aoe = this.getCloudEntity();
			if (aoe == null) {
				if (this.cloudEntity == null || !this.cloudEntity.isAlive())
					this.remove();
				return;
			}

			CreepieEntity creepie = SREntities.CREEPIE.get().create(this.level);
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
						AxisAlignedBB box = creepie.getBoundingBox();
						if (BlockPos.betweenClosedStream(MathHelper.floor(box.minX), MathHelper.floor(box.minY), MathHelper.floor(box.minZ), MathHelper.ceil(box.maxX), MathHelper.ceil(box.maxY), MathHelper.ceil(box.maxZ)).distinct().noneMatch(pos -> {
							if (this.level.getBlockState(pos).isSuffocating(this.level, pos)) {
								for (AxisAlignedBB blockBox : this.level.getBlockState(pos).getShape(this.level, pos).toAabbs()) {
									blockBox = new AxisAlignedBB(blockBox.minX + pos.getX(), blockBox.minY + pos.getY(), blockBox.minZ + pos.getZ(), blockBox.maxX + pos.getX(), blockBox.maxY + pos.getY(), blockBox.maxZ + pos.getZ());
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
					this.remove();
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
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
	public void writeSpawnData(PacketBuffer buf) {
		buf.writeBoolean(this.cloudId != null);
	}

	@Override
	public void readSpawnData(PacketBuffer buf) {
		this.hit = buf.readBoolean();
	}

	public boolean creepiesAttackPlayersOnly() {
		return creepiesAttackPlayersOnly;
	}

	public void creepiesAttackPlayersOnly(boolean creepiesAttackPlayersOnly) {
		this.creepiesAttackPlayersOnly = creepiesAttackPlayersOnly;
	}
}
