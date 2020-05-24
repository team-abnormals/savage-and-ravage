package com.farcr.savageandravage.common.entity;

import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRParticles;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CreeperSporeCloudEntity extends ThrowableEntity {
	
	public int cloudSize = 1;
	
    public CreeperSporeCloudEntity(EntityType<? extends CreeperSporeCloudEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    public CreeperSporeCloudEntity(World worldIn, LivingEntity throwerIn) {
        super(SREntities.CREEPER_SPORE_CLOUD.get(), throwerIn, worldIn);
     }

    public CreeperSporeCloudEntity(World worldIn, double x, double y, double z) {
        super(SREntities.CREEPER_SPORE_CLOUD.get(), x, y, z, worldIn);
    }

	@Override
    protected void onImpact(RayTraceResult result) {
    	 this.summonCreepies();
    	 this.world.setEntityState(this, (byte) 3);
         this.remove();
    }
    
    public void summonCreepies() {
        AreaEffectCloudEntity aoe = new AreaEffectCloudEntity(world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
        aoe.setOwner(this.getThrower());
        aoe.setParticleData(SRParticles.CREEPER_SPORES.get());
        for (int randoradius = 0; randoradius < world.rand.nextInt(); ++randoradius) {
            aoe.setRadius(randoradius);
        }
        aoe.setRadius(cloudSize + 0.3F);
         aoe.setRadiusOnUse(-0.05F);
         aoe.setDuration(100);
         aoe.setRadiusPerTick(-aoe.getRadius() / (float) aoe.getDuration());
         this.world.addEntity(aoe);
         //if (aoe.ticksExisted <= 50.0)  //TODO an attempt on making not the creepies spawn instantly, i will work on this later.
         for (int i = 0; i < cloudSize; ++i) {
             CreepieEntity creepie = SREntities.CREEPIE.get().create(world);
             creepie.setLocationAndAngles(aoe.getPosXRandom(0.1D), this.getPosY(), aoe.getPosZRandom(0.2D), 0.0F, 0.0F);
             boolean throwerIsInvisible;
             try { //TODO see if these two checks are needed
                throwerIsInvisible = getThrower().isPotionActive(Effects.INVISIBILITY);
             }
             catch(NullPointerException nullPointer){
                 throwerIsInvisible = false;
                //swallowed because it doesn't matter if the thrower has no effect
             }
             if(!throwerIsInvisible) {
                 try {
                    creepie.setOwnerId(getThrower().getUniqueID());
                 }
                 catch (NullPointerException nullPointer) {
                     creepie.setOwnerId(null);
                 }
             }
            this.world.addEntity(creepie);
            this.remove();
         }
    }
    
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Size", this.cloudSize);
     }
    
    public void tick() 
    {
    	super.tick();
    	this.world.addParticle(SRParticles.CREEPER_SPORES.get(), this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ() - 0.0D, 0.0D, 0.0D, 0.0D);
    }
    
    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    
    public void readAdditional(CompoundNBT compound) 
    {
      super.readAdditional(compound);
       if (compound.contains("Size", 99)) 
       {
           this.cloudSize = compound.getInt("Size");
       }
     }

    @Override
    protected void registerData() 
    {
        
    }
}
