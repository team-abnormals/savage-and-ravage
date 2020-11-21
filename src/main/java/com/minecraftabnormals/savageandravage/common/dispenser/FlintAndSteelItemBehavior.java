package com.minecraftabnormals.savageandravage.common.dispenser;

import com.minecraftabnormals.savageandravage.common.entity.BurningBannerEntity;
import com.minecraftabnormals.savageandravage.core.other.SREvents;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FlintAndSteelItemBehavior extends OptionalDispenseBehavior {

    private IDispenseItemBehavior originalBehavior;

    public FlintAndSteelItemBehavior (DefaultDispenseItemBehavior behavior) {
        this.originalBehavior = behavior;
    }

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
        Method original = ObfuscationReflectionHelper.findMethod(DefaultDispenseItemBehavior.class, "func_82487_b", IBlockSource.class, ItemStack.class);
        try {
            stack = (ItemStack) original.invoke(originalBehavior, source, stack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if(SREvents.isValidBannerPos(source.getWorld(),blockpos)) {
            world.addEntity(new BurningBannerEntity(world, blockpos, null));
            this.setSuccessful(true);
            if (stack.attemptDamageItem(1, world.rand, null)) {
                stack.setCount(0);
            }
        }
        return stack;
    }

}
