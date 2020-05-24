package com.farcr.savageandravage.core.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import com.farcr.savageandravage.common.EffectGrowth;
import com.farcr.savageandravage.common.EffectShrinking;
import com.farcr.savageandravage.common.advancement.SRTriggers;
import com.farcr.savageandravage.common.entity.BurningBannerEntity;
import com.farcr.savageandravage.common.entity.CreeperSporeCloudEntity;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.common.entity.IOwnableMob;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.farcr.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.farcr.savageandravage.core.SavageAndRavage;
import com.farcr.savageandravage.core.config.SRConfig;
import com.farcr.savageandravage.core.registry.SREntities;
import com.farcr.savageandravage.core.registry.SRItems;

import com.farcr.savageandravage.core.registry.SRSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.GolemEntity;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

//TODO work out how to suppress these fucking useless null warnings, @SuppressWarnings doesn't seem to work everywhere

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID)
public class SREvents {
	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof PillagerEntity) {
			PillagerEntity pillager = (PillagerEntity)event.getEntity();
			ImprovedCrossbowGoal<PillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<>(pillager, 1.0D, 8.0F, 5.0D);
			 pillager.goalSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof RangedCrossbowAttackGoal<?>)
              .findFirst().ifPresent(crossbowGoal -> {
     		    pillager.goalSelector.removeGoal(crossbowGoal);
     		    pillager.goalSelector.addGoal(3, aiCrossBow);
           });
			 if (event.getWorld().rand.nextInt(100) == 0 && !event.getWorld().isRemote) {
				 pillager.setItemStackToSlot(EquipmentSlotType.OFFHAND, createRocket());
				 pillager.setActiveHand(Hand.OFF_HAND);
				 pillager.setDropChance(EquipmentSlotType.OFFHAND, 2.0F);
			 }
		}

		if (event.getEntity() instanceof AbstractVillagerEntity) {
		   AbstractVillagerEntity villager = (AbstractVillagerEntity)event.getEntity();
		   villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, SkeletonVillagerEntity.class, 15.0F, 0.5D, 0.5D));
		}
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
	   if (event.getEntity() instanceof CreeperEntity) {
	   	CreeperEntity creeper = (CreeperEntity)event.getEntity();
	   	if (event.getSource().isExplosion() && SRConfig.CreepersSpawnSporesAfterDeathByBoom) {
			creeper.entityDropItem(new ItemStack(SRItems.CREEPER_SPORES.get(), 1 + creeper.world.rand.nextInt(5)));
		}
	   }
	   //kinda messy rn will clean it up later, maybe use switch cases instead of this?
	   else if (event.getEntity() instanceof PillagerEntity) {
		PillagerEntity pillager = (PillagerEntity) event.getEntity();
	   	if (pillager.isServerWorld() && ((ServerWorld)pillager.getEntityWorld()).findRaid(pillager.getPosition()) != null) {
	   		pillager.entityDropItem(new ItemStack(Items.EMERALD, pillager.world.rand.nextInt(2)));
	   		if (pillager.world.rand.nextDouble() < 0.05D) 
	   		{
	   			pillager.entityDropItem(new ItemStack(Items.EMERALD, 4 + pillager.world.rand.nextInt(1)));
	   		}
	   		if (pillager.world.rand.nextDouble() < 0.12D) 
	   		{
	   			pillager.entityDropItem(new ItemStack(Items.EMERALD, 2 + pillager.world.rand.nextInt(1)));
	   		}
		}
	   }
	}

	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
		if(event.getEntityLiving() instanceof GolemEntity && !(event.getEntityLiving() instanceof ShulkerEntity) && event.getTarget() instanceof IOwnableMob){
			if(((IOwnableMob)event.getTarget()).getOwner()!=null&&((MobEntity)event.getTarget()).getAttackTarget()!=event.getEntityLiving()){
				((GolemEntity)event.getEntityLiving()).setAttackTarget(null);
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
			 spores.cloudSize = (creeper.isCharged() ? (int) (creeper.getHealth() / 2) : (int) (creeper.getHealth() / 5));
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
		stack.damageItem(damage, entity, (onBroken) -> { onBroken.sendBreakAnimation(EquipmentSlotType.CHEST);});
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
		if(target instanceof CreepieEntity && event.getItemStack().getItem() == Items.POISONOUS_POTATO && SRConfig.PoisonPotatoCompatEnabled && ModList.get().isLoaded("quark")) {
			poisonCreepie(event);
		}
	}

	//Compatibility with Quark's potato poisoning
	public static String poisonTag = "savageandravage:poisoned_by_potato";

	public static void poisonCreepie(PlayerInteractEvent.EntityInteract event){
		CreepieEntity creepie = (CreepieEntity)event.getTarget();
		if(!creepie.getPersistentData().getBoolean(poisonTag) && !event.getWorld().isRemote) {
			//Vec3d pos = creepie.getPositionVec();
			event.getPlayer().swingArm(event.getHand());
			if(creepie.world.rand.nextDouble() < SRConfig.PoisonChance) {
				creepie.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f, 0.25f);
				//TODO reactivate this if fixed in Quark
				//if(((LivingEntity)target).isServerWorld()) ((ServerWorld)target.world).spawnParticle(ParticleTypes.ENTITY_EFFECT, pos.x, pos.y, pos.z, 5, 0, 1.0, 0, 0.8);
				creepie.getPersistentData().putBoolean(poisonTag, true);
				if(SRConfig.PoisonEffect) {
					creepie.addPotionEffect(new EffectInstance(Effects.POISON, 200));
				} else {
					creepie.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f, 0.5f + creepie.world.rand.nextFloat() / 2);
					//if(((LivingEntity)target).isServerWorld()) ((ServerWorld)target.world).spawnParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 5, 0, 1.0, 0, 0.1);
				}
				if (!event.getPlayer().isCreative()) event.getItemStack().shrink(1);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity() instanceof CreepieEntity && SRConfig.PoisonPotatoCompatEnabled && ModList.get().isLoaded("quark")) {
			CreepieEntity creepie = (CreepieEntity) event.getEntity();
			if(creepie.getPersistentData().getBoolean(poisonTag)) creepie.setGrowingAge(-24000);
		}
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onInteractWithBlock(PlayerInteractEvent.RightClickBlock event) {
		ItemStack heldItemStack = event.getItemStack();
		Item heldItem = event.getItemStack().getItem();
		PlayerEntity player = event.getPlayer();
		BlockPos blockPos = event.getPos();
		@SuppressWarnings("null")
		ResourceLocation pot = new ResourceLocation(("savageandravage:potted_" + heldItem.getRegistryName().getPath()));
		if (event.getWorld().getBlockState(blockPos).getBlock() == Blocks.FLOWER_POT && ForgeRegistries.BLOCKS.containsKey(pot)) {
			event.getWorld().setBlockState(blockPos, ForgeRegistries.BLOCKS.getValue(pot).getDefaultState());
			event.getPlayer().swingArm(event.getHand());
			player.addStat(Stats.POT_FLOWER);
			if (!event.getPlayer().abilities.isCreativeMode) heldItemStack.shrink(1);
		}
		if (event.getWorld().getBlockState(blockPos).getBlock() instanceof AbstractBannerBlock && event.getWorld().getEntitiesWithinAABB(BurningBannerEntity.class, new AxisAlignedBB(blockPos)).isEmpty()) {
			TileEntity te = event.getWorld().getTileEntity(blockPos);
			boolean isFlintAndSteel = heldItem instanceof FlintAndSteelItem;
			boolean isFireCharge = heldItem instanceof FireChargeItem;
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
	public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) throws InvocationTargetException, IllegalAccessException {
		LivingEntity affected = event.getEntityLiving();
		//Values initialised for what they should be if the potion is growth
		boolean shouldSetChild = false;
		int growingAgeValue = 0;
		if(event.getPotionEffect().getPotion() instanceof EffectShrinking){
			shouldSetChild = true;
			growingAgeValue = -24000;
		}
		if(event.getPotionEffect().getPotion() instanceof EffectGrowth || shouldSetChild) {
			boolean canChange = false;
			if(affected instanceof SlimeEntity){
				SlimeEntity slime = (SlimeEntity)affected;
				int size = slime.getSlimeSize();
				if(shouldSetChild ? size > 1 : size < 3){
					canChange = true;
					Method setSize = ObfuscationReflectionHelper.findMethod(SlimeEntity.class,"func_70799_a",int.class,boolean.class);
					setSize.invoke(slime,(size + (shouldSetChild ? (size < 4 ? -1:-2) : (size < 2 ? 1:2))), false);
				}
			}
			else if (checkBooflo(affected,shouldSetChild)) canChange = true;
			else if(shouldSetChild != affected.isChild()){
				canChange = true;
				if(affected instanceof AgeableEntity) ((AgeableEntity)affected).setGrowingAge(growingAgeValue);
				else if(shouldSetChild && affected instanceof CreeperEntity) convertCreeper((CreeperEntity)affected);
				else if(!shouldSetChild && affected instanceof CreepieEntity) ((CreepieEntity)affected).setGrowingAge(growingAgeValue);
				else if(affected instanceof ZombieEntity) ((ZombieEntity)affected).setChild(shouldSetChild);
				else canChange = false;
			}
			if(!canChange) {
				EffectInstance effectInstance;
				if (!shouldSetChild) affected.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 2400, 0));
				if (affected.isEntityUndead()) shouldSetChild = !shouldSetChild;
				effectInstance = new EffectInstance(shouldSetChild ? Effects.INSTANT_DAMAGE : Effects.INSTANT_HEALTH, 1, 1);
				effectInstance.getPotion().affectEntity(null, null, affected, effectInstance.getAmplifier(), 1.0D);
			}
			if(affected.isServerWorld()){
				((ServerWorld) affected.world).spawnParticle(canChange ? (shouldSetChild ? ParticleTypes.TOTEM_OF_UNDYING : ParticleTypes.HAPPY_VILLAGER) : ParticleTypes.LARGE_SMOKE, affected.getPosXRandom(0.3D), affected.getPosYRandom() - 0.1D, affected.getPosZRandom(0.3D), canChange ? 40 : 20, 0.3D, 0.6D, 0.3D, canChange ? 0.2D : 0.01D);
				affected.playSound(canChange? SRSounds.GROWTH_MODIFICATION_SUCCESS.get() : SRSounds.GROWTH_MODIFICATION_FAILURE.get(),1.0F,1.0F);
			}
		}
	}

	public static boolean checkBooflo(LivingEntity affected, boolean isBabyPotion){
		if(ModList.get().isLoaded("endergetic")) {
			return ((!isBabyPotion && affected.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("endergetic:booflo_baby")) ||
			affected.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("endergetic:booflo_adolescent")) ||
			(isBabyPotion && affected.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("endergetic:booflo")))));
		}
		return false;
	}

	public static void convertCreeper(CreeperEntity creeper){
		CreepieEntity creepie = SREntities.CREEPIE.get().create(creeper.world);
		creepie.copyLocationAndAnglesFrom(creeper.getEntity());
		creepie.onInitialSpawn(creeper.world, creeper.world.getDifficultyForLocation(new BlockPos(creepie)), SpawnReason.CONVERSION, null, null);
		creeper.remove();
		creepie.setNoAI(creeper.isAIDisabled());
		if (creeper.hasCustomName()) {
			creepie.setCustomName(creeper.getCustomName());
			creepie.setCustomNameVisible(creeper.isCustomNameVisible());
		}

		if (creeper.isNoDespawnRequired()) {
			creepie.enablePersistence();
		}
		if(creeper.getLeashed()) {
			creepie.setLeashHolder(creeper.getLeashHolder(), true);
			creeper.clearLeashed(true, false);
		}

		if(creeper.getRidingEntity() != null) {
			creepie.startRiding(creeper.getRidingEntity());
		}
		creepie.setInvulnerable(creeper.isInvulnerable());
		creeper.setHealth(creeper.getMaxHealth());
		creeper.world.addEntity(creepie);
	}
	
	public static ItemStack createRocket() {
	    ItemStack rocket= new ItemStack(Items.FIREWORK_ROCKET);
        ItemStack star = new ItemStack(Items.FIREWORK_STAR);
        CompoundNBT compoundnbt = star.getOrCreateChildTag("Explosion");
        compoundnbt.putInt("Type", FireworkRocketItem.Shape.BURST.getIndex());
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
