package com.minecraftabnormals.savageandravage.common.entity.goals;

import com.minecraftabnormals.savageandravage.core.mixin.RaiderAccessor;
import com.minecraftabnormals.savageandravage.core.other.SRDataProcessors;
import com.mojang.math.Vector3f;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class ImprovedCrossbowGoal<T extends PathfinderMob & RangedAttackMob & CrossbowAttackMob> extends Goal {
	private final T mob;
	private ImprovedCrossbowGoal.CrossbowState crossbowState = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
	private final double speedChanger;
	private final float radiusSq;
	private int seeTime;
	private int wait;
	private final double blocksUntilBackupSq;
	private int ticksTillSearch;
	private int practisingTicks;
	private BlockPos blockPos;
	private Vec3 blockPosVector;
	private Vec3 blockPosVectorCentred;

	public ImprovedCrossbowGoal(T mob, double speedChanger, float radius, double blocksUntilBackup) {
		this.mob = mob;
		this.speedChanger = speedChanger;
		this.radiusSq = radius * radius;
		this.blocksUntilBackupSq = blocksUntilBackup;
		this.ticksTillSearch = this.ticksTillSearch(this.mob);
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
			if (this.hasAttackTarget())
				return true;
			else if (this.noCelebrationAndNoRaid()) {
				if (this.practisingTicks > 0)
					return true;
				else if (this.ticksTillSearch > 0) {
					this.ticksTillSearch--;
					return false;
				} else {
					this.ticksTillSearch = this.ticksTillSearch(this.mob);
					return this.findNearestBlock();
				}
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.hasAttackTarget() || (this.practisingTicks > 0 && this.isValidTarget(this.mob.level, this.blockPos) && this.noCelebrationAndNoRaid());
	}

	@Override
	public void start() {
		super.start();
		this.practisingTicks = 200 + this.mob.getRandom().nextInt(160);
		if (this.blockPos != null) {
			this.blockPosVector = new Vec3(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
			this.blockPosVectorCentred = this.blockPosVector.add(0.5D, 0.5D, 0.5D);
		}
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
		this.practisingTicks = 0;
	}

	@Override
	public void tick() {
		LivingEntity target = this.mob.getTarget();

		if (this.practisingTicks > 0)
			this.practisingTicks--;

		if (TrackedDataManager.INSTANCE.getValue(this.mob, SRDataProcessors.TARGET_HIT)) {
			TrackedDataManager.INSTANCE.setValue(this.mob, SRDataProcessors.CELEBRATION_TIME, 100 + this.mob.getRandom().nextInt(100));
			TrackedDataManager.INSTANCE.setValue(this.mob, SRDataProcessors.TARGET_HIT, false);
			this.practisingTicks = 0;
		}

		if (target == null && practisingTicks <= 0)
			return;

		boolean canSeeEnemy = target != null ? this.mob.getSensing().hasLineOfSight(target) : this.canSeeTargetBlock();
		if (canSeeEnemy)
			++this.seeTime;
		else {
			if (target == null) {
				this.practisingTicks = 0;
				return;
			}
			this.seeTime = 0;
		}

		this.mob.setAggressive(true);

		double distanceSq = target != null ? this.mob.distanceToSqr(target) : this.mob.distanceToSqr(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
		double distance = Mth.sqrt((float) distanceSq);
		int distanceCoefficient = target == null ? 2 : 1;
		if (distance <= (blocksUntilBackupSq * distanceCoefficient) && (!(target instanceof AbstractVillager) || this.hasFirework())) {
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
				this.mob.getNavigation().moveTo(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ(), speedChange);
		} else this.mob.getNavigation().stop();

		if (target != null)
			this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
		else
			this.mob.getLookControl().setLookAt(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ(), 30.0F, 30.0F);

		if (this.crossbowState == ImprovedCrossbowGoal.CrossbowState.UNCHARGED && !CrossbowItem.isCharged(activeStack)) {
			if (canSeeEnemy) {
				this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
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
					mob.startUsingItem(InteractionHand.OFF_HAND);
				}
				this.mob.setChargingCrossbow(false);
			}
		} else if (this.crossbowState == ImprovedCrossbowGoal.CrossbowState.CHARGED) {
			--this.wait;
			if (this.wait == 0) {
				this.crossbowState = ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK;
			}
		} else if (this.crossbowState == ImprovedCrossbowGoal.CrossbowState.READY_TO_ATTACK && canSeeEnemy) {
			this.performRangedAttack(target != null ? target.position() : this.blockPosVector.add(0.5D, 0.0D, 0.5D));
			CrossbowItem.setCharged(this.mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem)), false);
			this.crossbowState = ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
		}
	}

	private boolean noCelebrationAndNoRaid() {
		if (this.mob instanceof Raider)
			return ((ServerLevel) this.mob.level).getRaidAt(this.mob.blockPosition()) == null && !(this.mob.getEntityData().get(((RaiderAccessor) this.mob).getIsCelebrating()));
		return true;
	}

	private boolean isWalkable() {
		PathNavigation pathnavigator = this.mob.getNavigation();
		NodeEvaluator nodeprocessor = pathnavigator.getNodeEvaluator();
		return nodeprocessor.getBlockPathType(this.mob.level, Mth.floor(this.mob.getX() + 1.0D), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + 1.0D)) == BlockPathTypes.WALKABLE;
	}

	protected boolean findNearestBlock() {
		int hDiameter = 16;
		int vDiameter = 8;
		BlockPos pos = this.mob.blockPosition();
		BlockPos.MutableBlockPos searchPos = new BlockPos.MutableBlockPos();

		for (int y = 0; y <= vDiameter; y = y > 0 ? -y : 1 - y) {
			for (int hDist = 0; hDist < hDiameter; hDist++) {
				for (int x = 0; x <= hDist; x = x > 0 ? -x : 1 - x) {
					for (int z =
						 x < hDist && x > -hDist ? hDist : 0; z <= hDist; z = z > 0 ? -z : 1 - z) {
						searchPos.setWithOffset(pos, x, y - 1, z);
						if (this.mob.isWithinRestriction(searchPos) && this.isValidTarget(this.mob.level, searchPos)) {
							this.blockPos = searchPos;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	protected int ticksTillSearch(PathfinderMob creature) {
		return 1000 + creature.getRandom().nextInt(1200);
	}

	protected boolean isValidTarget(LevelReader world, BlockPos pos) {
		return pos != null && world.getBlockState(pos).is(Blocks.TARGET);
	}

	private boolean canSeeTargetBlock() {
		this.mob.level.getProfiler().push("canSee");
		Vec3 mobPos = new Vec3(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
		BlockHitResult result = this.mob.level.clip(new ClipContext(mobPos, this.blockPosVectorCentred, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));
		boolean canSee = result.getBlockPos().equals(this.blockPos) || result.getType() == HitResult.Type.MISS;
		this.mob.level.getProfiler().pop();
		return canSee;
	}

	private boolean isCrossbowUncharged() {
		return this.crossbowState == ImprovedCrossbowGoal.CrossbowState.UNCHARGED;
	}

	private boolean hasFirework() {
		if (this.mob.getOffhandItem().getItem() == Items.FIREWORK_ROCKET) return true;
		else
			for (ItemStack projectileStack : CrossbowItem.getChargedProjectiles(this.mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem)))) {
				if (projectileStack.getItem() == Items.FIREWORK_ROCKET) return true;
			}
		return false;
	}

	private void performRangedAttack(Vec3 targetPos) {
		InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem);
		ItemStack weapon = this.mob.getItemInHand(hand);
		if (this.mob.isHolding(stack -> stack.getItem() instanceof CrossbowItem)) {
			performShooting(this.mob.level, this.mob, targetPos, hand, weapon);
		}
		this.mob.onCrossbowAttackPerformed();
	}

	private void performShooting(Level world, T shooter, Vec3 targetPos, InteractionHand hand, ItemStack weapon) {
		List<ItemStack> projectiles = CrossbowItem.getChargedProjectiles(weapon);
		float[] soundPitches = CrossbowItem.getShotPitches(world.getRandom());

		for (int i = 0; i < Math.min(projectiles.size(), 3); i++) {
			ItemStack projectileStack = projectiles.get(i);
			if (!projectileStack.isEmpty()) {
				if (!world.isClientSide()) {
					float yaw = i == 0 ? 0.0F : i == 1 ? -10.0F : 10.0F;
					if (this.hasAttackTarget() && !(projectileStack.getItem() == Items.FIREWORK_ROCKET))
						CrossbowItem.shootProjectile(world, shooter, hand, weapon, projectileStack, soundPitches[i], false, 1.6F, (float) (14 - shooter.level.getDifficulty().getId() * 4), yaw);
					else {
						Projectile shot = shootProjectile(world, shooter, targetPos, hand, weapon, projectileStack, soundPitches[i], yaw);
						if (i == 0)
							TrackedDataManager.INSTANCE.setValue(shot, SRDataProcessors.CROSSBOW_OWNER, Optional.of(this.mob.getUUID()));
					}
				}
			}
		}
		CrossbowItem.onCrossbowShot(world, shooter, weapon);
	}

	private Projectile shootProjectile(Level world, T shooter, Vec3 targetPos, InteractionHand hand, ItemStack weapon, ItemStack projectileStack, float soundPitch, float yaw) {
		boolean isFirework = projectileStack.getItem() == Items.FIREWORK_ROCKET;
		Projectile projectile = isFirework ? new FireworkRocketEntity(world, projectileStack, shooter, shooter.getX(), shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true) : CrossbowItem.getArrow(world, shooter, weapon, projectileStack);
		this.shootCrossbowProjectile(shooter, targetPos, projectile, yaw, isFirework);
		weapon.hurtAndBreak(isFirework ? 3 : 1, shooter, (s) -> s.broadcastBreakEvent(hand));
		world.addFreshEntity(projectile);
		world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
		return projectile;
	}

	private void shootCrossbowProjectile(T shooter, Vec3 targetPos, Projectile projectile, float yaw, boolean isFirework) {
		double x = targetPos.x() - shooter.getX();
		double z = targetPos.z() - shooter.getZ();
		double distance = Mth.sqrt((float) (x * x + z * z));
		double y = isFirework ? (targetPos.y() + 1.0D) - projectile.getY() : getYIntoBox(targetPos.y()) - projectile.getY() + distance * (double) 0.2F;
		Vector3f vector3f = shooter.getProjectileShotVector(shooter, new Vec3(x, y, z), yaw);
		projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 1.6F, (float) (14 - shooter.level.getDifficulty().getId() * 4));
		shooter.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
	}

	private double getYIntoBox(double y) {
		LivingEntity target = this.mob.getTarget();
		return y + (target != null ? target.getBbHeight() * 0.3333333333333333D : -0.5D);
	}

	enum CrossbowState {
		UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK
	}
}