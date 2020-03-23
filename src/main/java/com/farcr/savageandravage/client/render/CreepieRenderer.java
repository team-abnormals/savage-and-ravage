package com.farcr.savageandravage.client.render;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.util.ResourceLocation;

public class CreepieRenderer extends MobRenderer<CreepieEntity, CreeperModel<CreepieEntity>>{
    //private static final ResourceLocation CREEPIE_TEXTURES = new ResourceLocation("savageandravage:textures/entity/creepie.png");
    private static final ResourceLocation CREEPIE_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
    
    public CreepieRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new CreeperModel<CreepieEntity>(), 0.3F);
    }

    protected void preRenderCallback(CreepieEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
     }

    @Override
    public ResourceLocation getEntityTexture(CreepieEntity entity) {
        return CREEPIE_TEXTURES;
    }

}