package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.savageandravage.client.model.MaskOfDishonestyModel;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.other.SRDataProcessors;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, value = Dist.CLIENT)
public class MaskOfDishonestyItem extends ArmorItem {
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.TOTEM_OF_UNDYING);

	public MaskOfDishonestyItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return SavageAndRavage.MOD_ID + ":textures/models/armor/mask_of_dishonesty.png";
	}

	@SuppressWarnings("unchecked")
	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
		return (A) new MaskOfDishonestyModel<>();
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}

	@SubscribeEvent
	public static void livingRender(RenderLivingEvent.Pre<?, ?> event) {
		if (((IDataManager) event.getEntity()).getValue(SRDataProcessors.INVISIBLE_DUE_TO_MASK))
			event.setCanceled(true);
	}
}