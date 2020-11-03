package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.core.registry.SREffects;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * @author Ocelot
 */
public class IceologerIceCloudEntity extends DamagingProjectileEntity {

    public IceologerIceCloudEntity(EntityType<IceologerIceCloudEntity> entityType, World world) {
        super(entityType, world);
    }

    public IceologerIceCloudEntity(double x, double y, double z, double targetX, double targetY, double targetZ, World world) {
        super(SREntities.ICEOLOGER_ICE_CLOUD.get(), x, y, z, targetX - x, targetY - y, targetZ - z, world);
    }

    @Override
    public void tick() {
        super.tick();

        for (Entity entity : this.world.getEntitiesInAABBexcluding(this.func_234616_v_(), this.getBoundingBox().expand(2, 2, 2), this::func_230298_a_)) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.WITHER, 60, 1, false, false, true));
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(SREffects.FROZEN.get(), 60, 0, false, false, true));
            }
        }

        if (!this.world.isRemote()) {
            ((ServerWorld) this.world).spawnParticle(this.getParticle(), this.getPosX(), this.getPosY(), this.getPosZ(), 30, 1.5, 1.5, 1.5, 1);
        }

        if (this.ticksExisted > 100)
            this.remove();
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    protected IParticleData getParticle() {
        return ParticleTypes.EFFECT;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
