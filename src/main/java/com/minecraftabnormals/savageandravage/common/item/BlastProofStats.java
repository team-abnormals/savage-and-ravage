package com.minecraftabnormals.savageandravage.common.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.IStringSerializable;

public enum BlastProofStats implements IStringSerializable {
    HEAD("head", EquipmentSlotType.HEAD, 25), 
    CHEST("chest", EquipmentSlotType.CHEST, 30), 
    LEGS("legs", EquipmentSlotType.LEGS, 25), 
    FEET("feet", EquipmentSlotType.FEET, 20);

    private final String name;
    private EquipmentSlotType slot;
    private int reduction;
    
    private BlastProofStats(String name, EquipmentSlotType slot, int reduction) {
        this.name = name;
        this.slot = slot;
        this.reduction = reduction;
    }
    
    public EquipmentSlotType getSlot() {
        return this.slot;
    }
    
    public static BlastProofStats slotToType(EquipmentSlotType slot) {
        switch(slot) {
        case CHEST:
            return BlastProofStats.CHEST;
        case FEET:
            return BlastProofStats.FEET;
        case HEAD:
            return BlastProofStats.HEAD;
        case LEGS:
            return BlastProofStats.LEGS;
        default:
            return null;
        }
    }

    public String getReductionString() {
        return String.valueOf(this.reduction);
    }

    public float getReductionAmount() {
        return this.reduction * 0.01F;
    }

    @Override
    public String getString() {
        return this.name;
    }
}