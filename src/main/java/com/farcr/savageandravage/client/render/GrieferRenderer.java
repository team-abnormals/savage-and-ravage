package com.farcr.savageandravage.client.render;

import com.farcr.savageandravage.client.model.GrieferModel;
import com.farcr.savageandravage.common.entity.GrieferEntity;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

public class GrieferRenderer extends BipedRenderer<GrieferEntity, GrieferModel> {
	private static final ResourceLocation GRIEFER_TEXTURE = new ResourceLocation("savageandravage:textures/entity/griefer.png");

	public GrieferRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new GrieferModel(0), 0.5f);
		this.addLayer(new BipedArmorLayer(this, new BipedModel(0.5F), new BipedModel(1.0F)));
	}
	
    public ResourceLocation getEntityTexture(GrieferEntity entity) 
    {
	  return GRIEFER_TEXTURE;
	}
}
