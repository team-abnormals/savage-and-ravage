package com.farcr.savageandravage.common.entity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.farcr.savageandravage.common.entity.goals.ConditionalNearestAttackableTargetGoal;
import com.farcr.savageandravage.common.entity.goals.CreepieSwellGoal;
import com.farcr.savageandravage.common.entity.goals.FollowMobOwnerGoal;
import com.farcr.savageandravage.common.entity.goals.MobOwnerHurtByTargetGoal;
import com.farcr.savageandravage.common.entity.goals.MobOwnerHurtTargetGoal;
import com.farcr.savageandravage.core.registry.SRParticles;
import com.farcr.savageandravage.core.registry.SRSounds;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class CreepieEntity extends MonsterEntity implements IOwnableMob {
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(CreepieEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(CreepieEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.createKey(CreepieEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Boolean> CONVERTING = EntityDataManager.createKey(CreepieEntity.class, DataSerializers.BOOLEAN);
    public int lastActiveTime;
    public int timeSinceIgnited;
    public int fuseTime = 30;
    private float explosionRadius;
    private int growingAge = -24000; //I literally had to do this because the entity was being deleted before it got the chance to have its age set, update order moment
    private int forcedAge;
    private int forcedAgeTimer;
    private int conversionTime;

    public CreepieEntity(EntityType<? extends CreepieEntity> type, World worldIn) {
        super(type, worldIn);
        this.explosionRadius = 1.2f;
        this.experienceValue = 2;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new CreepieSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new FollowMobOwnerGoal(this, 1.0D, 2.0F, 20.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, new MobOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new MobOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new ConditionalNearestAttackableTargetGoal(this, true));
    }

    @Override
    protected void registerAttributes(){
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallHeight() {
        return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    @Override
    protected boolean isDespawnPeaceful() {
        return this.getOwner()==null;
    }

    @Override
    public boolean isPreventingPlayerRest(PlayerEntity playerIn) {
        return this.getOwner()==null;
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        boolean flag = super.onLivingFall(distance, damageMultiplier);
        this.timeSinceIgnited = (int)((float)this.timeSinceIgnited + distance * 1.5F);
        if (this.timeSinceIgnited > this.fuseTime - 5) {
            this.timeSinceIgnited = this.fuseTime - 5;
        }

        return flag;
    }

    @Override
    protected void registerData(){
        super.registerData();
        this.dataManager.register(STATE, -1);
        this.dataManager.register(IGNITED, false);
        this.dataManager.register(CONVERTING, false);
        this.dataManager.register(OWNER_UUID, Optional.empty());
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.getOwnerId() == null) {
            compound.putString("OwnerUUID", "");
        } else {
            compound.putString("OwnerUUID", this.getOwnerId().toString());
        }
        compound.putInt("Age", this.getGrowingAge());
        compound.putInt("ForcedAge", this.forcedAge);
        compound.putInt("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        compound.putShort("Fuse", (short)this.fuseTime);
        compound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.putBoolean("ignited", this.hasIgnited());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
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
            try {
                this.setOwnerId(UUID.fromString(s));
            } catch (Throwable var4) {
                this.setOwnerId(null);
            }
        }
        if (compound.contains("Fuse", 99)) {
            this.fuseTime = compound.getShort("Fuse");
        }

        if (compound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }

        if (compound.getBoolean("ignited")) {
            this.ignite();
        }
        this.setGrowingAge(compound.getInt("Age"));
        this.forcedAge = compound.getInt("ForcedAge");
        if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1) {
            this.startConverting(compound.getInt("ConversionTime"));
        }
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. Don't confuse this with EntityLiving.getAge. With a negative value the
     * Entity is considered a child.
     */
    private int getGrowingAge() {
        if (this.world.isRemote) {
            return this.growingAge < 0 ? -1 : 1;
        } else {
            return this.growingAge;
        }
    }

    /**
     * Increases this entity's age, optionally updating {@link #forcedAge}. If the entity is an adult (if the entity's
     * age is greater than or equal to 0) then the entity's age will be set to {@link #forcedAge}.
     */
    private void ageUp(int growthSeconds, boolean updateForcedAge) {
        int i = this.getGrowingAge();
        i = i + growthSeconds * 20;
        if (i > 0) {
            i = 0;
        }

        int j = 0/*i - i*/;
        this.setGrowingAge(i);
        if (updateForcedAge) {
            this.forcedAge += j; //TODO see if this is needed since j seems to always equal 0
            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }

        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.forcedAge);
        }

    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. With a negative value the Entity is considered a child.
     */
    public void setGrowingAge(int age) {
        int i = this.growingAge;
        this.growingAge = age;
        if (i < 0 && age >= 0 || i >= 0 && age < 0) {
            this.onGrowingIntoCreeper();
        }

    }

    /**
     * Creates an explosion as determined by this creeper's power and explosion radius.
     */
	protected void explode() {
        if (!this.world.isRemote) {
            Explosion.Mode explosion$mode = Explosion.Mode.NONE;
            float chargedModifier = 1.0F;
            this.dead = true;
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), this.explosionRadius * chargedModifier, explosion$mode);
            this.remove();
            this.spawnLingeringCloud();
        }

    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick() {
        super.livingTick();
        if (this.world.isRemote) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), 0.0D, 0.0D, 0.0D);
                }

                --this.forcedAgeTimer;
            }
        } else if (this.isAlive()) {
            int i = this.getGrowingAge();
            if (i < 0) {
                ++i;
                this.setGrowingAge(i);
            } else if (i > 0) {
                --i;
                this.setGrowingAge(i);
            }
        }

    }
    
    protected float getSoundPitch() {
        return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F;
     }

     @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SRSounds.CREEPIE_HURT.get();
     }

     @Override
     protected SoundEvent getDeathSound() {
        return SRSounds.CREEPIE_DEATH.get();
     }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        if (this.isAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setCreeperState(1);
            }
            int i = this.getCreeperState();
            if (i > 0 && this.timeSinceIgnited == 0) {
               this.playSound(SRSounds.CREEPIE_PRIMED.get(), this.getSoundVolume(), this.getSoundPitch());
            }
            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
            if (this.isConverting()) {
                if(!this.world.isRemote) {
                    this.conversionTime--;
                    if (this.conversionTime <= 0) {
                        this.finishConversion((ServerWorld) this.world);
                    }
                }
                this.world.addParticle(SRParticles.CREEPER_SPORES.get(), this.getPosX() -0.5d + (double)(this.rand.nextFloat()), this.getPosY()+0.5d, this.getPosZ() -0.5d + (double)(this.rand.nextFloat()), 0.0D, (double)(this.rand.nextFloat() / 5.0F), 0.0D);
            }
        }
        super.tick();
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        return true;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.BONE_MEAL) {
            if (this.isNotCreeper()) {
                this.consumeItemFromStack(player, itemstack);
                player.func_226292_a_(hand, true); //this makes the player's hand swing
                this.ageUp((int)((float)(-this.getGrowingAge() / 20) * 0.1F), true);
                return true;
            }
        }
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.world.playSound(player, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            if (!this.world.isRemote) {
                this.ignite();
                itemstack.damageItem(1, player, (p_213625_1_) -> {
                    p_213625_1_.sendBreakAnimation(hand);
                });
            }

            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    /**
     * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
     */
    @OnlyIn(Dist.CLIENT)
    public float getCreeperFlashIntensity(float partialTicks) {
        return MathHelper.lerp(partialTicks, (float)this.lastActiveTime, (float)this.timeSinceIgnited) / (float)(this.fuseTime - 2);
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getCreeperState() {
        return this.dataManager.get(STATE);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setCreeperState(int state) {
        this.dataManager.set(STATE, state);
    }

    public boolean hasIgnited() {
        return this.dataManager.get(IGNITED);
    }

    public void ignite() {
        this.dataManager.set(IGNITED, true);
    }

    protected void spawnLingeringCloud() {
        Collection<EffectInstance> collection = this.getActivePotionEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
            areaeffectcloudentity.setRadius(1.0F);
            areaeffectcloudentity.setRadiusOnUse(-0.5F);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

            for(EffectInstance effectinstance : collection) {
                areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
            }

            this.world.addEntity(areaeffectcloudentity);
        }

    }

    /**
     * Decreases ItemStack size by one
     */
    private void consumeItemFromStack(PlayerEntity player, ItemStack stack) {
        if (!player.abilities.isCreativeMode) {
            stack.shrink(1);
        }

    }

    /**
     * This is called when Entity's growing age timer reaches 0 (negative values are considered as a child, positive as
     * an adult)
     */
    private void onGrowingIntoCreeper() {
        if (!this.world.isRemote) {
            this.startConverting(this.rand.nextInt(80)+160); //10 seconds before it converts
        }

    }

    /**
     * Checks if the age timer is negative
     */
    private boolean isNotCreeper() {
        return this.getGrowingAge() < 0;
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return !this.getLeashed();
    }


    @Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UUID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID ownerId) {
        this.dataManager.set(OWNER_UUID, Optional.ofNullable(ownerId));
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

    public boolean canAttack(LivingEntity target) {
        return !this.isOwner(target) && super.canAttack(target); /*this was originally this.isOwner(target) ? false : super.canAttack(target), change back if this breaks*/
    }

    private boolean isOwner(LivingEntity entityIn) {
        return entityIn == this.getOwner();
    }


    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (target instanceof CreepieEntity) {
            CreepieEntity creepieEntity = (CreepieEntity) target;
            return creepieEntity.getOwner() != owner;
        } else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isConverting() {
        return this.getDataManager().get(CONVERTING);
    }

    /**
     * Starts conversion of this zombie villager to a villager
     */
    private void startConverting( int conversionTimeIn) {
        this.conversionTime = conversionTimeIn;
        this.getDataManager().set(CONVERTING, true);
        this.world.setEntityState(this, (byte)16);
    }


    private void finishConversion(ServerWorld world) {
        CreeperEntity creeperEntity = EntityType.CREEPER.create(this.world);
        creeperEntity.copyLocationAndAnglesFrom(this.getEntity());
        creeperEntity.onInitialSpawn(this.world, this.world.getDifficultyForLocation(new BlockPos(creeperEntity)), SpawnReason.CONVERSION, null, (CompoundNBT)null);
        this.dead = true;
        this.remove();
        creeperEntity.setNoAI(this.isAIDisabled());
        if (this.hasCustomName()) {
            creeperEntity.setCustomName(this.getCustomName());
            creeperEntity.setCustomNameVisible(this.isCustomNameVisible());
        }

        if (this.isNoDespawnRequired()) {
            creeperEntity.enablePersistence();
        }

        creeperEntity.setInvulnerable(this.isInvulnerable());
        this.world.addEntity(creeperEntity);
        this.world.playEvent((PlayerEntity)null, 1026, new BlockPos(this), 0);
    }


    /**
     * Handler for {@link World#setEntityState} - creates the sound for when the creepie starts converting
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 16) {
            if (!this.isSilent()) {
                this.world.playSound(this.getPosX(), this.getPosYEye(), this.getPosZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }

        } else {
            super.handleStatusUpdate(id);
        }
    }

    public Team getTeam() {
        if (this.getOwnerId()!=null) {
            LivingEntity livingentity = this.getOwner();
            if (livingentity != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn) {
        if (this.getOwnerId()!=null) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isOnSameTeam(entityIn);
            }
        }

        return super.isOnSameTeam(entityIn);
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


}
