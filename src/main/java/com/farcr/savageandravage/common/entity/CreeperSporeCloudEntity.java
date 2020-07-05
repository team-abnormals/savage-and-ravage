package com.farcr.savageandravage.common.entity;

import java.util.List;

import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRParticles;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CreeperSporeCloudEntity extends ThrowableEntity {
	
	public int cloudSize = 1;
	private boolean shouldSpawnCreepies = false;
    private static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.createKey(BurningBannerEntity.class, DataSerializers.VARINT);
	private AreaEffectCloudEntity aoe;

    public CreeperSporeCloudEntity(EntityType<? extends CreeperSporeCloudEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public CreeperSporeCloudEntity(World worldIn, LivingEntity throwerIn) {
        super(SREntities.CREEPER_SPORE_CLOUD.get(), throwerIn, worldIn);
     }

    public CreeperSporeCloudEntity(World worldIn, double x, double y, double z) {
        super(SREntities.CREEPER_SPORE_CLOUD.get(), x, y, z, worldIn);
    }


    @Override
    public void registerData(){
        this.dataManager.register(TICKS_TILL_REMOVE, 1);
    }

    public int getTicksTillRemove() {
        return this.dataManager.get(TICKS_TILL_REMOVE);
    }

    public void setTicksTillRemove(int tickCount) {
        this.dataManager.set(TICKS_TILL_REMOVE, tickCount);
    }

	@Override
    protected void onImpact(RayTraceResult result) {
    	 this.summonCreepies();
    	 this.world.setEntityState(this, (byte) 3);
    }
    
    public void summonCreepies() {
        if (aoe == null) {
            aoe = new AreaEffectCloudEntity(world, this.getPositionVec().getX(), this.getBoundingBox().maxY-0.2, this.getPositionVec().getZ());
            // TODO: func_234616_v_ = new getOwner -> returns Entity instead of LivingEntity
            aoe.setOwner(this.getOwner());
            aoe.setParticleData(SRParticles.CREEPER_SPORES.get());
            aoe.setRadius(cloudSize + 0.3F);
            aoe.setRadiusOnUse(-0.05F);
            aoe.setDuration((cloudSize * 20) + 40);
            aoe.setRadiusPerTick(-aoe.getRadius() / (float) aoe.getDuration());
            this.world.addEntity(aoe);
            shouldSpawnCreepies = true;
            this.setTicksTillRemove((cloudSize * 20) + 40);
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("TicksTillRemove",this.getTicksTillRemove());
        compound.putInt("CloudSize", this.cloudSize);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
       if (compound.contains("CloudSize", 99))
       {
           this.cloudSize = compound.getInt("CloudSize");
       }
        this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
    }
    
    @SuppressWarnings("deprecation")
	public void tick() {
    	super.tick();
    	this.world.addParticle(SRParticles.CREEPER_SPORES.get(), this.getPosX(), this.getPosY(), this.getPosZ() - 0.0D, 0.0D, 0.0D, 0.0D);
        if(shouldSpawnCreepies) {
            this.setMotion(0,0,0);
            this.setTicksTillRemove(this.getTicksTillRemove()-1);
            if(this.getTicksTillRemove() % 20 == 0) {
                double xPos = aoe.getPosXRandom(0.1D);
                double zPos = aoe.getPosZRandom(0.2D);
                BlockPos pos = new BlockPos(xPos, this.getPosY(), zPos);
                List<AxisAlignedBB> blockShapes = world.getBlockState(pos).getShape(world,pos).toBoundingBoxList();
                boolean flag = true;
                for(AxisAlignedBB box : blockShapes) {
                    if(box.intersects(aoe.getBoundingBox())&&world.getBlockState(pos).getBlock().causesSuffocation(world.getBlockState(pos),world,pos)) flag = false;
                }
                if(flag) {
                    CreepieEntity creepie = SREntities.CREEPIE.get().create(world);
                    creepie.setLocationAndAngles(xPos, aoe.getPosY(), zPos, 0.0F, 0.0F);
                    boolean throwerIsInvisible;
                    try { //TODO see if these two checks are needed
                        throwerIsInvisible = getThrower().isPotionActive(Effects.INVISIBILITY);
                    } catch (NullPointerException nullPointer) {
                        throwerIsInvisible = false;
                        //swallowed because it doesn't matter if the thrower has no effect
                    }
                    if (!throwerIsInvisible) {
                        try {
                            creepie.setOwnerId(getThrower().getUniqueID());
                        } catch (NullPointerException nullPointer) {
                            creepie.setOwnerId(null);
                        }
                    }
                    this.world.addEntity(creepie);
                }
            }
            if(!aoe.isAlive()){
                this.remove();
            }
        }
    }
    
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
