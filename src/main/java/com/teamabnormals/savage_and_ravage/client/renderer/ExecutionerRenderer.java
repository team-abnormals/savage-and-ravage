package com.teamabnormals.savage_and_ravage.client.renderer;

import com.teamabnormals.savage_and_ravage.client.model.ExecutionerModel;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vindicator;

public class ExecutionerRenderer extends MobRenderer<Vindicator, ExecutionerModel> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/executioner.png");

	public ExecutionerRenderer(EntityRendererProvider.Context context) {
		super(context, new ExecutionerModel(context.bakeLayer(SRModelLayers.EXECUTIONER)), 0.5f);
		this.addLayer(new ItemInHandLayer<>(this) {
			@Override
			public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Vindicator entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.isAggressive()) {
					super.render(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(Vindicator entity) {
		return TEXTURE;
	}

	@Override
	protected void scale(Vindicator entity, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
