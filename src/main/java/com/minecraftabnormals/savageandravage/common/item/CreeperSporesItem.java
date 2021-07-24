package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.savageandravage.common.block.PottedCreeperSporesBlock;
import com.minecraftabnormals.savageandravage.common.entity.SporeCloudEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Random;

public class CreeperSporesItem extends Item implements IPottableItem {
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.EGG);

	public CreeperSporesItem(Item.Properties properties) {
		super(properties);
	}

	public static int getThrownSporeCloudSize(Random rand) {
		return rand.nextInt(50) == 0 ? 0 : 1 + rand.nextInt(3);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		world.playSound(player, player.getX(), player.getY(), player.getZ(), SRSounds.ENTITY_CREEPER_SPORES_THROW.get(), SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!world.isClientSide()) {
			SporeCloudEntity spores = new SporeCloudEntity(world, player);
			spores.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 0.99F, 1.0F);
			spores.setCloudSize(getThrownSporeCloudSize(spores.level.getRandom()));
			world.addFreshEntity(spores);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.isCreative())
			stack.shrink(1);
		player.getCooldowns().addCooldown(this, 30);
		return ActionResult.success(stack);
	}

	@Override
	public BlockState getPottedState(Direction direction) {
		return ((PottedCreeperSporesBlock) SRBlocks.POTTED_CREEPER_SPORES.get()).getDirectionalState(direction);
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}