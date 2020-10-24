package com.minecraftabnormals.savageandravage.common.block;

import com.minecraftabnormals.savageandravage.common.entity.RunePrisonEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RunedGloomyTilesBlock extends ChiseledGloomyTilesBlock {

    public RunedGloomyTilesBlock(Properties blockProperties) {
        super(blockProperties);
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        super.onEntityWalk(world, pos, entity);
        BlockState state = world.getBlockState(pos);
        if (entity instanceof LivingEntity) {
        	LivingEntity livingEntity = (LivingEntity)entity;
        	boolean isIllager = livingEntity.getCreatureAttribute() == CreatureAttribute.ILLAGER || EntityTypeTags.RAIDERS.contains(entity.getType());
        	boolean isCreative = entity instanceof PlayerEntity ? ((PlayerEntity) entity).isCreative() : false;
        	if (!state.get(POWERED) && !isIllager && entity.getType() != EntityType.ARMOR_STAND && !entity.isSpectator() && !isCreative) {
                world.setBlockState(pos, state.with(POWERED, true));
                world.playSound(null, pos, SRSounds.RUNES_ACTIVATED.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
                EvokerFangsEntity evokerFangs = EntityType.EVOKER_FANGS.create(world);
                if (evokerFangs != null) {
                    evokerFangs.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
                    world.addEntity(evokerFangs);
                }
                RunePrisonEntity runePrison = new RunePrisonEntity(world, pos, 25);
                runePrison.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
                world.addEntity(runePrison);
            }
        }
        
    }
}
