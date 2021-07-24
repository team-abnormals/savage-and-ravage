package com.minecraftabnormals.savageandravage.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class VillagerArmorModel<T extends LivingEntity> extends BipedModel<T> {
	public VillagerArmorModel(float modelSize) {
		super(modelSize);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);
		this.head.setPos(0.0F, 0.0F, 0.0F);
	}
}
