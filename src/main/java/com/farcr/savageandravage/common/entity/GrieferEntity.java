package com.farcr.savageandravage.common.entity;

import java.util.Map;

import javax.annotation.Nullable;

import com.farcr.savageandravage.common.item.CreeperSporesItem;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;
import com.google.common.collect.Maps;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GrieferEntity extends AbstractIllagerEntity implements IRangedAttackMob
{
	public GrieferEntity(EntityType<? extends GrieferEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	 protected void registerGoals() 
	 {
	   super.registerGoals();
	   this.goalSelector.addGoal(0, new SwimGoal(this));
	   this.goalSelector.addGoal(2, new AbstractRaiderEntity.FindTargetGoal(this, 10.0F));
	   this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 50, 8.0F));
	   this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
	   this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F, 1.0F));
	   this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 15.0F));
	   this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
	   this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	   this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
	   this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
	 }
	 
	//The fuck does this do?
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
	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) 
	{
	   this.setEquipmentBasedOnDifficulty(difficultyIn);;
	   return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) 
	{
	  ItemStack itemstack = new ItemStack(SRItems.CREEPER_SPORES.get());
	  this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
	} 

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
		CreeperSporeCloudEntity creeperSpores = new CreeperSporeCloudEntity(this.world, this);
		this.swingArm(getActiveHand());
		double d0 = target.getPosYEye() - (double)1.1F;
		double d1 = target.getPosX() - this.getPosX();
		double d2 = d0 - creeperSpores.getPosY();
		double d3 = target.getPosZ() - this.getPosZ();
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
		creeperSpores.shoot(d1, d2 + (double)f, d3, 1.6F, 12.0F);
		creeperSpores.cloudSize = creeperSpores.world.rand.nextInt(50) == 0 ? 0 : 1 + creeperSpores.world.rand.nextInt(3);
		this.playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(creeperSpores);
	}

}
