package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.minecraftabnormals.savageandravage.core.registry.SRParticles;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.entity.monster.SpellcastingIllagerEntity.CastingASpellGoal;
import net.minecraft.entity.monster.SpellcastingIllagerEntity.SpellType;
import net.minecraft.entity.monster.SpellcastingIllagerEntity.UseSpellGoal;

public class TricksterEntity extends SpellcastingIllagerEntity {
    public TricksterEntity(EntityType<? extends SpellcastingIllagerEntity> type, World p_i48551_2_) {
        super(type, p_i48551_2_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new CastingASpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(5, new CreatePrisonGoal());
        //this.goalSelector.addGoal(6, new ThrowSparkGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<PlayerEntity>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && ((IDataManager) this.mob).getValue(SREntities.TOTEM_SHIELD_TIME) > 0;
            }
        });
        this.goalSelector.addGoal(1, new AvoidEntityGoal<IronGolemEntity>(this, IronGolemEntity.class, 8.0F, 0.6D, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && ((IDataManager) this.mob).getValue(SREntities.TOTEM_SHIELD_TIME) > 0;
            }
        });
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && ((IDataManager) this.mob).getValue(SREntities.TOTEM_SHIELD_TIME) <= 0;
            }
        }.setUnseenMemoryTicks(300)));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<AbstractVillagerEntity>(this, AbstractVillagerEntity.class, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && ((IDataManager) this.mob).getValue(SREntities.TOTEM_SHIELD_TIME) <= 0;
            }
        }.setUnseenMemoryTicks(300)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<IronGolemEntity>(this, IronGolemEntity.class, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && ((IDataManager) this.mob).getValue(SREntities.TOTEM_SHIELD_TIME) <= 0;
            }
        }.setUnseenMemoryTicks(300));

        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.775F;
    }

    @Override
    public void tick() {
        super.tick();
        if (level.random.nextInt(14) == 0 && this.isCastingSpell() && this.getCurrentSpell() == SpellType.FANGS) {
            float f = this.yBodyRot * ((float)Math.PI / 180F) + MathHelper.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            this.level.addParticle(SRParticles.RUNE.get(), this.getX() + (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double)f2 * 0.6D, 0.0, 0.0, 0.0);
            this.level.addParticle(SRParticles.RUNE.get(), this.getX() - (double)f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double)f2 * 0.6D, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        IDataManager data = (IDataManager) this;
        if (source.getDirectEntity() instanceof ProjectileEntity) {
            if (this.getHealth() - amount <= 0 && data.getValue(SREntities.TOTEM_SHIELD_COOLDOWN) <= 0) {
                this.setHealth(2.0F);
                data.setValue(SREntities.TOTEM_SHIELD_COOLDOWN, 1800);
                if (!this.level.isClientSide())
                    this.level.broadcastEntityEvent(this, (byte) 35);
                this.level.playSound(null, this.blockPosition(), SRSounds.ENTITY_TRICKSTER_LAUGH.get(), SoundCategory.HOSTILE, 1.0f, 1.0f);
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SRSounds.ENTITY_TRICKSTER_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SRSounds.ENTITY_TRICKSTER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SRSounds.ENTITY_TRICKSTER_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        super.playStepSound(pos, blockIn);
        this.playSound(SRSounds.ENTITY_TRICKSTER_STEP.get(), 0.5F, 1.0F);
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SRSounds.ENTITY_TRICKSTER_CAST_SPELL.get();
    }

    @Override
    public void applyRaidBuffs(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SRSounds.ENTITY_TRICKSTER_CELEBRATE.get();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(SRItems.TRICKSTER_SPAWN_EGG.get());
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    class CreatePrisonGoal extends UseSpellGoal {

        @Override
        protected void performSpellCasting() {
            //TODO rune particles while charging
            LivingEntity target = TricksterEntity.this.getTarget();
            if (target != null) {
                BlockPos pos = target.blockPosition();
                World world = TricksterEntity.this.level;
                RunePrisonEntity runePrison = new RunePrisonEntity(world, pos, 25);
                runePrison.moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0F, 0.0F);
                world.addFreshEntity(runePrison);
            }
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SRSounds.ENTITY_RUNE_PRISON_APPEAR.get();
        }

        @Override
        protected SpellType getSpell() {
            return SpellType.FANGS;
        }
    }

    class ThrowSparkGoal extends UseSpellGoal {

        @Override
        protected void performSpellCasting() {

        }

        @Override
        protected int getCastingTime() {
            return 0;
        }

        @Override
        protected int getCastingInterval() {
            return 0;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return null;
        }

        @Override
        protected SpellType getSpell() {
            return null;
        }
    }
}
