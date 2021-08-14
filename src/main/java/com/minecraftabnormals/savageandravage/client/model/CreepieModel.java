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
	public ModelRenderer sprout;
	public ModelRenderer body;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;

	public CreepieModel() {
		this(0.0F);
	}

	public CreepieModel(float scaleIncrease) {
		this.texWidth = 64;
		this.texHeight = 32;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 6.0F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 2.0F + scaleIncrease);
		this.sprout = new ModelRenderer(this, 48, 16);
		this.sprout.setPos(0.0F, 6.0F, 0.0F);
		this.head.addChild(this.sprout);
		this.sprout.addBox(0.0F, -24.0F, -4.0F, 0.0F, 8.0F, 8.0F, scaleIncrease);
		this.sprout.addBox(-4.0F, -24.0F, 0.0F, 8.0F, 8.0F, 0.0F, scaleIncrease);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.setPos(0.0F, 6.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scaleIncrease);
		this.leg1 = new ModelRenderer(this, 0, 16);
		this.leg1.setPos(2.0F, 18.0F, 4.0F);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, scaleIncrease);
		this.leg2 = new ModelRenderer(this, 0, 16);
		this.leg2.setPos(-2.0F, 18.0F, 4.0F);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, scaleIncrease);
		this.leg3 = new ModelRenderer(this, 0, 16);
		this.leg3.setPos(2.0F, 18.0F, -4.0F);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, scaleIncrease);
		this.leg4 = new ModelRenderer(this, 0, 16);
		this.leg4.setPos(-2.0F, 18.0F, -4.0F);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, scaleIncrease);
	}

	@Override
	public void setupAnim(CreepieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * 0.017453292F;
		this.head.xRot = headPitch * 0.017453292F;
		this.leg1.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.xRot = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg3.xRot = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg4.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.leg4, this.head, this.body, this.leg1, this.leg3, this.leg2);
	}
}

