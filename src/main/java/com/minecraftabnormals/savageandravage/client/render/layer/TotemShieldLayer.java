package com.minecraftabnormals.savageandravage.client.render.layer;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TotemShieldLayer<E extends Entity, M extends EntityModel<E>> extends LayerRenderer<E, M> {
	private static final ResourceLocation SHIELD_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final EntityModel<E> model;

	public TotemShieldLayer(IEntityRenderer<E, M> entityRenderer, M scaledUpModel) {
		super(entityRenderer);
		this.model = scaledUpModel;
	}

	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if ((!(entity instanceof EvokerEntity) || SRConfig.COMMON.evokersUseTotems.get()) && ((IDataManager) entity).getValue(SREntities.TOTEM_SHIELD_TIME) > 0) {
			float f = (float) entity.ticksExisted + partialTicks;
			model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
			this.getEntityModel().copyModelAttributesTo(model);
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(this.func_225633_a_(), this.func_225634_a_(f), f * 0.01F));
			model.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		}
	}

	protected float func_225634_a_(float p_225634_1_) {
		return p_225634_1_ * 0.01F;
	}

	protected ResourceLocation func_225633_a_() {
		return SHIELD_TEXTURE;
	}
}
