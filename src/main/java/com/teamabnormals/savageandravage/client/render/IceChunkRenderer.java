package com.teamabnormals.savageandravage.client.render;

import com.teamabnormals.savageandravage.common.entity.IceChunkEntity;
import com.teamabnormals.savageandravage.core.SavageAndRavage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

/**
 * @author Ocelot
 */
public class IceChunkRenderer extends EntityRenderer<IceChunkEntity> {
	public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(SavageAndRavage.MOD_ID, "entity/ice_chunk");
	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/ice_chunk.png");

	public IceChunkRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(IceChunkEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.pushPose();
		matrixStack.translate(-0.5, 0, -0.5);

		BakedModel model = Minecraft.getInstance().getModelManager().getModel(MODEL_LOCATION);
		renderModel(model, buffer.getBuffer(RenderType.solid()), matrixStack, packedLight);
		matrixStack.popPose();

		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(IceChunkEntity entity) {
		return TEXTURE_LOCATION;
	}

	/**
	 * <p>Snippet from Sonar</p>
	 * Renders the specified model into the provided buffer.
	 *
	 * @param model       The model to render
	 * @param builder     The builder to put the model into
	 * @param matrixStack The stack of transformations to move elements
	 * @param packedLight The packed uv into the light texture the parts should be rendered at
	 */
	private static void renderModel(BakedModel model, VertexConsumer builder, PoseStack matrixStack, int packedLight) {
		Random random = new Random(42L);
		for (Direction direction : Direction.values()) {
			for (BakedQuad quad : model.getQuads(null, direction, random, EmptyModelData.INSTANCE)) {
				builder.putBulkData(matrixStack.last(), quad, 1, 1, 1, packedLight, OverlayTexture.NO_OVERLAY);
			}
		}
		for (BakedQuad quad : model.getQuads(null, null, random, EmptyModelData.INSTANCE)) {
			builder.putBulkData(matrixStack.last(), quad, 1, 1, 1, packedLight, OverlayTexture.NO_OVERLAY);
		}
	}
}