package com.teamabnormals.savage_and_ravage.common.item;

import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.savage_and_ravage.client.model.MaskOfDishonestyModel;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRDataProcessors;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, value = Dist.CLIENT)
public class MaskOfDishonestyItem extends ArmorItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.TOTEM_OF_UNDYING);

	public MaskOfDishonestyItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return SavageAndRavage.MOD_ID + ":textures/models/armor/mask_of_dishonesty.png";
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Override
			public HumanoidModel<?> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> properties) {
				return new MaskOfDishonestyModel<>(MaskOfDishonestyModel.createArmorLayer().bakeRoot());
			}
		});
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}

	@SubscribeEvent
	public static void livingRender(RenderLivingEvent.Pre<?, ?> event) {
		if ((TrackedDataManager.INSTANCE.getValue(event.getEntity(), SRDataProcessors.INVISIBLE_DUE_TO_MASK)))
			event.setCanceled(true);
	}
}