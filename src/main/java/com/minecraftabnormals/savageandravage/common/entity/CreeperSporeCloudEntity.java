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
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CreeperSporeCloudEntity extends ThrowableEntity implements IEntityAdditionalSpawnData {

    private AreaEffectCloudEntity cloudEntity;
    private UUID cloudId;

    private int cloudSize;
    private boolean sporeBomb;
    private boolean hit;

    public CreeperSporeCloudEntity(EntityType<? extends CreeperSporeCloudEntity> type, World world) {
        super(type, world);
    }

    public CreeperSporeCloudEntity(World world, LivingEntity thrower) {
        super(SREntities.CREEPER_SPORE_CLOUD.get(), thrower, world);
    }

    public CreeperSporeCloudEntity(World world, double x, double y, double z) {
        super(SREntities.CREEPER_SPORE_CLOUD.get(), x, y, z, world);
    }

    private void spawnAreaEffectCloud(double x, double y, double z) {
        if (this.cloudId != null)
            return;

        this.setPosition(x, y, z);
        AreaEffectCloudEntity aoe = new AreaEffectCloudEntity(this.world, x, y, z);
        Entity thrower = this.func_234616_v_();
        if (thrower instanceof LivingEntity)
            aoe.setOwner((LivingEntity) thrower);
        aoe.setParticleData(SRParticles.CREEPER_SPORES.get());
        aoe.setRadius(this.cloudSize + 1.3F);
        aoe.setRadiusOnUse(-0.05F);
        aoe.setDuration((this.cloudSize * 20) + 60);
        aoe.setRadiusPerTick(-aoe.getRadius() / (float) aoe.getDuration());
        this.world.addEntity(aoe);
        this.setCloudEntity(aoe);
        this.world.setEntityState(this, (byte) 3);
    }

    public void setCloudEntity(@Nullable AreaEffectCloudEntity entity) {
        this.cloudEntity = entity;
        this.cloudId = entity == null ? null : entity.getUniqueID();
    }

    @Nullable
    private AreaEffectCloudEntity getCloudEntity() {
        if (this.cloudId != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.cloudId);
            return entity instanceof AreaEffectCloudEntity ? (AreaEffectCloudEntity) entity : null;
        }
        return null;
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);

        if (this.cloudId != null)
            nbt.putUniqueId("CloudEntity", this.cloudId);
        nbt.putInt("CloudSize", this.cloudSize);
        nbt.putBoolean("SporeBomb", this.sporeBomb);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);

        this.cloudId = nbt.hasUniqueId("CloudEntity") ? nbt.getUniqueId("CloudEntity") : null;
        this.cloudSize = nbt.getInt("CloudSize");
        this.sporeBomb = nbt.getBoolean("SporeBomb");
    }

    @Override
    protected void registerData() {
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote()) {
            Vector3d hitVec = result.getHitVec();
            this.spawnAreaEffectCloud(hitVec.getX(), hitVec.getY(), hitVec.getZ());
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == 3)
            this.hit = true;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isRemote() && this.sporeBomb && this.cloudId == null && this.cloudEntity == null)
            this.spawnAreaEffectCloud(this.getPosX(), this.getPosY(), this.getPosZ());

        if (this.cloudId != null || this.hit)
            this.setMotion(0, 0, 0);

        if (this.world.isRemote()) {
            if (!this.hit)
                this.world.addParticle(SRParticles.CREEPER_SPORES.get(), this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
        } else if (this.cloudId != null) {
            AreaEffectCloudEntity aoe = this.getCloudEntity();
            if (aoe == null) {
                if (this.cloudEntity == null || !this.cloudEntity.isAlive())
                    this.remove();
                return;
            }

            aoe.setNoGravity(false);

            if (aoe.ticksExisted % 20 == 0) {
                double xPos = aoe.getPosXRandom(0.1D);
                double zPos = aoe.getPosZRandom(0.2D);
                BlockPos pos = new BlockPos(xPos, this.getPosY(), zPos);
                List<AxisAlignedBB> blockShapes = this.world.getBlockState(pos).getShape(this.world, pos).toBoundingBoxList();

                boolean flag = true;
                for (AxisAlignedBB box : blockShapes) {
                    if (box.intersects(aoe.getBoundingBox()) && this.world.getBlockState(pos).isSuffocating(this.world, pos)) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    CreepieEntity creepie = SREntities.CREEPIE.get().create(this.world);
                    if (creepie == null)
                        return;

                    creepie.setLocationAndAngles(xPos, aoe.getPosY(), zPos, 0.0F, 0.0F);
                    creepie.spawnedFromSporeBomb = this.sporeBomb;

                    Entity thrower = this.func_234616_v_();
                    if (thrower != null && (!(thrower instanceof LivingEntity) || !((LivingEntity) thrower).isPotionActive(Effects.INVISIBILITY)))
                        creepie.setOwnerId(thrower.getUniqueID());
                    this.world.addEntity(creepie);
                }
            }

            if (!aoe.isAlive())
                this.remove();
        }
    }

    @Override
    public PushReaction getPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setCloudSize(int cloudSize) {
        this.cloudSize = cloudSize;
    }

    public void setSporeBomb(boolean sporeBomb) {
        this.sporeBomb = sporeBomb;
    }

    @Override
    public void writeSpawnData(PacketBuffer buf) {
        buf.writeBoolean(this.cloudId != null);
    }

    @Override
    public void readSpawnData(PacketBuffer buf) {
        this.hit = buf.readBoolean();
    }
}
