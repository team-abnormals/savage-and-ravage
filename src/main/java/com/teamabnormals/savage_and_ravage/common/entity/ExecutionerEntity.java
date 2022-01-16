package com.teamabnormals.savage_and_ravage.common.entity;

import com.google.common.collect.Maps;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class ExecutionerEntity extends Vindicator {
	//TODO fix attack time
	public ExecutionerEntity(EntityType<? extends Vindicator> entity, Level world) {
		super(entity, world);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		if (this.getCurrentRaid() == null) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(SRItems.CLEAVER_OF_BEHEADING.get()));
			this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 0.5F;
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.availableGoals.stream().map(g -> g.goal).filter(goal -> goal instanceof MeleeAttackGoal).findFirst().ifPresent(goal -> {
			this.goalSelector.removeGoal(goal);
			this.goalSelector.addGoal(4, new AttackGoal(this, 1.0D));
		});

	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.30F)
				.add(Attributes.FOLLOW_RANGE, 14.0D)
				.add(Attributes.MAX_HEALTH, 35.0D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.ARMOR, 3.0D);
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(SRItems.EXECUTIONER_SPAWN_EGG.get());
	}

	@Override
	public void applyRaidBuffs(int wave, boolean p_213660_2_) {
		ItemStack itemstack = new ItemStack(SRItems.CLEAVER_OF_BEHEADING.get());
		Raid raid = this.getCurrentRaid();
		if (raid == null)
			return;
		int i = 1;
		if (wave > raid.getNumGroups(Difficulty.NORMAL)) {
			i = 2;
		}

		boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
		if (flag) {
			Map<Enchantment, Integer> map = Maps.newHashMap();
			map.put(Enchantments.SHARPNESS, i);
			EnchantmentHelper.setEnchantments(map, itemstack);
		}

		this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
		this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 0.5F;
	}

	static class AttackGoal extends MeleeAttackGoal {
		public AttackGoal(Vindicator p_i50577_2_, double speedModifier) {
			super(p_i50577_2_, speedModifier, false);
		}

		protected double getAttackReachSqr(LivingEntity p_179512_1_) {
			if (this.mob.getVehicle() instanceof Ravager) {
				float f = this.mob.getVehicle().getBbWidth() - 0.1F;
				return f * 2.0F * f * 2.0F + p_179512_1_.getBbWidth();
			} else {
				return super.getAttackReachSqr(p_179512_1_);
			}
		}

		@Override
		protected void resetAttackCooldown() {
			this.ticksUntilNextAttack = this.getAttackInterval();
		}

		@Override
		protected int getAttackInterval() {
			return 50;
		}
	}
}
