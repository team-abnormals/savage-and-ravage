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
		this.texWidth = 64;
		this.texHeight = 64;

		this.head = new ModelRenderer(this);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		this.head.texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		this.head.texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.5F, false);

		this.body = new ModelRenderer(this);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
		this.body.texOffs(0, 38).addBox(-4.0F, 1.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, false);

		this.arms = new ModelRenderer(this);
		this.arms.setPos(0.0F, 2.0F, 0.0F);
		this.arms.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);
		this.arms.texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		ModelRenderer arms_sub_0 = new ModelRenderer(this);
		arms_sub_0.setPos(0.0F, 22.0F, 0.0F);
		this.arms.addChild(arms_sub_0);
		arms_sub_0.texOffs(44, 22).addBox(4.0F, -24.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);

		this.rightLeg = new ModelRenderer(this);
		this.rightLeg.setPos(2.0F, 12.0F, 0.0F);
		this.rightLeg.texOffs(0, 22).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		this.leftLeg = new ModelRenderer(this);
		this.leftLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.leftLeg.texOffs(0, 22).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		this.rightArm = new ModelRenderer(this);
		this.rightArm.setPos(5.0F, 2.0F, 0.0F);
		this.rightArm.texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		this.leftArm = new ModelRenderer(this);
		this.leftArm.setPos(-5.0F, 2.0F, 0.0F);
		this.leftArm.texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		this.cape = new ModelRenderer(this);
		this.cape.setPos(0.0F, 0.0F, 3.5F);
		this.setRotationAngle(this.cape, -0.2618F, 0.0F, 3.1416F);
		this.cape.texOffs(19, 43).addBox(-4.0F, -18.0F, -0.5F, 8.0F, 18.0F, 1.0F, 0.0F, false);
		this.hat.visible = false;
	}

	@Override
	public void prepareMobModel(IceologerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		double d0 = entity.prevChasingPosX + (entity.chasingPosX - entity.prevChasingPosX) * partialTicks - (entity.xo + (entity.getX() - entity.xo) * partialTicks);
		double d1 = entity.prevChasingPosY + (entity.chasingPosY - entity.prevChasingPosY) * partialTicks - (entity.yo + (entity.getY() - entity.yo) * partialTicks);
		double d2 = entity.prevChasingPosZ + (entity.chasingPosZ - entity.prevChasingPosZ) * partialTicks - (entity.zo + (entity.getZ() - entity.zo) * partialTicks);
		float f = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
		double d3 = MathHelper.sin(f * 0.017453292F);
		double d4 = (-MathHelper.cos(f * 0.017453292F));
		float f1 = (float) d1 * 10.0F;
		f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
		float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
		f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
		float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
		f3 = MathHelper.clamp(f3, -20.0F, 20.0F);

		float f4 = entity.prevCameraYaw + (entity.cameraYaw - entity.prevCameraYaw) * partialTicks;
		f1 = f1 + MathHelper.sin((entity.walkDistO + (entity.walkDist - entity.walkDistO) * partialTicks) * 6.0F) * 32.0F * f4;
		if (entity.isShiftKeyDown())
			f1 += 25.0F;

		this.cape.xRot = (float) Math.toRadians(180 - (6.0F + f2 / 2.0F + f1));
		this.cape.yRot = (float) Math.toRadians(180.0F - f3 / 2.0F);
		this.cape.zRot = (float) Math.toRadians(f3 / 2.0F);
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
	}

	@Override
	public void setupAnim(IceologerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.arms.y = 3.0F;
		this.arms.z = -1.0F;
		this.arms.xRot = -0.75F;
		AbstractIllagerEntity.ArmPose armPose = entity.getArmPose();
		if (armPose == AbstractIllagerEntity.ArmPose.ATTACKING) {
			if (entity.getMainHandItem().isEmpty()) {
				ModelHelper.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, ageInTicks);
			} else {
				ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, ageInTicks);
			}
		} else if (armPose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
			this.rightArm.zRot = 2.3561945F;
			this.leftArm.zRot = -2.3561945F;
			this.rightArm.yRot = 0.0F;
			this.leftArm.yRot = 0.0F;
		} else if (armPose == AbstractIllagerEntity.ArmPose.BOW_AND_ARROW) {
			this.rightArm.yRot = -0.1F + this.head.yRot;
			this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			this.leftArm.xRot = -0.9424779F + this.head.xRot;
			this.leftArm.yRot = this.head.yRot - 0.4F;
			this.leftArm.zRot = ((float) Math.PI / 2F);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD) {
			ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE) {
			ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
		} else if (armPose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
			this.rightArm.z = 0.0F;
			this.rightArm.x = -5.0F;
			this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.rightArm.zRot = 2.670354F;
			this.rightArm.yRot = 0.0F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.leftArm.zRot = -2.3561945F;
			this.leftArm.yRot = 0.0F;
		}

		boolean flag = armPose == AbstractIllagerEntity.ArmPose.CROSSED;
		this.arms.visible = flag;
		this.leftArm.visible = !flag;
		this.rightArm.visible = !flag;
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.arms, this.cape));
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}