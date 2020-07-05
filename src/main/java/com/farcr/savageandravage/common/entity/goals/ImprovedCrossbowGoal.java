package com.farcr.savageandravage.common.entity.goals;

import java.util.EnumSet;

import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ImprovedCrossbowGoal<T extends CreatureEntity & IRangedAttackMob & ICrossbowUser> extends Goal {
	   private final T entity;
	   private ImprovedCrossbowGoal.CrossbowState field_220749_b = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
	   private final double field_220750_c;
	   private final float field_220751_d;
	   private int field_220752_e;
	   private int field_220753_f;
	   private double blockstillbackup;

	   public ImprovedCrossbowGoal(T p_i50322_1_, double p_i50322_2_, float p_i50322_4_, double blockstillbackup) {
	      this.entity = p_i50322_1_;
	      this.field_220750_c = p_i50322_2_;
	      this.field_220751_d = p_i50322_4_ * p_i50322_4_;
	      this.blockstillbackup = blockstillbackup;
	      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	   }

	   public boolean shouldExecute() {
	      return this.func_220746_h() && this.func_220745_g();
	   }

	   private boolean func_220745_g() {
	      return this.entity.isHolding(Items.CROSSBOW);
	   }

	   public boolean shouldContinueExecuting() {
	      return this.func_220746_h() && (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.func_220745_g();
	   }

	   private boolean func_220746_h() {
	      return this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isAlive();
	   }

	   public void resetTask() {
	      super.resetTask();
	      this.entity.setAggroed(false);
	      this.entity.setAttackTarget((LivingEntity)null);
	      this.field_220752_e = 0;
	      if (this.entity.isHandActive()) {
	         this.entity.resetActiveHand();
	         ((ICrossbowUser)this.entity).setCharging(false);
	      }

	   }

	   public void tick() {
		      LivingEntity livingentity = this.entity.getAttackTarget();
		      this.entity.setAggroed(true); /* i had to put this in because minecraft doesnt think shooting an arrow at another entity is
		      aggression.  */
		      if (livingentity != null) {
		         boolean flag = this.entity.getEntitySenses().canSee(livingentity);
		         boolean flag1 = this.field_220752_e > 0;
		         if (flag != flag1) {
		            this.field_220752_e = 0;
		         }

		         if (flag) {
		            ++this.field_220752_e;
		         } else {
		            --this.field_220752_e;
		         }
		         
		         double d1 = livingentity.getDistance(entity);	         
		         //makes the entity that has this goal backup if the attack target is whatever number blockstillbackup is, infront of them.
		         if (d1 <= blockstillbackup  && !(entity.getAttackTarget() instanceof AbstractVillagerEntity)) {
		            if (entity instanceof PillagerEntity) {
		            	//stupid problems require stupid solutions
						EntityDataManager dataManager = ObfuscationReflectionHelper.getPrivateValue(Entity.class, entity,"field_70180_af");
						DataParameter<Boolean> dataChargingState = ObfuscationReflectionHelper.getPrivateValue(PillagerEntity.class, (PillagerEntity) entity,"field_213676_b");
						boolean isCharging = dataManager.get(dataChargingState);
		              this.entity.getMoveHelper().strafe(isCharging ? -0.5F : -3.0F, 0);
		            } else if (entity instanceof SkeletonVillagerEntity) {
		              this.entity.getMoveHelper().strafe(((SkeletonVillagerEntity)entity).isCharging() ? -0.5F : -3.0F, 0);
		            }
		            this.entity.faceEntity(livingentity, 30.0F, 30.0F);
		         }

		         double d0 = this.entity.getDistanceSq(livingentity);
		         boolean flag2 = (d0 > (double)this.field_220751_d || this.field_220752_e < 5) && this.field_220753_f == 0;
		         if (flag2) {
		          this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.func_220747_j() ? this.field_220750_c : this.field_220750_c * 0.5D);
		         } else {
		            this.entity.getNavigator().clearPath();
		         }

		         this.entity.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
		         if (this.field_220749_b == ImprovedCrossbowGoal.CrossbowState.UNCHARGED) {
		            if (flag) {
		               this.entity.setActiveHand(ProjectileHelper.getHandWith(this.entity, Items.CROSSBOW));
		               this.field_220749_b = ImprovedCrossbowGoal.CrossbowState.CHARGING;
		               ((ICrossbowUser)this.entity).setCharging(true);
		            }
		         } else if (this.field_220749_b == ImprovedCrossbowGoal.CrossbowState.CHARGING) {
		            if (!this.entity.isHandActive()) {
		               this.field_220749_b = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
		            }

		            int i = this.entity.getItemInUseMaxCount();
		            ItemStack itemstack = this.entity.getActiveItemStack();
		            if (i >= CrossbowItem.getChargeTime(itemstack)) {
		               this.entity.stopActiveHand();
		               this.field_220749_b = ImprovedCrossbowGoal.CrossbowState.CHARGED;
		               this.field_220753_f = 20 + this.entity.getRNG().nextInt(20);
		               if (entity.getHeldItemOffhand().getItem() instanceof FireworkRocketItem) {
		            	   entity.setActiveHand(Hand.OFF_HAND);
		               }
		               ((ICrossbowUser)this.entity).setCharging(false);
		            }
		         } else if (this.field_220749_b == ImprovedCrossbowGoal.CrossbowState.CHARGED) {
		            --this.field_220753_f;
		            if (this.field_220753_f == 0) {
		               this.field_220749_b = ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK;
		            }
		         } else if (this.field_220749_b == ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK && flag) {
		            ((IRangedAttackMob)this.entity).attackEntityWithRangedAttack(livingentity, 1.0F);
		            ItemStack itemstack1 = this.entity.getHeldItem(ProjectileHelper.getHandWith(this.entity, Items.CROSSBOW));
		            CrossbowItem.setCharged(itemstack1, false);
		            this.field_220749_b = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
		         }
		      }
		   }

	   private boolean func_220747_j() {
	      return this.field_220749_b == ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
	   }

	   static enum CrossbowState {
	      UNCHARGED,
	      CHARGING,
	      CHARGED,
	      READY_TO_ATTACK;
	   }
	}
