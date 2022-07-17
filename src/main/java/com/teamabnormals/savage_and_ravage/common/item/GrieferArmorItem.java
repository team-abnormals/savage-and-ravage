package com.teamabnormals.savage_and_ravage.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.savage_and_ravage.client.model.GrieferArmorModel;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.registry.SRAttributes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.NonNullList;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.UUID;
import java.util.function.Consumer;

public class GrieferArmorItem extends ArmorItem {
	private static final UUID MODIFIER = UUID.fromString("B77CAE62-FCEB-40F9-BD4D-A15F8F44CB91");
	private final LazyLoadedValue<Multimap<Attribute, AttributeModifier>> attributes;
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.GOLDEN_BOOTS);

	public GrieferArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
		this.attributes = new LazyLoadedValue<>(() -> {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getDefaultAttributeModifiers(slot));
			builder.put(SRAttributes.EXPLOSIVE_DAMAGE_REDUCTION.get(), new AttributeModifier(MODIFIER, "Blast proof", BlastProofArmorType.slotToType(slot).getReductionAmount(), AttributeModifier.Operation.ADDITION));
			return builder.build();
		});
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return SavageAndRavage.MOD_ID + ":textures/models/armor/griefer_armor.png";
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Override
			public HumanoidModel<?> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
				return GrieferArmorModel.getModel(slot, entity);
			}
		});
	}

	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		return equipmentSlot == this.slot ? this.attributes.get() : super.getDefaultAttributeModifiers(equipmentSlot);
	}
}