package com.farcr.savageandravage.common.entity;

import com.farcr.savageandravage.core.registry.SREntities;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.xml.crypto.Data;
import java.util.Optional;

public class BurningBannerEntity extends Entity {
    private Boolean blockDestroyed = false;
    private Block currentBanner;
    private static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<BlockPos>> BLOCK_POS = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);

    public BurningBannerEntity(EntityType<? extends BurningBannerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BurningBannerEntity(World worldIn, BlockPos positionIn) {
        super(SREntities.BURNING_BANNER.get(), worldIn);
        this.setBannerPosition(positionIn);
        currentBanner = world.getBlockState(getBannerPosition()).getBlock();
        setBoundingBoxAndOrPosition(true);
    }

    private void setBoundingBoxAndOrPosition(boolean shouldSetPosition){
        if(getBannerPosition()!=null) {
            double xPos = getBannerPosition().getX();
            double yPos = getBannerPosition().getY();
            double zPos = getBannerPosition().getZ();
            AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
            if (world.getBlockState(getBannerPosition()).getBlock() instanceof BannerBlock) {
                xPos += 0.5d; yPos += 0.2d; zPos += 0.5d;
                if (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 0 || world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 8) {
                    boundingBox = new AxisAlignedBB(0, 0, 0, 0.8, 1.65, 0.4);
                } else if (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 1 || world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 9) {
                    boundingBox = new AxisAlignedBB(0, 0, 0, 0.8, 1.65, 0.6);
                } else if (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 2 || world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 10) {
                    boundingBox = new AxisAlignedBB(0, 0, 0, 0.8, 1.65, 0.8);
                } else if (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 3 || world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 11) {
                    boundingBox = new AxisAlignedBB(0, 0, 0, 0.6, 1.65, 0.8);
                } else if (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 4 || world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 12) {
                    boundingBox = new AxisAlignedBB(0, 0, 0, 0.4, 1.65, 0.8);
                } else if (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 5 || world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 13) {
                    boundingBox = new AxisAlignedBB(0, 0, 0, 0.6, 1.65, 0.8);
                } else if (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 6 || world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 14) {
                    boundingBox = new AxisAlignedBB(0, 0, 0, 0.8, 1.65, 0.8);
                } else if (world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 7 || world.getBlockState(getBannerPosition()).get(BlockStateProperties.ROTATION_0_15) == 15) {
                    boundingBox = new AxisAlignedBB(0, 0, 0, 0.8, 1.65, 0.6);
                }
            }
            if (world.getBlockState(getBannerPosition()).getBlock() instanceof WallBannerBlock) {
                switch (world.getBlockState(getBannerPosition()).get(BlockStateProperties.HORIZONTAL_FACING)) {
                    case NORTH:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.9, 1.65, 0.3);
                        xPos += 0.48d; yPos -= 0.76d; zPos += 0.84d;
                        break;
                    case EAST:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.3, 1.65, 0.9);
                        xPos += 0.17d; yPos -= 0.76d; zPos += 0.5d;
                        break;
                    case SOUTH:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.9, 1.65, 0.3);
                        xPos += 0.5d; yPos -= 0.76d; zPos += 0.17d;
                        break;
                    case WEST:
                        boundingBox = new AxisAlignedBB(0, 0, 0, 0.3, 1.65, 0.9);
                        xPos += 0.84d; yPos -= 0.76d; zPos += 0.48d;
                        break;
                }
            }
            this.setBoundingBox(boundingBox);
            if(shouldSetPosition) {
                this.setPosition(xPos, yPos, zPos);
            }
        }
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also sets up a bounding box but it's not in the name,
     * MCP moment.
     */
    @Override
    public void setPosition(double x, double y, double z) {
        setRawPosition(x,y,z);
        if (this.isAddedToWorld() && !this.world.isRemote && world instanceof ServerWorld) ((ServerWorld)this.world).chunkCheck(this); // Forge - Process chunk registration after moving.
        setBoundingBoxAndOrPosition(false);
        double halfXSize = this.getBoundingBox().getXSize() / 2.0F;
        double YSize = this.getBoundingBox().getYSize();
        double halfZSize = this.getBoundingBox().getZSize() / 2.0F;
        this.setBoundingBox(new AxisAlignedBB(x - halfXSize, y, z - halfZSize, x + halfXSize, y + YSize, z + halfZSize));
        this.isAirBorne = true;
    }

    @Override
    public void recalculateSize() {

    }

    public void tick() {
        setTicksTillRemove(getTicksTillRemove()-1);
        if(getTicksTillRemove()>30) {
            setBoundingBoxAndOrPosition(true);
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
        if(!world.isRemote) {
            try {
                if (getTicksTillRemove() > 10 && world.getBlockState(getBannerPosition()).getBlock() != this.currentBanner) {
                    this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, world.rand.nextFloat() * 0.4F + 0.8F);
                    this.blockDestroyed = true;
                }
            }
            catch(NullPointerException nullPointer){
                this.blockDestroyed = true;
                //Swallowed because of crash when summoning
            }
            if (getTicksTillRemove() == 10) {
                this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, world.rand.nextFloat() * 0.4F + 0.8F);
                world.removeBlock(getBannerPosition(), false);
            }
            if (getTicksTillRemove() > 10) {
                this.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, 2F, world.rand.nextFloat() * 0.4F + 0.8F);
            }
        }
    }


    @Override
    protected void registerData(){
        this.dataManager.register(BLOCK_POS, null);
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
        if(this.getBannerPosition() != null) {
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
        try {
            return this.dataManager.get(BLOCK_POS).orElse(null);
        }
        catch(NullPointerException nullPointer){
            return null;
        }
    }

    private void setBannerPosition(@Nullable BlockPos positionIn){
        this.dataManager.set(BLOCK_POS, Optional.ofNullable(positionIn));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

