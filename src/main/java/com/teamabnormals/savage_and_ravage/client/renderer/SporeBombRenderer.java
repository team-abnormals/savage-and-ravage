package com.teamabnormals.savage_and_ravage.client.renderer;

import com.teamabnormals.savage_and_ravage.common.entity.item.SporeBomb;
import com.teamabnormals.savage_and_ravage.core.registry.SRBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

public class SporeBombRenderer extends EntityRenderer<SporeBomb> {

	public SporeBombRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}

	public void render(SporeBomb entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		if ((float) entityIn.getFuse() - partialTicks + 1.0F < 10.0F) {
			float f = 1.0F - ((float) entityIn.getFuse() - partialTicks + 1.0F) / 10.0F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f = f * f;
			f = f * f;
			float f1 = 1.0F + f * 0.3F;
			matrixStackIn.scale(f1, f1, f1);
		}

		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
		matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
		SporeBombRenderer.renderBombFlash(SRBlocks.SPORE_BOMB.get().defaultBlockState(), matrixStackIn, bufferIn, packedLightIn, entityIn.getFuse() / 5 % 2 == 0);
		matrixStackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	public static void renderBombFlash(BlockState blockStateIn, PoseStack matrixStackIn, MultiBufferSource renderTypeBuffer, int combinedLight, boolean doFullBright) {
		int i;
		if (doFullBright) {
			i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
		} else {
			i = OverlayTexture.NO_OVERLAY;
		}

		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockStateIn, matrixStackIn, renderTypeBuffer, combinedLight, i, EmptyModelData.INSTANCE);
	}

	@Override
	public ResourceLocation getTextureLocation(SporeBomb entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}