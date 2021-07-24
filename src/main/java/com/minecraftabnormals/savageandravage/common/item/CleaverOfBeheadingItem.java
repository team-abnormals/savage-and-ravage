package com.minecraftabnormals.savageandravage.common.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import net.minecraft.item.Item.Properties;

@EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class CleaverOfBeheadingItem extends SwordItem {
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.TOTEM_OF_UNDYING);
	private final float attackDamage;
	private final float attackSpeed;

	public CleaverOfBeheadingItem(IItemTier tier, float attackDamage, float attackSpeed, Properties properties) {
		super(tier, 6, attackSpeed, properties);
		this.attackDamage = attackDamage;
		this.attackSpeed = attackSpeed;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();

		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

	@SubscribeEvent
	public static void onExecutionerCleaverKill(LivingDamageEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity && event.getEntity() instanceof PlayerEntity) {
			LivingEntity wielder = (LivingEntity) event.getSource().getEntity();
			PlayerEntity targetPlayer = (PlayerEntity) event.getEntity();
			World world = wielder.level;

			if (wielder.getMainHandItem().getItem() != SRItems.CLEAVER_OF_BEHEADING.get() || targetPlayer == null || targetPlayer.getHealth() - event.getAmount() > 0)
				return;

			CompoundNBT skullNbt = new CompoundNBT();
			skullNbt.putString("SkullOwner", targetPlayer.getName().getString());

			ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
			stack.setTag(skullNbt);

			if (!world.isClientSide())
				world.addFreshEntity(new ItemEntity(world, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), stack));
		}
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}