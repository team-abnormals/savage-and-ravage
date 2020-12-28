package com.minecraftabnormals.savageandravage.common.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.IStringSerializable;

public enum BlastProofArmorType implements IStringSerializable {
	HEAD("head", EquipmentSlotType.HEAD, 25),
	CHEST("chest", EquipmentSlotType.CHEST, 30),
	LEGS("legs", EquipmentSlotType.LEGS, 25),
	FEET("feet", EquipmentSlotType.FEET, 20);

	private final String name;
	private final EquipmentSlotType slot;
	private final int reduction;

	BlastProofArmorType(String name, EquipmentSlotType slot, int reduction) {
		this.name = name;
		this.slot = slot;
		this.reduction = reduction;
	}

	public EquipmentSlotType getSlot() {
		return this.slot;
	}

	public static BlastProofArmorType slotToType(EquipmentSlotType slot) {
		for (BlastProofArmorType type : values())
			if (type.slot == slot)
				return type;
		return HEAD;
	}

	public float getReductionAmount() {
		return this.reduction * 0.01F;
	}

	@Override
	public String getString() {
		return this.name;
	}
}