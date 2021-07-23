package com.minecraftabnormals.savageandravage.client.render.layer;

import com.minecraftabnormals.savageandravage.client.model.CreepieModel;
import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreepieChargeLayer extends EnergyLayer<CreepieEntity, CreepieModel> {
	private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final CreepieModel creepieModel = new CreepieModel(2.0F);

	public CreepieChargeLayer(IEntityRenderer<CreepieEntity, CreepieModel> entityRenderer) {
		super(entityRenderer);
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