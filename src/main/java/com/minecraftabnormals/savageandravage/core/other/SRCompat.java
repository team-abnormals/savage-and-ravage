package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.minecraftabnormals.savageandravage.common.entity.MischiefArrowEntity;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.teamabnormals.abnormals_core.core.utils.DataUtils;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SRCompat {

    public static void registerFlammables() {
        DataUtils.registerFlammable(SRBlocks.CREEPER_SPORE_SACK.get(), 30, 60);
        DataUtils.registerFlammable(SRBlocks.SPORE_BOMB.get(), 15, 100);
    }

    public static void registerDispenserBehaviors() {
        SavageAndRavage.REGISTRY_HELPER.processSpawnEggDispenseBehaviors();
        DispenserBlock.registerDispenseBehavior(SRItems.MISCHIEF_ARROW.get(), new ProjectileDispenseBehavior() {
            @Override
            protected ProjectileEntity getProjectileEntity(World world, IPosition position, ItemStack stack) {
                return new MischiefArrowEntity(world, position.getX(), position.getY(), position.getZ());
            }
        });
        DispenserBlock.registerDispenseBehavior(SRItems.CREEPER_SPORES.get(), new ProjectileDispenseBehavior() {
            @Override
            protected ProjectileEntity getProjectileEntity(World world, IPosition position, ItemStack stack) {
                CreeperSporeCloudEntity spore = new CreeperSporeCloudEntity(world, position.getX(), position.getY(), position.getZ());
                spore.setSporeBomb(true);
                return spore;
            }
        });

        DispenserBlock.registerDispenseBehavior(SRBlocks.SPORE_BOMB.get(), new DefaultDispenseItemBehavior() {
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World world = source.getWorld();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                SporeBombEntity sporeBomb = new SporeBombEntity(world, (double) blockpos.getX() + 0.5D, blockpos.getY(), (double) blockpos.getZ() + 0.5D, (LivingEntity) null);
                world.addEntity(sporeBomb);
                world.playSound(null, sporeBomb.getPosX(), sporeBomb.getPosY(), sporeBomb.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                stack.shrink(1);
                return stack;
            }
        });
    }
}
