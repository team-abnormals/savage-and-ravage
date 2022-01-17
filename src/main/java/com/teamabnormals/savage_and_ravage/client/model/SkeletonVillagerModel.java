package com.teamabnormals.savage_and_ravage.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.savage_and_ravage.common.entity.monster.SkeletonVillager;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;

public class SkeletonVillagerModel extends HumanoidModel<SkeletonVillager> {
	public ModelPart middleClosedArm;
	public ModelPart nose;
	public ModelPart rightClosedArm;
	public ModelPart leftClosedArm;

	public SkeletonVillagerModel(ModelPart root) {
		super(root);
		this.hat.visible = false;
		this.middleClosedArm = root.getChild("middle_closed_arm");
		this.leftClosedArm = this.middleClosedArm.getChild("left_closed_arm");
		this.rightClosedArm = this.middleClosedArm.getChild("right_closed_arm");
		this.nose = this.head.getChild("nose");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition middleClosedArm = root.addOrReplaceChild("middle_closed_arm", CubeListBuilder.create().texOffs(32, 11).addBox(-4.0F, 3.0F, -1.0F, 8.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.7853982F, 0.0F, 0.0F));
		PartDefinition leftClosedArm = middleClosedArm.addOrReplaceChild("left_closed_arm", CubeListBuilder.create().texOffs(32, 0).addBox(4.0F, -2.0F, -1.0F, 3.0F, 8.0F, 3.0F, true), PartPose.ZERO);
		PartDefinition rightClosedArm = middleClosedArm.addOrReplaceChild("right_closed_arm", CubeListBuilder.create().texOffs(32, 0).addBox(-7.0F, -2.0F, -1.0F, 3.0F, 8.0F, 3.0F, false), PartPose.ZERO);
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(12, 18).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, false), PartPose.ZERO);
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, false), PartPose.ZERO);
		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 4.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, -3.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 19).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, true), PartPose.offsetAndRotation(5.5F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 19).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, false), PartPose.offsetAndRotation(-5.5F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, true), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, false), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.middleClosedArm));
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim(SkeletonVillager entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		boolean flag = entityIn.isAggressive();
		this.rightClosedArm.visible = !flag;
		this.leftClosedArm.visible = !flag;
		this.middleClosedArm.visible = !flag;
		this.leftArm.visible = flag;
		this.rightArm.visible = flag;
		ItemStack itemstack = entityIn.getMainHandItem();
		if (entityIn.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof ProjectileWeaponItem))) {
			float f = Mth.sin(this.attackTime * (float) Math.PI);
			float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightArm.yRot = -(0.1F - f * 0.6F);
			this.leftArm.yRot = 0.1F - f * 0.6F;
			this.rightArm.xRot = (-(float) Math.PI / 2F);
			this.leftArm.xRot = (-(float) Math.PI / 2F);
			this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
			this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
			AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
		}
	}

	@Override
	public void prepareMobModel(SkeletonVillager entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		ItemStack itemstack = entityIn.getMainHandItem();
		UseAnim useaction = itemstack.getUseAnimation();
		this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
		this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
		if (entityIn.getMainArm() == HumanoidArm.RIGHT) {
			switch (useaction) {
				case BLOCK:
					this.rightArmPose = HumanoidModel.ArmPose.BLOCK;
					break;
				case CROSSBOW:
					this.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
					if (entityIn.isCharging()) {
						this.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_CHARGE;
					}
					break;
				case BOW:
					this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
					break;
				default:
					this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
					if (!itemstack.isEmpty()) {
						this.rightArmPose = HumanoidModel.ArmPose.ITEM;
					}
					break;
			}
		}
		if (entityIn.getMainArm() == HumanoidArm.LEFT) {
			switch (useaction) {
				case BLOCK:
					this.leftArmPose = HumanoidModel.ArmPose.BLOCK;
					break;
				case CROSSBOW:
					this.leftArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
					if (entityIn.isCharging()) {
						this.leftArmPose = HumanoidModel.ArmPose.CROSSBOW_CHARGE;
					}
					break;
				case BOW:
					this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
					break;
				default:
					this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
					if (!itemstack.isEmpty()) {
						this.leftArmPose = HumanoidModel.ArmPose.ITEM;
					}
					break;
			}
		}
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public void translateToHand(HumanoidArm sideIn, PoseStack matrixStackIn) {
		float f = sideIn == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		ModelPart modelrenderer = this.getArm(sideIn);
		modelrenderer.x += f;
		modelrenderer.translateAndRotate(matrixStackIn);
		modelrenderer.x -= f;
	}
}