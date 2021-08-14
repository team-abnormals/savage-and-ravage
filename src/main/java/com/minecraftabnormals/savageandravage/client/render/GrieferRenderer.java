package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.client.model.GrieferModel;
import com.minecraftabnormals.savageandravage.client.model.VillagerArmorModel;
import com.minecraftabnormals.savageandravage.common.entity.GrieferEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class GrieferRenderer extends BipedRenderer<GrieferEntity, GrieferModel> {

	private static final ResourceLocation GRIEFER_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/griefer/griefer.png");
	private static final ResourceLocation APESHIT_MODE_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/griefer/griefer_melee.png");

	public GrieferRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new GrieferModel(0.0F), 0.5F);
		this.addLayer(new BipedArmorLayer<>(this, new VillagerArmorModel<>(0.5F), new VillagerArmorModel<>(1.0F)));
	}

	@Override
	public ResourceLocation getTextureLocation(GrieferEntity entity) {
		return entity.isApeshit() ? APESHIT_MODE_TEXTURE : GRIEFER_TEXTURE;
	}

	@Override
	protected void scale(GrieferEntity entity, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
