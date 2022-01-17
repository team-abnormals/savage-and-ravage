package com.teamabnormals.savage_and_ravage.client.renderer.entity;

import com.teamabnormals.savage_and_ravage.client.model.TricksterModel;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Trickster;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class TricksterRenderer extends MobRenderer<Trickster, TricksterModel> {
	private static final ResourceLocation NORMAL = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/trickster/trickster.png");
	private static final ResourceLocation BASED = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/trickster/trickster_based.png");

	public TricksterRenderer(EntityRendererProvider.Context context) {
		super(context, new TricksterModel(context.bakeLayer(SRModelLayers.TRICKSTER)), 0.5f);
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(Trickster entity) {
		return entity.getName().getString().equalsIgnoreCase("based") ? BASED : NORMAL;
	}
}
