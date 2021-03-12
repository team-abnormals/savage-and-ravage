package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.common.entity.TricksterEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.util.ResourceLocation;

public class TricksterRenderer extends IllagerRenderer<TricksterEntity> {
    private static final ResourceLocation TRICKSTER_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/trickster.png");

    public TricksterRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new IllagerModel<>(0.0F, 0.0F, 64, 64), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(TricksterEntity entity) {
        return TRICKSTER_TEXTURE;
    }
}
