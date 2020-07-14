package com.farcr.savageandravage.client.render;

import com.farcr.savageandravage.client.model.GrieferEntityArmorModel;
import com.farcr.savageandravage.client.model.GrieferModel;
import com.farcr.savageandravage.common.entity.GrieferEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

public class GrieferRenderer extends BipedRenderer<GrieferEntity, GrieferModel> {
	private static final ResourceLocation GRIEFER_TEXTURE = new ResourceLocation("savageandravage:textures/entity/griefer.png");
	private static final ResourceLocation APESHIT_MODE_TEXTURE = new ResourceLocation("savageandravage:textures/entity/griefer_melee.png");

	public GrieferRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new GrieferModel(0), 0.5f);
		this.addLayer(new BipedArmorLayer<GrieferEntity, GrieferModel, GrieferEntityArmorModel>(this, new GrieferEntityArmorModel(0.5F), new GrieferEntityArmorModel(1.0F)));
	}
	
    public ResourceLocation getEntityTexture(GrieferEntity entity) 
    {
	  return entity.isApeshit() ? APESHIT_MODE_TEXTURE : GRIEFER_TEXTURE;
	}
}
