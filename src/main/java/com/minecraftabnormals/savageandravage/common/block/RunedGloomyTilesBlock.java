package com.minecraftabnormals.savageandravage.common.block;

import com.minecraftabnormals.savageandravage.common.entity.RunePrisonEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class RunedGloomyTilesBlock extends Block {
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public RunedGloomyTilesBlock(Properties blockProperties) {
        super(blockProperties);
        this.setDefaultState(this.stateContainer.getBaseState().with(TRIGGERED, Boolean.valueOf(false)));
        //TODO check if boxing here is unnecessary
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(TRIGGERED, Boolean.valueOf(context.getWorld().isBlockPowered(context.getPos())));
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
        activate(worldIn.getBlockState(pos), worldIn, pos, entityIn);
    }

    private void activate(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!state.get(TRIGGERED)) {
            boolean shouldNotActivate = false;
            if(entity instanceof PlayerEntity && !state.get(TRIGGERED)) {
                shouldNotActivate = ((PlayerEntity)entity).abilities.isCreativeMode;
            }
            if (!(EntityTypeTags.RAIDERS.contains(entity.getType())) && entity.getType()!=EntityType.ARMOR_STAND && !shouldNotActivate) {
                    if (entity instanceof LivingEntity) {
                        world.setBlockState(pos, state.with(TRIGGERED, Boolean.valueOf(true))); //TODO check this as well
                        world.playSound(null, pos, SRSounds.RUNES_ACTIVATED.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
                        EvokerFangsEntity evokerFangs = EntityType.EVOKER_FANGS.create(world);
                        evokerFangs.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
                        world.addEntity(evokerFangs);
                        RunePrisonEntity runePrison = new RunePrisonEntity(world, pos, 25);
                        runePrison.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
                        world.addEntity(runePrison);
                    }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            boolean flag = state.get(TRIGGERED);
            if (flag != worldIn.isBlockPowered(pos)) {
                if (flag) {
                    worldIn.getPendingBlockTicks().scheduleTick(pos, this, 4);
                } else {
                    worldIn.setBlockState(pos, state.func_235896_a_(TRIGGERED), 2);
                }
            }

        }
    }

    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(TRIGGERED) && !worldIn.isBlockPowered(pos)) {
            worldIn.setBlockState(pos, state.func_235896_a_(TRIGGERED), 2);
        }

    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }
}
