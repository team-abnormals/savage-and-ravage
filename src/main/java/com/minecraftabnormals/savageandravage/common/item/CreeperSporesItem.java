package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.savageandravage.common.block.PottedCreeperSporesBlock;
import com.minecraftabnormals.savageandravage.common.entity.SporeCloudEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class CreeperSporesItem extends Item implements PottableItem {

	public CreeperSporesItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SRSounds.ENTITY_CREEPER_SPORES_THROW.get(), SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!world.isRemote()) {
			SporeCloudEntity spores = new SporeCloudEntity(world, player);
			spores.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.99F, 1.0F);
			spores.setCloudSize(world.getRandom().nextInt(50) == 0 ? 0 : 1 + spores.world.getRandom().nextInt(3));
			world.addEntity(spores);
		}

		player.addStat(Stats.ITEM_USED.get(this));
		if (!player.isCreative())
			stack.shrink(1);

		return ActionResult.resultSuccess(stack);
	}

	@Override
	public BlockState getPottedState(Direction direction) {
		return ((PottedCreeperSporesBlock) SRBlocks.POTTED_CREEPER_SPORES.get()).getDirectionalState(direction);
	}
}