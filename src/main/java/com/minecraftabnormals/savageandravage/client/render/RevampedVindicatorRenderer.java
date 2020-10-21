package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.client.model.RevampedVindicatorModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.util.ResourceLocation;

public class RevampedVindicatorRenderer extends MobRenderer<VindicatorEntity, RevampedVindicatorModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("savageandravage:textures/entity/vindicator.png");

    public RevampedVindicatorRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new RevampedVindicatorModel(0), 0.5f);
        this.addLayer(new HeldItemLayer<VindicatorEntity, RevampedVindicatorModel>(this) {
            @Override
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, VindicatorEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entitylivingbaseIn.isAggressive()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
    }

    @Override
    public ResourceLocation getEntityTexture(VindicatorEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void preRenderCallback(VindicatorEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
