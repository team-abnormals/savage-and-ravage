package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.savageandravage.common.entity.*;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.common.entity.goals.AttackTargetBlockRandomlyGoal;
import com.minecraftabnormals.savageandravage.common.entity.goals.AvoidGrieferOwnedCreepiesGoal;
import com.minecraftabnormals.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.minecraftabnormals.savageandravage.common.item.IPottableItem;
import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.mixin.LivingEntityAccessor;
import com.minecraftabnormals.savageandravage.core.registry.SRAttributes;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.minecraftabnormals.savageandravage.core.registry.SREffects;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SREvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof PillagerEntity) {
			PillagerEntity pillager = (PillagerEntity) event.getEntity();
			ImprovedCrossbowGoal<PillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<>(pillager, 1.0D, 8.0F, 5.0D);
			pillager.goalSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof RangedCrossbowAttackGoal<?>).findFirst().ifPresent(crossbowGoal -> {
				pillager.goalSelector.removeGoal(crossbowGoal);
				pillager.goalSelector.addGoal(3, aiCrossBow);
			});
			AttackTargetBlockRandomlyGoal<PillagerEntity> attackTargetBlock = new AttackTargetBlockRandomlyGoal<>(pillager);
			pillager.goalSelector.addGoal(5, attackTargetBlock);
			if (event.getWorld().rand.nextInt(100) == 0 && !event.getWorld().isRemote()) {
				pillager.setItemStackToSlot(EquipmentSlotType.OFFHAND, createRocket());
				pillager.setActiveHand(Hand.OFF_HAND);
				pillager.setDropChance(EquipmentSlotType.OFFHAND, 2.0F); // We should make this into a mixin as this would make it so pillagers spawn with a rocket item in their hand everytime the world is loaded.
			}
		}
		if (SRConfig.COMMON.evokersUseTotems.get() && event.getEntity() instanceof EvokerEntity) {
			EvokerEntity evoker = (EvokerEntity) event.getEntity();
			evoker.goalSelector.addGoal(1, new AvoidEntityGoal<IronGolemEntity>(evoker, IronGolemEntity.class, 8.0F, 0.6D, 1.0D) {
				@Override
				public boolean shouldExecute() {
					return super.shouldExecute() && ((IDataManager) this.entity).getValue(SREntities.EVOKER_SHIELD_TIME) > 0;
				}
			});
		}
		if (SRConfig.COMMON.reducedVexHealth.get() && event.getEntity() instanceof VexEntity) {
			VexEntity vex = (VexEntity) event.getEntity();
			ModifiableAttributeInstance maxHealth = vex.getAttribute(Attributes.MAX_HEALTH);
			if (maxHealth != null)
				maxHealth.setBaseValue(2.0);
			if (vex.getHealth() > vex.getMaxHealth()) {
				vex.setHealth(vex.getMaxHealth());
			}
		}
		if (event.getEntity() instanceof IronGolemEntity && !SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			IronGolemEntity golem = (IronGolemEntity) event.getEntity();
			golem.targetSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(noAngryAtCreeper -> {
				golem.targetSelector.removeGoal(noAngryAtCreeper);
				golem.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(golem, MobEntity.class, 5, false, false, (p_213619_0_) -> p_213619_0_ instanceof IMob));
			});
		}
		if (event.getEntity().getType() == EntityType.CREEPER && !SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			CreeperEntity creeper = (CreeperEntity) event.getEntity();
			creeper.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(creeper, IronGolemEntity.class, true));
		}

		if (event.getEntity() instanceof CatEntity) {
			CatEntity cat = (CatEntity) event.getEntity();
			cat.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(cat, CreepieEntity.class, false));
		}

		if (event.getEntity() instanceof OcelotEntity) {
			OcelotEntity ocelot = (OcelotEntity) event.getEntity();
			ocelot.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(ocelot, CreepieEntity.class, false));
		}

		if (event.getEntity() instanceof AbstractVillagerEntity) {
			AbstractVillagerEntity villager = (AbstractVillagerEntity) event.getEntity();
			villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, SkeletonVillagerEntity.class, 8.0F, 0.6D, 0.6D));
			villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, GrieferEntity.class, 8.0F, 0.8D, 0.8D));
			villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, IceologerEntity.class, 8.0F, 0.8D, 0.8D));
			villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, ExecutionerEntity.class, 8.0F, 0.8D, 0.8D));
			villager.goalSelector.addGoal(1, new AvoidGrieferOwnedCreepiesGoal<>(villager, CreepieEntity.class, 8.0F, 0.8D, 0.8D));
		}
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.CREEPER) {
			CreeperEntity creeper = (CreeperEntity) entity;
			MinecraftServer server = entity.getServer();
			if (event.getSource().isExplosion() && SRConfig.COMMON.creepersDropSporesAfterExplosionDeath.get() && server != null) {
				LootTable loottable = server.getLootTableManager().getLootTableFromLocation(SRLoot.CREEPER_EXPLOSION_DROPS);
				LivingEntityAccessor accessor = (LivingEntityAccessor) creeper;
				LootContext ctx = accessor.invokeGetLootContextBuilder(accessor.getRecentlyHit() > 0, event.getSource()).build(LootParameterSets.ENTITY);
				loottable.generate(ctx).forEach(creeper::entityDropItem);
			}
		} else if (entity instanceof PillagerEntity) {
			PillagerEntity pillager = (PillagerEntity) entity;
			MinecraftServer server = entity.getServer();
			if (!pillager.world.isRemote() && ((ServerWorld) pillager.getEntityWorld()).findRaid(pillager.getPosition()) != null && server != null) {
				LootTable loottable = server.getLootTableManager().getLootTableFromLocation(SRLoot.PILLAGER_RAID_DROPS);
				LivingEntityAccessor accessor = (LivingEntityAccessor) entity;
				LootContext ctx = accessor.invokeGetLootContextBuilder(accessor.getRecentlyHit() > 0, event.getSource()).build(LootParameterSets.ENTITY);
				loottable.generate(ctx).forEach(pillager::entityDropItem);
			}
		} else if (entity instanceof EvokerEntity) {
			MinecraftServer server = entity.getServer();
			if (server != null) {
				LootTable loottable = server.getLootTableManager().getLootTableFromLocation(SRLoot.EVOKER_TOTEM_REPLACEMENT);
				LivingEntityAccessor accessor = (LivingEntityAccessor) entity;
				LootContext ctx = accessor.invokeGetLootContextBuilder(accessor.getRecentlyHit() > 0, event.getSource()).build(LootParameterSets.ENTITY);
				List<ItemStack> stacks = loottable.generate(ctx);
				if (!stacks.isEmpty()) {
					Collection<ItemEntity> drops = event.getDrops();
					for (ItemEntity item : new ArrayList<>(drops)) {
						if (item.getItem().getItem() == Items.TOTEM_OF_UNDYING) {
							drops.remove(item);
							for (ItemStack stack : stacks)
								entity.entityDropItem(stack);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity.getActivePotionEffect(SREffects.WEIGHT.get()) != null)
			entity.setMotion(entity.getMotion().getX(), 0.0D, entity.getMotion().getZ());
	}

	@SubscribeEvent
	public static void onKeyInput(InputEvent.KeyInputEvent event) {
		//do player stuff
	}

	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
		LivingEntity entity = event.getEntityLiving();
		LivingEntity target = event.getTarget();
		if (target != null) {
			if (entity instanceof GolemEntity && !(entity instanceof ShulkerEntity) && target instanceof IOwnableMob) {
				if (((IOwnableMob) target).getOwner() instanceof PlayerEntity && ((MobEntity) target).getAttackTarget() != entity) {
					((GolemEntity) entity).setAttackTarget(null);
				}
			}
			if (SRConfig.COMMON.evokersUseTotems.get() && entity instanceof EvokerEntity && ((IDataManager) entity).getValue(SREntities.EVOKER_SHIELD_TIME) > 0)
				((EvokerEntity) entity).setAttackTarget(null);
		}
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event) {
		World world = event.getWorld();
		Explosion explosion = event.getExplosion();
		if (explosion.getExplosivePlacedBy() != null) {
			if (explosion.getExplosivePlacedBy().getType() == EntityType.CREEPER) {
				if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
					event.getAffectedBlocks().clear();
				}
				if (SRConfig.COMMON.creeperExplosionsSpawnCreepies.get()) {
					CreeperEntity creeper = (CreeperEntity) explosion.getExplosivePlacedBy();
					SporeCloudEntity spores = SREntities.SPORE_CLOUD.get().create(world);
					if (spores == null)
						return;
					spores.setSpawnCloudInstantly(true);
					spores.creepiesAttackPlayersOnly(true);
					if (creeper.isCharged()) {
						spores.setCharged(true);
					}
					spores.setCloudSize(creeper.isCharged() ? (int) (creeper.getHealth() / 2) : (int) (creeper.getHealth() / 5));
					spores.copyLocationAndAnglesFrom(creeper);
					creeper.world.addEntity(spores);
				}
			}
		}

		if (explosion.getExploder() != null && explosion.getExploder().getType() == SREntities.SPORE_BOMB.get()) {
			for (BlockPos pos : event.getAffectedBlocks()) {
				if (world.getBlockState(pos).getBlock() == SRBlocks.SPORE_BOMB.get()) {
					world.removeBlock(pos, false);
					SporeBombEntity sporebomb = new SporeBombEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosion.getExplosivePlacedBy());
					sporebomb.setFuse((short) (world.getRandom().nextInt(sporebomb.getFuse() / 4) + sporebomb.getFuse() / 8));
					world.addEntity(sporebomb);
				}
			}
		}

		List<Entity> safeItems = new ArrayList<>();
		for (Entity entity : event.getAffectedEntities()) {
			if (entity instanceof ItemEntity) {
				ItemStack itemstack = ((ItemEntity) entity).getItem();
				if (itemstack.getItem().isIn(SRTags.BLAST_PROOF_ITEMS)) {
					safeItems.add(entity);
				}
			}
		}
		event.getAffectedEntities().removeAll(safeItems);
	}

	@SubscribeEvent
	public static void onEntityDamage(LivingDamageEvent event) {
		LivingEntity entity = event.getEntityLiving();

		if (event.getSource().isExplosion()) {
			double decrease = 0;

			for (EquipmentSlotType slot : EquipmentSlotType.values()) {
				ItemStack stack = entity.getItemStackFromSlot(slot);
				Collection<AttributeModifier> modifiers = stack.getAttributeModifiers(slot).get(SRAttributes.EXPLOSIVE_DAMAGE_REDUCTION.get());
				if (modifiers.isEmpty())
					continue;

				decrease += modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				stack.damageItem(22 - EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack) * 8, entity, onBroken -> onBroken.sendBreakAnimation(slot));
			}

			if (decrease == 0)
				return;

			event.setAmount(event.getAmount() - (float) (event.getAmount() * decrease));
		}

		IDataManager data = (IDataManager) entity;
		if (SRConfig.COMMON.evokersUseTotems.get() && entity instanceof EvokerEntity) {
			if (entity.getHealth() - event.getAmount() <= 0 && event.getSource().getImmediateSource() instanceof ProjectileEntity) {
				if (data.getValue(SREntities.EVOKER_SHIELD_TIME) <= 0 && data.getValue(SREntities.EVOKER_SHIELD_COOLDOWN) <= 0) {
					event.setCanceled(true);
					entity.setHealth(2.0F);
					data.setValue(SREntities.EVOKER_SHIELD_TIME, 600);
					if (!entity.world.isRemote()) {
						entity.world.setEntityState(entity, (byte) 35);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event) {
		Entity entity = event.getEntity();
		if (SRConfig.COMMON.evokersUseTotems.get() && entity instanceof EvokerEntity) {
			IDataManager data = (IDataManager) entity;
			if (data.getValue(SREntities.EVOKER_SHIELD_TIME) > 0) {
				if (event.getSource().getImmediateSource() instanceof ProjectileEntity)
					event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
		ItemStack stack = event.getItemStack();
		Entity target = event.getTarget();
		if (target.getType() == EntityType.CREEPER || target.getType() == SREntities.CREEPIE.get()) {
			if (stack.getItem() == Items.CREEPER_SPAWN_EGG) {
				World world = event.getWorld();
				CreepieEntity creepie = SREntities.CREEPIE.get().create(world);
				if (creepie != null) {
					creepie.copyLocationAndAnglesFrom(target);
					if (stack.hasDisplayName()) creepie.setCustomName(stack.getDisplayName());
					if (!event.getPlayer().isCreative()) stack.shrink(1);
					creepie.attackPlayersOnly = true;
					world.addEntity(creepie);
					event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote()));
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		if (world.getBlockState(pos).getBlock() instanceof AbstractBannerBlock) {
			List<BurningBannerEntity> burningBanners = world.getEntitiesWithinAABB(BurningBannerEntity.class, new AxisAlignedBB(pos));
			for (BurningBannerEntity burningBanner : burningBanners) {
				if (burningBanner.getBannerPosition() != null && burningBanner.getBannerPosition().equals(pos)) {
					burningBanner.extinguishFire();
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		PlayerEntity player = event.getPlayer();
		BlockPos pos = event.getPos();
		World world = event.getWorld();

		if (stack.getItem() instanceof IPottableItem && world.getBlockState(pos).getBlock() == Blocks.FLOWER_POT) {
			BlockState pottedState = ((IPottableItem) stack.getItem()).getPottedState(player.getHorizontalFacing().getOpposite());
			if (pottedState == null)
				return;
			world.setBlockState(pos, pottedState);
			player.addStat(Stats.POT_FLOWER);
			if (!event.getPlayer().isCreative()) stack.shrink(1);
			event.setCancellationResult(ActionResultType.SUCCESS);
			event.setCanceled(true);
		} else if (isValidBurningBannerPos(world, pos)) {
			boolean isFlintAndSteel = stack.getItem() instanceof FlintAndSteelItem;
			if ((isFlintAndSteel || stack.getItem() instanceof FireChargeItem)) {
				SoundEvent sound = isFlintAndSteel ? SoundEvents.ITEM_FLINTANDSTEEL_USE : SoundEvents.ITEM_FIRECHARGE_USE;
				float pitch = isFlintAndSteel ? new Random().nextFloat() * 0.4F + 0.8F : (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F;
				world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0F, pitch);

				if (isFlintAndSteel) {
					stack.damageItem(1, player, (p_219998_1_) -> p_219998_1_.sendBreakAnimation(event.getHand()));
				} else if (!player.isCreative()) {
					stack.shrink(1);
				}

				world.addEntity(new BurningBannerEntity(world, pos, player));
				event.setCancellationResult(ActionResultType.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void livingUpdate(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity.getFireTimer() > 0 && entity.getActivePotionEffect(SREffects.FROSTBITE.get()) != null)
			entity.removePotionEffect(SREffects.FROSTBITE.get());
		IDataManager data = (IDataManager) entity;
		if (entity instanceof EvokerEntity) {
			int shieldTime = data.getValue(SREntities.EVOKER_SHIELD_TIME);
			if (shieldTime > 0)
				data.setValue(SREntities.EVOKER_SHIELD_TIME, shieldTime - 1);
			else if (shieldTime == 0) {
				data.setValue(SREntities.EVOKER_SHIELD_COOLDOWN, 1800);
				data.setValue(SREntities.EVOKER_SHIELD_TIME, -1);
			}
			int cooldown = data.getValue(SREntities.EVOKER_SHIELD_COOLDOWN);
			if (cooldown > 0)
				data.setValue(SREntities.EVOKER_SHIELD_COOLDOWN, cooldown - 1);
		}

	}

	@SubscribeEvent
	public static void onNoteBlockPlay(NoteBlockEvent.Play event) {
		BlockPos pos = event.getPos();
		BlockState state = event.getWorld().getBlockState(pos.offset(Direction.DOWN));
		SoundEvent sound = state.isIn(Blocks.TARGET) ? SRSounds.BLOCK_NOTE_BLOCK_HIT_MARKER.get() : state.isIn(SRBlocks.GLOOMY_TILES.get()) ? SRSounds.BLOCK_NOTE_BLOCK_HARPSICHORD.get() : state.isIn(SRBlocks.BLAST_PROOF_PLATES.get()) ? SRSounds.BLOCK_NOTE_BLOCK_ORCHESTRAL_HIT.get() : null;
		if (sound != null) {
			int note = event.getVanillaNoteId();
			float f = (float)Math.pow(2.0D, (double)(note - 12) / 12.0D);
			event.getWorld().playSound(null, pos, sound, SoundCategory.RECORDS, 3.0F, f);
			if (!event.getWorld().isRemote())
				NetworkUtil.spawnParticle("note",  pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, (double) note / 24.0D, 0.0D, 0.0D);
			event.setCanceled(true);
		}
	}

	public static boolean isValidBurningBannerPos(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() instanceof AbstractBannerBlock) {
			List<BurningBannerEntity> banners = world.getEntitiesWithinAABB(BurningBannerEntity.class, new AxisAlignedBB(pos));
			boolean noBurningBanners = true;
			for (BurningBannerEntity banner : banners) {
				if (banner.getBannerPosition() != null && banner.getBannerPosition().equals(pos))
					noBurningBanners = false;
			}
			return noBurningBanners;
		}
		return false;
	}

	public static ItemStack createRocket() {
		ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
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
