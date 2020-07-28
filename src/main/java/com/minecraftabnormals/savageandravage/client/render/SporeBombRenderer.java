package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SporeBombRenderer extends EntityRenderer<SporeBombEntity> 
{
	   public SporeBombRenderer(EntityRendererManager renderManagerIn) {
	      super(renderManagerIn);
	      this.shadowSize = 0.5F;
	   }

	   public void render(SporeBombEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) 
	   {
		  matrixStackIn.push();
		  matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		  if ((float)entityIn.getFuse() - partialTicks + 1.0F < 10.0F) 
		  {
		    float f = 1.0F - ((float)entityIn.getFuse() - partialTicks + 1.0F) / 10.0F;
		    f = MathHelper.clamp(f, 0.0F, 1.0F);
		    f = f * f;
	        f = f * f;
	        float f1 = 1.0F + f * 0.3F;
	        matrixStackIn.scale(f1, f1, f1);
		  }

		  matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90.0F));
		  matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
		  matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
		  SporeBombRenderer.renderBombFlash(SRBlocks.SPORE_BOMB.get().getDefaultState(), matrixStackIn, bufferIn, packedLightIn, entityIn.getFuse() / 5 % 2 == 0);
		  matrixStackIn.pop();
		  super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	   }
	   
	   @SuppressWarnings("deprecation")
	   public static void renderBombFlash(BlockState blockStateIn, MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, int combinedLight, boolean doFullBright) 
	   {
		      int i;
		      if (doFullBright) {
		         i = OverlayTexture.getPackedUV(OverlayTexture.getU(1.0F), 10);
		      } else {
		         i = OverlayTexture.NO_OVERLAY;
		      }

		      Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(blockStateIn, matrixStackIn, renderTypeBuffer, combinedLight, i);
		   }

	   public ResourceLocation getEntityTexture(SporeBombEntity entity) {
		   return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
	   }
	}