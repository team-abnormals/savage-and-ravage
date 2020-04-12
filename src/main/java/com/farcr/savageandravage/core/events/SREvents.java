package com.farcr.savageandravage.core.events;

import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.farcr.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SREvents
{
	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) 
	{
		if (event.getEntity() instanceof PillagerEntity) 
		{
			PillagerEntity pillager = (PillagerEntity)event.getEntity();
			ImprovedCrossbowGoal<PillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<PillagerEntity>(pillager, 1.0D, 8.0F, 9.0D);
			 pillager.goalSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof RangedCrossbowAttackGoal<?>)
              .findFirst().ifPresent(crossbowGoal -> {
     		    pillager.goalSelector.removeGoal(crossbowGoal);  
     		    pillager.goalSelector.addGoal(3, aiCrossBow);
           });
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
	public static void potSRItem(PlayerInteractEvent.RightClickBlock event) {
		BlockPos pos = event.getPos();
		ItemStack item = event.getItemStack();
		World world = event.getWorld();
		PlayerEntity player = event.getPlayer();
		ResourceLocation pot = new ResourceLocation(("savageandravage:potted_" + item.getItem().getRegistryName().getPath()));
		if (world.getBlockState(pos).getBlock() == Blocks.FLOWER_POT && ForgeRegistries.BLOCKS.containsKey(pot)) {
			world.setBlockState(pos, ForgeRegistries.BLOCKS.getValue(pot).getDefaultState());
			event.getPlayer().swingArm(event.getHand());
			player.addStat(Stats.POT_FLOWER);
			if (!event.getPlayer().abilities.isCreativeMode) item.shrink(1);
		}
	}

}
