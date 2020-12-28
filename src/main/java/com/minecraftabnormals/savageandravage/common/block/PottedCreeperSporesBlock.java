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

	private static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private final Supplier<Item> flower;

	public PottedCreeperSporesBlock(Supplier<Item> flower, Block.Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
		this.flower = flower;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack itemStack = player.getHeldItem(hand);
		ItemStack newItemStack = new ItemStack((this.flower.get()));
		if (!itemStack.getItem().equals(this.flower.get())) {
			if (itemStack.isEmpty()) {
				player.setHeldItem(hand, newItemStack);
			} else if (!player.addItemStackToInventory(newItemStack)) {
				player.dropItem(newItemStack, false);
			}
			world.setBlockState(pos, Blocks.FLOWER_POT.getDefaultState());
			return ActionResultType.func_233537_a_(world.isRemote());
		} else return ActionResultType.CONSUME;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return this.flower == Blocks.AIR ? new ItemStack(Blocks.FLOWER_POT) : new ItemStack(this.flower.get().asItem());
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public BlockState getDirectionalState(Direction direction) {
		return this.getDefaultState().with(FACING, direction);
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

}
