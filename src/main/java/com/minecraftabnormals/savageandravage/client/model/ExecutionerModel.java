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
		super(RenderType::getEntityTranslucent, size, 0.0F, 64, 64);

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.setTextureOffset(0, 46).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		bipedHead.setTextureOffset(32, 28).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.5F, false);

		nose = new ModelRenderer(this);
		nose.setRotationPoint(0.0F, -3.0F, -4.0F);
		bipedHead.addChild(nose);
		nose.setTextureOffset(24, 46).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.setTextureOffset(0, 28).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
		bipedBody.setTextureOffset(0, 0).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 18.0F, 6.0F, 0.5F, false);

		closedArms = new ModelRenderer(this);
		closedArms.setRotationPoint(0.0F, 4.0F, -1.0F);
		setRotationAngle(closedArms, -0.7854F, 0.0F, 0.0F);
		closedArms.setTextureOffset(32, 40).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);

		closedRightArm = new ModelRenderer(this);
		closedRightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		closedArms.addChild(closedRightArm);
		closedRightArm.setTextureOffset(48, 12).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);
		closedRightArm.setTextureOffset(40, 0).addBox(4.0F, -3.0F, -2.5F, 6.0F, 7.0F, 5.0F, 0.0F, true);

		closedLeftArm = new ModelRenderer(this);
		closedLeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		closedArms.addChild(closedLeftArm);
		closedLeftArm.setTextureOffset(48, 12).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
		closedLeftArm.setTextureOffset(40, 0).addBox(-10.0F, -3.0F, -2.5F, 6.0F, 7.0F, 5.0F, 0.0F, false);

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(4.0F, 2.0F, 0.0F);
		setRotationAngle(bipedRightArm, 0.0F, 0.0F, -0.0436F);
		bipedRightArm.setTextureOffset(48, 48).addBox(-3.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		bipedRightArm.setTextureOffset(40, 0).addBox(-4.5F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, 0.0F, true);

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(-4.0F, 2.0F, 0.0F);
		setRotationAngle(bipedLeftArm, 0.0F, 0.0F, 0.0436F);
		bipedLeftArm.setTextureOffset(48, 48).addBox(-0.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		bipedLeftArm.setTextureOffset(40, 0).addBox(-1.5F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, 0.0F, false);

		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		bipedRightLeg.setTextureOffset(32, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		bipedRightLeg.setTextureOffset(30, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		bipedLeftLeg.setTextureOffset(32, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		bipedLeftLeg.setTextureOffset(30, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
	}

	protected Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.closedArms));
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setRotationAngles(VindicatorEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		AbstractIllagerEntity.ArmPose illagerArmPose = entityIn.getArmPose();
		if (illagerArmPose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (!entityIn.getHeldItemMainhand().isEmpty())
				ModelHelper.func_239103_a_(this.bipedRightArm, this.bipedLeftArm, entityIn, this.swingProgress, ageInTicks);
		}
		boolean isCrossed = illagerArmPose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.closedArms.showModel = isCrossed;
		this.bipedLeftArm.showModel = !isCrossed;
		this.bipedRightArm.showModel = !isCrossed;
		this.bipedHeadwear.showModel = false;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
