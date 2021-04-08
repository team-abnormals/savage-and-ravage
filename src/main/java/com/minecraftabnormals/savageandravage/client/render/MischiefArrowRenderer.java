package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.common.entity.MischiefArrowEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class MischiefArrowRenderer extends ArrowRenderer<MischiefArrowEntity> {
	private static final ResourceLocation MISCHIEF_ARROW_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/projectiles/mischief_arrow.png");

	public MischiefArrowRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getEntityTexture(MischiefArrowEntity entity) {
		return MISCHIEF_ARROW_TEXTURE;
	}
}