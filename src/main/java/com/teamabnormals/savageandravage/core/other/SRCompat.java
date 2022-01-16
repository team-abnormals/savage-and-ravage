package com.teamabnormals.savageandravage.core.other;

import com.teamabnormals.savageandravage.core.registry.SRSounds;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.savageandravage.common.entity.BurningBannerEntity;
import com.teamabnormals.savageandravage.common.entity.MischiefArrowEntity;
import com.teamabnormals.savageandravage.common.entity.SporeCloudEntity;
import com.teamabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.teamabnormals.savageandravage.common.item.CreeperSporesItem;
import com.teamabnormals.savageandravage.core.SavageAndRavage;
import com.teamabnormals.savageandravage.core.registry.SRBlocks;
import com.teamabnormals.savageandravage.core.registry.SRItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

import static com.teamabnormals.blueprint.core.util.BlockUtil.getEntitiesAtOffsetPos;
import static com.teamabnormals.blueprint.core.util.BlockUtil.offsetPos;

public class SRCompat {

	public static void registerCompat() {
		registerFlammables();
		registerDispenserBehaviors();
		registerNoteBlocks();
	}

	public static void registerFlammables() {
		DataUtil.registerFlammable(SRBlocks.CREEPER_SPORE_SACK.get(), 30, 60);
		DataUtil.registerFlammable(SRBlocks.SPORE_BOMB.get(), 15, 100);
	}

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(SRItems.MISCHIEF_ARROW.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level world, Position position, ItemStack stack) {
				return new MischiefArrowEntity(world, position.x(), position.y(), position.z());
			}
		});
		DispenserBlock.registerBehavior(SRItems.CREEPER_SPORES.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level world, Position position, ItemStack stack) {
				SporeCloudEntity cloud = new SporeCloudEntity(world, position.x(), position.y(), position.z());
				cloud.setCloudSize(CreeperSporesItem.getThrownSporeCloudSize(world.getRandom()));
				return cloud;
			}
		});

		DispenserBlock.registerBehavior(SRBlocks.SPORE_BOMB.get(), new DefaultDispenseItemBehavior() {
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				Level world = source.getLevel();
				BlockPos blockpos = offsetPos(source);
				SporeBombEntity sporeBomb = new SporeBombEntity(world, (double) blockpos.getX() + 0.5D, blockpos.getY(), (double) blockpos.getZ() + 0.5D, null);
				world.addFreshEntity(sporeBomb);
				world.playSound(null, sporeBomb.getX(), sporeBomb.getY(), sporeBomb.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.shrink(1);
				return stack;
			}
		});
		ForgeRegistries.ITEMS.getEntries().stream().map(Map.Entry::getValue).filter(i -> i instanceof BannerItem).forEach(i -> DataUtil.registerAlternativeDispenseBehavior(new DataUtil.AlternativeDispenseBehavior(SavageAndRavage.MOD_ID, i, (source, stack) -> !getEntitiesAtOffsetPos(source, LivingEntity.class, EntitySelector.NO_SPECTATORS.and(new EntitySelector.MobCanWearArmorEntitySelector(stack))).isEmpty(), ArmorItem.DISPENSE_ITEM_BEHAVIOR, (id1, id2) -> id2.equals("quark") ? 1 : 0)));
		DataUtil.registerAlternativeDispenseBehavior(new DataUtil.AlternativeDispenseBehavior(SavageAndRavage.MOD_ID, Items.FLINT_AND_STEEL, (source, stack) -> SREvents.isValidBurningBannerPos(source.getLevel(), offsetPos(source)), new DefaultDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				Level world = source.getLevel();
				world.addFreshEntity(new BurningBannerEntity(world, offsetPos(source), null));
				if (stack.hurt(1, world.random, null)) {
					stack.setCount(0);
				}
				return stack;
			}
		}));
	}

	public static void registerNoteBlocks() {
		DataUtil.registerNoteBlockInstrument(new DataUtil.CustomNoteBlockInstrument(SavageAndRavage.MOD_ID, source -> source.getBlockState().is(Blocks.TARGET), SRSounds.BLOCK_NOTE_BLOCK_HIT_MARKER.get()));
		DataUtil.registerNoteBlockInstrument(new DataUtil.CustomNoteBlockInstrument(SavageAndRavage.MOD_ID, source -> source.getBlockState().is(SRTags.GLOOMY_TILES), SRSounds.BLOCK_NOTE_BLOCK_HARPSICHORD.get()));
		DataUtil.registerNoteBlockInstrument(new DataUtil.CustomNoteBlockInstrument(SavageAndRavage.MOD_ID, source -> source.getBlockState().is(SRTags.BLAST_PROOF), SRSounds.BLOCK_NOTE_BLOCK_ORCHESTRAL_HIT.get()));
	}
}
