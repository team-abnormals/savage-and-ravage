package com.farcr.savageandravage.client.render;

import com.farcr.savageandravage.client.model.CreepieModel;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class CreepieRenderer extends MobRenderer<CreepieEntity, CreepieModel<CreepieEntity>>{
    //private static final ResourceLocation CREEPIE_TEXTURES = new ResourceLocation("savageandravage:textures/entity/creepie.png");
    private static final ResourceLocation CREEPIE_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");

    public CreepieRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new CreepieModel(), 0.3F);
    }

    @Override
    public ResourceLocation getEntityTexture(CreepieEntity entity) {
        return CREEPIE_TEXTURES;
    }

}
