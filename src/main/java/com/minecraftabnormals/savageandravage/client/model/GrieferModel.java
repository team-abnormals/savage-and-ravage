package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.minecraftabnormals.savageandravage.common.entity.GrieferEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class GrieferModel extends BipedModel<GrieferEntity> {
	public ModelRenderer bipedBody2Layer;
	public ModelRenderer nose;
	public ModelRenderer tnt;
	public ModelRenderer pouch;
	public ModelRenderer shoulderPad;

	public GrieferModel(float size) {
		super(size);
		this.texWidth = 64;
		this.texHeight = 64;
		this.body = new ModelRenderer(this, 36, 0);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 18);
		this.rightLeg.mirror = true;
		this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.rightArm = new ModelRenderer(this, 16, 34);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.nose = new ModelRenderer(this, 24, 0);
		this.nose.setPos(0.0F, -2.0F, 0.0F);
		this.nose.addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
		this.tnt = new ModelRenderer(this, 50, 45);
		this.tnt.setPos(-0.0F, 6.0F, -6.00F);
		this.tnt.addBox(0.0F, 0.0F, 0.0F, 4, 4, 3, 0.0F);
		this.pouch = new ModelRenderer(this, 46, 36);
		this.pouch.setPos(-2.9F, 1.7F, 3.0F);
		this.pouch.addBox(0.0F, -0.9F, 0.0F, 6, 6, 3, 0.0F);
		this.shoulderPad = new ModelRenderer(this, 11, 51);
		this.shoulderPad.setPos(0.0F, 0.0F, 0.0F);
		this.shoulderPad.addBox(-4.0F, -2.7F, -3.2F, 5, 6, 6, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
		this.leftLeg = new ModelRenderer(this, 0, 18);
		this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leftArm = new ModelRenderer(this, 16, 18);
		this.leftArm.mirror = true;
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedBody2Layer = new ModelRenderer(this, 36, 18);
		this.bipedBody2Layer.setPos(0.0F, 0.0F, 0.0F);
		this.bipedBody2Layer.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.03F);
		this.head.addChild(this.nose);
		this.body.addChild(this.tnt);
		this.body.addChild(this.pouch);
		this.rightArm.addChild(this.shoulderPad);
		this.hat.visible = false;
	}

	protected Iterable<ModelRenderer> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.bipedBody2Layer));
	}

	@Override
	public void setupAnim(GrieferEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		boolean flag = entityIn.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem;
		this.bipedBody2Layer.copyFrom(this.body);
		this.shoulderPad.visible = !flag;
		if (entityIn.getKickTicks() > 0) {
			float f1 = 1.0F - (float) MathHelper.abs(10 - 2 * entityIn.getKickTicks()) / 10.0F;
			this.rightLeg.xRot = MathHelper.lerp(f1, 0.0F, -1.40F);
		}
		AbstractIllagerEntity.ArmPose armPose = entityIn.getArmPose();
		if (armPose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
			this.head.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.7000F) * 0.05F;
			this.leftArm.zRot = -2.3561945F;
			this.leftArm.yRot = 0.0F;
		}
	}

	@Override
	public void prepareMobModel(GrieferEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		ItemStack itemstack = entityIn.getMainHandItem();
		UseAction useaction = itemstack.getUseAnimation();
		this.rightArmPose = BipedModel.ArmPose.EMPTY;
		this.leftArmPose = BipedModel.ArmPose.EMPTY;
		if (entityIn.getMainArm() == HandSide.RIGHT) {
			switch (useaction) {
				case BLOCK:
					this.rightArmPose = BipedModel.ArmPose.BLOCK;
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
}