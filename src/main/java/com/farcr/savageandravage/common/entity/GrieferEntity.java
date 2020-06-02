package com.farcr.savageandravage.common.entity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.farcr.savageandravage.common.item.CreeperSporesItem;
import com.farcr.savageandravage.core.registry.SRItems;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GrieferEntity extends AbstractIllagerEntity implements IRangedAttackMob
{
	private static final DataParameter<Boolean> KICKING = EntityDataManager.createKey(GrieferEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> APESHIT_MODE = EntityDataManager.createKey(GrieferEntity.class, DataSerializers.BOOLEAN);
	private final EntityPredicate social_distance = (new EntityPredicate()).setDistance(10.0D);
	
	public GrieferEntity(EntityType<? extends GrieferEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	public int kickTicks;
	public int creeperSporeStacks;
	
	protected void registerGoals() 
	{
	   super.registerGoals();
	   this.goalSelector.addGoal(0, new SwimGoal(this));
	   this.goalSelector.addGoal(2, new AbstractRaiderEntity.FindTargetGoal(this, 10.0F));
	   this.goalSelector.addGoal(2, new GrieferEntity.MeleePhaseGoal(this, 1.0D, true));
	   this.goalSelector.addGoal(3, new GrieferEntity.GrieferAttackWithSporesGoal(this, 1.0D, 100));
	   this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true) {
		   @Override
		   public boolean shouldExecute() {
			return !(attacker.getHeldItemMainhand().getItem() instanceof CreeperSporesItem) && super.shouldExecute();
		   }
	   });
	   this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
	   this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F, 1.0F));
	   this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 15.0F));
	   this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
	   this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	   this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
	   this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
	}
	
	protected void registerAttributes() 
	{
	  super.registerAttributes();
      this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.35F);
	  this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
	  this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	   this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}
	
	@Override
	protected void registerData() 
	{
		super.registerData();
		this.dataManager.register(KICKING, false);
		this.dataManager.register(APESHIT_MODE, false);
	}
	
	//temporary sounds
    protected SoundEvent getAmbientSound() {
	   return SoundEvents.ENTITY_PILLAGER_AMBIENT;
	}

	protected SoundEvent getDeathSound() {
	   return SoundEvents.ENTITY_PILLAGER_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	   return SoundEvents.ENTITY_PILLAGER_HURT;
	}

	
	@Override
	public void func_213660_a(int p_213660_1_, boolean p_213660_2_) 
	{
		// TODO Auto-generated method stub
	}

	//Just the pillagers ymca sound for now
	@Override
	public SoundEvent getRaidLossSound() 
	{
		return SoundEvents.ENTITY_PILLAGER_CELEBRATE;
	}
	
	public void livingTick() 
	{
      if (this.kickTicks > 0) {
            ++this.kickTicks;
            LivingEntity attacker = this.getAttackTarget();
            if (this.kickTicks == 10 && attacker != null && this.isKicking()) 
            {
        	  this.attackEntityAsMob(attacker);
        	  double distance = this.getDistance(attacker);
        	  attacker.knockBack(attacker, 6.0F, distance, distance);
            }
        }
	    super.livingTick();
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) 
	{
		super.readAdditional(compound);
		this.kickTicks = compound.getInt("KickTicks");
		
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) 
	{
		super.writeAdditional(compound);
		compound.putInt("KickTicks", this.kickTicks);
	}
	
	public int getKickTicks()
	{
		return this.kickTicks;
	}
	
	public void kick(double distance, LivingEntity entity) {
		if (this.getDistance(entity) <= distance) {
		 this.setKicking(true);
		 this.kickTicks = 9;
		} else {
			this.setKicking(false);
		}
	}
	
    public boolean isKicking() 
    {
	  return this.dataManager.get(KICKING);
    }
    
    public boolean isApeshit() 
    {
	  return this.dataManager.get(APESHIT_MODE);
    }

    public void setKicking(boolean p_213671_1_) 
    {
	   this.dataManager.set(KICKING, p_213671_1_);
    }
    
    public void becomeApeshit(boolean p_213671_1_) 
    {
	   this.dataManager.set(APESHIT_MODE, p_213671_1_);
    }
  
    
	@Override
	protected void constructKnockBackVector(LivingEntity entityIn) 
    {  
	   if (this.isKicking())
	   {
		  this.setKicking(false);
	   }
	   super.constructKnockBackVector(this);
    }
	   
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) 
	{
	   this.setEquipmentBasedOnDifficulty(difficultyIn);
	   this.creeperSporeStacks = 10;
	   return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) 
	{
	  ItemStack itemstack = new ItemStack(SRItems.CREEPER_SPORES.get());
	  this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
	  //bruh moment this aint working
	  if (this.isLeader()) {
		  this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(SRItems.GRIEFER_LEGGINGS.get())); 
	  }
	} 

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) 
	{
		if (this.world.getTargettableEntitiesWithinAABB(CreepieEntity.class, social_distance, this, this.getBoundingBox().grow(30.0D)).size() < 5) {
		 CreeperSporeCloudEntity creeperSpores = new CreeperSporeCloudEntity(this.world, this);
		 this.swingArm(getActiveHand());
		 double d0 = target.getPosYEye() - (double)1.1F;
		 double d1 = target.getPosX() - this.getPosX();
		 double d2 = d0 - creeperSpores.getPosY();
		 double d3 = target.getPosZ() - this.getPosZ();
		 float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
		 creeperSpores.shoot(d1, d2 + (double)f, d3, 1.6F, 12.0F);
		 creeperSpores.cloudSize = creeperSpores.world.rand.nextInt(50) == 0 ? 0 : 1 + creeperSpores.world.rand.nextInt(3);
		 this.playSound(SoundEvents.ENTITY_EGG_THROW, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		 this.world.addEntity(creeperSpores);
         this.faceEntity(target, 30.0F, 30.0F);
		 this.creeperSporeStacks--;
		}
	}
	
	//just a copy of melee attack goal for now.
	public static class MeleePhaseGoal extends MeleeAttackGoal 
	{
		private final GrieferEntity griefer;
		
		public MeleePhaseGoal(GrieferEntity griefer, double speedIn, boolean useLongMemory) {
			super(griefer, speedIn, useLongMemory);
			this.griefer = griefer;
		}
		
		public boolean shouldExecute()
		{
			return griefer.creeperSporeStacks <= 0 && super.shouldExecute();
		}
		
		public void startExecuting()
		{
			griefer.becomeApeshit(true);
			super.startExecuting();
		}
	}

    public static class GrieferAttackWithSporesGoal extends Goal 
    {
      private final GrieferEntity griefer;
      private int rangedAttackTime = -1;
      private final double entityMoveSpeed;
      private final int attackIntervalMin;
      private final int maxRangedAttackTime;
      private final float attackRadius;
      private boolean strafingClockwise;
      private boolean strafingBackwards;
      private int seeTime;

      public GrieferAttackWithSporesGoal(GrieferEntity attacker, double movespeed, int maxAttackTime) {
         this(attacker, movespeed, maxAttackTime, maxAttackTime);
      }

      public GrieferAttackWithSporesGoal(GrieferEntity attacker, double movespeed, int p_i1650_4_, int maxAttackTime) {
          this.griefer = attacker;
          this.entityMoveSpeed = movespeed;
          this.attackIntervalMin = p_i1650_4_;
          this.maxRangedAttackTime = maxAttackTime;
          this.attackRadius = (float)15.0D;
          this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
       }

      public boolean shouldExecute() 
      {
        LivingEntity livingentity = this.griefer.getAttackTarget();
        return livingentity != null && livingentity.isAlive() && this.griefer.getHeldItemMainhand().getItem() instanceof CreeperSporesItem && this.griefer.creeperSporeStacks > 0; 
      }

     public boolean shouldContinueExecuting() 
     {
       return this.shouldExecute() || !this.griefer.getNavigator().noPath();
     }

     public void resetTask() 
     {
	   this.griefer.setAggroed(false);
	   this.griefer.setAttackTarget((LivingEntity)null);
       this.rangedAttackTime = -1;
       this.griefer.setKicking(false);
     }

     public void tick() {
       LivingEntity attackTarget = griefer.getAttackTarget();
       if (attackTarget != null) {
        double d0 = this.griefer.getDistance(attackTarget);
        boolean flag = this.griefer.getEntitySenses().canSee(attackTarget);
        this.griefer.setAggroed(true);
        if (flag) {
        	++this.seeTime;
        } else {
        	--this.seeTime;
        }
        if (this.griefer.getRNG().nextDouble() < 0.3D) {
          this.strafingClockwise = !this.strafingClockwise;
        }

        if (this.griefer.getRNG().nextDouble() < 0.3D) {
          this.strafingBackwards = !this.strafingBackwards;
        }
        griefer.kick(3.0D, attackTarget);
        if (!(d0 > 15.0D)) {
         this.griefer.getNavigator().clearPath();
         } else {
          this.griefer.getNavigator().tryMoveToEntityLiving(attackTarget, this.entityMoveSpeed);
         }

         this.griefer.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
         if (--this.rangedAttackTime == 0 || this.seeTime == 3) {
           if (!flag) {
            return;
          }
           float f = MathHelper.sqrt(d0) / this.attackRadius;
           float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
           this.griefer.attackEntityWithRangedAttack(attackTarget, lvt_5_1_);
  		   this.griefer.getMoveHelper().strafe((float) (this.strafingBackwards ? -this.entityMoveSpeed : this.entityMoveSpeed), (float) (this.strafingClockwise ? this.entityMoveSpeed : -this.entityMoveSpeed));
           this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
          } else if (this.rangedAttackTime < 0) 
          {
           float f2 = MathHelper.sqrt(d0) / this.attackRadius;
           this.rangedAttackTime = MathHelper.floor(f2 * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
       }
    }
  }
 }
}
