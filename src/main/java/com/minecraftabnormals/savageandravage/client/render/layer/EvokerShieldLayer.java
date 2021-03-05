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
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.util.ResourceLocation;

public class EvokerShieldLayer extends LayerRenderer<EvokerEntity, IllagerModel<EvokerEntity>> {
    private static final ResourceLocation SHIELD_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final IllagerModel<EvokerEntity> evokerModel = new IllagerModel<>(2.0F, 0.0F, 64, 64); //Hardcoding here isn't ideal

    public EvokerShieldLayer(IEntityRenderer<EvokerEntity, IllagerModel<EvokerEntity>> entityRenderer) {
        super(entityRenderer);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EvokerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (SRConfig.COMMON.evokersUseTotems.get() && ((IDataManager) entitylivingbaseIn).getValue(SREntities.EVOKER_SHIELD_TIME) > 0) {
            float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
            evokerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.getEntityModel().copyModelAttributesTo(evokerModel);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(this.func_225633_a_(), this.func_225634_a_(f), f * 0.01F));
            evokerModel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            evokerModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
        }
    }

    protected float func_225634_a_(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    protected ResourceLocation func_225633_a_() {
        return SHIELD_TEXTURE;
    }
}
