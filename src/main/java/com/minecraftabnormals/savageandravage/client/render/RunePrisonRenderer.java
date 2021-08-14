package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.abnormals_core.client.ACRenderTypes;
import com.minecraftabnormals.savageandravage.client.model.RunePrisonModel;
import com.minecraftabnormals.savageandravage.common.entity.RunePrisonEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RunePrisonRenderer extends EntityRenderer<RunePrisonEntity> {

	public static final ResourceLocation[] RUNE_PRISON_FRAMES = new ResourceLocation[]{
			new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/rune_prison/rune_prison_0.png"),
			new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/rune_prison/rune_prison_1.png"),
			new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/rune_prison/rune_prison_2.png"),
			new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/rune_prison/rune_prison_3.png"),
			new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/rune_prison/rune_prison_4.png"),
	};
	private final RunePrisonModel model;

	public RunePrisonRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowRadius = 0.0F;
		model = new RunePrisonModel();
	}

	@Override
	public void render(RunePrisonEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(0.15F, -0.7F, 0.15F);
		this.model.setupAnim(entityIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEmissiveTransluscentEntity(getTextureLocation(entityIn), false));
		this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(RunePrisonEntity entity) {
		return RUNE_PRISON_FRAMES[entity.getCurrentFrame()];
	}
}
