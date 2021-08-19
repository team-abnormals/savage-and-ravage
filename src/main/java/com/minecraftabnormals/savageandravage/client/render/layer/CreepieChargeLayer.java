package com.minecraftabnormals.savageandravage.client.render.layer;

import com.minecraftabnormals.savageandravage.client.model.CreepieModel;
import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
	public void render(MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, CreepieEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
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