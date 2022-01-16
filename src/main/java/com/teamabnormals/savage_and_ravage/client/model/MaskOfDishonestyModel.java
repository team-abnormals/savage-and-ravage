package com.teamabnormals.savage_and_ravage.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class MaskOfDishonestyModel<T extends LivingEntity> extends HumanoidModel<T> {
	public ModelPart nose;

	public MaskOfDishonestyModel(ModelPart root) {
		super(root);
		this.nose = this.head.getChild("nose");
	}

	public static LayerDefinition createArmorLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, -2.5F, -8.0F, 2.0F, 2.0F, 4.0F, false), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 32, 32);
	}
}
