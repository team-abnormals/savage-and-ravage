package com.farcr.savageandravage.client.render;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

//Used for CreeperSpores and BurningBanner - entities with no model
public class NoModelRenderer extends EntityRenderer<Entity> {
    public NoModelRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }
    
    /*@Override
    public boolean shouldRender(Entity livingEntityIn, ClippingHelperImpl camera, double camX, double camY, double camZ) {
		return false;
    }*/

    @Nullable
    @Override
	public ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
