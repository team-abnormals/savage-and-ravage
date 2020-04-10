package com.farcr.savageandravage.client.render;

import com.farcr.savageandravage.client.model.CreepieModel;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class CreepieRenderer extends MobRenderer<CreepieEntity, CreepieModel>{
    //private static final ResourceLocation CREEPIE_TEXTURES = new ResourceLocation("savageandravage:textures/entity/creepie.png");
    private static final ResourceLocation CREEPIE_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
    
    public CreepieRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new CreepieModel(), 0.3F);
    }

    @Override
    protected void preRenderCallback(CreepieEntity entityLivingBaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float creeperFlashIntensity = entityLivingBaseIn.getCreeperFlashIntensity(partialTickTime);
        float mathsThing = 1.0f + MathHelper.sin(creeperFlashIntensity * 100.0f) * creeperFlashIntensity * 0.01f;
        creeperFlashIntensity = MathHelper.clamp(creeperFlashIntensity, 0.0f, 1.0f);
        creeperFlashIntensity = creeperFlashIntensity * creeperFlashIntensity ;
        creeperFlashIntensity = creeperFlashIntensity * creeperFlashIntensity ;
        float multipliedByMathsThing = (1.0f + creeperFlashIntensity * 0.4f)  * mathsThing;
        float dividedByMathsThing = (1.0f + creeperFlashIntensity * 0.1f) / mathsThing;
        matrixStackIn.scale(multipliedByMathsThing, dividedByMathsThing, multipliedByMathsThing);
        matrixStackIn.scale(0.5F, 0.5F, 0.5F); //it small - i hope this doesn't conflict with swelling
        //the names for the maths variables are temporary - i just haven't bothered fully understanding what this does yet
    }
    
    protected float getOverlayProgress(CreepieEntity livingEntityIn, float partialTicks) {
        float flashIntensity = livingEntityIn.getCreeperFlashIntensity(partialTicks);
        return (int)(flashIntensity * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(flashIntensity, 0.5F, 1.0F);
     }

    @Override
    public ResourceLocation getEntityTexture(CreepieEntity entity) {
        return CREEPIE_TEXTURES;
    }

    protected void applyRotations(CreepieEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving.isConverting()) {
            rotationYaw += (float)(Math.cos((double)entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }

        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

}