package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.client.model.TricksterModel;
import com.minecraftabnormals.savageandravage.common.entity.TricksterEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.util.ResourceLocation;

public class TricksterRenderer extends MobRenderer<TricksterEntity, TricksterModel> {
    private static final ResourceLocation TRICKSTER_TEXTURE = new ResourceLocation(SavageAndRavage.MOD_ID, "textures/entity/trickster.png");

    public TricksterRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new TricksterModel(), 0.5f);
        this.addLayer(new HeadLayer<>(this));
    }

    @Override
    public ResourceLocation getEntityTexture(TricksterEntity entity) {
        return TRICKSTER_TEXTURE;
    }
}
