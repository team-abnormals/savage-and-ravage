package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.abnormals_core.core.api.AbnormalsArmorMaterial;
import com.minecraftabnormals.abnormals_core.core.api.AbnormalsItemTier;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;

public class SRTiers {
	public static final AbnormalsItemTier CLEAVER = new AbnormalsItemTier(0, 459, 0.0F, 7.5F, 10, () -> Ingredient.fromItems(Items.IRON_INGOT));
	public static final AbnormalsArmorMaterial GRIEFER = new AbnormalsArmorMaterial(new ResourceLocation(SavageAndRavage.MOD_ID, "griefer"), 15, new int[]{2, 5, 6, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, 0.0F, () -> Ingredient.fromItems(SRItems.BLAST_PROOF_PLATING.get()));
}
