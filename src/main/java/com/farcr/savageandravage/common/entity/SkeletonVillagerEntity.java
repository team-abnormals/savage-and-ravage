package com.farcr.savageandravage.common.entity;

import java.util.Map;

import javax.annotation.Nullable;

import com.farcr.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.farcr.savageandravage.core.registry.SRItems;
import com.google.common.collect.Maps;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.ILivingEntityData;
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
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class SkeletonVillagerEntity extends AbstractSkeletonEntity implements ICrossbowUser 
{
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.createKey(SkeletonVillagerEntity.class, DataSerializers.BOOLEAN);
    private final ImprovedCrossbowGoal<SkeletonVillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<SkeletonVillagerEntity>(this, 1.0D, 8.0F, 5.0D);
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
	public double getYOffset() 
	{
	  return -0.4D;
	}
	
	@Override
	public void setCombatTask() 
	{
	 {
	   this.goalSelector.removeGoal(this.aiMelee);
	   this.goalSelector.removeGoal(this.aiCrossBow);
	   ItemStack itemstack = this.getHeldItem(ProjectileHelper.getHandWith(this, Items.CROSSBOW));
	   if (itemstack.getItem() instanceof CrossbowItem) 
	   {
	      this.goalSelector.addGoal(3, this.aiCrossBow);
	   } else 
	   {
	     super.setCombatTask();
	   } 
	  }
	}
	
	@Override
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
	   this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SKELETON_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SKELETON_HURT;
	}
	
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}
	
	protected SoundEvent getStepSound() {
		return SoundEvents.ENTITY_SKELETON_STEP;
	}

	@Override
	public void setCharging(boolean trueorfalse) 
	{
	  this.dataManager.set(DATA_CHARGING_STATE, trueorfalse);
	}
	
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(SRItems.SKELETON_VILLAGER_SPAWN_EGG.get());
	}
	
	@Nullable
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) 
	{
	   this.setEquipmentBasedOnDifficulty(difficultyIn);
	   this.setEnchantmentBasedOnDifficulty(difficultyIn);
	   if (worldIn.getRandom().nextInt(100) == 0) 
	   {
	     SpiderEntity spider = EntityType.SPIDER.create(this.world);
	     spider.copyLocationAndAnglesFrom(this);
	     worldIn.addEntity(spider);
	     this.startRiding(spider);
	   }
	   this.setCombatTask();
	   return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) 
	{
	  ItemStack itemstack = new ItemStack(Items.CROSSBOW);
	  if (this.rand.nextInt(300) == 0) 
	  {
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
	  this.func_234281_b_(this, 1.6F);
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

	@Override
	public void func_230283_U__()
	{
      this.idleTime = 0;
	}

	@Override
	public void func_230284_a_(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) 
	{
	  this.func_234279_a_(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
	}
}