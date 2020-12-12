package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.abnormals_core.core.api.IAgeableEntity;
import com.minecraftabnormals.savageandravage.common.effect.GrowingEffect;
import com.minecraftabnormals.savageandravage.common.effect.ShrinkingEffect;
import com.minecraftabnormals.savageandravage.common.entity.*;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.common.entity.goals.AvoidGrieferOwnedCreepiesGoal;
import com.minecraftabnormals.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.minecraftabnormals.savageandravage.common.item.PottableItem;
import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.*;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SREvents {
	private static final Method setSize = ObfuscationReflectionHelper.findMethod(SlimeEntity.class, "func_70799_a", int.class, boolean.class);

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof PillagerEntity) {
			PillagerEntity pillager = (PillagerEntity) event.getEntity();
			ImprovedCrossbowGoal<PillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<>(pillager, 1.0D, 8.0F, 5.0D);
			pillager.goalSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof RangedCrossbowAttackGoal<?>).findFirst().ifPresent(crossbowGoal -> {
				pillager.goalSelector.removeGoal(crossbowGoal);
				pillager.goalSelector.addGoal(3, aiCrossBow);
			});
			if (event.getWorld().rand.nextInt(100) == 0 && !event.getWorld().isRemote()) {
				pillager.setItemStackToSlot(EquipmentSlotType.OFFHAND, createRocket());
				pillager.setActiveHand(Hand.OFF_HAND);
				pillager.setDropChance(EquipmentSlotType.OFFHAND, 2.0F);
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
			villager.goalSelector.addGoal(1, new AvoidGrieferOwnedCreepiesGoal<>(villager, CreepieEntity.class, 8.0F, 0.8D, 0.8D));
		}
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		if (event.getEntity().getType() == EntityType.CREEPER) {
			CreeperEntity creeper = (CreeperEntity) event.getEntity();
			if (event.getSource().isExplosion() && SRConfig.COMMON.creepersDropSporesAfterExplosionDeath.get()) {
				creeper.entityDropItem(new ItemStack(SRItems.CREEPER_SPORES.get(), 1 + creeper.world.rand.nextInt(3)));
			}
		} else if (event.getEntity() instanceof PillagerEntity) {
			PillagerEntity pillager = (PillagerEntity) event.getEntity();
			if (pillager.world.isRemote() && ((ServerWorld) pillager.getEntityWorld()).findRaid(pillager.getPosition()) != null) {
				pillager.entityDropItem(new ItemStack(Items.EMERALD, pillager.world.rand.nextInt(2)));
				if (pillager.world.rand.nextDouble() < 0.05D) {
					pillager.entityDropItem(new ItemStack(Items.EMERALD, 4 + pillager.world.rand.nextInt(1)));
				}
				if (pillager.world.rand.nextDouble() < 0.12D) {
					pillager.entityDropItem(new ItemStack(Items.EMERALD, 2 + pillager.world.rand.nextInt(1)));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
		if (event.getEntityLiving() instanceof GolemEntity && !(event.getEntityLiving() instanceof ShulkerEntity) && event.getTarget() instanceof IOwnableMob) {
			if (((IOwnableMob) event.getTarget()).getOwner() instanceof PlayerEntity && ((MobEntity) event.getTarget()).getAttackTarget() != event.getEntityLiving()) {
				((GolemEntity) event.getEntityLiving()).setAttackTarget(null);
			}
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
					spores.setCloudSize(creeper.isCharged() ? (int) (creeper.getHealth() / 4) : (int) (creeper.getHealth() / 5));
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
	public static void handleBlastProof(LivingDamageEvent event) {
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

		if (stack.getItem() instanceof PottableItem && world.getBlockState(pos).getBlock() == Blocks.FLOWER_POT) {
			BlockState pottedState = ((PottableItem) stack.getItem()).getPottedState(player.getHorizontalFacing().getOpposite());
			if (pottedState == null)
				return;
			world.setBlockState(pos, pottedState);
			player.addStat(Stats.POT_FLOWER);
			if (!event.getPlayer().isCreative()) stack.shrink(1);
			event.setCancellationResult(ActionResultType.SUCCESS);
			event.setCanceled(true);
		} else if (isValidBannerPos(world, pos)) {
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
	}

	public static boolean isValidBannerPos(World world, BlockPos pos) {
		if (!(world.getBlockState(pos).getBlock() instanceof AbstractBannerBlock)) return false;
		List<BurningBannerEntity> banners = world.getEntitiesWithinAABB(BurningBannerEntity.class, new AxisAlignedBB(pos));
		return banners.stream().noneMatch(b -> Objects.equals(b.getBannerPosition(), pos));
	}

	//TODO refactor, probably a lot of redundancy here
	@SubscribeEvent
	public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
		if (event.getPotionEffect() != null) {
			LivingEntity affected = event.getEntityLiving();
			boolean shouldSetChild = event.getPotionEffect().getPotion() instanceof ShrinkingEffect;
			if (event.getPotionEffect().getPotion() instanceof GrowingEffect || shouldSetChild) {
				boolean canChange = false;
				if(affected instanceof IAgeableEntity && ((IAgeableEntity) affected).canAge(!shouldSetChild)) {
					((IAgeableEntity) affected).attemptAging(!shouldSetChild);
					canChange = true;
				}
				else if (affected instanceof SlimeEntity) {
					SlimeEntity slime = (SlimeEntity) affected;
					int size = slime.getSlimeSize();
					if (shouldSetChild ? size > 1 : size < 3) {
						canChange = true;
						try {
							setSize.invoke(slime, (size + (shouldSetChild ? (size < 4 ? -1 : -2) : (size < 2 ? 1 : 2))), false);
						} catch (IllegalAccessException | InvocationTargetException e) {
							throw new RuntimeException("Invoking setSize failed. Something has gone horribly wrong with Savage & Ravage!");
						}
					}
				}
				else if (shouldSetChild != affected.isChild()) {
					canChange = true;
					if (affected instanceof AgeableEntity && !(affected instanceof ParrotEntity))
						((AgeableEntity) affected).setGrowingAge(shouldSetChild ? -24000 : 0);
					else if (shouldSetChild && affected instanceof CreeperEntity)
						convertCreeper((CreeperEntity) affected);
					else if (affected instanceof ZombieEntity || affected instanceof PiglinEntity || affected instanceof ZoglinEntity)
						((MobEntity) affected).setChild(shouldSetChild);
					else
						canChange = false;
				}
				if (!canChange) {
					EffectInstance effectInstance;
					if (!shouldSetChild)
						affected.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 2400, 0));
					if (affected.isEntityUndead())
						shouldSetChild = !shouldSetChild;
					effectInstance = new EffectInstance(shouldSetChild ? Effects.INSTANT_DAMAGE : Effects.INSTANT_HEALTH, 1, 1);
					effectInstance.getPotion().affectEntity(null, null, affected, effectInstance.getAmplifier(), 1.0D);
				}
				if (!affected.world.isRemote()) {
					((ServerWorld) affected.world).spawnParticle(canChange ? (shouldSetChild ? ParticleTypes.TOTEM_OF_UNDYING : ParticleTypes.HAPPY_VILLAGER) : ParticleTypes.LARGE_SMOKE, affected.getPosXRandom(0.3D), affected.getPosYRandom() - 0.1D, affected.getPosZRandom(0.3D), canChange ? 40 : 20, 0.3D, 0.6D, 0.3D, canChange ? 0.2D : 0.01D);
					affected.playSound(canChange ? SRSounds.ENTITY_GENERIC_GROWTH_SUCCESS.get() : SRSounds.ENTITY_GENERIC_GROWTH_FAILURE.get(), 1.0F, 1.0F);
				}
			}
		}
	}

	public static void convertCreeper(CreeperEntity creeper) {
		CreepieEntity creepie = SREntities.CREEPIE.get().create(creeper.world);
		if (creepie == null)
			return;

		creepie.copyLocationAndAnglesFrom(creeper.getEntity());
		creeper.remove();
		creepie.setNoAI(creeper.isAIDisabled());
		if (creeper.hasCustomName()) {
			creepie.setCustomName(creeper.getCustomName());
			creepie.setCustomNameVisible(creeper.isCustomNameVisible());
		}

		if (creeper.isNoDespawnRequired()) {
			creepie.enablePersistence();
		}
		if (creeper.getLeashed() && creeper.getLeashHolder() != null) {
			creepie.setLeashHolder(creeper.getLeashHolder(), true);
			creeper.clearLeashed(true, false);
		}

		if (creeper.getRidingEntity() != null) {
			creepie.startRiding(creeper.getRidingEntity());
		}
		creepie.setInvulnerable(creeper.isInvulnerable());
		creeper.setHealth(creeper.getMaxHealth());
		creeper.world.addEntity(creepie);
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
