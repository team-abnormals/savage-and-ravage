package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.client.model.IceologerModel;
import com.minecraftabnormals.savageandravage.common.entity.IceologerEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.other.SRModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Ocelot
 */
public class IceologerRenderer extends MobRenderer<IceologerEntity, IceologerModel> {
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/iceologer.png");

	public IceologerRenderer(EntityRendererProvider.Context context) {
		super(context, new IceologerModel(context.bakeLayer(SRModelLayers.ICEOLOGER)), 0.5F);
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(IceologerEntity entity) {
		return TEXTURE_LOCATION;
	}

	@Override
	protected void scale(IceologerEntity entity, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}
}