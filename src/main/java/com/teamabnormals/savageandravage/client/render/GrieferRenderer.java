package com.teamabnormals.savageandravage.client.render;

import com.teamabnormals.savageandravage.client.model.GrieferModel;
import com.teamabnormals.savageandravage.client.model.VillagerArmorModel;
import com.teamabnormals.savageandravage.common.entity.GrieferEntity;
import com.teamabnormals.savageandravage.core.SavageAndRavage;
import com.teamabnormals.savageandravage.core.other.SRModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class GrieferRenderer extends HumanoidMobRenderer<GrieferEntity, GrieferModel> {
	private static final ResourceLocation GRIEFER_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/griefer/griefer.png");
	private static final ResourceLocation APESHIT_MODE_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/griefer/griefer_melee.png");

	public GrieferRenderer(EntityRendererProvider.Context context) {
		super(context, new GrieferModel(context.bakeLayer(SRModelLayers.GRIEFER)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new VillagerArmorModel<>(context.bakeLayer(SRModelLayers.VILLAGER_INNER_ARMOR)), new VillagerArmorModel<>(context.bakeLayer(SRModelLayers.VILLAGER_OUTER_ARMOR))));
	}

	@Override
	public ResourceLocation getTextureLocation(GrieferEntity entity) {
		return entity.isApeshit() ? APESHIT_MODE_TEXTURE : GRIEFER_TEXTURE;
	}

	@Override
	protected void scale(GrieferEntity entity, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
