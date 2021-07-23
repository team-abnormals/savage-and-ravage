package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.minecraftabnormals.savageandravage.common.entity.SkeletonVillagerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

/*
  ModelSkeletonVillager - Vinny
 */
public class SkeletonVillagerModel extends BipedModel<SkeletonVillagerEntity> {
	public ModelRenderer MiddleClosedArm;
	public ModelRenderer Nose;
	public ModelRenderer RightClosedArm;
	public ModelRenderer LeftClosedArm;

	public SkeletonVillagerModel(float f) {
		super(f);
		this.texWidth = 64;
		this.texHeight = 64;
		this.rightLeg = new ModelRenderer(this, 0, 18);
		this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.rightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.body = new ModelRenderer(this, 12, 18);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
		this.LeftClosedArm = new ModelRenderer(this, 32, 0);
		this.LeftClosedArm.mirror = true;
		this.LeftClosedArm.setPos(0.0F, 0.0F, 0.0F);
		this.LeftClosedArm.addBox(4.0F, -2.0F, -1.0F, 3, 8, 3, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
		this.leftLeg = new ModelRenderer(this, 0, 18);
		this.leftLeg.mirror = true;
		this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
		this.leftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.RightClosedArm = new ModelRenderer(this, 32, 0);
		this.RightClosedArm.setPos(0.0F, 0.0F, 0.0F);
		this.RightClosedArm.addBox(-7.0F, -2.0F, -1.0F, 3, 8, 3, 0.0F);
		this.Nose = new ModelRenderer(this, 24, 0);
		this.Nose.setPos(0.0F, -3.0F, -4.0F);
		this.Nose.addBox(-1.0F, 0.0F, -2.0F, 2, 4, 2, 0.0F);
		this.rightArm = new ModelRenderer(this, 40, 19);
		this.rightArm.setPos(-5.5F, 2.0F, 0.0F);
		this.rightArm.addBox(-1.5F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
		this.leftArm = new ModelRenderer(this, 40, 19);
		this.leftArm.mirror = true;
		this.leftArm.setPos(5.5F, 2.0F, 0.0F);
		this.leftArm.addBox(-1.5F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
		this.MiddleClosedArm = new ModelRenderer(this, 32, 11);
		this.MiddleClosedArm.setPos(0.0F, 3.0F, -1.0F);
		this.MiddleClosedArm.addBox(-4.0F, 3.0F, -1.0F, 8, 3, 3, 0.0F);
		this.setRotateAngle(MiddleClosedArm, -0.7853981633974483F, 0.0F, 0.0F);
		this.MiddleClosedArm.addChild(this.LeftClosedArm);
		this.MiddleClosedArm.addChild(this.RightClosedArm);
		this.head.addChild(this.Nose);
		this.hat.visible = false;
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.MiddleClosedArm));
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim(SkeletonVillagerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		boolean flag = entityIn.isAggressive();
		this.RightClosedArm.visible = !flag;
		this.LeftClosedArm.visible = !flag;
		this.MiddleClosedArm.visible = !flag;
		this.leftArm.visible = flag;
		this.rightArm.visible = flag;
		ItemStack itemstack = entityIn.getMainHandItem();
		if (entityIn.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof ShootableItem))) {
			float f = MathHelper.sin(this.attackTime * (float) Math.PI);
			float f1 = MathHelper.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightArm.yRot = -(0.1F - f * 0.6F);
			this.leftArm.yRot = 0.1F - f * 0.6F;
			this.rightArm.xRot = (-(float) Math.PI / 2F);
			this.leftArm.xRot = (-(float) Math.PI / 2F);
			this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
			this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
			ModelHelper.bobArms(this.rightArm, this.leftArm, ageInTicks);
		}
	}

	@Override
	public void prepareMobModel(SkeletonVillagerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		ItemStack itemstack = entityIn.getMainHandItem();
		UseAction useaction = itemstack.getUseAnimation();
		this.rightArmPose = BipedModel.ArmPose.EMPTY;
		this.leftArmPose = BipedModel.ArmPose.EMPTY;
		if (entityIn.getMainArm() == HandSide.RIGHT) {
			switch (useaction) {
				case BLOCK:
					this.rightArmPose = BipedModel.ArmPose.BLOCK;
					break;
				case CROSSBOW:
					this.rightArmPose = BipedModel.ArmPose.CROSSBOW_HOLD;
					if (entityIn.isCharging()) {
						this.rightArmPose = BipedModel.ArmPose.CROSSBOW_CHARGE;
					}
					break;
				case BOW:
					this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
					break;
				default:
					this.rightArmPose = BipedModel.ArmPose.EMPTY;
					if (!itemstack.isEmpty()) {
						this.rightArmPose = BipedModel.ArmPose.ITEM;
					}
					break;
			}
		}
		if (entityIn.getMainArm() == HandSide.LEFT) {
			switch (useaction) {
				case BLOCK:
					this.leftArmPose = BipedModel.ArmPose.BLOCK;
					break;
				case CROSSBOW:
					this.leftArmPose = BipedModel.ArmPose.CROSSBOW_HOLD;
					if (entityIn.isCharging()) {
						this.leftArmPose = BipedModel.ArmPose.CROSSBOW_CHARGE;
					}
					break;
				case BOW:
					this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
					break;
				default:
					this.leftArmPose = BipedModel.ArmPose.EMPTY;
					if (!itemstack.isEmpty()) {
						this.leftArmPose = BipedModel.ArmPose.ITEM;
					}
					break;
			}
		}
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
		float f = sideIn == HandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer modelrenderer = this.getArm(sideIn);
		modelrenderer.x += f;
		modelrenderer.translateAndRotate(matrixStackIn);
		modelrenderer.x -= f;
	}
}