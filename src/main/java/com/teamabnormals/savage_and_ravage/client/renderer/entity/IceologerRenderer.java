package com.teamabnormals.savage_and_ravage.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.savage_and_ravage.client.model.IceologerModel;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Iceologer;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Ocelot
 */
public class IceologerRenderer extends MobRenderer<Iceologer, IceologerModel> {
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/iceologer.png");

	public IceologerRenderer(EntityRendererProvider.Context context) {
		super(context, new IceologerModel(context.bakeLayer(SRModelLayers.ICEOLOGER)), 0.5F);
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(Iceologer entity) {
		return TEXTURE_LOCATION;
	}

	@Override
	protected void scale(Iceologer entity, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}
}