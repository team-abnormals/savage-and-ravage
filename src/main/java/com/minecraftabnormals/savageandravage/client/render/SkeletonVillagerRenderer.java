package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.client.model.SkeletonVillagerModel;
import com.minecraftabnormals.savageandravage.client.model.VillagerArmorModel;
import com.minecraftabnormals.savageandravage.common.entity.SkeletonVillagerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class SkeletonVillagerRenderer extends MobRenderer<SkeletonVillagerEntity, SkeletonVillagerModel> {
	private static final ResourceLocation SKELETON_VILLAGER_TEXTURES = new ResourceLocation("savageandravage:textures/entity/skeleton_villager.png");

	public SkeletonVillagerRenderer(EntityRendererManager manager) {
		super(manager, new SkeletonVillagerModel(0.0F), 0.5f);
		this.addLayer(new BipedArmorLayer<>(this, new VillagerArmorModel<>(0.5F), new VillagerArmorModel<>(1.0F)));
		this.addLayer(new HeldItemLayer<SkeletonVillagerEntity, SkeletonVillagerModel>(this) {
			@Override
			public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, SkeletonVillagerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.isAggressive()) {
					super.render(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
	}

	@Override
	protected void scale(SkeletonVillagerEntity entity, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}

	@Override
	public ResourceLocation getTextureLocation(SkeletonVillagerEntity entity) {
		return SKELETON_VILLAGER_TEXTURES;
	}
}