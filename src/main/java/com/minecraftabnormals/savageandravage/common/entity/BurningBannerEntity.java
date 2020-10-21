package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BurningBannerEntity extends Entity {

    public static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Optional<BlockPos>> BLOCK_POS = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public static final DataParameter<Optional<UUID>> OFFENDER_UUID = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public BurningBannerEntity(EntityType<? extends BurningBannerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BurningBannerEntity(World worldIn, BlockPos positionIn, PlayerEntity playerIn) {
        super(SREntities.BURNING_BANNER.get(), worldIn);
        this.setBannerPosition(positionIn);
        this.setOffenderId(playerIn.getUniqueID());
        this.setBoundingBoxWithPosition(true);
    }

    private boolean isOminousBanner(BlockPos pos) {
        if (this.world.getTileEntity(pos) instanceof BannerTileEntity) {
            BannerTileEntity banner = (BannerTileEntity) Objects.requireNonNull(this.world.getTileEntity(pos));
            if (banner.getName() instanceof TranslationTextComponent) {
                return ((TranslationTextComponent) banner.getName()).getKey().contains("block.minecraft.ominous_banner");
            }
        }
        return false;
    }

    private void setBoundingBoxWithPosition(boolean shouldSetPosition) {
        BlockPos bannerPos = this.getBannerPosition();
        if (bannerPos == null)
            return;

        AxisAlignedBB boundingBox = this.world.getBlockState(bannerPos).getShape(this.world, bannerPos).getBoundingBox();
        this.setBoundingBox(boundingBox);
        if (shouldSetPosition)
            this.setPositionNew(bannerPos.getX() + boundingBox.getMin(Direction.Axis.X), bannerPos.getY() + boundingBox.getMin(Direction.Axis.Y), bannerPos.getZ() + boundingBox.getMin(Direction.Axis.Z));
    }

    private void setPositionNew(double x, double y, double z) {
        this.setRawPosition(x, y, z);
        this.setBoundingBoxWithPosition(false);
        double halfXSize = this.getBoundingBox().getXSize() / 2.0F;
        double YSize = this.getBoundingBox().getYSize();
        double halfZSize = this.getBoundingBox().getZSize() / 2.0F;
        this.setBoundingBox(new AxisAlignedBB(x - halfXSize, y, z - halfZSize, x + halfXSize, y + YSize, z + halfZSize));
        this.isAirBorne = true;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.setRawPosition(x, y, z);
        if (this.isAddedToWorld() && !this.world.isRemote())
            ((ServerWorld) this.world).chunkCheck(this); // Forge - Process chunk registration after moving.
    }

    @Override
    protected void registerData() {
        this.dataManager.register(BLOCK_POS, Optional.empty());
        this.dataManager.register(OFFENDER_UUID, Optional.empty());
        this.dataManager.register(TICKS_TILL_REMOVE, 50);
    }

    @Override
    public void tick() {
        this.setTicksTillRemove(this.getTicksTillRemove() - 1);
        int ticksRemaining = this.getTicksTillRemove();

        BlockPos bannerPos = this.getBannerPosition();
        if (bannerPos == null) {
            this.remove();
            return;
        }

        if (this.isOminousBanner(bannerPos))
            this.setBoundingBoxWithPosition(true);

        if ((ticksRemaining > 10 && !this.isOminousBanner(bannerPos)) || ticksRemaining <= 0) {
            this.remove();
            return;
        }

        if (this.world.isRemote()) {
            for (int i = 0; i < 5; i++) {
                double randomPositionX = this.getRenderBoundingBox().getMin(Direction.Axis.X) + (this.world.rand.nextFloat() * this.getRenderBoundingBox().getXSize());
                double randomPositionY = this.getRenderBoundingBox().getMin(Direction.Axis.Y) + (this.world.rand.nextFloat() * this.getRenderBoundingBox().getYSize());
                double randomPositionZ = this.getRenderBoundingBox().getMin(Direction.Axis.Z) + (this.world.rand.nextFloat() * this.getRenderBoundingBox().getZSize());

                if (ticksRemaining > 10) {
                    if (this.rand.nextInt(5) == 2)
                        this.world.addParticle(ParticleTypes.FLAME, randomPositionX, randomPositionY, randomPositionZ - 0.0D, 0.0D, 0.0D, 0.0D);
                    if (this.rand.nextInt(5) == 3)
                        this.world.addParticle(ParticleTypes.LAVA, randomPositionX, randomPositionY, randomPositionZ - 0.0D, 0.0D, 0.0D, 0.0D);
                } else {
                    this.world.addParticle(ParticleTypes.LARGE_SMOKE, randomPositionX, randomPositionY, randomPositionZ - 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            if (this.getTicksTillRemove() > 10) {
                this.playSound(this.isOminousBanner(bannerPos) ? SoundEvents.BLOCK_FIRE_AMBIENT : SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, world.rand.nextFloat() * 0.4F + 0.8F);
            } else if (this.getTicksTillRemove() == 10) {
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, world.rand.nextFloat() * 0.4F + 0.8F);
                this.world.removeBlock(bannerPos, false);

                if (((ServerWorld) this.world).findRaid(bannerPos) == null) {
                    PlayerEntity offender = this.getOffender();
                    if (offender == null)
                        return;

                    SRTriggers.BURN_BANNER.trigger((ServerPlayerEntity) offender);
                    EffectInstance effect = offender.getActivePotionEffect(Effects.BAD_OMEN);
                    if (effect != null)
                        offender.removeActivePotionEffect(Effects.BAD_OMEN);

                    if (!this.world.getGameRules().getBoolean(GameRules.DISABLE_RAIDS))
                        offender.addPotionEffect(new EffectInstance(Effects.BAD_OMEN, 120000, MathHelper.clamp(effect == null ? 0 : effect.getAmplifier() + 1, 0, 5), false, false, true));
                }
            }
        }
    }

    @Override
    public void recalculateSize() {
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("Size", 0);
        compound.putInt("TicksTillRemove", this.getTicksTillRemove());
        if (this.getBannerPosition() != null)
            compound.put("BannerPosition", NBTUtil.writeBlockPos(this.getBannerPosition()));
        if (this.getOffenderId() != null)
            compound.putUniqueId("Offender", this.getOffenderId());
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
        if (compound.contains("BannerPosition", 10))
            this.setBannerPosition(NBTUtil.readBlockPos(compound.getCompound("BannerPosition")));
        if (compound.hasUniqueId("Offender"))
            this.setOffenderId(compound.getUniqueId("Offender"));
    }

    public int getTicksTillRemove() {
        return this.dataManager.get(TICKS_TILL_REMOVE);
    }

    public void setTicksTillRemove(int tickCount) {
        this.dataManager.set(TICKS_TILL_REMOVE, tickCount);
    }

    @Nullable
    public BlockPos getBannerPosition() {
        return this.dataManager.get(BLOCK_POS).orElse(null);
    }

    private void setBannerPosition(@Nullable BlockPos positionIn) {
        this.dataManager.set(BLOCK_POS, Optional.ofNullable(positionIn));
    }

    @Nullable
    public UUID getOffenderId() {
        return this.dataManager.get(OFFENDER_UUID).orElse(null);
    }

    public void setOffenderId(@Nullable UUID ownerId) {
        this.dataManager.set(OFFENDER_UUID, Optional.ofNullable(ownerId));
    }

    @Nullable
    public PlayerEntity getOffender() {
        UUID uuid = this.getOffenderId();
        return uuid == null ? null : this.world.getPlayerByUuid(uuid);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

