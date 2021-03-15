package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.client.model.ExecutionerModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.util.ResourceLocation;

public class ExecutionerRenderer extends MobRenderer<VindicatorEntity, ExecutionerModel> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("savageandravage:textures/entity/executioner.png");

	public ExecutionerRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new ExecutionerModel(0), 0.5f);
		this.addLayer(new HeldItemLayer<VindicatorEntity, ExecutionerModel>(this) {
			@Override
			public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, VindicatorEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.isAggressive()) {
					super.render(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
	}

	@Override
	public ResourceLocation getEntityTexture(VindicatorEntity entity) {
		return TEXTURE;
	}

	@Override
	protected void preRenderCallback(VindicatorEntity entity, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
