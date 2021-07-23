package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * ModelExecutioner - MCVinnyQ Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ExecutionerModel extends BipedModel<VindicatorEntity> {
	public ModelRenderer closedArms;
	public ModelRenderer closedRightArm;
	public ModelRenderer closedLeftArm;
	public ModelRenderer nose;

	public ExecutionerModel(float size) {
		super(RenderType::entityTranslucent, size, 0.0F, 64, 64);

		head = new ModelRenderer(this);
		head.setPos(0.0F, 0.0F, 0.0F);
		head.texOffs(0, 46).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		head.texOffs(32, 28).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.5F, false);

		nose = new ModelRenderer(this);
		nose.setPos(0.0F, -3.0F, -4.0F);
		head.addChild(nose);
		nose.texOffs(24, 46).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		body.texOffs(0, 28).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
		body.texOffs(0, 0).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 18.0F, 6.0F, 0.5F, false);

		closedArms = new ModelRenderer(this);
		closedArms.setPos(0.0F, 4.0F, -1.0F);
		setRotationAngle(closedArms, -0.7854F, 0.0F, 0.0F);
		closedArms.texOffs(32, 40).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);

		closedRightArm = new ModelRenderer(this);
		closedRightArm.setPos(0.0F, 0.0F, 0.0F);
		closedArms.addChild(closedRightArm);
		closedRightArm.texOffs(48, 12).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);
		closedRightArm.texOffs(40, 0).addBox(4.0F, -3.0F, -2.5F, 6.0F, 7.0F, 5.0F, 0.0F, true);

		closedLeftArm = new ModelRenderer(this);
		closedLeftArm.setPos(0.0F, 0.0F, 0.0F);
		closedArms.addChild(closedLeftArm);
		closedLeftArm.texOffs(48, 12).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
		closedLeftArm.texOffs(40, 0).addBox(-10.0F, -3.0F, -2.5F, 6.0F, 7.0F, 5.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(4.0F, 2.0F, 0.0F);
		setRotationAngle(rightArm, 0.0F, 0.0F, -0.0436F);
		rightArm.texOffs(48, 48).addBox(-3.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		rightArm.texOffs(40, 0).addBox(-4.5F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, 0.0F, true);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(-4.0F, 2.0F, 0.0F);
		setRotationAngle(leftArm, 0.0F, 0.0F, 0.0436F);
		leftArm.texOffs(48, 48).addBox(-0.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		leftArm.texOffs(40, 0).addBox(-1.5F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(1.9F, 12.0F, 0.0F);
		rightLeg.texOffs(32, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		rightLeg.texOffs(30, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(-1.9F, 12.0F, 0.0F);
		leftLeg.texOffs(32, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		leftLeg.texOffs(30, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
	}

	protected Iterable<ModelRenderer> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.closedArms));
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(VindicatorEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		AbstractIllagerEntity.ArmPose illagerArmPose = entityIn.getArmPose();
		if (illagerArmPose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (!entityIn.getMainHandItem().isEmpty())
				ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
		}
		boolean isCrossed = illagerArmPose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.closedArms.visible = isCrossed;
		this.leftArm.visible = !isCrossed;
		this.rightArm.visible = !isCrossed;
		this.hat.visible = false;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
