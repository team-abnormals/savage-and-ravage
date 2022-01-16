package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.client.model.TricksterModel;
import com.minecraftabnormals.savageandravage.common.entity.TricksterEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.other.SRModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class TricksterRenderer extends MobRenderer<TricksterEntity, TricksterModel> {
	private static final ResourceLocation NORMAL = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/trickster/trickster.png");
	private static final ResourceLocation BASED = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/trickster/trickster_based.png");

	public TricksterRenderer(EntityRendererProvider.Context context) {
		super(context, new TricksterModel(context.bakeLayer(SRModelLayers.TRICKSTER)), 0.5f);
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(TricksterEntity entity) {
		return entity.getName().getString().equalsIgnoreCase("based") ? BASED : NORMAL;
	}
}
