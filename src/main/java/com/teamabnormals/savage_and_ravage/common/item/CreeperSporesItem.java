package com.teamabnormals.savage_and_ravage.common.item;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.savage_and_ravage.common.block.PottedCreeperSporesBlock;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.SporeCloud;
import com.teamabnormals.savage_and_ravage.core.registry.SRBlocks;
import com.teamabnormals.savage_and_ravage.core.registry.SRSounds;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CreeperSporesItem extends Item implements PottableItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.EGG);

	public CreeperSporesItem(Item.Properties properties) {
		super(properties);
	}

	public static int getThrownSporeCloudSize(RandomSource rand) {
		return rand.nextInt(50) == 0 ? 0 : 1 + rand.nextInt(3);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		world.playSound(player, player.getX(), player.getY(), player.getZ(), SRSounds.ENTITY_CREEPER_SPORES_THROW.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
		if (!world.isClientSide()) {
			SporeCloud spores = new SporeCloud(world, player);
			spores.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.99F, 1.0F);
			spores.setCloudSize(getThrownSporeCloudSize(spores.level.getRandom()));
			world.addFreshEntity(spores);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.isCreative())
			stack.shrink(1);
		player.getCooldowns().addCooldown(this, 30);
		return InteractionResultHolder.success(stack);
	}

	@Override
	public BlockState getPottedState(Direction direction) {
		return ((PottedCreeperSporesBlock) SRBlocks.POTTED_CREEPER_SPORES.get()).getDirectionalState(direction);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}