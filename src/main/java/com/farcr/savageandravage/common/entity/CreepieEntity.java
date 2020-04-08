package com.farcr.savageandravage.common.entity;

import com.farcr.savageandravage.common.entity.goals.ImprovedOwnerHurtByTargetGoal;
import com.farcr.savageandravage.common.entity.goals.ImprovedOwnerHurtTargetGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import com.farcr.savageandravage.common.entity.goals.CreepieSwellGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class CreepieEntity extends CreeperEntity implements IOwnableMob {
	private float explosionRadius;
	private LivingEntity creepieCreator;
    private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(CreepieEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);


    public CreepieEntity(EntityType<? extends CreepieEntity> type, World worldIn) {
        super(type, worldIn);
        this.explosionRadius = 1.2f;
    }
    public CreepieEntity(EntityType<? extends CreepieEntity> type, LivingEntity creator,  World worldIn) {
        super(type, worldIn);
        this.creepieCreator = creator;
        this.explosionRadius = 1.2f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new CreepieSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        //this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new ImprovedOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ImprovedOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        if(creepieCreator==null){
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        }
    }

    @Override
    protected void registerAttributes(){
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    }

    @Override
    protected void registerData(){
        super.registerData();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Override
	protected void explode() {
        if (!this.world.isRemote) {
            Explosion.Mode explosion$mode = Explosion.Mode.NONE;
            float chargedModifier = this.isCharged() ? 2.0F : 1.0F;
            this.dead = true;
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), this.explosionRadius * chargedModifier, explosion$mode);
            this.remove();
            this.spawnLingeringCloud();
        }

    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.getOwnerId() == null) {
            compound.putString("OwnerUUID", "");
        } else {
            compound.putString("OwnerUUID", this.getOwnerId().toString());
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        String s;
        if (compound.contains("OwnerUUID", 8)) {
            s = compound.getString("OwnerUUID");
        } else {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty()) {
            this.setOwnerId(UUID.fromString(s));
        }

    }
    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return !this.getLeashed();
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.world.getPlayerByUuid(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        return true;
    }

    public boolean canAttack(LivingEntity target) {
        return this.isOwner(target) && super.canAttack(target);
    }

    public boolean isOwner(LivingEntity entityIn) {
        return entityIn == this.getOwner();
    }



}
