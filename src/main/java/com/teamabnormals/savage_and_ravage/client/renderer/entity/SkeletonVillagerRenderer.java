package com.teamabnormals.savage_and_ravage.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.savage_and_ravage.client.model.SkeletonVillagerModel;
import com.teamabnormals.savage_and_ravage.client.model.VillagerArmorModel;
import com.teamabnormals.savage_and_ravage.common.entity.monster.SkeletonVillager;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SkeletonVillagerRenderer extends MobRenderer<SkeletonVillager, SkeletonVillagerModel> {
	private static final ResourceLocation SKELETON_VILLAGER_TEXTURES = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/skeleton_villager.png");

	public SkeletonVillagerRenderer(EntityRendererProvider.Context context) {
		super(context, new SkeletonVillagerModel(context.bakeLayer(SRModelLayers.SKELETON_VILLAGER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer<>(this, new VillagerArmorModel<>(context.bakeLayer(SRModelLayers.VILLAGER_INNER_ARMOR)), new VillagerArmorModel<>(context.bakeLayer(SRModelLayers.VILLAGER_OUTER_ARMOR))));
		this.addLayer(new ItemInHandLayer<>(this) {
			@Override
			public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, SkeletonVillager entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.isAggressive()) {
					super.render(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
	}

	@Override
	protected void scale(SkeletonVillager entity, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}

	@Override
	public ResourceLocation getTextureLocation(SkeletonVillager entity) {
		return SKELETON_VILLAGER_TEXTURES;
	}
}