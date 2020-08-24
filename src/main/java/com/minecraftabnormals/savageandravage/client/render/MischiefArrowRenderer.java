package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.common.entity.MischiefArrowEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class MischiefArrowRenderer extends ArrowRenderer<MischiefArrowEntity> {

   public MischiefArrowRenderer(EntityRendererManager manager) {
      super(manager);
   }

   public ResourceLocation getEntityTexture(MischiefArrowEntity entity) {
      return new ResourceLocation(SavageAndRavage.MODID, "textures/entity/projectiles/mischief_arrow.png");
   }
}