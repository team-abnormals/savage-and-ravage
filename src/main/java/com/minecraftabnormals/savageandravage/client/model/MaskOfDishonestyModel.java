package com.minecraftabnormals.savageandravage.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class MaskOfDishonestyModel<T extends LivingEntity> extends BipedModel<T> {
	public ModelRenderer nose;

	public MaskOfDishonestyModel() {
		super(1.0f, 0.0f, 32, 32);
		this.hat = new ModelRenderer(this, 0, 0);
		this.head.texOffs(0, 0);
		this.nose = new ModelRenderer(this, 0, 26);
		this.head.addChild(nose);
		this.nose.addBox(-1.0F, -2.5F, -9.25F, 2.0F, 2.0F, 4.0F, 0.25F);
	}
}
