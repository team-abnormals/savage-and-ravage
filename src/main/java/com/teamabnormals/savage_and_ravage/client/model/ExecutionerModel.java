package com.teamabnormals.savage_and_ravage.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * ModelExecutioner - MCVinnyQ Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ExecutionerModel extends HumanoidModel<Vindicator> {
	public ModelPart closedArms;
	public ModelPart closedRightArm;
	public ModelPart closedLeftArm;
	public ModelPart nose;

	public ExecutionerModel(ModelPart root) {
		super(root);
		this.hat.visible = false;
		this.closedArms = root.getChild("closed_arms");
		this.closedRightArm = this.closedArms.getChild("closed_right_arm");
		this.closedLeftArm = this.closedArms.getChild("closed_left_arm");
		this.nose = this.head.getChild("nose");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition closedArms = root.addOrReplaceChild("closed_arms", CubeListBuilder.create().texOffs(32, 40).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, false), PartPose.offsetAndRotation(0.0F, 4.0F, -1.0F, -0.7854F, 0.0F, 0.0F));
		PartDefinition closedRightArm = closedArms.addOrReplaceChild("closed_right_arm", CubeListBuilder.create().texOffs(48, 12).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, true).texOffs(40, 0).addBox(4.0F, -3.0F, -2.5F, 6.0F, 7.0F, 5.0F, false), PartPose.ZERO);
		PartDefinition closedLeftArm = closedArms.addOrReplaceChild("closed_left_arm", CubeListBuilder.create().texOffs(48, 12).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, false).texOffs(40, 0).addBox(-10.0F, -3.0F, -2.5F, 6.0F, 7.0F, 5.0F, false), PartPose.ZERO);
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 28).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, false).texOffs(0, 0).addBox(-4.5F, 0.0F, -3.0F, 9.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 46).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, false).texOffs(32, 28).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.ZERO);
		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 46).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 4.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, -3.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 48).addBox(-0.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, false).texOffs(40, 0).addBox(-1.5F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, false), PartPose.offsetAndRotation(-4.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0436F));
		PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 48).addBox(-3.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, true).texOffs(40, 0).addBox(-4.5F, -2.5F, -2.5F, 6.0F, 7.0F, 5.0F, false), PartPose.offsetAndRotation(4.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.0436F));
		PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, false).texOffs(30, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, false), PartPose.offsetAndRotation(-1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, true).texOffs(30, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, false), PartPose.offsetAndRotation(1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	protected Iterable<ModelPart> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.closedArms));
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(Vindicator entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		AbstractIllager.IllagerArmPose illagerArmPose = entityIn.getArmPose();
		if (illagerArmPose == AbstractIllager.IllagerArmPose.ATTACKING) {
			if (!entityIn.getMainHandItem().isEmpty())
				AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
		}
		boolean isCrossed = illagerArmPose == AbstractIllager.IllagerArmPose.CROSSED;
		this.closedArms.visible = isCrossed;
		this.leftArm.visible = !isCrossed;
		this.rightArm.visible = !isCrossed;
		this.hat.visible = false;
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
