package com.minecraftabnormals.savageandravage.common.entity;

import java.util.UUID;

import javax.annotation.Nullable;

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
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

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
            this.setPositionAndRotation(target.getPosX(), target.getPosYHeight(1) + HOVER_DISTANCE, target.getPosZ(), this.rotationYaw, this.rotationPitch);
        this.setCaster(caster);
        this.setTarget(target);
    }

    private void onImpact(RayTraceResult result) {
        if (!this.world.isRemote()) {
            BlockState state = Blocks.PACKED_ICE.getDefaultState();
            SoundType soundtype = state.getSoundType(this.world, this.getPosition(), null);
            this.playSound(soundtype.getBreakSound(), (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            ((ServerWorld) this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, state), this.getPosX(), this.getPosY() + this.getHeight() / 2.0, this.getPosZ(), 256, this.getWidth() / 2.0, this.getHeight() / 2.0, this.getWidth() / 2.0, 1);

            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) result).getEntity();
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getCaster()), 8.0f);
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addPotionEffect(new EffectInstance(SREffects.FROSTBITE.get(), 160, 0, false, false, true));
                }
            }
        }

        this.remove();
    }

    @Nullable
    public Entity getCaster() {
        if (this.casterEntityUUID != null && this.world instanceof ServerWorld) {
            return ((ServerWorld) this.world).getEntityByUuid(this.casterEntityUUID);
        } else {
            return this.casterEntity != 0 ? this.world.getEntityByID(this.casterEntity) : null;
        }
    }

    public void setCaster(@Nullable Entity caster) {
        this.casterEntity = caster == null ? 0 : caster.getEntityId();
        this.casterEntityUUID = caster == null ? null : caster.getUniqueID();
    }

    @Nullable
    public Entity getTarget() {
        if (this.targetEntityUUID != null && this.world instanceof ServerWorld) {
            return ((ServerWorld) this.world).getEntityByUuid(this.targetEntityUUID);
        } else {
            return this.targetEntity != 0 ? this.world.getEntityByID(this.targetEntity) : null;
        }
    }

    public void setTarget(@Nullable Entity target) {
        this.targetEntity = target == null ? 0 : target.getEntityId();
        this.targetEntityUUID = target == null ? null : target.getUniqueID();
    }

    @Override
    public void tick() {
        super.tick();

        this.hoverTicks++;
        Entity target = this.getTarget();
        if (this.hoverTicks < HOVER_TIME) {
            if (target != null) {
                this.setPosition(target.getPosX(), target.getPosYHeight(1) + HOVER_DISTANCE, target.getPosZ());
            }
        } else if (this.hoverTicks >= HOVER_TIME + 20) {
            if (target != null)
                this.setMotion(Vector3d.ZERO);
            this.setTarget(null);
        }

        RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, entity -> !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && !entity.noClip, RayTraceContext.BlockMode.COLLIDER);
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }

        if (target == null) {
            this.setMotion(this.getMotion().add(0, -0.05, 0));

            this.setPosition(this.getPosX() + this.getMotion().getX(), this.getPosY() + this.getMotion().getY(), this.getPosZ() + this.getMotion().getZ());
        }
    }

    @Override
    protected void registerData() {
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        this.casterEntityUUID = nbt.hasUniqueId("Caster") ? nbt.getUniqueId("Caster") : null;
        this.targetEntityUUID = nbt.hasUniqueId("Target") ? nbt.getUniqueId("Target") : null;
        this.hoverTicks = nbt.getInt("HoverTicks");
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        if (this.casterEntityUUID != null)
            nbt.putUniqueId("Caster", this.casterEntityUUID);
        if (this.targetEntityUUID != null)
            nbt.putUniqueId("Target", this.targetEntityUUID);
        nbt.putInt("HoverTicks", this.hoverTicks);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buf) {
        Entity target = this.getTarget();
        buf.writeVarInt(target == null ? 0 : target.getEntityId());
        buf.writeVarInt(this.hoverTicks);
    }

    @Override
    public void readSpawnData(PacketBuffer buf) {
        this.targetEntity = buf.readVarInt();
        this.hoverTicks = buf.readVarInt();
    }
}
