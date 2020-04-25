package com.farcr.savageandravage.client.render;

import com.farcr.savageandravage.common.entity.BurningBannerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class BurningBannerRenderer extends EntityRenderer<BurningBannerEntity> {
    public BurningBannerRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
    }

    @Override
    public ResourceLocation getEntityTexture(BurningBannerEntity entity) {
        return null;
    }
}
