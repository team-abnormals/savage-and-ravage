package com.farcr.savageandravage.common.block;

import com.farcr.savageandravage.common.entity.RunePrisonEntity;
import com.farcr.savageandravage.core.registry.SREntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.NoteBlockEvent;

public class RunedGloomyTilesBlock extends Block {
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public RunedGloomyTilesBlock(Properties blockProperties) {
        super(blockProperties);
        this.setDefaultState(this.stateContainer.getBaseState().with(TRIGGERED, Boolean.valueOf(false)));
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
        activate(worldIn.getBlockState(pos), worldIn, pos, entityIn);
    }

    private void activate(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!state.get(TRIGGERED)) {
            //TODO make raiders tag check work
            if (!(EntityTypeTags.RAIDERS.contains(entity.getType())) && entity.getType()!=EntityType.ARMOR_STAND) {
                //TODO add creative mode check when this is done
                    if (entity instanceof LivingEntity) {
                        world.setBlockState(pos, state.with(TRIGGERED, Boolean.valueOf(true)));
                        if (entity instanceof PlayerEntity) {
                            world.playSound((PlayerEntity) entity, pos, SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.HOSTILE, 1.0F, 1.0F);
                        } else {
                            world.playSound(null, pos, SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.HOSTILE, 1.0F, 1.0F);
                        }
                        EvokerFangsEntity evokerFangs = EntityType.EVOKER_FANGS.create(world);
                        evokerFangs.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
                        world.addEntity(evokerFangs);
                        RunePrisonEntity runePrison = new RunePrisonEntity(world, pos);
                        runePrison.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
                        world.addEntity(runePrison);
                    }
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }
}
