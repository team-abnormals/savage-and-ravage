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
	public float floatthing;

	public SkeletonVillagerModel(float f) {
		super(f);
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.bipedRightLeg = new ModelRenderer(this, 0, 18);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.bipedBody = new ModelRenderer(this, 12, 18);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
		this.LeftClosedArm = new ModelRenderer(this, 32, 0);
		this.LeftClosedArm.mirror = true;
		this.LeftClosedArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.LeftClosedArm.addBox(4.0F, -2.0F, -1.0F, 3, 8, 3, 0.0F);
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 18);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.RightClosedArm = new ModelRenderer(this, 32, 0);
		this.RightClosedArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.RightClosedArm.addBox(-7.0F, -2.0F, -1.0F, 3, 8, 3, 0.0F);
		this.Nose = new ModelRenderer(this, 24, 0);
		this.Nose.setRotationPoint(0.0F, -3.0F, -4.0F);
		this.Nose.addBox(-1.0F, 0.0F, -2.0F, 2, 4, 2, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 40, 19);
		this.bipedRightArm.setRotationPoint(-5.5F, 2.0F, 0.0F);
		this.bipedRightArm.addBox(-1.5F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 40, 19);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.setRotationPoint(5.5F, 2.0F, 0.0F);
		this.bipedLeftArm.addBox(-1.5F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
		this.MiddleClosedArm = new ModelRenderer(this, 32, 11);
		this.MiddleClosedArm.setRotationPoint(0.0F, 3.0F, -1.0F);
		this.MiddleClosedArm.addBox(-4.0F, 3.0F, -1.0F, 8, 3, 3, 0.0F);
		this.setRotateAngle(MiddleClosedArm, -0.7853981633974483F, 0.0F, 0.0F);
		this.MiddleClosedArm.addChild(this.LeftClosedArm);
		this.MiddleClosedArm.addChild(this.RightClosedArm);
		this.bipedHead.addChild(this.Nose);
		this.bipedHeadwear.showModel = false;
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.MiddleClosedArm));
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(SkeletonVillagerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		boolean flag = entityIn.isAggressive();
		this.RightClosedArm.showModel = !flag;
		this.LeftClosedArm.showModel = !flag;
		this.MiddleClosedArm.showModel = !flag;
		this.bipedLeftArm.showModel = flag;
		this.bipedRightArm.showModel = flag;
		ItemStack itemstack = entityIn.getHeldItemMainhand();
		if (entityIn.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof ShootableItem))) {
			float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
			float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
			this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
			this.bipedRightArm.rotateAngleX = (-(float) Math.PI / 2F);
			this.bipedLeftArm.rotateAngleX = (-(float) Math.PI / 2F);
			this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
			ModelHelper.func_239101_a_(this.bipedRightArm, this.bipedLeftArm, ageInTicks);
		}
	}

	@Override
	public void setLivingAnimations(SkeletonVillagerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		ItemStack itemstack = entityIn.getHeldItemMainhand();
		UseAction useaction = itemstack.getUseAction();
		this.rightArmPose = BipedModel.ArmPose.EMPTY;
		this.leftArmPose = BipedModel.ArmPose.EMPTY;
		if (entityIn.getPrimaryHand() == HandSide.RIGHT) {
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
		if (entityIn.getPrimaryHand() == HandSide.LEFT) {
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
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
		float f = sideIn == HandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer modelrenderer = this.getArmForSide(sideIn);
		modelrenderer.rotationPointX += f;
		modelrenderer.translateRotate(matrixStackIn);
		modelrenderer.rotationPointX -= f;
	}
}