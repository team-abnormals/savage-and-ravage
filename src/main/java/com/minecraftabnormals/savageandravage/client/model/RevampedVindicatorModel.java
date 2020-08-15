package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;

public class RevampedVindicatorModel extends BipedModel<VindicatorEntity> {

	public ModelRenderer closedArms;
	
	public RevampedVindicatorModel(float size) {
		super(RenderType::getEntityTranslucent, size, 0.0F, 64, 64);

		this.bipedLeftLeg = new ModelRenderer(this, 32, 48);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		this.bipedRightLeg = new ModelRenderer(this, 32, 48);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		this.bipedBody = new ModelRenderer(this);
		this.bipedBody.setRotationPoint(0.0F, 12.0F, 0.0F);
		this.bipedBody.setTextureOffset(0, 0).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 18.0F, 6.0F, 0.1F, false);
		this.bipedBody.setTextureOffset(0, 28).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);

		this.closedArms = new ModelRenderer(this);
		this.closedArms.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.setRotateAngle(this.closedArms, -0.7854F, 0.0F, 0.0F);
		this.closedArms.setTextureOffset(32, 40).addBox(-4.0F, 4.0F, -1.0F, 8.0F, 4.0F, 4.0F, 0.0F, true);
		this.closedArms.setTextureOffset(32, 20).addBox(4.0F, 0.0F, -1.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);
		this.closedArms.setTextureOffset(32, 20).addBox(-8.0F, 0.0F, -1.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
		this.closedArms.setTextureOffset(40, 0).addBox(4.0F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, 0.1F, true);
		this.closedArms.setTextureOffset(40, 0).addBox(-10.0F, -2.5F, -1.5F, 6.0F, 7.0F, 5.0F, 0.1F, false);

		this.bipedRightArm = new ModelRenderer(this);
		this.bipedRightArm.setRotationPoint(-4.0F, 2.0F, 0.0F);
		this.bipedRightArm.setTextureOffset(48, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		this.bipedRightArm.setTextureOffset(40, 0).addBox(-4.5F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, 0.1F, false);

		this.bipedLeftArm = new ModelRenderer(this);
		this.bipedLeftArm.setRotationPoint(4.0F, 2.0F, 0.0F);
		this.bipedLeftArm.setTextureOffset(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		this.bipedLeftArm.setTextureOffset(40, 0).addBox(-1.0F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, 0.1F, true);

		this.bipedHead = new ModelRenderer(this);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.setTextureOffset(0, 46).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		this.bipedHead.setTextureOffset(24, 46).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		this.bipedHeadwear.showModel = false;
	}
	
	protected Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.closedArms));
	}
	
	public void setRotationAngles(VindicatorEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		AbstractIllagerEntity.ArmPose illagerArmPose = entityIn.getArmPose();
		switch (illagerArmPose) {
			case ATTACKING:
				if (!entityIn.getHeldItemMainhand().isEmpty())
				ModelHelper.func_239103_a_(this.bipedRightArm, this.bipedLeftArm, entityIn, this.swingProgress, ageInTicks);
				break;
			default:
				break;
		}
		
		boolean isCrossed = illagerArmPose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.closedArms.showModel = isCrossed;
		this.bipedLeftArm.showModel = !isCrossed;
		this.bipedRightArm.showModel = !isCrossed;
	}
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
