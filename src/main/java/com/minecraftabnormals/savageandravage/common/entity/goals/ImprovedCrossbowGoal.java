package com.minecraftabnormals.savageandravage.common.entity.goals;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class ImprovedCrossbowGoal<T extends CreatureEntity & IRangedAttackMob & ICrossbowUser> extends Goal {
    private final T entity;
    private ImprovedCrossbowGoal.CrossbowState crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
    private final double speedChanger;
    private final float radius;
    private int seeTime;
    private int wait;
    private double blockstillbackup;

    public ImprovedCrossbowGoal(T p_i50322_1_, double p_i50322_2_, float p_i50322_4_, double blockstillbackup) {
        this.entity = p_i50322_1_;
        this.speedChanger = p_i50322_2_;
        this.radius = p_i50322_4_ * p_i50322_4_;
        this.blockstillbackup = blockstillbackup;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean shouldExecute() {
        return this.isAttackTargetStillExisting() && this.hasCrossbowOnMainHand();
    }

    private boolean hasCrossbowOnMainHand() {
        return this.entity.getHeldItemMainhand().getItem() instanceof CrossbowItem;
    }

    public boolean shouldContinueExecuting() {
        return this.isAttackTargetStillExisting() && (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.hasCrossbowOnMainHand();
    }

    private boolean isAttackTargetStillExisting() {
        return this.entity.getAttackTarget() != null && this.entity.getAttackTarget().isAlive();
    }

    public void resetTask() {
        super.resetTask();
        this.entity.setAggroed(false);
        this.entity.setAttackTarget((LivingEntity) null);
        this.seeTime = 0;
        if (this.entity.isHandActive()) {
            this.entity.resetActiveHand();
            ((ICrossbowUser) this.entity).setCharging(false);
        }

    }

    public void tick() {
        LivingEntity livingentity = this.entity.getAttackTarget();
        this.entity.setAggroed(true); /*
                                       * i had to put this in because minecraft doesnt think shooting an arrow at
                                       * another entity is aggression.
                                       */
        if (livingentity != null) {
            boolean canSeeEnemy = this.entity.getEntitySenses().canSee(livingentity);
            boolean seeTimeGreaterThenZero = this.seeTime > 0;
            if (canSeeEnemy != seeTimeGreaterThenZero) {
                this.seeTime = 0;
            }

            if (canSeeEnemy) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            double distance = livingentity.getDistance(entity);
            // makes the entity that has this goal backup if the attack target is whatever
            // number blockstillbackup is, infront of them.
            if (distance <= blockstillbackup && !(entity.getAttackTarget() instanceof AbstractVillagerEntity)) {
                this.entity.getMoveHelper().strafe(entity.isHandActive() ? -0.5F : -3.0F, 0); // note : when an entity is "charging" their crossbow
                                                                                              // they set an active hand
            }

            double distanceButSquared = this.entity.getDistanceSq(livingentity);
            boolean shouldMoveTowardsEnemy = (distanceButSquared > (double) this.radius || this.seeTime < 5) && this.wait == 0;
            if (shouldMoveTowardsEnemy) {
                this.entity.getNavigator().tryMoveToEntityLiving(livingentity,
                        this.isCrossbowUncharged() ? this.speedChanger : this.speedChanger * 0.5D);
            } else {
                this.entity.getNavigator().clearPath();
            }

            this.entity.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
            if (this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.UNCHARGED) {
                if (canSeeEnemy) {
                    this.entity.setActiveHand(ProjectileHelper.getHandWith(this.entity, Items.CROSSBOW));
                    this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.CHARGING;
                    ((ICrossbowUser) this.entity).setCharging(true);
                }
            } else if (this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.CHARGING) {
                if (!this.entity.isHandActive()) {
                    this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
                }

                int i = this.entity.getItemInUseMaxCount();
                ItemStack itemstack = this.entity.getActiveItemStack();
                if (i >= CrossbowItem.getChargeTime(itemstack)) {
                    this.entity.stopActiveHand();
                    this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.CHARGED;
                    this.wait = 20 + this.entity.getRNG().nextInt(20);
                    if (entity.getHeldItemOffhand().getItem() instanceof FireworkRocketItem) {
                        entity.setActiveHand(Hand.OFF_HAND);
                    }
                    ((ICrossbowUser) this.entity).setCharging(false);
                }
            } else if (this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.CHARGED) {
                --this.wait;
                if (this.wait == 0) {
                    this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK;
                }
            } else if (this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK && canSeeEnemy) {
                ((IRangedAttackMob) this.entity).attackEntityWithRangedAttack(livingentity, 1.0F);
                ItemStack itemstack1 = this.entity.getHeldItem(ProjectileHelper.getHandWith(this.entity, Items.CROSSBOW));
                CrossbowItem.setCharged(itemstack1, false);
                this.crossbowStateUnCharged = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
            }
        }
    }

    private boolean isCrossbowUncharged() {
        return this.crossbowStateUnCharged == ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
    }

    static enum CrossbowState {
        UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK;
    }
}
