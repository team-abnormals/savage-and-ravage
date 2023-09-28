package com.teamabnormals.savage_and_ravage.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Creepie;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

/**
 * CreepieModel - TallestEgg
 * Created using Tabula 7.1.0
 */
public class CreepieModel extends ListModel<Creepie> {
	public ModelPart head;
	public ModelPart sprout;
	public ModelPart body;
	public ModelPart leg1;
	public ModelPart leg2;
	public ModelPart leg3;
	public ModelPart leg4;

	public CreepieModel(ModelPart root) {
		this.head = root.getChild("head");
		this.sprout = this.head.getChild("sprout");
		this.body = root.getChild("body");
		this.leg1 = root.getChild("leg1");
		this.leg2 = root.getChild("leg2");
		this.leg3 = root.getChild("leg3");
		this.leg4 = root.getChild("leg4");
	}

	public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition sprout = head.addOrReplaceChild("sprout", CubeListBuilder.create().texOffs(48, 16).addBox(0.0F, -24.0F, -4.0F, 0.0F, 8.0F, 8.0F, deformation).texOffs(48, 16).addBox(-4.0F, -24.0F, 0.0F, 8.0F, 8.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(2.0F, 18.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(-2.0F, 18.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(2.0F, 18.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offsetAndRotation(-2.0F, 18.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.pushPose();
		poseStack.scale(1.5F, 1.5F, 1.5F);
		this.head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		poseStack.popPose();
		this.parts().forEach(modelPart -> modelPart.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
	}

	@Override
	public void setupAnim(Creepie entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * 0.017453292F;
		this.head.xRot = headPitch * 0.017453292F;
		this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.leg4, this.body, this.leg1, this.leg3, this.leg2);
	}
}

