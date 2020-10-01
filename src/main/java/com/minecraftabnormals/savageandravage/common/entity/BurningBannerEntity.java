package com.minecraftabnormals.savageandravage.common.entity;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import com.minecraftabnormals.savageandravage.core.registry.SREntities;

import java.util.Optional;

public class BurningBannerEntity extends Entity {
    private Boolean blockDestroyed = false;
    private static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<BlockPos>> BLOCK_POS = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);

    public BurningBannerEntity(EntityType<? extends BurningBannerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BurningBannerEntity(World worldIn, BlockPos positionIn) {
        super(SREntities.BURNING_BANNER.get(), worldIn);
        this.setBannerPosition(positionIn);
        setBoundingBoxWithPosition(true);
    }

    private void setBoundingBoxWithPosition(boolean shouldSetPosition) {
        if (getBannerPosition() != null) {
            AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
            double xPos = getBannerPosition().getX();
            double yPos = getBannerPosition().getY();
            double zPos = getBannerPosition().getZ();
            if (world.getBlockState(getBannerPosition()).getBlock() instanceof BannerBlock) {
                xPos += 0.5d;
                yPos += 0.2d;
                zPos += 0.5d;
                switch (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15)) {
                    case 0: case 8:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.8, 1.65, 0.4);
                        break;
                    case 1: case 9: case 7: case 15:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.8, 1.65, 0.6);
                        break;
                    case 2: case 10: case 6: case 14:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.8, 1.65, 0.8);
                        break;
                    case 3: case 11: case 5: case 13:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.6, 1.65, 0.8);
                        break;
                    case 4: case 12:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.4, 1.65, 0.8);
                }
            }
            if (world.getBlockState(getBannerPosition()).getBlock() instanceof WallBannerBlock) {
                switch (world.getBlockState(getBannerPosition()).get(BlockStateProperties.HORIZONTAL_FACING)) {
                    case NORTH:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.9, 1.65, 0.3);
                        xPos += 0.48d;
                        yPos -= 0.76d;
                        zPos += 0.84d;
                        break;
                    case EAST:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.3, 1.65, 0.9);
                        xPos += 0.17d;
                        yPos -= 0.76d;
                        zPos += 0.5d;
                        break;
                    case SOUTH:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.9, 1.65, 0.3);
                        xPos += 0.5d;
                        yPos -= 0.76d;
                        zPos += 0.17d;
                        break;
                    case WEST:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.3, 1.65, 0.9);
                        xPos += 0.84d;
                        yPos -= 0.76d;
                        zPos += 0.48d;
                }
            }
            this.setBoundingBox(boundingBox);
            if (shouldSetPosition) {
                this.setPositionNew(xPos, yPos, zPos);
            }
        }
    }

    /**
     * Sets the x,y,z of the entity from the given parameters.
     */
    @Override
    public void setPosition(double x, double y, double z) {
        setRawPosition(x,y,z);
        if (this.isAddedToWorld() && !this.world.isRemote && world instanceof ServerWorld) ((ServerWorld)this.world).chunkCheck(this); // Forge - Process chunk registration after moving.
    }

    public void setPositionNew(double x, double y, double z){
        setRawPosition(x,y,z);
        setBoundingBoxWithPosition(false);
        double halfXSize = this.getBoundingBox().getXSize() / 2.0F;
        double YSize = this.getBoundingBox().getYSize();
        double halfZSize = this.getBoundingBox().getZSize() / 2.0F;
        this.setBoundingBox(new AxisAlignedBB(x - halfXSize, y, z - halfZSize, x + halfXSize, y + YSize, z + halfZSize));
        this.isAirBorne = true;
    }

    @Override
    public void recalculateSize() {

    }

    private boolean isOminousBanner(BlockPos positionIn) {
        boolean ominousBannerExists = false;
        if (positionIn != null) {
            if (world.getBlockState(positionIn).getBlock() instanceof AbstractBannerBlock) {
                TileEntity te = world.getTileEntity(positionIn);
                BannerTileEntity banner = (BannerTileEntity) te;
                TranslationTextComponent bannerName;
                if (banner.getName() instanceof TranslationTextComponent) {
                    bannerName = (TranslationTextComponent) banner.getName();
                    ominousBannerExists = bannerName.getKey().contains("block.minecraft.ominous_banner");
                }
            }
        }
        return ominousBannerExists;
    }

    public void tick() {
        setTicksTillRemove(getTicksTillRemove()-1);
        if(isOminousBanner(getBannerPosition())) {
            setBoundingBoxWithPosition(true);
        }
        if (blockDestroyed||getTicksTillRemove()<=0) {
            this.remove();
            blockDestroyed = true;
        }
        if(world.isRemote){
            for(int i=0; i<5; i++) {
                double randomPositionX = this.getRenderBoundingBox().getMin(Direction.Axis.X) + (world.rand.nextFloat() * this.getRenderBoundingBox().getXSize());
                double randomPositionY = this.getRenderBoundingBox().getMin(Direction.Axis.Y) + (world.rand.nextFloat() * this.getRenderBoundingBox().getYSize());
                double randomPositionZ = this.getRenderBoundingBox().getMin(Direction.Axis.Z) + (world.rand.nextFloat() * this.getRenderBoundingBox().getZSize());
                if (getTicksTillRemove() > 10) {
                    if (world.rand.nextInt(5) == 2) {
                        world.addParticle(ParticleTypes.FLAME, randomPositionX, randomPositionY, randomPositionZ - 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                    if (world.rand.nextInt(5) == 3) {
                        world.addParticle(ParticleTypes.LAVA, randomPositionX, randomPositionY, randomPositionZ - 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                } else if (getTicksTillRemove() < 10) {
                    world.addParticle(ParticleTypes.LARGE_SMOKE, randomPositionX, randomPositionY, randomPositionZ - 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        else {
            if (getTicksTillRemove() > 10 && !isOminousBanner(getBannerPosition())) {
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, world.rand.nextFloat() * 0.4F + 0.8F);
                this.blockDestroyed = true;
            }
            else if (getTicksTillRemove() == 10) {
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, world.rand.nextFloat() * 0.4F + 0.8F);
                if(getBannerPosition() != null) {
                    world.removeBlock(getBannerPosition(), false);
                }
            }
            else if (getTicksTillRemove() > 10) {
                this.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, 2F, world.rand.nextFloat() * 0.4F + 0.8F);
            }
        }
    }

    @Override
    protected void registerData(){
        this.dataManager.register(BLOCK_POS, Optional.empty());
        this.dataManager.register(TICKS_TILL_REMOVE, 50);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("Size", 0);
        compound.putInt("TicksTillRemove",this.getTicksTillRemove());
        if(this.getBannerPosition() != null) {
            compound.put("BannerPosition", NBTUtil.writeBlockPos(this.getBannerPosition()));
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
        if(compound.contains("BannerPosition", 10)) {
            this.setBannerPosition(NBTUtil.readBlockPos(compound.getCompound("BannerPosition")));
        }
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

    private void setBannerPosition(@Nullable BlockPos positionIn){
        this.dataManager.set(BLOCK_POS, Optional.ofNullable(positionIn));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

