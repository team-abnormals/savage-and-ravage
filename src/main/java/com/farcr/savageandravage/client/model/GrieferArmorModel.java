package com.farcr.savageandravage.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * ModelGrieferArmor - MCVinnyq & Farcr
 * Created using Tabula 7.1.0
 */
public class GrieferArmorModel<T extends LivingEntity> extends BipedModel<T> {
    public ModelRenderer helmet1;
    public ModelRenderer helmet2;
    public ModelRenderer chestplate1;
    public ModelRenderer chestplate2;
    public ModelRenderer leggingsright;
    public ModelRenderer leggingsleft;
    public ModelRenderer bootsright;
    public ModelRenderer bootsleft;
    public ModelRenderer shoulderpadright;
    public ModelRenderer shoulderpadleft;
    
    private EquipmentSlotType slot;

    public GrieferArmorModel(float modelSize, EquipmentSlotType slot) {
    	super(modelSize, 0.0F, 64, 64);
    	this.slot = slot;
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shoulderpadright = new ModelRenderer(this, 42, 20);
        this.shoulderpadright.setRotationPoint(-5.0F, 1.0F, 0.0F);
        this.shoulderpadright.addBox(-4.0F, -2.0F, -3.0F, 5, 6, 6, 0.3F);
        this.leggingsleft = new ModelRenderer(this, 48, 50);
        this.leggingsleft.mirror = true;
        this.leggingsleft.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.leggingsleft.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.25F);
        this.bootsleft = new ModelRenderer(this, 32, 48);
        this.bootsleft.mirror = true;
        this.bootsleft.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bootsleft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F);
        this.chestplate2 = new ModelRenderer(this, 0, 34);
        this.chestplate2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.chestplate2.addBox(-4.0F, 0.0F, -2.0F, 8, 15, 4, 0.3F);
        this.helmet2 = new ModelRenderer(this, 36, 0);
        this.helmet2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmet2.addBox(-1.0F, -11.0F, -6.1F, 2, 8, 12, 0.125F);
        this.chestplate1 = new ModelRenderer(this, 0, 17);
        this.chestplate1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.chestplate1.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, 0.25F);
        this.leggingsright = new ModelRenderer(this, 48, 50);
        this.leggingsright.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.leggingsright.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.25F);
        this.bootsright = new ModelRenderer(this, 32, 48);
        this.bootsright.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bootsright.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F);
        this.helmet1 = new ModelRenderer(this, 0, 0);
        this.helmet1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmet1.addBox(-4.0F, -8.0F, -4.0F, 8, 9, 8, 0.5F);
        this.shoulderpadleft = new ModelRenderer(this, 42, 20);
        this.shoulderpadleft.mirror = true;
        this.shoulderpadleft.setRotationPoint(5.0F, 1.0F, 0.0F);
        this.shoulderpadleft.addBox(-1.0F, -2.0F, -3.0F, 5, 6, 6, 0.3F);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    	if (this.slot == EquipmentSlotType.HEAD) {
            this.helmet1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.helmet2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    	}
    	
    	if (this.slot == EquipmentSlotType.CHEST) {
            this.chestplate1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.chestplate2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.shoulderpadleft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.shoulderpadright.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    	}
    	
    	if (this.slot == EquipmentSlotType.LEGS) {
            this.leggingsleft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            this.leggingsright.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    	}

		if (this.slot == EquipmentSlotType.FEET) {
	        this.bootsleft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	        this.bootsright.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
    }
    
    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    	
    	this.helmet1.copyModelAngles(this.bipedHead);
    	this.helmet2.copyModelAngles(this.bipedHead);
    	
    	this.shoulderpadleft.copyModelAngles(this.bipedLeftArm);
    	this.shoulderpadright.copyModelAngles(this.bipedRightArm);
    	
    	this.bootsleft.copyModelAngles(this.bipedLeftLeg);
    	this.bootsright.copyModelAngles(this.bipedRightLeg);
    	
    	this.leggingsleft.copyModelAngles(this.bipedLeftLeg);
    	this.leggingsright.copyModelAngles(this.bipedRightLeg);
    	
    	this.chestplate1.copyModelAngles(this.bipedBody);
    	this.chestplate2.copyModelAngles(this.bipedBody);
    }
}
