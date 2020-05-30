package com.farcr.savageandravage.client.render;

import com.farcr.savageandravage.client.model.GrieferModel;
import com.farcr.savageandravage.common.entity.GrieferEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

public class GrieferRenderer extends BipedRenderer<GrieferEntity, GrieferModel> {
	private static final ResourceLocation GRIEFER_TEXTURE = new ResourceLocation("savageandravage:textures/entity/griefer.png");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GrieferRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new GrieferModel(0), 0.5f);
		this.addLayer(new BipedArmorLayer(this, new BipedModel(0.5F), new BipedModel(1.0F)));
	}
	
    public void render(GrieferEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        this.setModelVisibilities(entity);
        super.render(entity, partialTicks, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
	
    private void setModelVisibilities(GrieferEntity entityIn) {
        GrieferModel guardmodel = this.getEntityModel();
           ItemStack itemstack = entityIn.getHeldItemMainhand();
           ItemStack itemstack1 = entityIn.getHeldItemOffhand();
           guardmodel.setVisible(true);
           BipedModel.ArmPose bipedmodel$armpose = this.getArmPose(entityIn, itemstack, itemstack1, Hand.MAIN_HAND);
           BipedModel.ArmPose bipedmodel$armpose1 = this.getArmPose(entityIn, itemstack, itemstack1, Hand.OFF_HAND);
           if (entityIn.getPrimaryHand() == HandSide.RIGHT) {
              guardmodel.rightArmPose = bipedmodel$armpose;
              guardmodel.leftArmPose = bipedmodel$armpose1;
           } else {
              guardmodel.rightArmPose = bipedmodel$armpose1;
              guardmodel.leftArmPose = bipedmodel$armpose;
           }
     }
	
	private BipedModel.ArmPose getArmPose(GrieferEntity playerIn, ItemStack itemStackMain, ItemStack itemStackOff, Hand handIn) {
		BipedModel.ArmPose bipedmodel$armpose = BipedModel.ArmPose.EMPTY;
		 ItemStack itemstack = handIn == Hand.MAIN_HAND ? itemStackMain : itemStackOff;
		 if (!itemstack.isEmpty()) {
		   bipedmodel$armpose = BipedModel.ArmPose.ITEM;
		    if (playerIn.getItemInUseCount() > 0) {
		       UseAction useaction = itemstack.getUseAction();
		        if (useaction == UseAction.BLOCK) {
		               bipedmodel$armpose = BipedModel.ArmPose.BLOCK;
		            } else if (useaction == UseAction.BOW) {
		               bipedmodel$armpose = BipedModel.ArmPose.BOW_AND_ARROW;
		            } else if (useaction == UseAction.SPEAR) {
		               bipedmodel$armpose = BipedModel.ArmPose.THROW_SPEAR;
		            } else if (useaction == UseAction.CROSSBOW && handIn == playerIn.getActiveHand()) {
		               bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_CHARGE;
		            }
		         } else {
		            boolean flag3 = itemStackMain.getItem() == Items.CROSSBOW;
		            boolean flag = CrossbowItem.isCharged(itemStackMain);
		            boolean flag1 = itemStackOff.getItem() == Items.CROSSBOW;
		            boolean flag2 = CrossbowItem.isCharged(itemStackOff);
		            if (flag3 && flag) {
		               bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
		            }

		            if (flag1 && flag2 && itemStackMain.getItem().getUseAction(itemStackMain) == UseAction.NONE) {
		               bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
		            }
		         }
		     }
			return bipedmodel$armpose;
	}
	
    public ResourceLocation getEntityTexture(GrieferEntity entity) 
    {
	  return GRIEFER_TEXTURE;
	}
}
