package com.farcr.savageandravage.client.model;

import com.farcr.savageandravage.common.entity.GrieferEntity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class GrieferEntityArmorModel extends BipedModel<GrieferEntity> 
{
	public GrieferEntityArmorModel(float modelSize) {
		super(modelSize);
	    this.bipedHead = new ModelRenderer(this, 0, 0);
	    this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);
	    this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
	}
}
