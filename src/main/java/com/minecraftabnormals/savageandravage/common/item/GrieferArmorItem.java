package com.minecraftabnormals.savageandravage.common.item;

import com.google.common.base.Supplier;
import com.teamabnormals.abnormals_core.core.utils.ItemStackUtils;
import com.minecraftabnormals.savageandravage.client.model.GrieferArmorModel;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GrieferArmorItem extends ArmorItem {
	private EquipmentSlotType slot;

	public GrieferArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
		super(material, slot, properties);
		this.slot = slot;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return SavageAndRavage.MODID + ":textures/models/armor/griefer_armor.png";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
		return (A) new GrieferArmorModel(1.0F, slot, entityLiving);
	}
	
	@Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		Supplier<Item> item = () -> Items.GOLDEN_BOOTS;
		if (this.getItem() == SRItems.GRIEFER_HELMET.get()) item = () -> Items.GOLDEN_BOOTS;
		if (this.getItem() == SRItems.GRIEFER_CHESTPLATE.get()) item = () -> SRItems.GRIEFER_HELMET.get();
		if (this.getItem() == SRItems.GRIEFER_LEGGINGS.get()) item = () -> SRItems.GRIEFER_CHESTPLATE.get();
		if (this.getItem() == SRItems.GRIEFER_BOOTS.get()) item = () -> SRItems.GRIEFER_LEGGINGS.get();
		
		if(ItemStackUtils.isInGroup(this.asItem(), group)) {
			int targetIndex = ItemStackUtils.findIndexOfItem(item.get(), items);
			if(targetIndex != -1) {
				items.add(targetIndex + 1, new ItemStack(this));
			} else {
				super.fillItemGroup(group, items);
			}
		}
	}
}