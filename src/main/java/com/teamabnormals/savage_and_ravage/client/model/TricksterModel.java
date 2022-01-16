package com.teamabnormals.savage_and_ravage.client.model;

import com.teamabnormals.savage_and_ravage.common.entity.TricksterEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.AbstractIllager;

public class TricksterModel extends HumanoidModel<TricksterEntity> {
	public ModelPart nose;
	public ModelPart leftDecor;
	public ModelPart rightDecor;
	public ModelPart leftWiggly;
	public ModelPart rightWiggly;

	public TricksterModel(ModelPart root) {
		super(root);
		this.hat.visible = false;
		this.nose = this.head.getChild("nose");
		this.leftWiggly = this.head.getChild("left_wiggly");
		this.rightWiggly = this.head.getChild("right_wiggly");
		this.leftDecor = this.head.getChild("left_decor");
		this.rightDecor = this.head.getChild("right_decor");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, 4.0F, -3.0F, 8.0F, 10.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 14.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 10.0F, 8.0F, false), PartPose.ZERO);
		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, 3.0F, -6.0F, 2.0F, 4.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftWiggly = head.addOrReplaceChild("left_wiggly", CubeListBuilder.create().texOffs(44, 8).addBox(0.0F, 0.0F, 0.0F, 6.0F, 4.0F, 4.0F, false), PartPose.offsetAndRotation(4.0F, -6.0F, -2.0F, 0.0F, 0.0F, 0.43633232F));
		PartDefinition rightWiggly = head.addOrReplaceChild("right_wiggly", CubeListBuilder.create().texOffs(44, 0).addBox(-6.0F, 0.0F, 0.0F, 6.0F, 4.0F, 4.0F, true), PartPose.offsetAndRotation(-4.0F, -6.0F, -2.0F, 0.0F, 0.0F, -0.43633232F));
		PartDefinition leftDecor = head.addOrReplaceChild("left_decor", CubeListBuilder.create().texOffs(0, 0).addBox(5.5F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, false), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightDecor = head.addOrReplaceChild("right_decor", CubeListBuilder.create().texOffs(0, 0).addBox(-9.5F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, false), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 50).addBox(-1.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, true), PartPose.offsetAndRotation(5.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, false), PartPose.offsetAndRotation(-5.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 18).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, false), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(48, 18).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, true), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(TricksterEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		AbstractIllager.IllagerArmPose pose = entityIn.getArmPose();
		//TODO fix rotation points considering different arm length
		if (pose == AbstractIllager.IllagerArmPose.SPELLCASTING || pose == AbstractIllager.IllagerArmPose.CELEBRATING) {
			//Initial adjustments
			this.rightArm.z = 0.0F;
			this.rightArm.y = 8.0F;
			this.rightArm.x = -1.5F;
			this.leftArm.z = 0.0F;
			this.leftArm.y = 8.0F;
			this.leftArm.x = 1.5F;
			this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F; //Right arm wave from -14.3 to 14.3 degrees
			this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F; //Left arm wave from -14.3 to 14.3 degrees
			this.rightArm.zRot = 1.9F; //Constant z rotation for arm
			this.leftArm.zRot = -1.9F; //Constant z rotation for arm
			this.rightArm.yRot = 0.0F; //Constant y rotation for arm
			this.leftArm.yRot = 0.0F;//Constant y rotation for arm
		}
	}
}
