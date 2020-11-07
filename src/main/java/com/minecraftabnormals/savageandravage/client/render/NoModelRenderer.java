package com.minecraftabnormals.savageandravage.client.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;

// Used for CreeperSpores and BurningBanner - entities with no model
public class NoModelRenderer extends EntityRenderer<Entity> {

    public NoModelRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }
}
