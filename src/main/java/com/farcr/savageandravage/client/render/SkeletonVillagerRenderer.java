package com.farcr.savageandravage.client.render;

import javax.annotation.Nullable;

import com.farcr.savageandravage.client.model.SkeletonVillagerModel;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class SkeletonVillagerRenderer extends MobRenderer<SkeletonVillagerEntity, SkeletonVillagerModel>
{
    private static final ResourceLocation SKELETON_VILLAGER_TEXTURES = new ResourceLocation("savageandravage:textures/entity/skeletonvillager.png");

    public SkeletonVillagerRenderer(EntityRendererManager manager) 
    {
        super(manager, new SkeletonVillagerModel(), 0.5f);
        this.addLayer(new HeldItemLayer<>(this));
    }

    @Nullable
    @Override
	public ResourceLocation getEntityTexture(SkeletonVillagerEntity entity) 
    {
    	return SKELETON_VILLAGER_TEXTURES;	
    }
}