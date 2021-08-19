package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.abnormals_core.common.network.particle.MessageS2CSpawnParticle;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedDataManager;
import com.minecraftabnormals.abnormals_core.core.AbnormalsCore;
import com.minecraftabnormals.savageandravage.common.entity.*;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.common.entity.goals.CelebrateTargetBlockHitGoal;
import com.minecraftabnormals.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.minecraftabnormals.savageandravage.common.item.IPottableItem;
import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.*;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SREvents {
	public static String POOF_KEY = "minecraft:poof";
	public static String NOTE_KEY = "minecraft:note";


	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof MobEntity) {
			MobEntity mob = (MobEntity) entity;
			if (mob instanceof AbstractVillagerEntity) {
				AbstractVillagerEntity villager = (AbstractVillagerEntity) mob;
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, SkeletonVillagerEntity.class, 8.0F, 0.6D, 0.6D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, GrieferEntity.class, 8.0F, 0.8D, 0.8D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, IceologerEntity.class, 8.0F, 0.8D, 0.8D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, ExecutionerEntity.class, 8.0F, 0.8D, 0.8D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, TricksterEntity.class, 8.0F, 0.8D, 0.8D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, CreepieEntity.class, 8.0F, 0.8D, 0.8D, e -> {
					CreepieEntity creepie = ((CreepieEntity) e);
					return creepie.getOwnerId() == null || creepie.getOwner() instanceof GrieferEntity;
				}));
			} else if (mob instanceof PillagerEntity) {
				PillagerEntity pillager = (PillagerEntity) mob;
				mob.goalSelector.availableGoals.stream().map(it -> it.goal).filter(it -> it instanceof RangedCrossbowAttackGoal<?>).findFirst().ifPresent(goal -> {
					mob.goalSelector.removeGoal(goal);
					mob.goalSelector.addGoal(3, new ImprovedCrossbowGoal<>(pillager, 1.0D, 8.0F, 5.0D));
				});
				mob.goalSelector.addGoal(5, new CelebrateTargetBlockHitGoal(pillager));
			}
			else if (mob instanceof CatEntity || mob instanceof OcelotEntity)
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, CreepieEntity.class, false));
			else if (mob instanceof EvokerEntity && SRConfig.COMMON.evokersUseTotems.get())
				mob.goalSelector.addGoal(1, new AvoidEntityGoal<IronGolemEntity>((EvokerEntity) mob, IronGolemEntity.class, 8.0F, 0.6D, 1.0D) {
					@Override
					public boolean canUse() {
						return SRConfig.COMMON.evokersUseTotems.get() && TrackedDataManager.INSTANCE.getValue(this.mob, SRDataProcessors.TOTEM_SHIELD_TIME) > 0 && super.canUse();
					}
				});
			else if (mob instanceof VexEntity && SRConfig.COMMON.reducedVexHealth.get()) {
				ModifiableAttributeInstance maxHealth = mob.getAttribute(Attributes.MAX_HEALTH);
				if (maxHealth != null)
					maxHealth.setBaseValue(2.0);
				if (mob.getHealth() > mob.getMaxHealth())
					mob.setHealth(mob.getMaxHealth());
			}
			if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
				if (mob instanceof IronGolemEntity)
					mob.targetSelector.availableGoals.stream().map(it -> it.goal).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(goal -> {
						mob.targetSelector.removeGoal(goal);
						mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, MobEntity.class, 5, false, false, e -> e instanceof IMob));
					});
				else if (mob.getType() == EntityType.CREEPER)
					mob.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(mob, IronGolemEntity.class, true));
			}
		}
	}


	@SubscribeEvent
	public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity.getEffect(SREffects.WEIGHT.get()) != null)
			entity.setDeltaMovement(entity.getDeltaMovement().x(), 0.0D, entity.getDeltaMovement().z());
	}

	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
		LivingEntity entity = event.getEntityLiving();
		LivingEntity target = event.getTarget();
		if (target != null) {
			if (entity instanceof GolemEntity && !(entity instanceof ShulkerEntity) && target instanceof IOwnableMob)
				if (((IOwnableMob) target).getOwner() instanceof PlayerEntity && ((MobEntity) target).getTarget() != entity)
					((GolemEntity) entity).setTarget(null);
			if (entity instanceof EvokerEntity && SRConfig.COMMON.evokersUseTotems.get() && TrackedDataManager.INSTANCE.getValue(entity, SRDataProcessors.TOTEM_SHIELD_TIME) > 0)
				((EvokerEntity) entity).setTarget(null);
		}
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event) {
		World world = event.getWorld();
		Explosion explosion = event.getExplosion();
		if (explosion.getSourceMob() != null) {
			if (explosion.getSourceMob().getType() == EntityType.CREEPER) {
				if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get())
					event.getAffectedBlocks().clear();
				if (SRConfig.COMMON.creeperExplosionsSpawnCreepies.get()) {
					CreeperEntity creeper = (CreeperEntity) explosion.getSourceMob();
					SporeCloudEntity spores = SREntities.SPORE_CLOUD.get().create(world);
					if (spores == null)
						return;
					spores.setSpawnCloudInstantly(true);
					spores.creepiesAttackPlayersOnly(true);
					if (creeper.isPowered()) {
						spores.setCharged(true);
					}
					spores.setCloudSize(creeper.isPowered() ? (int) (creeper.getHealth() / 2) : (int) (creeper.getHealth() / 5));
					spores.copyPosition(creeper);
					creeper.level.addFreshEntity(spores);
				}
			}
		}

		if (explosion.getExploder() != null && explosion.getExploder().getType() == SREntities.SPORE_BOMB.get()) {
			for (BlockPos pos : event.getAffectedBlocks()) {
				if (world.getBlockState(pos).getBlock() == SRBlocks.SPORE_BOMB.get()) {
					world.removeBlock(pos, false);
					SporeBombEntity sporebomb = new SporeBombEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosion.getSourceMob());
					sporebomb.setFuse((short) (world.getRandom().nextInt(sporebomb.getLife() / 4) + sporebomb.getLife() / 8));
					world.addFreshEntity(sporebomb);
				}
			}
		}

		List<Entity> safeItems = new ArrayList<>();
		for (Entity entity : event.getAffectedEntities()) {
			if (entity instanceof ItemEntity) {
				ItemStack itemstack = ((ItemEntity) entity).getItem();
				if (itemstack.getItem().is(SRTags.BLAST_PROOF_ITEMS)) {
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
				ItemStack stack = entity.getItemBySlot(slot);
				Collection<AttributeModifier> modifiers = stack.getAttributeModifiers(slot).get(SRAttributes.EXPLOSIVE_DAMAGE_REDUCTION.get());
				if (modifiers.isEmpty())
					continue;

				decrease += modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				stack.hurtAndBreak(22 - EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack) * 8, entity, onBroken -> onBroken.broadcastBreakEvent(slot));
			}

			if (decrease == 0)
				return;

			event.setAmount(event.getAmount() - (float) (event.getAmount() * decrease));
		}

		IDataManager data = (IDataManager) entity;
		if (entity instanceof EvokerEntity && SRConfig.COMMON.evokersUseTotems.get()) {
			if (entity.getHealth() - event.getAmount() <= 0 && event.getSource().getDirectEntity() instanceof ProjectileEntity) {
				if (data.getValue(SRDataProcessors.TOTEM_SHIELD_TIME) <= 0 && data.getValue(SRDataProcessors.TOTEM_SHIELD_COOLDOWN) <= 0) {
					event.setCanceled(true);
					entity.setHealth(2.0F);
					data.setValue(SRDataProcessors.TOTEM_SHIELD_TIME, 600);
					if (!entity.level.isClientSide())
						entity.level.broadcastEntityEvent(entity, (byte) 35);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof EvokerEntity && SRConfig.COMMON.evokersUseTotems.get()) {
			if (TrackedDataManager.INSTANCE.getValue(entity, SRDataProcessors.TOTEM_SHIELD_TIME) > 0) {
				if (event.getSource().getDirectEntity() instanceof ProjectileEntity)
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
					creepie.copyPosition(target);
					if (stack.hasCustomHoverName()) creepie.setCustomName(stack.getHoverName());
					if (!event.getPlayer().isCreative()) stack.shrink(1);
					creepie.attackPlayersOnly = true;
					world.addFreshEntity(creepie);
					event.setCancellationResult(ActionResultType.sidedSuccess(world.isClientSide()));
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
			List<BurningBannerEntity> burningBanners = world.getEntitiesOfClass(BurningBannerEntity.class, new AxisAlignedBB(pos));
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
			BlockState pottedState = ((IPottableItem) stack.getItem()).getPottedState(player.getDirection().getOpposite());
			if (pottedState == null)
				return;
			world.setBlockAndUpdate(pos, pottedState);
			player.awardStat(Stats.POT_FLOWER);
			if (!event.getPlayer().isCreative()) stack.shrink(1);
			event.setCancellationResult(ActionResultType.SUCCESS);
			event.setCanceled(true);
		} else if (isValidBurningBannerPos(world, pos)) {
			boolean isFlintAndSteel = stack.getItem() instanceof FlintAndSteelItem;
			if ((isFlintAndSteel || stack.getItem() instanceof FireChargeItem)) {
				SoundEvent sound = isFlintAndSteel ? SoundEvents.FLINTANDSTEEL_USE : SoundEvents.FIRECHARGE_USE;
				float pitch = isFlintAndSteel ? new Random().nextFloat() * 0.4F + 0.8F : (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F;
				world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0F, pitch);

				if (isFlintAndSteel) {
					stack.hurtAndBreak(1, player, (p_219998_1_) -> p_219998_1_.broadcastBreakEvent(event.getHand()));
				} else if (!player.isCreative()) {
					stack.shrink(1);
				}

				world.addFreshEntity(new BurningBannerEntity(world, pos, player));
				event.setCancellationResult(ActionResultType.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void livingUpdate(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		World world = entity.level;
		IDataManager data = (IDataManager) entity;
		if (entity.getRemainingFireTicks() > 0 && entity.getEffect(SREffects.FROSTBITE.get()) != null)
			entity.removeEffect(SREffects.FROSTBITE.get());
		if (!world.isClientSide()) {
			if (entity instanceof AbstractRaiderEntity) {
				int celebrationTime = data.getValue(SRDataProcessors.CELEBRATION_TIME);
				if (celebrationTime > 0)
					data.setValue(SRDataProcessors.CELEBRATION_TIME, celebrationTime-1);
			}
			boolean canBeInvisible = maskCanMakeInvisible(entity);
			boolean invisibleDueToMask = data.getValue(SRDataProcessors.INVISIBLE_DUE_TO_MASK);
			boolean maskStateChanged = canBeInvisible != invisibleDueToMask;
			if (maskStateChanged) {
				data.setValue(SRDataProcessors.INVISIBLE_DUE_TO_MASK, canBeInvisible);
				spawnMaskParticles(world, entity.getBoundingBox(), 3);
			}
			if (maskStateChanged || (canBeInvisible && !entity.isInvisible()))
				entity.setInvisible(canBeInvisible || entity.hasEffect(Effects.INVISIBILITY));
		}
		if (entity instanceof EvokerEntity) {
			int shieldTime = data.getValue(SRDataProcessors.TOTEM_SHIELD_TIME);
			if (shieldTime > 0)
				data.setValue(SRDataProcessors.TOTEM_SHIELD_TIME, shieldTime - 1);
			else if (shieldTime == 0) {
				data.setValue(SRDataProcessors.TOTEM_SHIELD_COOLDOWN, 1800);
				data.setValue(SRDataProcessors.TOTEM_SHIELD_TIME, -1);
			}
			int cooldown = data.getValue(SRDataProcessors.TOTEM_SHIELD_COOLDOWN);
			if (cooldown > 0)
				data.setValue(SRDataProcessors.TOTEM_SHIELD_COOLDOWN, cooldown - 1);
		}
	}

	@SubscribeEvent
	public static void visibilityMultiplierEvent(LivingEvent.LivingVisibilityEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (TrackedDataManager.INSTANCE.getValue(entity, SRDataProcessors.INVISIBLE_DUE_TO_MASK)) {
			double armorCover = entity.getArmorCoverPercentage();
			if (armorCover < 0.1F) {
				armorCover = 0.1F;
			}
			event.modifyVisibility(1 / armorCover); //potentially slightly inaccurate
			event.modifyVisibility(0.1);
		}
	}

	@SubscribeEvent
	public static void onProjectileImpact(ProjectileImpactEvent event) {
		RayTraceResult result = event.getRayTraceResult();
		if (result instanceof BlockRayTraceResult) {
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
			Entity entity = event.getEntity();
			if (entity.level.getBlockState(blockResult.getBlockPos()).is(Blocks.TARGET)) {
				if (!entity.level.isClientSide()) {
					IDataManager data = (IDataManager) entity;
					UUID id = data.getValue(SRDataProcessors.CROSSBOW_OWNER).orElse(null);
					if (id != null) {
						Entity crossbowOwner = ((ServerWorld) entity.level).getEntity(id);
						if (crossbowOwner instanceof AbstractRaiderEntity)
							TrackedDataManager.INSTANCE.setValue(crossbowOwner, SRDataProcessors.TARGET_HIT, true);
						data.setValue(SRDataProcessors.CROSSBOW_OWNER, Optional.empty());
					}
				}
			}
 		}
	}

	@SubscribeEvent
	public static void onNoteBlockPlay(NoteBlockEvent.Play event) {
		BlockPos pos = event.getPos();
		BlockState state = event.getWorld().getBlockState(pos.relative(Direction.DOWN));
		SoundEvent sound = state.is(Blocks.TARGET) ? SRSounds.BLOCK_NOTE_BLOCK_HIT_MARKER.get() : state.is(SRTags.GLOOMY_TILES) ? SRSounds.BLOCK_NOTE_BLOCK_HARPSICHORD.get() : state.is(SRTags.BLAST_PROOF) ? SRSounds.BLOCK_NOTE_BLOCK_ORCHESTRAL_HIT.get() : null;
		if (sound != null) {
			World world = (World) event.getWorld();
			double note = event.getVanillaNoteId();
			event.getWorld().playSound(null, pos, sound, SoundCategory.RECORDS, 3.0F, (float) Math.pow(2.0D, (note - 12) / 12.0D));
			if (!event.getWorld().isClientSide())
				AbnormalsCore.CHANNEL.send(PacketDistributor.DIMENSION.with(world::dimension), new MessageS2CSpawnParticle(NOTE_KEY, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, note / 24.0D, 0.0D, 0.0D));
			event.setCanceled(true);
		}
	}

	private static boolean maskCanMakeInvisible(LivingEntity entity) {
		if (entity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == SRItems.MASK_OF_DISHONESTY.get())
			return entity.isCrouching();
		return false;
	}

	public static void spawnMaskParticles(World world, AxisAlignedBB box, int loops) {
		Random random = world.getRandom();
		for (int i = 0; i < loops; i++) {
			double x = box.min(Direction.Axis.X) + (random.nextFloat() * box.getXsize());
			double y = box.min(Direction.Axis.Y) + (random.nextFloat() * box.getYsize());
			double z = box.min(Direction.Axis.Z) + (random.nextFloat() * box.getZsize());
			AbnormalsCore.CHANNEL.send(PacketDistributor.DIMENSION.with(world::dimension), new MessageS2CSpawnParticle(POOF_KEY, x, y, z, 0.0D, 0.0D, 0.0D));
		}
	}

	public static boolean isValidBurningBannerPos(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() instanceof AbstractBannerBlock) {
			List<BurningBannerEntity> banners = world.getEntitiesOfClass(BurningBannerEntity.class, new AxisAlignedBB(pos));
			boolean noBurningBanners = true;
			for (BurningBannerEntity banner : banners) {
				if (banner.getBannerPosition() != null && banner.getBannerPosition().equals(pos))
					noBurningBanners = false;
			}
			return noBurningBanners;
		}
		return false;
	}

	public static ItemStack createRocket(Random random) {
		ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET, random.nextInt(16) + 1);
		CompoundNBT fireworks = rocket.getOrCreateTagElement("Fireworks");
		ListNBT explosions = new ListNBT();
		CompoundNBT explosion = new CompoundNBT();
		fireworks.put("Explosions", explosions);
		explosions.add(explosion);
		explosion.putIntArray("Colors", randomColors(random));
		if (random.nextInt(2) == 0)
			explosion.putIntArray("FadeColors", randomColors(random));
		if (random.nextInt(2) == 0)
			explosion.putBoolean("Flicker", true);
		if (random.nextInt(2) == 0)
			explosion.putBoolean("Trail", true);
		FireworkRocketItem.Shape[] values = FireworkRocketItem.Shape.values();
		explosion.putInt("Type", values[random.nextInt(values.length)].getId());
		fireworks.putInt("Flight", random.nextInt(3) + 1);
		return rocket;
	}

	public static int[] randomColors(Random random) {
		int[] colors = new int[random.nextInt(3) + 1];
		DyeColor[] values = DyeColor.values();
		for (int i = 0; i < colors.length; i++) {
			colors[i] = values[random.nextInt(values.length)].getFireworkColor();
		}
		return colors;
	}
}
