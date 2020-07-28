package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

/**
 * CreepieModel - TallestEgg
 * Created using Tabula 7.1.0
 */
public class CreepieModel extends SegmentedModel<CreepieEntity> {
	public ModelRenderer head;
	public ModelRenderer body;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;

	public CreepieModel() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 2.0F);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.leg1 = new ModelRenderer(this, 0, 16);
		this.leg1.setRotationPoint(2.0F, 18.0F, 4.0F);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg2 = new ModelRenderer(this, 0, 16);
		this.leg2.setRotationPoint(-2.0F, 18.0F, 4.0F);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg3 = new ModelRenderer(this, 0, 16);
		this.leg3.setRotationPoint(2.0F, 18.0F, -4.0F);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg4 = new ModelRenderer(this, 0, 16);
		this.leg4.setRotationPoint(-2.0F, 18.0F, -4.0F);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
	}

	@Override
	public void setRotationAngles(CreepieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.rotateAngleY = netHeadYaw * 0.017453292F;
		this.head.rotateAngleX = headPitch * 0.017453292F;
		this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.leg4, this.head, this.body, this.leg1, this.leg3, this.leg2);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}

