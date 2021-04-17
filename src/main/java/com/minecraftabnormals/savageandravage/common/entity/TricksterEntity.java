package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TricksterEntity extends SpellcastingIllagerEntity { //TODO raid speed too high
    public TricksterEntity(EntityType<? extends SpellcastingIllagerEntity> type, World p_i48551_2_) {
        super(type, p_i48551_2_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new CastingASpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        /*this.goalSelector.addGoal(5, new CreatePrisonGoal());
        this.goalSelector.addGoal(6, new ThrowSparkGoal());*/
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<PlayerEntity>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && ((IDataManager) this.entity).getValue(SREntities.TOTEM_SHIELD_TIME) > 0;
            }
        });
        this.goalSelector.addGoal(1, new AvoidEntityGoal<IronGolemEntity>(this, IronGolemEntity.class, 8.0F, 0.6D, 1.0D) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && ((IDataManager) this.entity).getValue(SREntities.TOTEM_SHIELD_TIME) > 0;
            }
        });
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && ((IDataManager) this.goalOwner).getValue(SREntities.TOTEM_SHIELD_TIME) <= 0;
            }
        }.setUnseenMemoryTicks(300)));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<AbstractVillagerEntity>(this, AbstractVillagerEntity.class, true) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && ((IDataManager) this.goalOwner).getValue(SREntities.TOTEM_SHIELD_TIME) <= 0;
            }
        }.setUnseenMemoryTicks(300)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<IronGolemEntity>(this, IronGolemEntity.class, true) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && ((IDataManager) this.goalOwner).getValue(SREntities.TOTEM_SHIELD_TIME) <= 0;
            }
        }.setUnseenMemoryTicks(300));

        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.775F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        IDataManager data = (IDataManager) this;
        if (source.getImmediateSource() instanceof ProjectileEntity) {
            if (this.getHealth() - amount <= 0 && data.getValue(SREntities.TOTEM_SHIELD_COOLDOWN) <= 0) {
                this.setHealth(2.0F);
                data.setValue(SREntities.TOTEM_SHIELD_COOLDOWN, 1800);
                if (!this.world.isRemote())
                    this.world.setEntityState(this, (byte) 35);
                return false;
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL;
    }

    @Override
    public void applyWaveBonus(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_PILLAGER_CELEBRATE;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(SRItems.TRICKSTER_SPAWN_EGG.get());
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 24.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D);
    }

    class CreatePrisonGoal extends UseSpellGoal {

        @Override
        protected void castSpell() {
            //TODO set ticking particle task then prison
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
        protected SpellType getSpellType() {
            return null;
        }
    }

    class ThrowSparkGoal extends UseSpellGoal {

        @Override
        protected void castSpell() {

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
        protected SpellType getSpellType() {
            return null;
        }
    }
}
