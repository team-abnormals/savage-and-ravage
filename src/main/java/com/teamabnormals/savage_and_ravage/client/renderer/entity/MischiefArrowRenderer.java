package com.teamabnormals.savage_and_ravage.client.renderer.entity;

import com.teamabnormals.savage_and_ravage.common.entity.projectile.MischiefArrow;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MischiefArrowRenderer extends ArrowRenderer<MischiefArrow> {
	private static final ResourceLocation MISCHIEF_ARROW_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/projectiles/mischief_arrow.png");

	public MischiefArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(MischiefArrow entity) {
		return MISCHIEF_ARROW_TEXTURE;
	}
}