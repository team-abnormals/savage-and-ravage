package com.teamabnormals.savageandravage.client.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;

// Used for CreeperSpores and BurningBanner - entities with no model
public class NoModelRenderer extends EntityRenderer<Entity> {

	public NoModelRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Entity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
