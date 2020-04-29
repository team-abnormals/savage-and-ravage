package com.farcr.savageandravage.common.block;

import javax.annotation.Nullable;

import com.farcr.savageandravage.common.entity.block.SporeBombEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class SporeBombBlock extends TNTBlock
{
	   public SporeBombBlock(Block.Properties properties) {
	      super(properties);
	   }
	   
	   @Override
	   public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) {
		 SporeBombEntity sporebomb = new SporeBombEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, igniter);
		 world.addEntity(sporebomb);
		 world.playSound((PlayerEntity)null, sporebomb.getPosX(), sporebomb.getPosY(), sporebomb.getPosZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
	   }
	   
	   @Override
	   public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) 
	   {
	     SporeBombEntity sporebomb = new SporeBombEntity(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
	     sporebomb.setFuse((short)(worldIn.rand.nextInt(sporebomb.getFuse() / 4) + sporebomb.getFuse() / 8));
	     worldIn.addEntity(sporebomb);
	   }
	   
	   @Override
	   public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) 
	   {
		  if (worldIn.isBlockPowered(pos)) 
		  {
		    this.catchFire(state, worldIn, pos, null, null);
		    worldIn.removeBlock(pos, false);
		  }
    }
}
