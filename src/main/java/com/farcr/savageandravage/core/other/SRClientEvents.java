package com.farcr.savageandravage.core.other;

import java.util.List;

import com.farcr.savageandravage.common.item.GrieferArmorItem;
import com.farcr.savageandravage.core.SavageAndRavage;

import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = SavageAndRavage.MODID, value = Dist.CLIENT)
public class SRClientEvents {
	
	@SubscribeEvent
	public static void handleToolTip(ItemTooltipEvent event) {
		List<ITextComponent> tooltip = event.getToolTip();
		int index = 0;
		Item item = event.getItemStack().getItem();
		if (item instanceof GrieferArmorItem) {
			GrieferArmorItem armor = (GrieferArmorItem)item;
			for(int i = 0; i < tooltip.size(); i++) {
			    ITextComponent component = tooltip.get(i);
			    if(component instanceof TranslationTextComponent) {
			    	if(((TranslationTextComponent) component).getKey().equals("attribute.modifier.plus.0")) index = i;
			    }
			}
	    	tooltip.add(index + 1, new TranslationTextComponent("+" + armor.getReductionString() + "% ")
	    			.func_230529_a_(new TranslationTextComponent("attribute.name.grieferArmor.explosiveDamageReduction"))
	    			.func_240699_a_(TextFormatting.BLUE));
		}
		
	}	
}
