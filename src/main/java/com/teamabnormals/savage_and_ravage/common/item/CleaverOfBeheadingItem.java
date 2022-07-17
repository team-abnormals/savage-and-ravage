package com.teamabnormals.savage_and_ravage.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class CleaverOfBeheadingItem extends TieredItem {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.TOTEM_OF_UNDYING);
	private final float attackDamage;
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public CleaverOfBeheadingItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
		super(tier, properties);
		this.attackDamage = attackDamage + tier.getAttackDamageBonus();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
	}

	public float getDamage() {
		return this.attackDamage;
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();
			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity enemy, LivingEntity user) {
		stack.hurtAndBreak(1, user, (player) -> {
			player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
		if (state.getDestroySpeed(world, pos) != 0.0F) {
			stack.hurtAndBreak(2, entity, (player) -> {
				player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
		}

		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		return state.is(Blocks.COBWEB);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return super.canApplyAtEnchantingTable(stack, enchantment) || (enchantment.category == EnchantmentCategory.WEAPON && enchantment != Enchantments.SWEEPING_EDGE);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return super.isBookEnchantable(stack, book) && !EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.SWEEPING_EDGE);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}