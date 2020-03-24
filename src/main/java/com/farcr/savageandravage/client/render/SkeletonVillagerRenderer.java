package com.farcr.savageandravage.client.render;

import javax.annotation.Nullable;

import com.farcr.savageandravage.client.model.SkeletonVillagerModel;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class SkeletonVillagerRenderer extends MobRenderer<SkeletonVillagerEntity, SkeletonVillagerModel>
{
    private static final ResourceLocation SKELETON_VILLAGER_TEXTURES = new ResourceLocation("savageandravage:textures/entity/skeleton_villager.png");

    public SkeletonVillagerRenderer(EntityRendererManager manager) 
    {
        super(manager, new SkeletonVillagerModel(), 0.5f);
        this.addLayer(new HeldItemLayer<SkeletonVillagerEntity, SkeletonVillagerModel>(this) {
        public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, SkeletonVillagerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.isAggressive()) {
               super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
         }
      });
   }

    @Nullable
    @Override
	public ResourceLocation getEntityTexture(SkeletonVillagerEntity entity) 
    {
    	return SKELETON_VILLAGER_TEXTURES;	
    }
}