package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.savageandravage.common.entity.IceologerEntity;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.math.MathHelper;

public class IceologerModel extends SegmentedModel<IceologerEntity> implements IHasHead {

	private final ModelRenderer head;
	private final ModelRenderer body;
	private final ModelRenderer arms;
	private final ModelRenderer right_leg;
	private final ModelRenderer left_leg;
	private final ModelRenderer right_arm;
	private final ModelRenderer left_arm;
	private final ModelRenderer cape;

	public IceologerModel() {
		this.textureWidth = 64;
		this.textureHeight = 64;

		this.head = new ModelRenderer(this);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		this.head.setTextureOffset(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		this.head.setTextureOffset(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.5F, false);

		this.body = new ModelRenderer(this);
		this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.body.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
		this.body.setTextureOffset(0, 38).addBox(-4.0F, 1.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, false);

		this.arms = new ModelRenderer(this);
		this.arms.setRotationPoint(0.0F, 2.0F, 0.0F);
		this.arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);
		this.arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		ModelRenderer arms_sub_0 = new ModelRenderer(this);
		arms_sub_0.setRotationPoint(0.0F, 22.0F, 0.0F);
		this.arms.addChild(arms_sub_0);
		arms_sub_0.setTextureOffset(44, 22).addBox(4.0F, -24.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);

		this.right_leg = new ModelRenderer(this);
		this.right_leg.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.right_leg.setTextureOffset(0, 22).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		this.left_leg = new ModelRenderer(this);
		this.left_leg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.left_leg.setTextureOffset(0, 22).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		this.right_arm = new ModelRenderer(this);
		this.right_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.right_arm.setTextureOffset(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		this.left_arm = new ModelRenderer(this);
		this.left_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.left_arm.setTextureOffset(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		this.cape = new ModelRenderer(this);
		this.cape.setRotationPoint(0.0F, 0.0F, 3.5F);
		this.setRotationAngle(this.cape, -0.2618F, 0.0F, 3.1416F);
		this.cape.setTextureOffset(19, 43).addBox(-4.0F, -18.0F, -0.5F, 8.0F, 18.0F, 1.0F, 0.0F, false);
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
	}

	@Override
	public void setRotationAngles(IceologerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
		this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
		this.arms.rotationPointY = 3.0F;
		this.arms.rotationPointZ = -1.0F;
		this.arms.rotateAngleX = -0.75F;
		if (this.isSitting) {
			this.right_arm.rotateAngleX = (-(float) Math.PI / 5F);
			this.right_arm.rotateAngleY = 0.0F;
			this.right_arm.rotateAngleZ = 0.0F;
			this.left_arm.rotateAngleX = (-(float) Math.PI / 5F);
			this.left_arm.rotateAngleY = 0.0F;
			this.left_arm.rotateAngleZ = 0.0F;
			this.right_leg.rotateAngleX = -1.4137167F;
			this.right_leg.rotateAngleY = ((float) Math.PI / 10F);
			this.right_leg.rotateAngleZ = 0.07853982F;
			this.left_leg.rotateAngleX = -1.4137167F;
			this.left_leg.rotateAngleY = (-(float) Math.PI / 10F);
			this.left_leg.rotateAngleZ = -0.07853982F;
		} else {
			this.right_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.right_arm.rotateAngleY = 0.0F;
			this.right_arm.rotateAngleZ = 0.0F;
			this.left_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.left_arm.rotateAngleY = 0.0F;
			this.left_arm.rotateAngleZ = 0.0F;
			this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.right_leg.rotateAngleY = 0.0F;
			this.right_leg.rotateAngleZ = 0.0F;
			this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.left_leg.rotateAngleY = 0.0F;
			this.left_leg.rotateAngleZ = 0.0F;
		}

		AbstractIllagerEntity.ArmPose abstractillagerentity$armpose = entity.getArmPose();
		if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (entity.getHeldItemMainhand().isEmpty()) {
				ModelHelper.func_239105_a_(this.left_arm, this.right_arm, true, this.swingProgress, ageInTicks);
			} else {
				ModelHelper.func_239103_a_(this.right_arm, this.left_arm, entity, this.swingProgress, ageInTicks);
			}
		} else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
			this.right_arm.rotationPointZ = 0.0F;
			this.right_arm.rotationPointX = -5.0F;
			this.left_arm.rotationPointZ = 0.0F;
			this.left_arm.rotationPointX = 5.0F;
			this.right_arm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.left_arm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.right_arm.rotateAngleZ = 2.3561945F;
			this.left_arm.rotateAngleZ = -2.3561945F;
			this.right_arm.rotateAngleY = 0.0F;
			this.left_arm.rotateAngleY = 0.0F;
		} else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.BOW_AND_ARROW) {
			this.right_arm.rotateAngleY = -0.1F + this.head.rotateAngleY;
			this.right_arm.rotateAngleX = (-(float) Math.PI / 2F) + this.head.rotateAngleX;
			this.left_arm.rotateAngleX = -0.9424779F + this.head.rotateAngleX;
			this.left_arm.rotateAngleY = this.head.rotateAngleY - 0.4F;
			this.left_arm.rotateAngleZ = ((float) Math.PI / 2F);
		} else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD) {
			ModelHelper.func_239104_a_(this.right_arm, this.left_arm, this.head, true);
		} else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE) {
			ModelHelper.func_239102_a_(this.right_arm, this.left_arm, entity, true);
		} else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
			this.right_arm.rotationPointZ = 0.0F;
			this.right_arm.rotationPointX = -5.0F;
			this.right_arm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.right_arm.rotateAngleZ = 2.670354F;
			this.right_arm.rotateAngleY = 0.0F;
			this.left_arm.rotationPointZ = 0.0F;
			this.left_arm.rotationPointX = 5.0F;
			this.left_arm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.left_arm.rotateAngleZ = -2.3561945F;
			this.left_arm.rotateAngleY = 0.0F;
		}

		boolean flag = abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.arms.showModel = flag;
		this.left_arm.showModel = !flag;
		this.right_arm.showModel = !flag;
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.head, this.body, this.arms, this.right_leg, this.left_leg, this.right_arm, this.left_arm, this.cape);
	}

	@Override
	public ModelRenderer getModelHead() {
		return head;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}