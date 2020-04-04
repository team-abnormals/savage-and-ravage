package com.farcr.savageandravage.client.render;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class CreeperSporeRenderer extends EntityRenderer<Entity> {
    public CreeperSporeRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public boolean shouldRender(Entity livingEntityIn, ClippingHelperImpl camera, double camX, double camY, double camZ) {
		return false;
    }

    @Nullable
    @Override
	public ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
