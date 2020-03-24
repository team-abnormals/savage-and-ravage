package com.farcr.savageandravage.common.entity;

import java.util.Map;

import javax.annotation.Nullable;

import com.farcr.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.google.common.collect.Maps;

import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.FleeSunGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RestrictSunGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class SkeletonVillagerEntity extends AbstractSkeletonEntity implements ICrossbowUser, IRangedAttackMob 
{
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.createKey(SkeletonVillagerEntity.class, DataSerializers.BOOLEAN);
    private final ImprovedCrossbowGoal<SkeletonVillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<SkeletonVillagerEntity>(this, 1.0D, 8.0F);
    private final MeleeAttackGoal aiMelee = new MeleeAttackGoal(this, 1.2D, false) {
       public void resetTask() {
          super.resetTask();
          SkeletonVillagerEntity.this.setAggroed(false);
       }

       public void startExecuting() {
          super.startExecuting();
          SkeletonVillagerEntity.this.setAggroed(true);
       }
    };

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
     }
    
	public SkeletonVillagerEntity(EntityType<? extends SkeletonVillagerEntity> type, World worldIn) {
		super(type, worldIn);
		this.setCombatTask();
	}
	
	@Override
	public void setCombatTask() {
	{
	         this.goalSelector.removeGoal(this.aiMelee);
	         this.goalSelector.removeGoal(this.aiCrossBow);
	         ItemStack itemstack = this.getHeldItem(ProjectileHelper.getHandWith(this, Items.CROSSBOW));
	         if (itemstack.getItem() instanceof CrossbowItem) {
	            this.goalSelector.addGoal(4, this.aiCrossBow);
	         } else {
	            super.setCombatTask();
	         } 

	      }
	   }
	
	protected void registerGoals() 
	{
	   this.goalSelector.addGoal(2, new RestrictSunGoal(this));
       this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
	   this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
	   this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
	   this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
	   this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
	   this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, SkeletonVillagerEntity.class)).setCallsForHelp());
	   this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	   this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
	   this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, true));
	}

	@Override
	protected SoundEvent getStepSound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCharging(boolean p_213671_1_) 
	{
	  this.dataManager.set(DATA_CHARGING_STATE, p_213671_1_);
	}
	
	@Nullable
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) 
	{
	   this.setEquipmentBasedOnDifficulty(difficultyIn);
	   this.setEnchantmentBasedOnDifficulty(difficultyIn);
	   this.setCombatTask();
	   return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) 
	{
	     ItemStack itemstack = new ItemStack(Items.CROSSBOW);
	      if (this.rand.nextInt(300) == 0) {
	         Map<Enchantment, Integer> map = Maps.newHashMap();
	         map.put(Enchantments.PIERCING, 1);
	         EnchantmentHelper.setEnchantments(map, itemstack);
	      }

	      this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
	}
	 
    protected void registerData()
    {
	  super.registerData();
	  this.dataManager.register(DATA_CHARGING_STATE, false);
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) 
	{
	  Hand hand = ProjectileHelper.getHandWith(this, Items.CROSSBOW);
	  ItemStack itemstack = this.getHeldItem(hand);
	  if (this.isHolding(Items.CROSSBOW)) 
	  {
	   CrossbowItem.fireProjectiles(this.world, this, hand, itemstack, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
	  }

	  this.idleTime = 0;
	}
	
	public void readAdditional(CompoundNBT compound) 
	{
	  super.readAdditional(compound);
	  this.setCombatTask(); 
	}

	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) 
	{
       super.setItemStackToSlot(slotIn, stack);
	   {
		 this.setCombatTask();
	   }
    }
	
	public boolean isCharging() 
	{
	  return this.dataManager.get(DATA_CHARGING_STATE);
	} 

	public void shoot(LivingEntity target, ItemStack p_213670_2_, IProjectile projectile, float projectileAngle) 
	{
	  Entity entity = (Entity)projectile;
	  double d0 = target.getPosX() - this.getPosX();
	  double d1 = target.getPosZ() - this.getPosZ();
	  double d2 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1);
	  double d3 = target.getPosYHeight(0.3333333333333333D) - entity.getPosY() + d2 * (double)0.2F;
	  Vector3f vector3f = this.func_213673_a(new Vec3d(d0, d3, d1), projectileAngle);
	  projectile.shoot((double)vector3f.getX(), (double)vector3f.getY(), (double)vector3f.getZ(), 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
	  this.playSound(SoundEvents.ITEM_CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
	}

   private Vector3f func_213673_a(Vec3d p_213673_1_, float p_213673_2_) 
   {
	Vec3d vec3d = p_213673_1_.normalize();
	Vec3d vec3d1 = vec3d.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D));
	  if (vec3d1.lengthSquared() <= 1.0E-7D) 
	  {
       vec3d1 = vec3d.crossProduct(this.getUpVector(1.0F));
	  }
	Quaternion quaternion = new Quaternion(new Vector3f(vec3d1), 90.0F, true);
	Vector3f vector3f = new Vector3f(vec3d);
    vector3f.transform(quaternion);
	Quaternion quaternion1 = new Quaternion(vector3f, p_213673_2_, true);
	Vector3f vector3f1 = new Vector3f(vec3d);
	vector3f1.transform(quaternion1);
	 return vector3f1;
	}
}