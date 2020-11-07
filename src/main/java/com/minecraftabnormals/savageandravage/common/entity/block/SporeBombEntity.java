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
        this.setPosition(x, y, z);
        double d0 = world.getRandom().nextDouble() * (double) ((float) Math.PI * 2F);
        this.setMotion(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    @Override
    protected void explode() {
        SporeCloudEntity sporecloud = SREntities.SPORE_CLOUD.get().create(this.world);
        if (sporecloud == null)
            return;

        sporecloud.setCloudSize(4 + this.world.getRandom().nextInt(3));
        sporecloud.setSpawnCloudInstantly(true);
        this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 4.0F, Explosion.Mode.NONE);
        sporecloud.setPositionAndRotation(this.getPosX(), this.getPosYHeight(0.0625), this.getPosZ(), this.rotationYaw, this.rotationPitch);
        this.world.addEntity(sporecloud);
    }

    @Override
    @Nullable
    public LivingEntity getTntPlacedBy() {
        return this.tntPlacedBy;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
