package com.minecraftabnormals.savageandravage.client.render.layer;

import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SproutRenderLayer<E extends CreepieEntity, M extends EntityModel<E>> extends LayerRenderer<E, M> {
	private static final ResourceLocation SPROUT_TEXTURE = new ResourceLocation(SavageAndRavage.MODID, "textures/entity/creepie_sprout.png");

	public SproutRenderLayer(IEntityRenderer<E, M> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!SRConfig.CLIENT.creepieSprout.get()) return;
		matrixStackIn.push();
		matrixStackIn.translate(0.0F, 0.125F, 0.0F);
		matrixStackIn.scale(1.5F, 1.5F, 1.5F);
		IVertexBuilder builder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(SPROUT_TEXTURE));
		this.getEntityModel().setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.getEntityModel().render(matrixStackIn, builder, packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStackIn.pop();
	}
}