package com.minecraftabnormals.savageandravage.client.model;

import com.minecraftabnormals.savageandravage.common.entity.TricksterEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.math.MathHelper;

public class TricksterModel extends BipedModel<TricksterEntity> {
    public ModelRenderer nose;
    public ModelRenderer leftDecor;
    public ModelRenderer rightDecor;
    public ModelRenderer leftWiggly;
    public ModelRenderer rightWiggly;

    public TricksterModel() {
        this(0.0F);
    }

    public TricksterModel(float size) {
        super(size, 0.0F, 64, 64);

        this.bipedBody = new ModelRenderer(this, 0, 18);
        this.bipedBody.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 4.0F, -3.0F, 8.0F, 10.0F, 6.0F, size);

        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -6.0F, -4.0F, 8.0F, 10.0F, 8.0F, size);
        this.bipedHeadwear.showModel = false;

        this.nose = new ModelRenderer(this, 24, 0);
        this.nose.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.nose.addBox(-1.0F, 3.0F, -6.0F, 2.0F, 4.0F, 2.0F, size);
        this.bipedHead.addChild(this.nose);

        this.leftWiggly = new ModelRenderer(this, 44, 8);
        this.leftWiggly.setRotationPoint(4.0F, -6.0F, -2.0F);
        this.leftWiggly.addBox(0.0F, 0.0F, 0.0F, 6.0F, 4.0F, 4.0F, size);
        this.setRotateAngle(this.leftWiggly, 0.0F, 0.0F, 0.4363323129985824F);
        this.bipedHead.addChild(this.leftWiggly);

        this.rightWiggly = new ModelRenderer(this, 44, 0);
        this.rightWiggly.mirror = true;
        this.rightWiggly.setRotationPoint(-4.0F, -6.0F, -2.0F);
        this.rightWiggly.addBox(-6.0F, 0.0F, 0.0F, 6.0F, 4.0F, 4.0F, size);
        this.setRotateAngle(this.rightWiggly, 0.0F, 0.0F, -0.4363323129985824F);
        this.bipedHead.addChild(this.rightWiggly);

        this.leftDecor = new ModelRenderer(this, 0, 0);
        this.leftDecor.setRotationPoint(1.0F, 0.0F, 1.0F);
        this.leftDecor.addBox(5.5F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, size);
        this.bipedHead.addChild(this.leftDecor);

        this.rightDecor = new ModelRenderer(this, 0, 0);
        this.rightDecor.setRotationPoint(1.0F, 0.0F, 1.0F);
        this.rightDecor.addBox(-9.5F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, size);
        this.bipedHead.addChild(this.rightDecor);

        this.bipedLeftArm = new ModelRenderer(this, 0, 50);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(5.0F, 6.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, size);

        this.bipedRightArm = new ModelRenderer(this, 0, 34);
        this.bipedRightArm.setRotationPoint(-5.0F, 6.0F, 0.0F);
        this.bipedRightArm.addBox(-3.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, size);

        this.bipedLeftLeg = new ModelRenderer(this, 32, 18);
        this.bipedLeftLeg.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.bipedLeftLeg.addBox(-2.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, size);

        this.bipedRightLeg = new ModelRenderer(this, 48, 18);
        this.bipedRightLeg.mirror = true;
        this.bipedRightLeg.setRotationPoint(-2.0F, 0.0F, 0.0F);
        this.bipedRightLeg.addBox(-2.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, size);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(TricksterEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        AbstractIllagerEntity.ArmPose pose = entityIn.getArmPose();
        //TODO fix rotation points considering different arm length
        if (pose == AbstractIllagerEntity.ArmPose.SPELLCASTING || pose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
            this.bipedRightArm.rotationPointZ = 0.0F; //Initial adjustments
            this.bipedRightArm.rotationPointX = -5.0F; //Initial adjustments
            this.bipedLeftArm.rotationPointZ = 0.0F; //Initial adjustments
            this.bipedLeftArm.rotationPointX = 5.0F; //Initial adjustments
            this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F; //Right arm wave from -14.3 to 14.3 degrees
            this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F; //Left arm wave from -14.3 to 14.3 degrees
            this.bipedRightArm.rotateAngleZ = 2.3561945F; //Constant z rotation for arm
            this.bipedLeftArm.rotateAngleZ = -2.3561945F; //Constant z rotation for arm
            this.bipedRightArm.rotateAngleY = 0.0F; //Constant y rotation for arm
            this.bipedLeftArm.rotateAngleY = 0.0F;//Constant y rotation for arm
        }
    }
}
