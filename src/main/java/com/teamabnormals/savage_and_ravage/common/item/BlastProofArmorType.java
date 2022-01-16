package com.teamabnormals.savage_and_ravage.common.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.util.StringRepresentable;

public enum BlastProofArmorType implements StringRepresentable {
	HEAD("head", EquipmentSlot.HEAD, 25),
	CHEST("chest", EquipmentSlot.CHEST, 30),
	LEGS("legs", EquipmentSlot.LEGS, 25),
	FEET("feet", EquipmentSlot.FEET, 20);

	private final String name;
	private final EquipmentSlot slot;
	private final int reduction;

	BlastProofArmorType(String name, EquipmentSlot slot, int reduction) {
		this.name = name;
		this.slot = slot;
		this.reduction = reduction;
	}

	public EquipmentSlot getSlot() {
		return this.slot;
	}

	public static BlastProofArmorType slotToType(EquipmentSlot slot) {
		for (BlastProofArmorType type : values())
			if (type.slot == slot)
				return type;
		return HEAD;
	}

	public float getReductionAmount() {
		return this.reduction * 0.01F;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}