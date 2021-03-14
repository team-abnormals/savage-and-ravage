package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.minecraftabnormals.savageandravage.common.entity.IceologerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.math.MathHelper;

public class IceologerModel extends BipedModel<IceologerEntity> {

	private final ModelRenderer arms;
	private final ModelRenderer cape;

	public IceologerModel() {
		super(0.0F, 0.0F, 64, 64);
		this.textureWidth = 64;
		this.textureHeight = 64;

		this.bipedHead = new ModelRenderer(this);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		this.bipedHead.setTextureOffset(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		this.bipedHead.setTextureOffset(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.5F, false);

		this.bipedBody = new ModelRenderer(this);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
		this.bipedBody.setTextureOffset(0, 38).addBox(-4.0F, 1.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, false);

		this.arms = new ModelRenderer(this);
		this.arms.setRotationPoint(0.0F, 2.0F, 0.0F);
		this.arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);
		this.arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		ModelRenderer arms_sub_0 = new ModelRenderer(this);
		arms_sub_0.setRotationPoint(0.0F, 22.0F, 0.0F);
		this.arms.addChild(arms_sub_0);
		arms_sub_0.setTextureOffset(44, 22).addBox(4.0F, -24.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);

		this.bipedRightLeg = new ModelRenderer(this);
		this.bipedRightLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.bipedRightLeg.setTextureOffset(0, 22).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		this.bipedLeftLeg = new ModelRenderer(this);
		this.bipedLeftLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg.setTextureOffset(0, 22).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		this.bipedRightArm = new ModelRenderer(this);
		this.bipedRightArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedRightArm.setTextureOffset(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		this.bipedLeftArm = new ModelRenderer(this);
		this.bipedLeftArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedLeftArm.setTextureOffset(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		this.cape = new ModelRenderer(this);
		this.cape.setRotationPoint(0.0F, 0.0F, 3.5F);
		this.setRotationAngle(this.cape, -0.2618F, 0.0F, 3.1416F);
		this.cape.setTextureOffset(19, 43).addBox(-4.0F, -18.0F, -0.5F, 8.0F, 18.0F, 1.0F, 0.0F, false);
		this.bipedHeadwear.showModel = false;
	}

	@Override
	public void setLivingAnimations(IceologerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		double d0 = entity.prevChasingPosX + (entity.chasingPosX - entity.prevChasingPosX) * partialTicks - (entity.prevPosX + (entity.getPosX() - entity.prevPosX) * partialTicks);
		double d1 = entity.prevChasingPosY + (entity.chasingPosY - entity.prevChasingPosY) * partialTicks - (entity.prevPosY + (entity.getPosY() - entity.prevPosY) * partialTicks);
		double d2 = entity.prevChasingPosZ + (entity.chasingPosZ - entity.prevChasingPosZ) * partialTicks - (entity.prevPosZ + (entity.getPosZ() - entity.prevPosZ) * partialTicks);
		float f = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;
		double d3 = MathHelper.sin(f * 0.017453292F);
		double d4 = (-MathHelper.cos(f * 0.017453292F));
		float f1 = (float) d1 * 10.0F;
		f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
		float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
		f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
		float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
		f3 = MathHelper.clamp(f3, -20.0F, 20.0F);

		float f4 = entity.prevCameraYaw + (entity.cameraYaw - entity.prevCameraYaw) * partialTicks;
		f1 = f1 + MathHelper.sin((entity.prevDistanceWalkedModified + (entity.distanceWalkedModified - entity.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;
		if (entity.isSneaking())
			f1 += 25.0F;

		this.cape.rotateAngleX = (float) Math.toRadians(180 - (6.0F + f2 / 2.0F + f1));
		this.cape.rotateAngleY = (float) Math.toRadians(180.0F - f3 / 2.0F);
		this.cape.rotateAngleZ = (float) Math.toRadians(f3 / 2.0F);
		super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
	}

	@Override
	public void setRotationAngles(IceologerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.arms.rotationPointY = 3.0F;
		this.arms.rotationPointZ = -1.0F;
		this.arms.rotateAngleX = -0.75F;
		AbstractIllagerEntity.ArmPose armPose = entity.getArmPose();
		if (armPose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (entity.getHeldItemMainhand().isEmpty()) {
				ModelHelper.func_239105_a_(this.bipedLeftArm, this.bipedRightArm, true, this.swingProgress, ageInTicks);
			} else {
				ModelHelper.func_239103_a_(this.bipedRightArm, this.bipedLeftArm, entity, this.swingProgress, ageInTicks);
			}
		} else if (armPose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
			this.bipedRightArm.rotationPointZ = 0.0F;
			this.bipedRightArm.rotationPointX = -5.0F;
			this.bipedLeftArm.rotationPointZ = 0.0F;
			this.bipedLeftArm.rotationPointX = 5.0F;
			this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.bipedRightArm.rotateAngleZ = 2.3561945F;
			this.bipedLeftArm.rotateAngleZ = -2.3561945F;
			this.bipedRightArm.rotateAngleY = 0.0F;
			this.bipedLeftArm.rotateAngleY = 0.0F;
		} else if (armPose == AbstractIllagerEntity.ArmPose.BOW_AND_ARROW) {
			this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
			this.bipedRightArm.rotateAngleX = (-(float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -0.9424779F + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleY = this.bipedHead.rotateAngleY - 0.4F;
			this.bipedLeftArm.rotateAngleZ = ((float) Math.PI / 2F);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD) {
			ModelHelper.func_239104_a_(this.bipedRightArm, this.bipedLeftArm, this.bipedHead, true);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE) {
			ModelHelper.func_239102_a_(this.bipedRightArm, this.bipedLeftArm, entity, true);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
			this.bipedRightArm.rotationPointZ = 0.0F;
			this.bipedRightArm.rotationPointX = -5.0F;
			this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.bipedRightArm.rotateAngleZ = 2.670354F;
			this.bipedRightArm.rotateAngleY = 0.0F;
			this.bipedLeftArm.rotationPointZ = 0.0F;
			this.bipedLeftArm.rotationPointX = 5.0F;
			this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.bipedLeftArm.rotateAngleZ = -2.3561945F;
			this.bipedLeftArm.rotateAngleY = 0.0F;
		}

		boolean flag = armPose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.arms.showModel = flag;
		this.bipedLeftArm.showModel = !flag;
		this.bipedRightArm.showModel = !flag;
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.arms, this.cape));
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}