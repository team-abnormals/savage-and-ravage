package com.farcr.savageandravage.common.item;

import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.registry.SRItems;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum SRArmorMaterial implements IArmorMaterial {
    GRIEFER("griefer", 15, new int[]{2, 5, 6, 2}, 9, SRItems.BLAST_PROOF_PLATING.get(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f);

    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private String name;
    private SoundEvent equipSound;
    private int durability, enchantability;
    private int[] damageReductionAmounts;
    private Item repairItem;
    private float toughness;

    SRArmorMaterial(String name, int durability, int[] damageReductionAmounts, int enchantability, Item repairItem, SoundEvent equipSound, float toughness) {
        this.name = name;
        this.equipSound = equipSound;
        this.durability = durability;
        this.enchantability = enchantability;
        this.repairItem = repairItem;
        this.damageReductionAmounts = damageReductionAmounts;
        this.toughness = toughness;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slot) {
        return this.damageReductionAmounts[slot.getIndex()];
    }

    @Override
    public int getDurability(EquipmentSlotType slot) {
        return (MAX_DAMAGE_ARRAY[slot.getIndex()] * this.durability) + 200;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(this.repairItem);
    }

    @Override
    public String getName() {
        return SavageAndRavage.MODID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

	@Override
	public float func_230304_f_() {
		return 0;
	}
}

