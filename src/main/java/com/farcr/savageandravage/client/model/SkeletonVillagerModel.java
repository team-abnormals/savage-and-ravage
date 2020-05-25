package com.farcr.savageandravage.client.model;

import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

/*
  ModelSkeletonVillager - Vinny
 */
public class SkeletonVillagerModel extends SegmentedModel<SkeletonVillagerEntity> implements IHasArm, IHasHead {
    public ModelRenderer Head;
    public ModelRenderer Body;
    public ModelRenderer RightArm;
    public ModelRenderer LeftArm;
    public ModelRenderer RightLeg;
    public ModelRenderer LeftLeg;
    public ModelRenderer MiddleClosedArm;
    public ModelRenderer Nose;
    public ModelRenderer RightClosedArm;
    public ModelRenderer LeftClosedArm;
    public float floatthing; 

    public SkeletonVillagerModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.RightLeg = new ModelRenderer(this, 0, 18);
        this.RightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.RightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.Body = new ModelRenderer(this, 12, 18);
        this.Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Body.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
        this.LeftClosedArm = new ModelRenderer(this, 32, 0);
        this.LeftClosedArm.mirror = true;
        this.LeftClosedArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftClosedArm.addBox(4.0F, -2.0F, -1.0F, 3, 8, 3, 0.0F);
        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.LeftLeg = new ModelRenderer(this, 0, 18);
        this.LeftLeg.mirror = true;
        this.LeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.LeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.RightClosedArm = new ModelRenderer(this, 32, 0);
        this.RightClosedArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightClosedArm.addBox(-7.0F, -2.0F, -1.0F, 3, 8, 3, 0.0F);
        this.Nose = new ModelRenderer(this, 24, 0);
        this.Nose.setRotationPoint(0.0F, -3.0F, -4.0F);
        this.Nose.addBox(-1.0F, 0.0F, -2.0F, 2, 4, 2, 0.0F);
        this.RightArm = new ModelRenderer(this, 40, 19);
        this.RightArm.setRotationPoint(-5.5F, 2.0F, 0.0F);
        this.RightArm.addBox(-1.5F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
        this.LeftArm = new ModelRenderer(this, 40, 19);
        this.LeftArm.mirror = true;
        this.LeftArm.setRotationPoint(5.5F, 2.0F, 0.0F);
        this.LeftArm.addBox(-1.5F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
        this.MiddleClosedArm = new ModelRenderer(this, 32, 11);
        this.MiddleClosedArm.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.MiddleClosedArm.addBox(-4.0F, 3.0F, -1.0F, 8, 3, 3, 0.0F);
        this.setRotateAngle(MiddleClosedArm, -0.7853981633974483F, 0.0F, 0.0F);
        this.MiddleClosedArm.addChild(this.LeftClosedArm);
        this.MiddleClosedArm.addChild(this.RightClosedArm);
        this.Head.addChild(this.Nose);
    }

	@Override
	public Iterable<ModelRenderer> getParts()
	{
		return ImmutableList.of(this.Head, this.Body, this.RightLeg, this.LeftLeg, this.RightArm, this.LeftArm, this.MiddleClosedArm);
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
	public void setRotationAngles(SkeletonVillagerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
	   ItemStack itemstack = entityIn.getHeldItemMainhand();
       if (this.isSitting) 
	   {
	     this.RightLeg.rotateAngleX = -1.4137167F;
	     this.RightLeg.rotateAngleY = ((float)Math.PI / 10F);
	     this.RightLeg.rotateAngleZ = 0.07853982F;
	     this.LeftLeg.rotateAngleX = -1.4137167F;
	     this.LeftLeg.rotateAngleY = (-(float)Math.PI / 10F);
	     this.LeftLeg.rotateAngleZ = -0.07853982F;
	   }  else {
		   boolean flag = entityIn.isAggressive();
		   // hacky but gives me time to work on the real monsters.
		     this.RightClosedArm.showModel = !flag;
		     this.LeftClosedArm.showModel = !flag;
		     this.MiddleClosedArm.showModel = !flag;
		     this.LeftArm.showModel = flag;
	  	     this.RightArm.showModel = flag;
	   this.Head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
	   this.Head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
	   this.RightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	   this.LeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	   this.RightLeg.rotateAngleY = 0.0F;
	   this.LeftLeg.rotateAngleY = 0.0F;
	   this.RightLeg.rotateAngleZ = 0.0F;
	   this.LeftLeg.rotateAngleZ = 0.0F;
	   float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
       float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
       this.RightArm.rotateAngleZ = 0.0F;
       this.LeftArm.rotateAngleZ = 0.0F;
       this.RightArm.rotateAngleY = -(0.1F - f * 0.6F);
       this.LeftArm.rotateAngleY = 0.1F - f * 0.6F;
       this.RightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
       this.RightArm.rotateAngleY = 0.0F;
       this.RightArm.rotateAngleZ = 0.0F;
       this.LeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
       this.LeftArm.rotateAngleY = 0.0F;
       this.LeftArm.rotateAngleZ = 0.0F;
       if (entityIn.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof ShootableItem))) {
           this.LeftArm.rotateAngleX -= f * 2.2F - f1 * 0.4F;
           this.LeftArm.rotateAngleZ -= f * 1.0 - f1 * 1.0F;
           this.RightArm.rotateAngleX -= f * 2.2F - f1 * 0.4F;
           this.RightArm.rotateAngleZ -= f * 1.0 - f1 * 1.0F;
       }
       if (itemstack.getItem() instanceof CrossbowItem) 
       {
       	 if (entityIn.getPrimaryHand() == HandSide.RIGHT) 
	          {
       		    this.RightArm.rotateAngleY = -0.3F;
                this.LeftArm.rotateAngleY = 0.6F;
                this.RightArm.rotateAngleX = (-(float)Math.PI / 2F) + this.Head.rotateAngleX + 0.1F;
                this.LeftArm.rotateAngleX = -1.5F + this.Head.rotateAngleX;
	          }
       	      else if (entityIn.getPrimaryHand() == HandSide.LEFT) 
	           {
       	         this.RightArm.rotateAngleY = -0.6F;
       	         this.LeftArm.rotateAngleY = 0.3F;
       	         this.RightArm.rotateAngleX = -1.5F + this.Head.rotateAngleX;
       	         this.LeftArm.rotateAngleX = (-(float)Math.PI / 2F) + this.Head.rotateAngleX + 0.1F;

	           }
       if (entityIn.isCharging()) {
          if (entityIn.getPrimaryHand() == HandSide.RIGHT) 
          {
        	  this.RightArm.rotateAngleY = -0.8F;
              this.RightArm.rotateAngleX = -0.97079635F;
              this.LeftArm.rotateAngleX = -0.97079635F;
              float f2 = MathHelper.clamp(this.floatthing, 0.0F, 25.0F);
              this.LeftArm.rotateAngleY = MathHelper.lerp(f2 / 25.0F, 0.4F, 0.85F);
              this.LeftArm.rotateAngleX = MathHelper.lerp(f2 / 25.0F, this.LeftArm.rotateAngleX, (-(float)Math.PI / 2F));
          }
          if (entityIn.getPrimaryHand() == HandSide.LEFT) 
          {
              this.LeftArm.rotateAngleY = 0.8F;
              this.RightArm.rotateAngleX = -0.97079635F;
              this.LeftArm.rotateAngleX = -0.97079635F;
              float f2 = MathHelper.clamp(this.floatthing, 0.0F, 25.0F);
              this.RightArm.rotateAngleY = MathHelper.lerp(f2 / 25.0F, -0.4F, -0.85F);
              this.RightArm.rotateAngleX = MathHelper.lerp(f2 / 25.0F, this.RightArm.rotateAngleX, (-(float)Math.PI / 2F));
          }
       }
       }
	   }
	}
	
	@Override
	public void setLivingAnimations(SkeletonVillagerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) 
	{
	  this.floatthing = (float)entityIn.getItemInUseMaxCount();
	  super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public ModelRenderer getModelHead() {
		return this.Head;
	}

    protected ModelRenderer getArmForSide(HandSide side) 
    {
	  return side == HandSide.LEFT ? this.LeftArm : this.RightArm;
	}


	@Override
	public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) 
	{
	  float f = sideIn == HandSide.RIGHT ? 1.0F : -1.0F;
	  ModelRenderer modelrenderer = this.getArmForSide(sideIn);
	  modelrenderer.rotationPointX += f;
	  modelrenderer.translateRotate(matrixStackIn);
	  modelrenderer.rotationPointX -= f;
	}
}