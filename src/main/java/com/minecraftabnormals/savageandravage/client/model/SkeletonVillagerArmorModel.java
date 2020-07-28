package com.minecraftabnormals.savageandravage.client.model;

import com.minecraftabnormals.savageandravage.common.entity.SkeletonVillagerEntity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

//should we delete this and merge this with the model for griefer armor?
public class SkeletonVillagerArmorModel extends BipedModel<SkeletonVillagerEntity> {
	
	public SkeletonVillagerArmorModel(float modelSize) {
		super(modelSize);
	    this.bipedHead = new ModelRenderer(this, 0, 0);
	    this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);
	    this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
	}
}
