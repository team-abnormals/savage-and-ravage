package com.minecraftabnormals.savageandravage.common.entity;

import java.util.Map;

import com.google.common.collect.Maps;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

public class ExecutionerEntity extends VindicatorEntity {

	public ExecutionerEntity(EntityType<? extends VindicatorEntity> entity, World world) {
		super(entity, world);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (this.getRaid() == null) {
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(SRItems.CLEAVER_OF_BEHEADING.get()));
			this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0.5F;
		}
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, (double) 0.30F)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 14.0D)
				.createMutableAttribute(Attributes.MAX_HEALTH, 35.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D)
				.createMutableAttribute(Attributes.ARMOR, 3.0D);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(SRItems.EXECUTIONER_SPAWN_EGG.get());
	}

	@Override
	public void applyWaveBonus(int p_213660_1_, boolean p_213660_2_) {
		ItemStack itemstack = new ItemStack(SRItems.CLEAVER_OF_BEHEADING.get());
		Raid raid = this.getRaid();
		int i = 1;
		if (p_213660_1_ > raid.getWaves(Difficulty.NORMAL)) {
			i = 2;
		}

		boolean flag = this.rand.nextFloat() <= raid.getEnchantOdds();
		if (flag) {
			Map<Enchantment, Integer> map = Maps.newHashMap();
			map.put(Enchantments.SHARPNESS, i);
			EnchantmentHelper.setEnchantments(map, itemstack);
		}

		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
	}
}
