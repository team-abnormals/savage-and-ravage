package com.minecraftabnormals.savageandravage.common.entity;

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

import java.util.Map;

public class ExecutionerEntity extends VindicatorEntity {
	//TODO fix attack time
	public ExecutionerEntity(EntityType<? extends VindicatorEntity> entity, World world) {
		super(entity, world);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		if (this.getCurrentRaid() == null) {
			this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(SRItems.CLEAVER_OF_BEHEADING.get()));
			this.handDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0.5F;
		}
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.30F)
				.add(Attributes.FOLLOW_RANGE, 14.0D)
				.add(Attributes.MAX_HEALTH, 35.0D)
				.add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.ARMOR, 3.0D);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
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

		this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
		this.handDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0.5F;
	}
}
