package com.teamabnormals.savageandravage.client.render.layer;

import com.teamabnormals.savageandravage.client.model.CreepieModel;
import com.teamabnormals.savageandravage.common.entity.CreepieEntity;
import com.teamabnormals.savageandravage.core.SRConfig;
import com.teamabnormals.savageandravage.core.other.SRModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreepieChargeLayer extends EnergySwirlLayer<CreepieEntity, CreepieModel> {
	private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final CreepieModel creepieModel;

	public CreepieChargeLayer(RenderLayerParent<CreepieEntity, CreepieModel> entityRenderer, EntityModelSet modelSet) {
		super(entityRenderer);
		this.creepieModel = new CreepieModel(modelSet.bakeLayer(SRModelLayers.CREEPIE_ARMOR));
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, CreepieEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		super.render(stack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
		this.creepieModel.sprout.visible = SRConfig.CLIENT.creepieSprout.get();
	}

	@Override
	protected float xOffset(float p_225634_1_) {
		return p_225634_1_ * 0.01F;
	}

	@Override
	protected ResourceLocation getTextureLocation() {
		return LIGHTNING_TEXTURE;
	}

	@Override
	protected EntityModel<CreepieEntity> model() {
		return this.creepieModel;
	}
}