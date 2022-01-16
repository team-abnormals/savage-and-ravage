package com.teamabnormals.savageandravage.common.entity.block;

import com.teamabnormals.savageandravage.common.entity.SporeCloudEntity;
import com.teamabnormals.savageandravage.core.registry.SREntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class SporeBombEntity extends PrimedTnt {
	@Nullable
	private LivingEntity tntPlacedBy;

	public SporeBombEntity(EntityType<? extends SporeBombEntity> type, Level world) {
		super(type, world);
	}

	public SporeBombEntity(Level world, double x, double y, double z, @Nullable LivingEntity igniter) {
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
		this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, Explosion.BlockInteraction.NONE);
		sporeCloud.absMoveTo(this.getX(), this.getY(0.0625), this.getZ(), this.getYRot(), this.getXRot());
		this.level.addFreshEntity(sporeCloud);
	}

	@Override
	@Nullable
	public LivingEntity getOwner() {
		return this.tntPlacedBy;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
