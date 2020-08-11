package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.savageandravage.common.entity.GrieferEntity;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class GrieferModel extends BipedModel<GrieferEntity>
{
    public ModelRenderer bipedBody2Layer;
    public ModelRenderer lenose;
    public ModelRenderer tnt;
    public ModelRenderer pouch;
    public ModelRenderer shoulderPad;
    public float kickingTime;

    public GrieferModel(float f) {
    	super(f);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.bipedBody = new ModelRenderer(this, 36, 0);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 18);
        this.bipedRightLeg.mirror = true;
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 16, 34);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.lenose = new ModelRenderer(this, 24, 0);
        this.lenose.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.lenose.addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
        this.tnt = new ModelRenderer(this, 50, 45);
        this.tnt.setRotationPoint(-0.0F, 6.0F, -6.00F);
        this.tnt.addBox(0.0F, 0.0F, 0.0F, 4, 4, 3, 0.0F);
        this.pouch = new ModelRenderer(this, 46, 36);
        this.pouch.setRotationPoint(-2.9F, 1.7F, 3.0F);
        this.pouch.addBox(0.0F, -0.9F, 0.0F, 6, 6, 3, 0.0F);
        this.shoulderPad = new ModelRenderer(this, 11, 51);
        this.shoulderPad.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shoulderPad.addBox(-4.0F, -2.7F, -3.2F, 5, 6, 6, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 18);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 16, 18);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedBody2Layer = new ModelRenderer(this, 36, 18);
        this.bipedBody2Layer.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody2Layer.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.03F);
        this.bipedHead.addChild(this.lenose);
        this.bipedBody.addChild(this.tnt);
        this.bipedBody.addChild(this.pouch);
        this.bipedRightArm.addChild(this.shoulderPad);
        this.bipedHeadwear.showModel = false;
    }
    
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.bipedBody, this.bipedRightArm, this.bipedLeftArm, this.bipedRightLeg, this.bipedLeftLeg, this.bipedBody2Layer);
     }

	@Override
	public void setRotationAngles(GrieferEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netbipedbipedHeadYaw, float bipedbipedHeadPitch){
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netbipedbipedHeadYaw, bipedbipedHeadPitch);
		boolean flag = entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem;
		this.bipedBody2Layer.copyModelAngles(this.bipedBody);
		this.shoulderPad.showModel = !flag;
		if (entityIn.getKickTicks() > 0) {
		    float f1 = 1.0F - (float)MathHelper.abs(10 - 2 * entityIn.getKickTicks()) / 10.0F;
            this.bipedRightLeg.rotateAngleX = MathHelper.lerp(f1, 0.0F, -1.40F);
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
        ItemStack itemstack = entityIn.getHeldItemMainhand();
        UseAction useaction = itemstack.getUseAction();
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        if (entityIn.getPrimaryHand() == HandSide.RIGHT) 
        {
          switch(useaction) 
          {
            case BLOCK:
            this.rightArmPose = BipedModel.ArmPose.BLOCK;
            break;
            case BOW:
            this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            break;
  		    default:
  		    this.rightArmPose = BipedModel.ArmPose.EMPTY;
  		    if (!itemstack.isEmpty()) 
  		    {
  	  		  this.rightArmPose = BipedModel.ArmPose.ITEM;
  	  	    }
  	        break;
          }
        }
        if (entityIn.getPrimaryHand() == HandSide.LEFT) 
        {
         switch(useaction) 
         {
           case BLOCK:
           this.leftArmPose = BipedModel.ArmPose.BLOCK;
           break;
           case BOW:
           this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
           break;
           default:
           this.leftArmPose = BipedModel.ArmPose.EMPTY;
           if (!itemstack.isEmpty()) 
    	   {
             this.leftArmPose = BipedModel.ArmPose.ITEM;
    	   }
    	   break;
         }
        }
        super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
     }

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}