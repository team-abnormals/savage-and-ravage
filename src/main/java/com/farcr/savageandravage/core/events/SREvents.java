package com.farcr.savageandravage.core.events;

import java.awt.print.Pageable;
import java.util.Random;

import com.farcr.savageandravage.common.EffectGrowing;
import com.farcr.savageandravage.common.advancement.SRTriggers;
import com.farcr.savageandravage.common.entity.BurningBannerEntity;
import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.farcr.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.config.SRConfig;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID)
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
			 if (event.getWorld().rand.nextInt(100) == 0 && !event.getWorld().isRemote)
			 {
				 pillager.setItemStackToSlot(EquipmentSlotType.OFFHAND, createRocket());
				 pillager.setActiveHand(Hand.OFF_HAND);
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
	    if (event.getSource().isExplosion() && SRConfig.CreepersSpawnSporesAfterDeathByBoom) 
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
			if (SRConfig.CreeperNoDestroyBlocks) {
			   event.getAffectedBlocks().clear();
			}
			CreeperSporeCloudEntity spores = new CreeperSporeCloudEntity(SREntities.CREEPER_SPORE_CLOUD.get(), event.getWorld());
			if (SRConfig.CreepersSpawnCreepiesWhenBoom) {
			spores.cloudSize = (int) (creeper.getHealth() / 5);
			 if (creeper.isCharged()) {
		       spores.cloudSize = (int) (creeper.getHealth() / 2);  //feedback, this needs.
			 }
			 spores.copyLocationAndAnglesFrom(creeper);
			 creeper.world.addEntity(spores);
			}
		}
	}
	
	@SubscribeEvent
	public static void handleBlastProof(LivingDamageEvent event){
		LivingEntity entity = event.getEntityLiving();
		float decrease = 0.0F;
		
		boolean flag = false;
		
		ItemStack head = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);
		ItemStack chest = entity.getItemStackFromSlot(EquipmentSlotType.CHEST);
		ItemStack legs = entity.getItemStackFromSlot(EquipmentSlotType.LEGS);
		ItemStack feet = entity.getItemStackFromSlot(EquipmentSlotType.FEET);
		
		if (event.getSource().isExplosion()) {
			if (head.getItem() == SRItems.GRIEFER_HELMET.get()) {
				decrease += 0.25F; 
				flag = true;
				SREvents.blastProtect(head, event.getEntityLiving());

			}
			if (chest.getItem() == SRItems.GRIEFER_CHESTPLATE.get()) {
				decrease += 0.30F; 
				flag = true;
				SREvents.blastProtect(chest, event.getEntityLiving());
			}
			if (legs.getItem() == SRItems.GRIEFER_LEGGINGS.get()) {
				decrease += 0.25F; 
				flag = true;
				SREvents.blastProtect(legs, event.getEntityLiving());

			}
			if (feet.getItem() == SRItems.GRIEFER_BOOTS.get()) {
				decrease += 0.20F; 
				flag = true;
				SREvents.blastProtect(feet, event.getEntityLiving());
			}
			if (flag) {
				event.setAmount(event.getAmount() - (event.getAmount() * decrease));
			}
		}
	}
	
	public static void blastProtect(ItemStack stack, LivingEntity entity) {
		int damage = 22;
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack) > 0) {
			damage -= EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack) * 8;
		}
		stack.damageItem(damage, entity, (onBroken) -> { onBroken.sendBreakAnimation(stack.getEquipmentSlot());});
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
			event.getPlayer().swingArm(event.getHand());
			event.getWorld().addEntity(creepieEntity);
		}
		/**
		 * @author Vazkii (Vasco Lavos) - adapted from poison potato code in Quark
		 * */
		if(target instanceof CreepieEntity && event.getItemStack().getItem() == Items.POISONOUS_POTATO && SRConfig.PoisonPotatoCompatEnabled && ModList.get().isLoaded("quark")) {
			CreepieEntity creepie = (CreepieEntity)target;
			if(!creepie.getPersistentData().getBoolean("savageandravage:poison_potato_applied")) {
				if(!event.getWorld().isRemote) {
					Vec3d pos = creepie.getPositionVec();
					if(creepie.world.rand.nextDouble() < SRConfig.PoisonChance) {
						creepie.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f, 0.25f);
						creepie.world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.x, pos.y, pos.z, 0.2, 0.8, 0);
						creepie.getPersistentData().putBoolean("savageandravage:poison_potato_applied", true);
						if (SRConfig.PoisonEffect)
							creepie.addPotionEffect(new EffectInstance(Effects.POISON, 200));
					} else {
						creepie.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f, 0.5f + creepie.world.rand.nextFloat() / 2);
						creepie.world.addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 0, 0.1, 0);
					}

					if (!event.getPlayer().isCreative())
						event.getItemStack().shrink(1);

				} else event.getPlayer().swingArm(event.getHand());

			}
		}
	}

	/**
	 * @author Vazkii (Vasco Lavos) - adapted from poison potato code in Quark
	 * */
	@SubscribeEvent
	public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity() instanceof CreepieEntity && SRConfig.PoisonPotatoCompatEnabled && ModList.get().isLoaded("quark")) {
			CreepieEntity creepie = (CreepieEntity) event.getEntity();
			if (creepie.getPersistentData().getBoolean("savageandravage:poison_potato_applied")){
				creepie.setGrowingAge(-24000);
			}
		}
	}

	@SuppressWarnings("deprecation")
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
							heldItemStack.damageItem(1, player, (p_219998_1_) -> {
								p_219998_1_.sendBreakAnimation(event.getHand());
							});
						}
					}
					if (isFireCharge && !(event.getWorld().getBlockState(blockPos.offset(event.getFace())).isAir())) {
						event.getWorld().playSound(player, blockPos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F);
						player.swingArm(event.getHand());
						if (!(player.abilities.isCreativeMode)) {
							heldItemStack.shrink(1);
						}
					}
					if (player instanceof ServerPlayerEntity) {
						SRTriggers.BURN_BANNER.trigger((ServerPlayerEntity) player);
					}
					if(!event.getWorld().isRemote) {
						ServerWorld server = (ServerWorld) event.getWorld();
						if (server.findRaid(blockPos) == null) {
							EffectInstance badOmenOnPlayer = event.getPlayer().getActivePotionEffect(Effects.BAD_OMEN);
							int i = 1;
							if (badOmenOnPlayer != null) {
								i += badOmenOnPlayer.getAmplifier();
								event.getPlayer().removeActivePotionEffect(Effects.BAD_OMEN);
							} else {
								--i;
							}
							i = MathHelper.clamp(i, 0, 5);
							EffectInstance effectinstance = new EffectInstance(Effects.BAD_OMEN, 120000, i, false, false, true);
							if (!(event.getWorld().getGameRules().getBoolean(GameRules.DISABLE_RAIDS))) {
								event.getPlayer().addPotionEffect(effectinstance);
							}
						}
						event.getWorld().addEntity(new BurningBannerEntity(event.getWorld(), blockPos));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
		if(event.getPotionEffect().getPotion() instanceof EffectGrowing){
			if(event.getEntityLiving() instanceof AgeableEntity){
				((AgeableEntity)event.getEntityLiving()).setGrowingAge(0);
			}
			else if(event.getEntityLiving() instanceof CreepieEntity){
				((CreepieEntity)event.getEntityLiving()).setGrowingAge(0);
			}
			else{
				EffectInstance effectInstance = new EffectInstance(Effects.INSTANT_HEALTH,1,3);
				effectInstance.getPotion().affectEntity(null, null, event.getEntityLiving(), effectInstance.getAmplifier(), 1.0D);
				event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.ABSORPTION, 2400, 0));
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
