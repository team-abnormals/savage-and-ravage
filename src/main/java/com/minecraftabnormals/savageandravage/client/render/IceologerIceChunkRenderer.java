package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.common.entity.IceologerIceChunkEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

/**
 * @author Ocelot
 */
public class IceologerIceChunkRenderer extends EntityRenderer<IceologerIceChunkEntity> {

    public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(SavageAndRavage.MODID, "entity/iceologer_ice_chunk");
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SavageAndRavage.MODID, "textures/entity/iceologer_ice_chunk.png");

    public IceologerIceChunkRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(IceologerIceChunkEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.push();
        matrixStack.translate(-0.5, 0, -0.5);

        IBakedModel model = Minecraft.getInstance().getModelManager().getModel(MODEL_LOCATION);
        renderModel(model, buffer.getBuffer(RenderType.getSolid()), matrixStack, packedLight);
        matrixStack.pop();

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getEntityTexture(IceologerIceChunkEntity entity) {
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
    private static void renderModel(IBakedModel model, IVertexBuilder builder, MatrixStack matrixStack, int packedLight) {
        Random random = new Random(42L);
        for (Direction direction : Direction.values()) {
            for (BakedQuad quad : model.getQuads(null, direction, random, EmptyModelData.INSTANCE)) {
                builder.addQuad(matrixStack.getLast(), quad, 1, 1, 1, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }
        for (BakedQuad quad : model.getQuads(null, null, random, EmptyModelData.INSTANCE)) {
            builder.addQuad(matrixStack.getLast(), quad, 1, 1, 1, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}