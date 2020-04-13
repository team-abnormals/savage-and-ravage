package com.farcr.savageandravage.common.entity.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.core.registry.SREntities;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SporeBombEntity extends TNTEntity
{
	private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(SporeBombEntity.class, DataSerializers.VARINT);
	   @Nullable
	   private LivingEntity tntPlacedBy;
	   private int fuse = 80;

	   public SporeBombEntity(EntityType<? extends SporeBombEntity> type, World worldIn) {
	      super(type, worldIn);
	      this.preventEntitySpawning = true;
	   }

	   public SporeBombEntity(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter) {
	      this(SREntities.SPORE_BOMB.get(), worldIn);
	      this.setPosition(x, y, z);
	      double d0 = worldIn.rand.nextDouble() * (double)((float)Math.PI * 2F);
	      this.setMotion(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
	      this.setFuse(80);
	      this.prevPosX = x;
	      this.prevPosY = y;
	      this.prevPosZ = z;
	      this.tntPlacedBy = igniter;
	   }

	   protected void registerData() {
	      this.dataManager.register(FUSE, 80);
	   }
	   /**
	    * Called to update the entity's position/logic.
	    */
	   @Override
	   public void tick() {
	      if (!this.hasNoGravity()) {
	         this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
	      }

	      this.move(MoverType.SELF, this.getMotion());
	      this.setMotion(this.getMotion().scale(0.98D));
	      if (this.onGround) {
	         this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
	      }

	      --this.fuse;
	      if (this.fuse <= 0) {
	         this.remove();
	         if (!this.world.isRemote) {
	            this.spawnSporeCloud();
	            this.world.playSound((PlayerEntity)null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.0F);
	         }
	      } else {
	         this.handleWaterMovement();
	         if (this.world.isRemote) {
	            this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
	         }
	      }

	   }

	   public void spawnSporeCloud() 
	   {
		   CreeperSporeCloudEntity sporecloud = new CreeperSporeCloudEntity(SREntities.CREEPER_SPORE_CLOUD.get(), world);
		   sporecloud.size = 4 + sporecloud.world.rand.nextInt(6);
		   this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 4.0F, Explosion.Mode.NONE); //hacky.
		   sporecloud.copyLocationAndAnglesFrom(this);
		   this.world.addEntity(sporecloud);
	   }

	   protected void writeAdditional(CompoundNBT compound) {
	      compound.putShort("Fuse", (short)this.getFuse());
	   }

	   /**
	    * (abstract) Protected helper method to read subclass entity data from NBT.
	    */
	   protected void readAdditional(CompoundNBT compound) {
	      this.setFuse(compound.getShort("Fuse"));
	   }

	   /**
	    * returns null or the entityliving it was placed or ignited by
	    */
	   @Nullable
	   public LivingEntity getTntPlacedBy() {
	      return this.tntPlacedBy;
	   }

	   protected float getEyeHeight(Pose poseIn, EntitySize sizeIn) {
	      return 0.0F;
	   }

	   public void setFuse(int fuseIn) {
	      this.dataManager.set(FUSE, fuseIn);
	      this.fuse = fuseIn;
	   }

	   public void notifyDataManagerChange(DataParameter<?> key) {
	      if (FUSE.equals(key)) {
	         this.fuse = this.getFuseDataManager();
	      }

	   }

	   /**
	    * Gets the fuse from the data manager
	    */
	   public int getFuseDataManager() {
	      return this.dataManager.get(FUSE);
	   }

	   public int getFuse() {
	      return this.fuse;
	   }

	    @Override
	    public IPacket<?> createSpawnPacket()
	    {
	        return NetworkHooks.getEntitySpawningPacket(this);
	    }

}
