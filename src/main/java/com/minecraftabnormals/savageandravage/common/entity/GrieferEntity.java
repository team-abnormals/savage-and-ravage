package com.minecraftabnormals.savageandravage.common.entity;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.minecraftabnormals.savageandravage.common.item.CreeperSporesItem;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
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
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GrieferEntity extends AbstractIllagerEntity implements IRangedAttackMob {
    private static final DataParameter<Boolean> KICKING = EntityDataManager.createKey(GrieferEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> APESHIT_MODE = EntityDataManager.createKey(GrieferEntity.class, DataSerializers.BOOLEAN);
    private final EntityPredicate social_distance = (new EntityPredicate()).setDistance(10.0D);

    public GrieferEntity(EntityType<? extends GrieferEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public int creeperSporeStacks;
    public int kickTicks;
    public int kickCoolDown;

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new AbstractRaiderEntity.FindTargetGoal(this, 10.0F));
        this.goalSelector.addGoal(3, new GrieferEntity.MeleePhaseGoal(this, 0.9D, true));
        this.goalSelector.addGoal(3, new GrieferEntity.GrieferAttackWithSporesGoal(this, 0.7D, 100));
        this.goalSelector.addGoal(2, new GrieferEntity.KickGoal(this));
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
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }
    
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, (double) 0.35F)
                .createMutableAttribute(Attributes.MAX_HEALTH, 25.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(KICKING, false);
        this.dataManager.register(APESHIT_MODE, false);
    }

    // temporary sounds
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }

    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(SRItems.GRIEFER_SPAWN_EGG.get());
    }

    // Just the pillagers ymca sound for now
    @Override
    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_PILLAGER_CELEBRATE;
    }

    @Override
    public void livingTick() {
        if (this.kickTicks > 0) {
            --this.kickTicks;
        }

        if (this.kickCoolDown > 0) {
            --this.kickCoolDown;
        }

        super.livingTick();
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.isKicking()) {
            ((LivingEntity) entityIn).applyKnockback(1.0F, MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)), (-MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F))));
            this.kickTicks = 10;
            this.world.setEntityState(this, (byte) 4);
            this.faceEntity(entityIn, 90.0F, 90.0F);
        }
        return super.attackEntityAsMob(entityIn);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 4) {
            this.kickTicks = 10;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (this.isApeshit() && creeperSporeStacks <= 0 && damageSrc.getTrueSource() == this.getAttackTarget() && this.getAttackTarget() != null) {
            creeperSporeStacks = (int) this.lastDamage / 2;
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.kickTicks = compound.getInt("KickTicks");
        if (compound.contains("CreeperSporeStacks", 99)) {
            this.creeperSporeStacks = compound.getInt("CreeperSporeStacks");
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("KickTicks", this.kickTicks);
        compound.putInt("CreeperSporeStacks", this.creeperSporeStacks);
    }

    public int getKickTicks() {
        return this.kickTicks;
    }

    public void kick(LivingEntity entity) {
        this.setKicking(true);
        this.attackEntityAsMob(entity);
        this.kickCoolDown = 30;
    }

    public boolean isKicking() {
        return this.dataManager.get(KICKING);
    }

    public boolean isApeshit() {
        return this.dataManager.get(APESHIT_MODE);
    }

    public void setKicking(boolean trueorfalse) {
        this.dataManager.set(KICKING, trueorfalse);
    }

    public void becomeApeshit(boolean trueorfalse) {
        this.dataManager.set(APESHIT_MODE, trueorfalse);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.creeperSporeStacks = 10;
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(SRItems.CREEPER_SPORES.get()));
        this.giveArmorOnRandom(EquipmentSlotType.HEAD, new ItemStack(SRItems.GRIEFER_HELMET.get()));
        this.giveArmorOnRandom(EquipmentSlotType.CHEST, new ItemStack(SRItems.GRIEFER_CHESTPLATE.get()));
        this.giveArmorOnRandom(EquipmentSlotType.LEGS, new ItemStack(SRItems.GRIEFER_LEGGINGS.get()));
        this.giveArmorOnRandom(EquipmentSlotType.FEET, new ItemStack(SRItems.GRIEFER_BOOTS.get()));
    }

    private void giveArmorOnRandom(EquipmentSlotType slot, ItemStack stack) {
        ItemStack itemstack = this.getItemStackFromSlot(slot);
        float chance = this.isLeader() ? 1.00F : 0.100F; // feedback on chance would be epic
        if (itemstack.isEmpty() && this.world.rand.nextFloat() < chance) {
            this.setItemStackToSlot(slot, stack);
        }
    }

    @Override
    public void applyWaveBonus(int arg0, boolean arg1) {
        // TODO : put something here
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.world.getTargettableEntitiesWithinAABB(CreepieEntity.class, social_distance, this, this.getBoundingBox().grow(30.0D)).size() < 5 && this.getHeldItemMainhand().getItem() instanceof CreeperSporesItem) {
            CreeperSporeCloudEntity creeperSpores = new CreeperSporeCloudEntity(this.world, this);
            double distance = target.getPosYEye() - (double) 1.1F;
            double d1 = target.getPosX() - this.getPosX();
            double d2 = distance - creeperSpores.getPosY();
            double d3 = target.getPosZ() - this.getPosZ();
            float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
            creeperSpores.shoot(d1, d2 + (double) f, d3, 1.6F, 12.0F);
            creeperSpores.cloudSize = creeperSpores.world.rand.nextInt(50) == 0 ? 0 : 1 + creeperSpores.world.rand.nextInt(3);
            this.swingArm(getActiveHand());
            this.playSound(SoundEvents.ENTITY_EGG_THROW, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
            this.world.addEntity(creeperSpores);
            this.faceEntity(target, 30.0F, 30.0F);
            this.creeperSporeStacks--;
        }
    }

    public static class KickGoal extends Goal {

        private final GrieferEntity griefer;

        public KickGoal(GrieferEntity griefer) {
            this.griefer = griefer;
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity entity = griefer.getAttackTarget();
            return griefer.getAttackTarget() != null && entity != null && entity.getDistance(griefer) <= 2.5D && !griefer.isKicking() && griefer.kickTicks <= 0 && griefer.kickCoolDown == 0 && griefer.creeperSporeStacks > 0;
        }

        @Override
        public void startExecuting() {
            LivingEntity entity = griefer.getAttackTarget();
            griefer.kick(entity);
        }

        @Override
        public void resetTask() {
            griefer.kickTicks = 0;
            griefer.setKicking(false);
        }

    }

    public static class MeleePhaseGoal extends MeleeAttackGoal {
        private final GrieferEntity griefer;

        public MeleePhaseGoal(GrieferEntity griefer, double speedIn, boolean useLongMemory) {
            super(griefer, speedIn, useLongMemory);
            this.griefer = griefer;
        }

        public boolean shouldExecute() {
            return griefer.creeperSporeStacks == 0 && super.shouldExecute();
        }

        public boolean shouldContinueExecuting() {
            return griefer.creeperSporeStacks == 0 && super.shouldContinueExecuting();
        }

        public void startExecuting() {
            griefer.becomeApeshit(true);
            if (griefer.creeperSporeStacks == 0) {
                griefer.setHeldItem(griefer.getActiveHand(), ItemStack.EMPTY);
            }
            super.startExecuting();
        }

        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            double distance = this.getAttackReachSqr(enemy);
            int i = griefer.world.rand.nextInt(3);
            if (distToEnemySqr <= distance && this.field_234037_i_ <= 0) {
                this.field_234037_i_ = 60;
                switch (i) {
                case 0:
                    this.griefer.swingArm(Hand.MAIN_HAND);
                    break;

                case 1:
                    this.griefer.swingArm(Hand.OFF_HAND);
                    break;

                case 2:
                    this.griefer.kick(enemy);
                    break;
                } // if this breaks anything tell me
                this.griefer.faceEntity(enemy, 30.0F, 30.0F);
                this.griefer.attackEntityAsMob(enemy);
                enemy.applyKnockback(1.5F, (double) MathHelper.sin(griefer.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(griefer.rotationYaw * ((float) Math.PI / 180F))));
            }
        }
    }

    public static class GrieferAttackWithSporesGoal extends Goal {
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
            this.attackRadius = (float) 15.0D;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean shouldExecute() {
            LivingEntity livingentity = this.griefer.getAttackTarget();
            return livingentity != null && livingentity.isAlive() && this.griefer.creeperSporeStacks > 0;
        }

        public boolean shouldContinueExecuting() {
            return this.shouldExecute() || !this.griefer.getNavigator().noPath();
        }

        public void startExecuting() {
            if (griefer.isApeshit()) {
                griefer.becomeApeshit(false);
            }

            if (griefer.creeperSporeStacks > 0) {
                griefer.setHeldItem(Hand.MAIN_HAND, new ItemStack(SRItems.CREEPER_SPORES.get()));
            }
        }

        public void resetTask() {
            this.griefer.setAggroed(false);
            this.griefer.setAttackTarget((LivingEntity) null);
            this.rangedAttackTime = -1;
            if (griefer.isKicking()) {
                this.griefer.setKicking(false);
            }
        }

        public void tick() {
            LivingEntity attackTarget = griefer.getAttackTarget();
            if (attackTarget != null) {
                double distance = this.griefer.getDistance(attackTarget);
                boolean canSee = this.griefer.getEntitySenses().canSee(attackTarget);
                this.griefer.setAggroed(true);
                if (canSee) {
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
                if (distance < 15.0D) {
                    this.griefer.getNavigator().clearPath();
                } else {
                    this.griefer.getNavigator().tryMoveToEntityLiving(attackTarget, this.entityMoveSpeed);
                }

                this.griefer.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
                if (--this.rangedAttackTime == 0 || this.seeTime == 3) {
                    if (!canSee) {
                        return;
                    }
                    float f = MathHelper.sqrt(distance) / this.attackRadius;
                    float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
                    this.griefer.attackEntityWithRangedAttack(attackTarget, lvt_5_1_);
                    this.griefer.faceEntity(attackTarget, 30.0F, 30.0F);
                    this.griefer.getMoveHelper().strafe((float) (this.strafingBackwards ? -this.entityMoveSpeed : this.entityMoveSpeed), (float) (this.strafingClockwise ? this.entityMoveSpeed : -this.entityMoveSpeed));
                    this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
                } else if (this.rangedAttackTime < 0) {
                    float f2 = MathHelper.sqrt(distance) / this.attackRadius;
                    this.rangedAttackTime = MathHelper.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
                }
            }
        }
    }
}