package com.farcr.savageandravage.client.model;

import com.farcr.savageandravage.common.entity.GrieferEntity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class GrieferEntityArmorModel extends BipedModel<GrieferEntity> 
{
	public float kickingTime;
	
	public GrieferEntityArmorModel(float modelSize) {
		super(modelSize);
	    this.bipedHead = new ModelRenderer(this, 0, 0);
	    this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);
	    this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
	}
	
	@Override
	public void setRotationAngles(GrieferEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netbipedbipedHeadYaw, float bipedbipedHeadPitch){
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netbipedbipedHeadYaw, bipedbipedHeadPitch);
        if (entityIn.isKicking())
        {
        	 float f1 = MathHelper.clamp(this.kickingTime,  0.0F, 25.0F);
        	 this.bipedRightLeg.rotateAngleX = MathHelper.lerp(f1 / 10.0F, -1.40F, 1.05F);
        }
        if (entityIn.func_213656_en()) //party rockers in the hou
        {
            this.bipedHead.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
            this.bipedLeftArm.rotationPointZ = 0.0F;
            this.bipedLeftArm.rotationPointX = 5.0F;
            this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.7000F) * 0.05F;
            this.bipedLeftArm.rotateAngleZ = -2.3561945F;
            this.bipedLeftArm.rotateAngleY = 0.0F;
        }
	}
	
    public void setLivingAnimations(GrieferEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.kickingTime = (float)entityIn.getKickTicks();
        super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
     }
}
