package com.farcr.savageandravage.core;

import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class SRHooks {

    public static Boolean isBlockNotOminousBanner(BlockPos pos, World worldIn) {
        if (worldIn.getBlockState(pos).getBlock() instanceof AbstractBannerBlock) {
            TranslationTextComponent bannerName;
            try {
                bannerName = (TranslationTextComponent) ((BannerTileEntity) worldIn.getTileEntity(pos)).getName();
            } catch (ClassCastException cast) {
                bannerName = null;
            }
            return !(bannerName.getKey().contains("block.minecraft.ominous_banner"));
        }
        else{
            return true;
        }
    }
}
