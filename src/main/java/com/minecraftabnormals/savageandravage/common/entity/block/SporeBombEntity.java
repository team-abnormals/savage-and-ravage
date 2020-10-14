package com.minecraftabnormals.savageandravage.common.entity.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.minecraftabnormals.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SporeBombEntity extends TNTEntity {

    public SporeBombEntity(EntityType<? extends SporeBombEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public SporeBombEntity(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(worldIn, x, y, z, igniter);
    }

    @Override
    protected void explode() {
        CreeperSporeCloudEntity sporecloud = new CreeperSporeCloudEntity(SREntities.CREEPER_SPORE_CLOUD.get(), world);
        sporecloud.cloudSize = 4 + sporecloud.world.rand.nextInt(3);
        sporecloud.sporeBomb = true;
        this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 4.0F, Explosion.Mode.NONE); // hacky.
        sporecloud.copyLocationAndAnglesFrom(this);
        this.world.addEntity(sporecloud);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Nonnull
    @Override
    public EntityType<?> getType() {
        return SREntities.SPORE_BOMB.get();
    }
}
