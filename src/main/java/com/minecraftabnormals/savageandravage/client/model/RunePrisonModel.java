package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.savageandravage.common.entity.RunePrisonEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class RunePrisonModel extends SegmentedModel<RunePrisonEntity> {
	public ModelRenderer plane;

	public RunePrisonModel() {
		this.texWidth = 42;
		this.texHeight = 42;
		this.plane = new ModelRenderer(this, 0, 0);
		this.plane.setPos(-8.0F, 24.0F, -8.0F);
		this.plane.addBox(-5.0F, -5.0F, -5.0F, 21, 0, 21, 0.0F);
	}

	@Override
	public void setupAnim(RunePrisonEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.plane);
	}
}
