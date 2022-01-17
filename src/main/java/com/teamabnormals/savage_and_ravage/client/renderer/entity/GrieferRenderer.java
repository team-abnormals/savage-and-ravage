package com.teamabnormals.savage_and_ravage.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.savage_and_ravage.client.model.GrieferModel;
import com.teamabnormals.savage_and_ravage.client.model.VillagerArmorModel;
import com.teamabnormals.savage_and_ravage.common.entity.monster.Griefer;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class GrieferRenderer extends HumanoidMobRenderer<Griefer, GrieferModel> {
	private static final ResourceLocation GRIEFER_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/griefer/griefer.png");
	private static final ResourceLocation APESHIT_MODE_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/griefer/griefer_melee.png");

	public GrieferRenderer(EntityRendererProvider.Context context) {
		super(context, new GrieferModel(context.bakeLayer(SRModelLayers.GRIEFER)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new VillagerArmorModel<>(context.bakeLayer(SRModelLayers.VILLAGER_INNER_ARMOR)), new VillagerArmorModel<>(context.bakeLayer(SRModelLayers.VILLAGER_OUTER_ARMOR))));
	}

	@Override
	public ResourceLocation getTextureLocation(Griefer entity) {
		return entity.isApeshit() ? APESHIT_MODE_TEXTURE : GRIEFER_TEXTURE;
	}

	@Override
	protected void scale(Griefer entity, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}
}
