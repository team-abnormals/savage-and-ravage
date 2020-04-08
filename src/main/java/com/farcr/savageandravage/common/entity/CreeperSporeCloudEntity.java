package com.farcr.savageandravage.common.entity;

import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRParticles;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CreeperSporeCloudEntity extends ThrowableEntity {
	
	public int size = 1;
	public int radius = 1;
	
    public CreeperSporeCloudEntity(EntityType<? extends CreeperSporeCloudEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    public CreeperSporeCloudEntity(World worldIn, LivingEntity throwerIn) {
        super(SREntities.CREEPER_SPORE_CLOUD.get(), throwerIn, worldIn);
     }

	@Override
    protected void onImpact(RayTraceResult result) {
    	if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();

            if (this.getThrower() == entity && this.ticksExisted < 4) {
                return;
            }

            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) 2);
    	}
    	 this.summonCreepies();
    	 this.world.setEntityState(this, (byte) 3);
         this.remove();
    }
    
    public void summonCreepies() 
    {
      AreaEffectCloudEntity aoe = new AreaEffectCloudEntity(world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
      aoe.setOwner(this.getThrower());
      aoe.setParticleData(SRParticles.CREEPER_SPORES.get());
      for (int randoradius = 0; randoradius < world.rand.nextInt(); ++radius) {
        aoe.setRadius(randoradius);
      }
      aoe.setRadius(radius);
      aoe.setRadiusOnUse(-0.05F);
      aoe.setDuration(100);
      aoe.setRadiusPerTick(-aoe.getRadius() / (float) aoe.getDuration());
      this.world.addEntity(aoe); 
      //if (aoe.ticksExisted <= 50.0)  //TODO an attempt on making the creepies spawn instantly, i will work on this later.
      for (int i = 0; i < size; ++i) 
      {
        CreepieEntity creepie = SREntities.CREEPIE.get().create(world);
        creepie.setLocationAndAngles(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ() + 0.0F, 0.0F, 0.0F);
        try {
            creepie.setOwnerId(getThrower().getUniqueID());
        }
        catch (NullPointerException nullPointer){
            creepie.setOwnerId(null); //TODO check later to see if this is the best way to implement this
        }
        this.world.addEntity(creepie);
      }
    } 
    
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Size", this.size);
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
           this.size = compound.getInt("Size");
       }
       
       if (compound.contains("Radius", 99)) 
       {
           this.radius = compound.getInt("Radius");
       }
     }

    @Override
    protected void registerData() 
    {
        
    }
}
