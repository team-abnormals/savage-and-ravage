package com.farcr.savageandravage.core.events;

import com.farcr.savageandravage.common.advancement.SRTriggers;
import com.farcr.savageandravage.common.entity.BurningBannerEntity;
import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.farcr.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class SREvents
{
	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) 
	{
		if (event.getEntity() instanceof PillagerEntity) 
		{
			PillagerEntity pillager = (PillagerEntity)event.getEntity();
			ImprovedCrossbowGoal<PillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<PillagerEntity>(pillager, 1.0D, 8.0F, 5.0D);
			 pillager.goalSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof RangedCrossbowAttackGoal<?>)
              .findFirst().ifPresent(crossbowGoal -> {
     		    pillager.goalSelector.removeGoal(crossbowGoal);  
     		    pillager.goalSelector.addGoal(3, aiCrossBow);
           });
			 if (event.getWorld().rand.nextInt(100) == 0)
			 {
				 pillager.setItemStackToSlot(EquipmentSlotType.OFFHAND, createRocket());
				 pillager.setDropChance(EquipmentSlotType.OFFHAND, 2.0F);
			 }
		}
		
		if (event.getEntity() instanceof AbstractVillagerEntity)  
		{
		   AbstractVillagerEntity villager = (AbstractVillagerEntity)event.getEntity();
		   villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, SkeletonVillagerEntity.class, 15.0F, 0.5D, 0.5D));
		}
	}
	
	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) 
	{
	   if (event.getEntity() instanceof CreeperEntity && !(event.getEntity() instanceof CreepieEntity))
	   {
	    CreeperEntity creeper = (CreeperEntity)event.getEntity();
	    if (event.getSource().isExplosion()) 
	    {
	     creeper.entityDropItem(new ItemStack(SRItems.CREEPER_SPORES.get(), 1 + creeper.world.rand.nextInt(5)));
	    }
	   }
	 }
	
	
	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event)
	{
		if (event.getExplosion().getExplosivePlacedBy() instanceof CreeperEntity && !(event.getExplosion().getExplosivePlacedBy() instanceof CreepieEntity))
		{
			CreeperEntity creeper = (CreeperEntity)event.getExplosion().getExplosivePlacedBy();
			event.getAffectedBlocks().clear();
			CreeperSporeCloudEntity spores = new CreeperSporeCloudEntity(SREntities.CREEPER_SPORE_CLOUD.get(), event.getWorld());
			spores.size = (int) (creeper.getHealth() / 5);
			spores.copyLocationAndAnglesFrom(creeper);
			creeper.world.addEntity(spores);
		}
	}

	@SubscribeEvent
	public static void onInteractWithEntity(PlayerInteractEvent.EntityInteract event){
		Item heldItem = event.getItemStack().getItem();
		Entity target = event.getTarget();
		if (target instanceof CreeperEntity && heldItem == Items.CREEPER_SPAWN_EGG) {
			CreepieEntity creepieEntity = new CreepieEntity(SREntities.CREEPIE.get(), event.getWorld());
			creepieEntity.copyLocationAndAnglesFrom(target);
			if (event.getItemStack().hasDisplayName()) {
				creepieEntity.setCustomName(event.getItemStack().getDisplayName());
			}
			if (!event.getPlayer().abilities.isCreativeMode) {
				event.getItemStack().shrink(1);
			}
			event.getWorld().addEntity(creepieEntity);
		}
	}

	@SubscribeEvent
	public static void onInteractWithBlock(PlayerInteractEvent.RightClickBlock event) {
		ItemStack heldItemStack = event.getItemStack();
		Item heldItem = event.getItemStack().getItem();
		PlayerEntity player = event.getPlayer();
		BlockPos blockPos = event.getPos();
		ResourceLocation pot = new ResourceLocation(("savageandravage:potted_" + heldItem.getRegistryName().getPath()));
		if (event.getWorld().getBlockState(blockPos).getBlock() == Blocks.FLOWER_POT && ForgeRegistries.BLOCKS.containsKey(pot)) {
			event.getWorld().setBlockState(blockPos, ForgeRegistries.BLOCKS.getValue(pot).getDefaultState());
			event.getPlayer().swingArm(event.getHand());
			player.addStat(Stats.POT_FLOWER);
			if (!event.getPlayer().abilities.isCreativeMode) heldItemStack.shrink(1);
		}
		if (event.getWorld().getBlockState(blockPos).getBlock() instanceof AbstractBannerBlock && event.getWorld().getEntitiesWithinAABB(BurningBannerEntity.class, new AxisAlignedBB(blockPos)).isEmpty()) {
			TileEntity te = event.getWorld().getTileEntity(blockPos);
			Boolean isFlintAndSteel = heldItem instanceof FlintAndSteelItem;
			Boolean isFireCharge = heldItem instanceof FireChargeItem;
			if ((isFlintAndSteel || isFireCharge)) {
				BannerTileEntity banner = (BannerTileEntity) te;
				TranslationTextComponent bannerName;
				try {
					bannerName = (TranslationTextComponent) banner.getName();
				} catch (ClassCastException cast) {
					bannerName = null;
				}
				if (bannerName.getKey().contains("block.minecraft.ominous_banner")) {
					/*if(event.getWorld().getBlockState(event.getPos().offset(event.getFace())).getBlock() instanceof FireBlock){
					event.getWorld().removeBlock(event.getPos().offset(event.getFace()), false); TODO come back to this later
				}*/
					if (isFlintAndSteel) {
						event.getWorld().playSound(player, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
						player.swingArm(event.getHand());
						if (player instanceof ServerPlayerEntity) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, blockPos, heldItemStack);
							event.getItemStack().damageItem(1, player, (p_219998_1_) -> {
								p_219998_1_.sendBreakAnimation(event.getHand());
							});
						}
					}
					if (player instanceof ServerPlayerEntity) {
						SRTriggers.BURN_BANNER.trigger((ServerPlayerEntity)player);
					}
					event.getWorld().addEntity(new BurningBannerEntity(event.getWorld(),  blockPos));
				}
			}
		}
	}
	
	public static ItemStack createRocket() {
	    ItemStack rocket= new ItemStack(Items.FIREWORK_ROCKET);
        ItemStack star = new ItemStack(Items.FIREWORK_STAR);
        CompoundNBT compoundnbt = star.getOrCreateChildTag("Explosion");
        compoundnbt.putInt("Type", FireworkRocketItem.Shape.BURST.func_196071_a());
        CompoundNBT compoundnbt1 = rocket.getOrCreateChildTag("Fireworks");
        ListNBT listnbt = new ListNBT();
        CompoundNBT compoundnbt2 = star.getChildTag("Explosion");
        if (compoundnbt2 != null) {
            listnbt.add(compoundnbt2);
        }
        if (!listnbt.isEmpty()) {
            compoundnbt1.put("Explosions", listnbt);
        }
        return rocket;
	}

}
