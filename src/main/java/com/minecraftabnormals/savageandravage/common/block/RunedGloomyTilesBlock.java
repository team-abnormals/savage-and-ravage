package com.minecraftabnormals.savageandravage.common.block;

import com.minecraftabnormals.savageandravage.common.entity.RunePrisonEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.BlockState;
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
        if (!state.get(TRIGGERED)) {
            boolean canUse = false;
            if (entity instanceof PlayerEntity && !state.get(TRIGGERED))
                canUse = !((PlayerEntity) entity).isCreative();
            if (!(EntityTypeTags.RAIDERS.contains(entity.getType())) && entity.getType() != EntityType.ARMOR_STAND && entity instanceof LivingEntity && canUse) {
                world.setBlockState(pos, state.with(TRIGGERED, true));
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
