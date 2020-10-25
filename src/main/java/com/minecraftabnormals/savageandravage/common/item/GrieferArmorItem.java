package com.minecraftabnormals.savageandravage.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.minecraftabnormals.savageandravage.client.model.GrieferArmorModel;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SRAttributes;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.teamabnormals.abnormals_core.core.utils.ItemStackUtils;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.LazyValue;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;
import java.util.function.Supplier;

public class GrieferArmorItem extends ArmorItem {

    private static final UUID MODIFIER = UUID.fromString("B77CAE62-FCEB-40F9-BD4D-A15F8F44CB91");

    private final LazyValue<Multimap<Attribute, AttributeModifier>> attributes;

    public GrieferArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
        super(material, slot, properties);
        this.attributes = new LazyValue<>(() -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot));
            builder.put(SRAttributes.EXPLOSIVE_DAMAGE_REDUCTION.get(), new AttributeModifier(MODIFIER, "Blast proof", BlastProofArmorType.slotToType(slot).getReductionAmount(), AttributeModifier.Operation.ADDITION));
            return builder.build();
        });
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return SavageAndRavage.MODID + ":textures/models/armor/griefer_armor.png";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
        return GrieferArmorModel.getModel(armorSlot, entityLiving);
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        Supplier<Item> item = () -> Items.GOLDEN_BOOTS;
        if (this.getItem() == SRItems.GRIEFER_HELMET.get()) item = () -> Items.GOLDEN_BOOTS;
        if (this.getItem() == SRItems.GRIEFER_CHESTPLATE.get()) item = SRItems.GRIEFER_HELMET;
        if (this.getItem() == SRItems.GRIEFER_LEGGINGS.get()) item = SRItems.GRIEFER_CHESTPLATE;
        if (this.getItem() == SRItems.GRIEFER_BOOTS.get()) item = SRItems.GRIEFER_LEGGINGS;

        if (ItemStackUtils.isInGroup(this.asItem(), group)) {
            int targetIndex = ItemStackUtils.findIndexOfItem(item.get(), items);
            if (targetIndex != -1) {
                items.add(targetIndex + 1, new ItemStack(this));
            } else {
                super.fillItemGroup(group, items);
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == this.slot ? this.attributes.getValue() : super.getAttributeModifiers(equipmentSlot);
    }
}