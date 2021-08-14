package com.minecraftabnormals.savageandravage.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class PottedCreeperSporesBlock extends Block {
	private static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	private final Supplier<Item> flower;

	public PottedCreeperSporesBlock(Supplier<Item> flower, Block.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		this.flower = flower;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack itemStack = player.getItemInHand(hand);
		ItemStack newItemStack = new ItemStack((this.flower.get()));
		if (!itemStack.getItem().equals(this.flower.get())) {
			if (itemStack.isEmpty()) {
				player.setItemInHand(hand, newItemStack);
			} else if (!player.addItem(newItemStack)) {
				player.drop(newItemStack, false);
			}
			world.setBlockAndUpdate(pos, Blocks.FLOWER_POT.defaultBlockState());
			return ActionResultType.sidedSuccess(world.isClientSide());
		} else return ActionResultType.CONSUME;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return this.flower == Blocks.AIR ? new ItemStack(Blocks.FLOWER_POT) : new ItemStack(this.flower.get().asItem());
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public BlockState getDirectionalState(Direction direction) {
		return this.defaultBlockState().setValue(FACING, direction);
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

}
