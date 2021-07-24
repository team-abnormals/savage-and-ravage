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

        this.body = new ModelRenderer(this, 0, 18);
        this.body.setPos(0.0F, 14.0F, 0.0F);
        this.body.addBox(-4.0F, 4.0F, -3.0F, 8.0F, 10.0F, 6.0F, size);

        this.head = new ModelRenderer(this, 0, 0);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -6.0F, -4.0F, 8.0F, 10.0F, 8.0F, size);
        this.hat.visible = false;

        this.nose = new ModelRenderer(this, 24, 0);
        this.nose.setPos(0.0F, -2.0F, 0.0F);
        this.nose.addBox(-1.0F, 3.0F, -6.0F, 2.0F, 4.0F, 2.0F, size);
        this.head.addChild(this.nose);

        this.leftWiggly = new ModelRenderer(this, 44, 8);
        this.leftWiggly.setPos(4.0F, -6.0F, -2.0F);
        this.leftWiggly.addBox(0.0F, 0.0F, 0.0F, 6.0F, 4.0F, 4.0F, size);
        this.setRotateAngle(this.leftWiggly, 0.0F, 0.0F, 0.4363323129985824F);
        this.head.addChild(this.leftWiggly);

        this.rightWiggly = new ModelRenderer(this, 44, 0);
        this.rightWiggly.mirror = true;
        this.rightWiggly.setPos(-4.0F, -6.0F, -2.0F);
        this.rightWiggly.addBox(-6.0F, 0.0F, 0.0F, 6.0F, 4.0F, 4.0F, size);
        this.setRotateAngle(this.rightWiggly, 0.0F, 0.0F, -0.4363323129985824F);
        this.head.addChild(this.rightWiggly);

        this.leftDecor = new ModelRenderer(this, 0, 0);
        this.leftDecor.setPos(1.0F, 0.0F, 1.0F);
        this.leftDecor.addBox(5.5F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, size);
        this.head.addChild(this.leftDecor);

        this.rightDecor = new ModelRenderer(this, 0, 0);
        this.rightDecor.setPos(1.0F, 0.0F, 1.0F);
        this.rightDecor.addBox(-9.5F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, size);
        this.head.addChild(this.rightDecor);

        this.leftArm = new ModelRenderer(this, 0, 50);
        this.leftArm.mirror = true;
        this.leftArm.setPos(5.0F, 6.0F, 0.0F);
        this.leftArm.addBox(-1.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, size);

        this.rightArm = new ModelRenderer(this, 0, 34);
        this.rightArm.setPos(-5.0F, 6.0F, 0.0F);
        this.rightArm.addBox(-3.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, size);

        this.leftLeg = new ModelRenderer(this, 32, 18);
        this.leftLeg.setPos(2.0F, 0.0F, 0.0F);
        this.leftLeg.addBox(-2.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, size);

        this.rightLeg = new ModelRenderer(this, 48, 18);
        this.rightLeg.mirror = true;
        this.rightLeg.setPos(-2.0F, 0.0F, 0.0F);
        this.rightLeg.addBox(-2.0F, 2.0F, -2.0F, 4.0F, 10.0F, 4.0F, size);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void setupAnim(TricksterEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        AbstractIllagerEntity.ArmPose pose = entityIn.getArmPose();
        //TODO fix rotation points considering different arm length
        if (pose == AbstractIllagerEntity.ArmPose.SPELLCASTING || pose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
            this.rightArm.z = 0.0F; //Initial adjustments
            this.rightArm.x = -5.0F; //Initial adjustments
            this.leftArm.z = 0.0F; //Initial adjustments
            this.leftArm.x = 5.0F; //Initial adjustments
            this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F; //Right arm wave from -14.3 to 14.3 degrees
            this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F; //Left arm wave from -14.3 to 14.3 degrees
            this.rightArm.zRot = 2.3561945F; //Constant z rotation for arm
            this.leftArm.zRot = -2.3561945F; //Constant z rotation for arm
            this.rightArm.yRot = 0.0F; //Constant y rotation for arm
            this.leftArm.yRot = 0.0F;//Constant y rotation for arm
        }
    }
}
