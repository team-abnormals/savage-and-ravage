package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.savageandravage.core.mixin.IRaiderAccessor;
import com.minecraftabnormals.savageandravage.core.other.SRDataProcessors;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ImprovedCrossbowGoal<T extends CreatureEntity & IRangedAttackMob & ICrossbowUser> extends Goal {
    private static final float targetBlockChance = 0.001F;
    private final T mob;
    private ImprovedCrossbowGoal.CrossbowState crossbowState = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
    private final double speedChanger;
    private final float radiusSq;
    private int seeTime;
    private int wait;
    private final double blocksUntilBackupSq;
    private int targetFocusTime;
    private UUID projectileUUID = null;

    public ImprovedCrossbowGoal(T mob, double speedChanger, float radius, double blocksUntilBackup) {
        this.mob = mob;
        this.speedChanger = speedChanger;
        this.radiusSq = radius * radius;
        this.blocksUntilBackupSq = blocksUntilBackup;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    private boolean hasCrossbowOnMainHand() {
        return this.mob.getMainHandItem().getItem() instanceof CrossbowItem;
    }

    private boolean hasAttackTarget() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    @Override
    public boolean canUse() {
        if (this.hasCrossbowOnMainHand()) {
            World world = this.mob.level;
            Vector3d targetBlockPos = this.getTargetBlockPos();
            if (this.hasAttackTarget())
                return true;
            else if (this.mob instanceof AbstractRaiderEntity && ((ServerWorld)world).getRaidAt(this.mob.blockPosition()) == null) {
                boolean isCelebrating = this.mob.getEntityData().get(((IRaiderAccessor) this.mob).getIsCelebrating());
                if (targetBlockPos != null) {
                    if (isPractising())
                        return true;
                    else if (Math.abs(targetBlockPos.x()-this.mob.getX()) > 16 || Math.abs(targetBlockPos.y()-this.mob.getY()) > 8 || Math.abs(targetBlockPos.z()-this.mob.getZ()) > 16) {
                        if (this.isTargetAtPos(world, targetBlockPos)) {
                            if (this.mob.getRandom().nextFloat() < targetBlockChance) {
                                this.setPractising(true);
                                return true;
                            }
                        } else this.setTargetBlockPos(null);
                    }
                } else if (!isCelebrating && this.mob.getRandom().nextFloat() < targetBlockChance /*&& !practisingNearby()*/ && world.isAreaLoaded(this.mob.blockPosition(), 18)) {
                    BlockPos.Mutable searchPos = new BlockPos.Mutable();
                    int x = (int) Math.floor(this.mob.getX());
                    int y = (int) Math.floor(this.mob.getY());
                    int z = (int) Math.floor(this.mob.getZ());
                    for (int xSearch = x - 8; xSearch <= x + 8; xSearch++) {
                        for (int ySearch = y - 4; ySearch <= y + 4; ySearch++) {
                            for (int zSearch = z - 8; zSearch <= z + 8; zSearch++) {
                                searchPos.set(xSearch, ySearch, zSearch);
                                    if (world.getBlockState(searchPos).is(Blocks.TARGET)) {
                                        Vector3d targetPos = new Vector3d(xSearch, ySearch, zSearch);
                                        this.setTargetBlockPos(targetPos);
                                        this.targetFocusTime = 200 + this.mob.getRandom().nextInt(160);
                                        this.setPractising(true);
                                        return true;
                                    }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isTargetAtPos(World world, Vector3d targetPos) {
        BlockPos target = new BlockPos(targetPos);
        if (world.isAreaLoaded(target, 2))
            return world.getBlockState(target).is(Blocks.TARGET);
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return (this.hasAttackTarget() || this.isPractising()) && (this.canUse() || !this.mob.getNavigation().isDone()) && this.hasCrossbowOnMainHand();
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.mob.setTarget(null);
        this.seeTime = 0;
        if (this.mob.isUsingItem()) {
            this.mob.stopUsingItem();
            this.mob.setChargingCrossbow(false);
        }
        this.setProjectile(null);
        this.targetFocusTime = 0;
        this.setPractising(false);
    }

    private boolean isWalkable() {
        PathNavigator pathnavigator = this.mob.getNavigation();
        NodeProcessor nodeprocessor = pathnavigator.getNodeEvaluator();
        return nodeprocessor.getBlockPathType(this.mob.level, MathHelper.floor(this.mob.getX() + 1.0D), MathHelper.floor(this.mob.getY()), MathHelper.floor(this.mob.getZ() + 1.0D)) == PathNodeType.WALKABLE;
    }
    
    @Nullable
    private Vector3d getTargetBlockPos() {
        return ((IDataManager) this.mob).getValue(SRDataProcessors.TARGET_BLOCK_POS).orElse(null);
    }

    private void setTargetBlockPos(Vector3d pos) {
        ((IDataManager) this.mob).setValue(SRDataProcessors.TARGET_BLOCK_POS, pos != null ? Optional.of(pos) : Optional.empty());
    }

    @Nullable
    private boolean isPractising() {
        return ((IDataManager) this.mob).getValue(SRDataProcessors.IS_PRACTISING);
    }

    private void setPractising(boolean isPractising) {
        ((IDataManager) this.mob).setValue(SRDataProcessors.IS_PRACTISING, isPractising);
    }

    private void setCelebrationTime(int time) {
        if (this.mob instanceof AbstractRaiderEntity)
            ((IDataManager) this.mob).setValue(SRDataProcessors.CELEBRATION_TIME, time);
    }
    
    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        ProjectileEntity projectile = this.getProjectile();
        if (this.targetFocusTime > 0 && target == null && this.getTargetBlockPos() != null && this.mob.level.getBlockState(new BlockPos(this.getTargetBlockPos())).is(Blocks.TARGET))
            this.targetFocusTime--;
        else this.setTargetBlockPos(null);
        if (projectile != null && ((IDataManager) this.mob).getValue(SRDataProcessors.TARGET_HIT)) {
            this.setCelebrationTime(100 + this.mob.getRandom().nextInt(100));
            ((IDataManager) this.mob).setValue(SRDataProcessors.TARGET_HIT, false);
            this.setProjectile(null);
            this.setTargetBlockPos(null);
        }

        if (target == null && this.getTargetBlockPos() == null)
            return;

        boolean canSeeEnemy = target != null ? this.mob.getSensing().canSee(target) : this.canSeePos(this.getTargetBlockPos(), true);
        if (canSeeEnemy)
            ++this.seeTime;
        else {
            if (target == null && !this.canSeePos(this.getTargetBlockPos(), false)) {
                this.setTargetBlockPos(null);
                return;
            }
            this.seeTime = 0;
        }

        this.mob.setAggressive(true);

        double distanceSq = target != null ? this.mob.distanceToSqr(target) : this.mob.distanceToSqr(this.getTargetBlockPos());
        double distance = MathHelper.sqrt(distanceSq);
        int distanceCoefficient = target == null ? 2 : 1;
        if (distance <= (blocksUntilBackupSq * distanceCoefficient) && !(target instanceof AbstractVillagerEntity)) {
            if (this.isWalkable())
                this.mob.getMoveControl().strafe(mob.isUsingItem() ? -0.5F : -3.0F, 0);
        }
        ItemStack activeStack = this.mob.getUseItem();
        boolean shouldMoveTowardsEnemy = ((distanceSq > (double) this.radiusSq * distanceCoefficient) || this.seeTime < 5) && this.wait == 0;
        if (shouldMoveTowardsEnemy) {
            double speedChange = this.isCrossbowUncharged() ? this.speedChanger : this.speedChanger * 0.5D;
            if (target != null)
                this.mob.getNavigation().moveTo(target, speedChange);
            else
                this.mob.getNavigation().moveTo(this.getTargetBlockPos().x(), this.getTargetBlockPos().y(), this.getTargetBlockPos().z(), speedChange);
        } else this.mob.getNavigation().stop();

        if (target != null)
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        else this.mob.getLookControl().setLookAt(this.getTargetBlockPos().x(), this.getTargetBlockPos().y(), this.getTargetBlockPos().z(), 30.0F, 30.0F);

        if (this.crossbowState == ImprovedCrossbowGoal.CrossbowState.UNCHARGED && !CrossbowItem.isCharged(activeStack)) {
            if (canSeeEnemy) {
                this.mob.startUsingItem(ProjectileHelper.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
                this.crossbowState = ImprovedCrossbowGoal.CrossbowState.CHARGING;
                this.mob.setChargingCrossbow(true);
            }
        } else if (this.crossbowState == ImprovedCrossbowGoal.CrossbowState.CHARGING) {
            if (!this.mob.isUsingItem()) {
                this.crossbowState = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
            }

            int i = this.mob.getTicksUsingItem();
            if (i >= CrossbowItem.getChargeDuration(activeStack) || CrossbowItem.isCharged(activeStack)) {
                this.mob.releaseUsingItem();
                this.crossbowState = ImprovedCrossbowGoal.CrossbowState.CHARGED;
                this.wait = 20 + this.mob.getRandom().nextInt(20);
                if (mob.getOffhandItem().getItem() instanceof FireworkRocketItem) {
                    mob.startUsingItem(Hand.OFF_HAND);
                }
                this.mob.setChargingCrossbow(false);
            }
        } else if (this.crossbowState == ImprovedCrossbowGoal.CrossbowState.CHARGED) {
            --this.wait;
            if (this.wait == 0) {
                this.crossbowState = ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK;
            }
        } else if (this.crossbowState == ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK && canSeeEnemy) {
            this.performRangedAttack(target != null ? target.position() : this.getTargetBlockPos());
            CrossbowItem.setCharged(this.mob.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem)), false);
            this.crossbowState = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
        }
    }

    private boolean canSeePos(Vector3d pos, boolean checkEntities) {
        this.mob.level.getProfiler().push("canSee");
        Vector3d seerPos = new Vector3d(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        boolean canSee = this.mob.level.clip(new RayTraceContext(seerPos, pos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this.mob)).getType() == RayTraceResult.Type.MISS;
        if (checkEntities) {
            Vector3d lookVec = new Vector3d(pos.x - seerPos.x, pos.y - seerPos.y, pos.z - seerPos.z).normalize();
            AxisAlignedBB axisalignedbb = this.mob.getBoundingBox().expandTowards(lookVec.scale(16));
            EntityRayTraceResult result = ProjectileHelper.getEntityHitResult(this.mob.level, this.mob, seerPos, pos, axisalignedbb, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());
            if (result != null)
                canSee = canSee && result.getType() == RayTraceResult.Type.MISS;
        }
        this.mob.level.getProfiler().pop();
        return canSee;
    }

    private boolean isCrossbowUncharged() {
        return this.crossbowState == ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
    }

    private void performRangedAttack(Vector3d targetPos) {
        Hand hand = ProjectileHelper.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem);
        ItemStack weapon = this.mob.getItemInHand(hand);
        T shooter = this.mob;
        World world = this.mob.level;
        if (shooter.isHolding(item -> item instanceof CrossbowItem)) {
            performShooting(world, shooter, targetPos, hand, weapon);
        }
        shooter.onCrossbowAttackPerformed();
    }

    private void performShooting(World world, T shooter, Vector3d targetPos, Hand hand, ItemStack weapon) {
        List<ItemStack> projectiles = CrossbowItem.getChargedProjectiles(weapon);
        float[] soundPitches = CrossbowItem.getShotPitches(world.getRandom());

        for (int i = 0; i < Math.min(projectiles.size(), 3); i++) {
            ItemStack projectileStack = projectiles.get(i);
            if (!projectileStack.isEmpty()) {
                if (!world.isClientSide()) {
                    ProjectileEntity shot = shootProjectile(world, shooter, targetPos, hand, weapon, projectileStack, soundPitches[i], i == 0 ? 0.0F : i == 1 ? -10.0F : 10.0F);
                    if (i == 0) {
                        ((IDataManager) shot).setValue(SRDataProcessors.CROSSBOW_OWNER, Optional.of(this.mob.getUUID()));
                        this.setProjectile(shot);
                    }
                }
            }
        }
        CrossbowItem.onCrossbowShot(world, shooter, weapon);
    }

    private ProjectileEntity shootProjectile(World world, T shooter, Vector3d targetPos, Hand hand, ItemStack weapon, ItemStack projectileStack, float soundPitch, float yaw) {
        boolean isFirework = projectileStack.getItem() == Items.FIREWORK_ROCKET;
        ProjectileEntity projectile = isFirework ? new FireworkRocketEntity(world, projectileStack, shooter, shooter.getX(), shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true) : CrossbowItem.getArrow(world, shooter, weapon, projectileStack);
        this.shootCrossbowProjectile(shooter, targetPos, projectile, yaw, isFirework);
        weapon.hurtAndBreak(isFirework ? 3 : 1, shooter, (p_220017_1_) -> {
            p_220017_1_.broadcastBreakEvent(hand);
        });
        world.addFreshEntity(projectile);
        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        return projectile;
    }

    private void shootCrossbowProjectile(T shooter, Vector3d targetPos, ProjectileEntity projectile, float yaw, boolean isFirework) {
        double x = (targetPos.x() + (shooter.getTarget() == null ? 0.5D : 0.0D)) - shooter.getX();
        double z = (targetPos.z() + (shooter.getTarget() == null ? 0.5D : 0.0D)) - shooter.getZ();
        double distance = MathHelper.sqrt(x * x + z * z);
        double y = isFirework ? (targetPos.y() + 0.5D) - projectile.getY() : getYIntoBox(targetPos.y()) - projectile.getY() + distance * (double) 0.2F;
        Vector3f vector3f = shooter.getProjectileShotVector(shooter, new Vector3d(x, y, z), yaw);
        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 1.6F, (float) (14 - shooter.level.getDifficulty().getId() * 4));
        shooter.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private double getYIntoBox(double y) {
        LivingEntity target = this.mob.getTarget();
        return y + (target != null ? target.getBbHeight() * 0.3333333333333333D : -0.5D);
    }

    @Nullable
    public ProjectileEntity getProjectile() {
        if (this.projectileUUID != null && this.mob.level instanceof ServerWorld)
            return (ProjectileEntity) ((ServerWorld) this.mob.level).getEntity(this.projectileUUID);
        return null;
    }

    public void setProjectile(@Nullable ProjectileEntity projectile) {
        this.projectileUUID = projectile == null ? null : projectile.getUUID();
    }

    enum CrossbowState {
        UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK
    }
}