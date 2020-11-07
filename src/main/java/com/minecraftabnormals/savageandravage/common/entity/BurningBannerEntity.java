package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRTriggers;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BurningBannerEntity extends Entity implements IEntityAdditionalSpawnData {

    public static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Optional<BlockPos>> BLOCK_POS = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public static final DataParameter<Optional<UUID>> OFFENDER_UUID = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    private static final double[] ROTATED_VERTICES = new double[3];
    private AxisAlignedBB burningBox;
    private double burningBoxRotation;

    public BurningBannerEntity(EntityType<? extends BurningBannerEntity> type, World world) {
        super(type, world);
        this.burningBox = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
        this.burningBoxRotation = 0;
    }

    public BurningBannerEntity(World world, BlockPos pos, PlayerEntity player) {
        super(SREntities.BURNING_BANNER.get(), world);
        this.setBannerPosition(pos);
        this.setOffenderId(player.getUniqueID());
        BlockState state = world.getBlockState(pos);
        this.setPosition(pos.getX() + 0.5, pos.getY() - (state.getBlock() instanceof WallBannerBlock ? 1 : 0), pos.getZ() + 0.5);
        this.burningBox = getBurningBox(state);
        this.burningBoxRotation = getBurningBoxRotation(state);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(BLOCK_POS, Optional.empty());
        this.dataManager.register(OFFENDER_UUID, Optional.empty());
        this.dataManager.register(TICKS_TILL_REMOVE, 110);
    }

    @Override
    public void tick() {
        this.setTicksTillRemove(this.getTicksTillRemove() - 1);
        int ticksRemaining = this.getTicksTillRemove();

        BlockPos bannerPos = this.getBannerPosition();
        if (bannerPos == null || ticksRemaining <= 0) {
            this.remove();
            return;
        }
        else if (ticksRemaining > 10 && !(this.world.getTileEntity(bannerPos) instanceof BannerTileEntity)) {
            extinguishFire();
            return;
        }

        if (this.world.isRemote()) {
            for (int i = 0; i < 5; i++) {
                double randomPositionX = this.burningBox.getMin(Direction.Axis.X) + (this.world.getRandom().nextFloat() * this.burningBox.getXSize());
                double randomPositionY = this.burningBox.getMin(Direction.Axis.Y) + (this.world.getRandom().nextFloat() * this.burningBox.getYSize());
                double randomPositionZ = this.burningBox.getMin(Direction.Axis.Z) + (this.world.getRandom().nextFloat() * this.burningBox.getZSize());
                double[] rotatedPosition = rotate(randomPositionX, randomPositionY, randomPositionZ, this.burningBoxRotation);
                randomPositionX = bannerPos.getX() + rotatedPosition[0];
                randomPositionY = bannerPos.getY() + rotatedPosition[1];
                randomPositionZ = bannerPos.getZ() + rotatedPosition[2];

                if (ticksRemaining > 10) {
                    if (this.rand.nextInt(5) == 2)
                        this.world.addParticle(ParticleTypes.FLAME, randomPositionX, randomPositionY, randomPositionZ, 0.0D, 0.0D, 0.0D);
                    if (this.rand.nextInt(5) == 3)
                        this.world.addParticle(ParticleTypes.LAVA, randomPositionX, randomPositionY, randomPositionZ, 0.0D, 0.0D, 0.0D);
                } else {
                    this.world.addParticle(ParticleTypes.LARGE_SMOKE, randomPositionX, randomPositionY, randomPositionZ, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            if (this.getTicksTillRemove() > 10) {
                this.playSound(this.world.getTileEntity(bannerPos) instanceof BannerTileEntity ? SoundEvents.BLOCK_FIRE_AMBIENT : SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, this.world.getRandom().nextFloat() * 0.4F + 0.8F);
            } else if (this.getTicksTillRemove() == 10) {
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, this.world.getRandom().nextFloat() * 0.4F + 0.8F);
                if (isOminousBanner(this.world, bannerPos) && ((ServerWorld) this.world).findRaid(bannerPos) == null) {
                    PlayerEntity offender = this.getOffender();
                    if (offender == null)
                        return;

                    SRTriggers.BURN_BANNER.trigger((ServerPlayerEntity) offender);
                    EffectInstance effect = offender.getActivePotionEffect(Effects.BAD_OMEN);
                    if (effect != null)
                        offender.removeActivePotionEffect(Effects.BAD_OMEN);

                    if (!this.world.getGameRules().getBoolean(GameRules.DISABLE_RAIDS))
                        offender.addPotionEffect(new EffectInstance(Effects.BAD_OMEN, 120000, MathHelper.clamp(effect == null ? 0 : effect.getAmplifier() + 1, 0, 4, false, false, true));
                }
                this.world.removeBlock(bannerPos, false);
            }
        }
    }

    public void extinguishFire() {
        this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, this.world.getRandom().nextFloat() * 0.4F + 0.8F);
        this.remove();
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

    public AxisAlignedBB getBurningBox() {
        return burningBox;
    }

    public double getBurningBoxRotation() {
        return burningBoxRotation;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static boolean isOminousBanner(IBlockReader world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof BannerTileEntity) {
            BannerTileEntity banner = (BannerTileEntity) Objects.requireNonNull(world.getTileEntity(pos));
            if (banner.getName() instanceof TranslationTextComponent) {
                return ((TranslationTextComponent) banner.getName()).getKey().contains("block.minecraft.ominous_banner");
            }
        }
        return false;
    }

    public static AxisAlignedBB getBurningBox(BlockState state) {
        return state.hasProperty(WallBannerBlock.HORIZONTAL_FACING) ? new AxisAlignedBB(1 * 0.0625, -13.5 * 0.0625, 1.5 * 0.0625, 15 * 0.0625, 14 * 0.0625, 4 * 0.0625) : new AxisAlignedBB(1 * 0.0625, 2.5 * 0.0625, 8.5 * 0.0625, 15 * 0.0625, 29.5 * 0.0625, 11 * 0.0625);
    }

    public static double getBurningBoxRotation(BlockState state) {
        Direction direction = state.hasProperty(WallBannerBlock.HORIZONTAL_FACING) ? state.get(WallBannerBlock.HORIZONTAL_FACING) : null;
        return direction != null ? Math.toRadians(direction.getHorizontalAngle()) : (float) (state.get(BannerBlock.ROTATION) / 8.0 * Math.PI);
    }

    public static double[] rotate(double x, double y, double z, double angle) {
        float cos = MathHelper.cos((float) angle);
        float sin = MathHelper.sin((float) angle);
        ROTATED_VERTICES[0] = 0.5 + (x - 0.5) * cos - (z - 0.5) * sin;
        ROTATED_VERTICES[1] = y;
        ROTATED_VERTICES[2] = 0.5 + (x - 0.5) * sin + (z - 0.5) * cos;
        return ROTATED_VERTICES;
    }

    @Override
    public void writeSpawnData(PacketBuffer buf) {
        BlockPos bannerPos = this.getBannerPosition();
        buf.writeBoolean(bannerPos != null);
        if (bannerPos != null)
            buf.writeBlockPos(this.getBannerPosition());
    }

    @Override
    public void readSpawnData(PacketBuffer buf) {
        BlockPos bannerPos = buf.readBoolean() ? buf.readBlockPos() : null;
        if (bannerPos != null) {
            BlockState state = this.world.getBlockState(bannerPos);
            this.burningBox = getBurningBox(state);
            this.burningBoxRotation = getBurningBoxRotation(state);
        }
    }
}

