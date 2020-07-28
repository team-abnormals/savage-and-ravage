package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.savageandravage.common.entity.CreeperSporeCloudEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class CreeperSporesItem extends Item
{
	public CreeperSporesItem(Item.Properties properties) {
		super(properties);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
	      ItemStack itemstack = playerIn.getHeldItem(handIn);
	      worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
	      if(!worldIn.isRemote) {
			  CreeperSporeCloudEntity spores = new CreeperSporeCloudEntity(worldIn, playerIn);
			  spores.func_234612_a_(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.99F, 1.0F);
			  spores.cloudSize = worldIn.rand.nextInt(50) == 0 ? 0 : 1 + spores.world.rand.nextInt(3);
			  worldIn.addEntity(spores);
		  }

	      playerIn.addStat(Stats.ITEM_USED.get(this));
	      if (!playerIn.abilities.isCreativeMode) {
	         itemstack.shrink(1);
	      }

	      return ActionResult.resultSuccess(itemstack);
	   }
}