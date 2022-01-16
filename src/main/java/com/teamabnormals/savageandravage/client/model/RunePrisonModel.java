package com.teamabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.savageandravage.common.entity.RunePrisonEntity;
import com.teamabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class RunePrisonModel extends ListModel<RunePrisonEntity> {
	public static final ModelLayerLocation RUNE_PRISON = new ModelLayerLocation(new ResourceLocation(SavageAndRavage.MOD_ID, "rune_prison"), "main");

	public ModelPart plane;

	public RunePrisonModel(ModelPart root) {
		this.plane = root.getChild("plane");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition plane = root.addOrReplaceChild("plane", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 21.0F, 0.0F, 21.0F, false), PartPose.offsetAndRotation(-8.0F, 24.0F, -8.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 42, 42);
	}

	@Override
	public void setupAnim(RunePrisonEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.plane);
	}
}
