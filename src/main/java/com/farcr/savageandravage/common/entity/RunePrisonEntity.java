package com.farcr.savageandravage.common.entity;

import com.farcr.savageandravage.core.registry.SRBlocks;
import com.farcr.savageandravage.core.registry.SREntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class RunePrisonEntity extends Entity {
    private static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<BlockPos>> BLOCK_POS = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private int currentFrame = 0;
    private boolean isBackwardsFrameCycle = false;

    public RunePrisonEntity(EntityType<? extends RunePrisonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public RunePrisonEntity(World worldIn, BlockPos positionIn){
        super(SREntities.RUNE_PRISON.get(),  worldIn);
        this.setBlockPos(positionIn);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(BLOCK_POS, null);
        this.dataManager.register(TICKS_TILL_REMOVE, 25);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
        if(this.getBlockPos() != null) {
            this.setBlockPos(NBTUtil.readBlockPos(compound.getCompound("GloomyTilePosition")));
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("TicksTillRemove",this.getTicksTillRemove());
        if(this.getBlockPos() != null) {
            compound.put("GloomyTilePosition", NBTUtil.writeBlockPos(this.getBlockPos()));
        }
    }

    public int getTicksTillRemove() {
        return this.dataManager.get(TICKS_TILL_REMOVE);
    }

    public void setTicksTillRemove(int tickCount) {
        this.dataManager.set(TICKS_TILL_REMOVE, tickCount);
    }

    @Nullable
    public BlockPos getBlockPos() {
        try {
            return this.dataManager.get(BLOCK_POS).orElse(null);
        }
        catch(NullPointerException nullPointer){
            return null;
        }
    }

    private void setBlockPos(@Nullable BlockPos positionIn){
        this.dataManager.set(BLOCK_POS, Optional.ofNullable(positionIn));
    }

    @Override
    public void tick(){
        super.tick();
        if(world.isRemote && getTicksTillRemove()%5==0){
            if(!isBackwardsFrameCycle){
                currentFrame++;
                if(currentFrame==4){
                    isBackwardsFrameCycle = true;
                }
            }
            else{
                currentFrame--;
                if(currentFrame==0){
                    isBackwardsFrameCycle = false;
                }
            }
        }
        setTicksTillRemove(getTicksTillRemove()-1);
        List<LivingEntity> intersectingEntityList = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox());
        if (!intersectingEntityList.isEmpty()) {
            for (LivingEntity livingentity : intersectingEntityList) {
                if (livingentity.canBeHitWithPotion()&&!(EntityTypeTags.RAIDERS.contains(livingentity.getType()))) {
                    livingentity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20, 1));
                }
            }
        }
        if (getTicksTillRemove() == 0 || getBlockPos()==null) {
            try {
                world.setBlockState(getBlockPos(), SRBlocks.GLOOMY_TILES.get().getDefaultState());
            }
            catch (NullPointerException bruhMoment) {

            }
            this.remove();
        }
    }

    public int getCurrentFrame(){
        return this.currentFrame;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
