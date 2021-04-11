package com.minecraftabnormals.savageandravage.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

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
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}
}
