package com.minecraftabnormals.savageandravage.common.block;

import com.minecraftabnormals.savageandravage.core.registry.SRParticles;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ChiseledGloomyTilesBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ChiseledGloomyTilesBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(POWERED, context.getWorld().isBlockPowered(context.getPos()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote()) {
            boolean flag = state.get(POWERED);
            if (flag != worldIn.isBlockPowered(pos)) {
                if (flag) {
                    worldIn.getPendingBlockTicks().scheduleTick(pos, this, 4);
                } else {
                    worldIn.setBlockState(pos, state.func_235896_a_(POWERED), 2);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (state.get(POWERED) && !world.isBlockPowered(pos)) {
            world.setBlockState(pos, state.func_235896_a_(POWERED), 2);
        }
    }

    @Override
    public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
        if (stateIn.get(POWERED) && world.isRemote) {
            spawnParticles(world, pos);
        }
    }

    private void spawnParticles(World world, BlockPos pos) {
        Random random = world.rand;
        List<Direction> directions = Arrays.stream(Direction.values()).filter(d -> !world.getBlockState(pos.offset(d)).isOpaqueCube(world, pos.offset(d))).collect(Collectors.toList());
        for (Direction direction : directions) {
            if (world.rand.nextInt(directions.size()+2) == 0) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getXOffset() : (double) random.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getYOffset() : (double) random.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getZOffset() : (double) random.nextFloat();
                world.addParticle(SRParticles.RUNE.get(), (double) pos.getX() + d1, (double) pos.getY() + d2, (double) pos.getZ() + d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }
}
