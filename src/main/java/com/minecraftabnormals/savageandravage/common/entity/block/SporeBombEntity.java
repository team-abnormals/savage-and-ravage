package com.minecraftabnormals.savageandravage.common.entity.block;

import com.minecraftabnormals.savageandravage.common.entity.SporeCloudEntity;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class SporeBombEntity extends TNTEntity {
	@Nullable
	private LivingEntity tntPlacedBy;

	public SporeBombEntity(EntityType<? extends SporeBombEntity> type, World world) {
		super(type, world);
	}

	public SporeBombEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
		this(SREntities.SPORE_BOMB.get(), world);
		this.setPos(x, y, z);
		double d0 = world.getRandom().nextDouble() * (double) ((float) Math.PI * 2F);
		this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
		this.setFuse(80);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.tntPlacedBy = igniter;
	}

	@Override
	protected void explode() {
		SporeCloudEntity sporeCloud = SREntities.SPORE_CLOUD.get().create(this.level);
		if (sporeCloud == null)
			return;

		sporeCloud.setCloudSize(4 + this.level.getRandom().nextInt(3));
		sporeCloud.setSpawnCloudInstantly(true);
		this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, Explosion.Mode.NONE);
		sporeCloud.absMoveTo(this.getX(), this.getY(0.0625), this.getZ(), this.yRot, this.xRot);
		this.level.addFreshEntity(sporeCloud);
	}

	@Override
	@Nullable
	public LivingEntity getOwner() {
		return this.tntPlacedBy;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
