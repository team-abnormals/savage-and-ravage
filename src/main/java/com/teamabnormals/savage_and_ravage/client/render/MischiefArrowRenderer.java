package com.teamabnormals.savage_and_ravage.client.render;

import com.teamabnormals.savage_and_ravage.common.entity.MischiefArrowEntity;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MischiefArrowRenderer extends ArrowRenderer<MischiefArrowEntity> {
	private static final ResourceLocation MISCHIEF_ARROW_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/projectiles/mischief_arrow.png");

	public MischiefArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(MischiefArrowEntity entity) {
		return MISCHIEF_ARROW_TEXTURE;
	}
}