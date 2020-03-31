package com.farcr.savageandravage.common.entity;

import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRParticles;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class CreeperSporeCloudEntity extends ThrowableEntity {
	
	public int size = 1;
	
    public CreeperSporeCloudEntity(EntityType<? extends CreeperSporeCloudEntity> type, World worldIn) 
    {
        super(type, worldIn);
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
      aoe.setOwner(getThrower());
      aoe.setParticleData(SRParticles.CREEPER_SPORES.get());
      for (int radius = 0; radius < world.rand.nextInt(); ++radius) {
        aoe.setRadius(radius);
      }
      aoe.setRadius(size);
      aoe.setRadiusOnUse(-0.05F);
      aoe.setDuration(100);
      aoe.setRadiusPerTick(-aoe.getRadius() / (float) aoe.getDuration());
      this.world.addEntity(aoe); 
      if (aoe.ticksExisted <= 50.0) //TODO an attempt on making the creepies spawn instantly, i will work on this later.
      for (int i = 0; i < aoe.getRadius(); ++i) 
      {
        CreepieEntity creepie = SREntities.CREEPIE.get().create(world);
        creepie.setLocationAndAngles(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ() + 0.0F, 0.0F, 0.0F);
        this.world.addEntity(creepie);
      }
    } 
    
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Size", this.size);
     }

    public void readAdditional(CompoundNBT compound) 
    {
      super.readAdditional(compound);
       if (compound.contains("Size", 99)) 
       {
           this.size = compound.getInt("Size");
       }
     }

    @Override
    protected void registerData() 
    {
        
    }
}
