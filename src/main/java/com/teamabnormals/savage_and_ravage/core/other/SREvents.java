package com.teamabnormals.savage_and_ravage.core.other;

import com.teamabnormals.savage_and_ravage.common.entity.BurningBannerEntity;
import com.teamabnormals.savage_and_ravage.common.entity.CreepieEntity;
import com.teamabnormals.savage_and_ravage.common.entity.ExecutionerEntity;
import com.teamabnormals.savage_and_ravage.common.entity.GrieferEntity;
import com.teamabnormals.savage_and_ravage.common.entity.IOwnableMob;
import com.teamabnormals.savage_and_ravage.common.entity.IceologerEntity;
import com.teamabnormals.savage_and_ravage.common.entity.SkeletonVillagerEntity;
import com.teamabnormals.savage_and_ravage.common.entity.SporeCloudEntity;
import com.teamabnormals.savage_and_ravage.common.entity.TricksterEntity;
import com.teamabnormals.savage_and_ravage.common.entity.block.SporeBombEntity;
import com.teamabnormals.savage_and_ravage.common.entity.goals.CelebrateTargetBlockHitGoal;
import com.teamabnormals.savage_and_ravage.common.entity.goals.ImprovedCrossbowGoal;
import com.teamabnormals.savage_and_ravage.common.item.IPottableItem;
import com.teamabnormals.savage_and_ravage.core.SRConfig;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SREntityTypeTags;
import com.teamabnormals.savage_and_ravage.core.other.tags.SRItemTags;
import com.teamabnormals.savage_and_ravage.core.registry.SRAttributes;
import com.teamabnormals.savage_and_ravage.core.registry.SRBlocks;
import com.teamabnormals.savage_and_ravage.core.registry.SREffects;
import com.teamabnormals.savage_and_ravage.core.registry.SREntities;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import com.teamabnormals.savage_and_ravage.core.registry.SRParticles;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SREvents {
	public static String POOF_KEY = "minecraft:poof";
	public static String NO_KNOCKBACK_KEY = SavageAndRavage.MOD_ID + "no_knockback";

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Mob mob) {
			if (mob instanceof AbstractVillager villager) {
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, SkeletonVillagerEntity.class, 8.0F, 0.6D, 0.6D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, GrieferEntity.class, 8.0F, 0.8D, 0.8D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, IceologerEntity.class, 8.0F, 0.8D, 0.8D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, ExecutionerEntity.class, 8.0F, 0.8D, 0.8D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, TricksterEntity.class, 8.0F, 0.8D, 0.8D));
				villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, CreepieEntity.class, 8.0F, 0.8D, 0.8D, e -> {
					CreepieEntity creepie = ((CreepieEntity) e);
					return creepie.getOwnerId() == null || creepie.getOwner() instanceof GrieferEntity;
				}));
			} else if (mob instanceof Pillager pillager) {
				mob.goalSelector.availableGoals.stream().map(it -> it.goal).filter(it -> it instanceof RangedCrossbowAttackGoal<?>).findFirst().ifPresent(goal -> {
					mob.goalSelector.removeGoal(goal);
					mob.goalSelector.addGoal(3, new ImprovedCrossbowGoal<>(pillager, 1.0D, 8.0F, 5.0D));
				});
				mob.goalSelector.addGoal(5, new CelebrateTargetBlockHitGoal(pillager));
			} else if (mob instanceof Cat || mob instanceof Ocelot)
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, CreepieEntity.class, false));
			else if (mob instanceof Evoker && SRConfig.COMMON.evokersUseTotems.get())
				mob.goalSelector.addGoal(1, new AvoidEntityGoal<>((Evoker) mob, IronGolem.class, 8.0F, 0.6D, 1.0D) {
					@Override
					public boolean canUse() {
						return SRConfig.COMMON.evokersUseTotems.get() && TrackedDataManager.INSTANCE.getValue(this.mob, SRDataProcessors.TOTEM_SHIELD_TIME) > 0 && super.canUse();
					}
				});
			else if (mob instanceof Vex && SRConfig.COMMON.reducedVexHealth.get()) {
				AttributeInstance maxHealth = mob.getAttribute(Attributes.MAX_HEALTH);
				if (maxHealth != null)
					maxHealth.setBaseValue(2.0);
				if (mob.getHealth() > mob.getMaxHealth())
					mob.setHealth(mob.getMaxHealth());
			}
			if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
				if (mob instanceof IronGolem)
					mob.targetSelector.availableGoals.stream().map(it -> it.goal).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(goal -> {
						mob.targetSelector.removeGoal(goal);
						mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Mob.class, 5, false, false, e -> e instanceof Enemy));
					});
				else if (mob.getType() == EntityType.CREEPER)
					mob.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(mob, IronGolem.class, true));
			}
		}
	}

	private static final String POISON_TAG = SavageAndRavage.MOD_ID + "poison_potato_applied";

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		Entity target = event.getTarget();
		ItemStack stack = event.getItemStack();
		if (target instanceof CreepieEntity && stack.getItem() == Items.POISONOUS_POTATO && SRConfig.COMMON.poisonPotatoCompat.get() && ModList.get().isLoaded("quark")) {
			Player player = event.getPlayer();
			CompoundTag persistentData = target.getPersistentData();
			if (!persistentData.getBoolean(POISON_TAG)) {
				if (target.level.random.nextDouble() < SRConfig.COMMON.poisonPotatoChance.get()) {
					target.playSound(SoundEvents.GENERIC_EAT, 0.5f, 0.25f);
					persistentData.putBoolean(POISON_TAG, true);
					if (SRConfig.COMMON.poisonPotatoEffect.get()) {
						((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.POISON, 200));
					}
				} else {
					target.playSound(SoundEvents.GENERIC_EAT, 0.5f, 0.5f + target.level.random.nextFloat() / 2);
				}
				if (!player.isCreative()) stack.shrink(1);
				event.setCancellationResult(InteractionResult.sidedSuccess(event.getWorld().isClientSide()));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onUpdateEntity(LivingEvent.LivingUpdateEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof CreepieEntity creepie && SRConfig.COMMON.poisonPotatoCompat.get() && ModList.get().isLoaded("quark")) {
			if (entity.getPersistentData().getBoolean(POISON_TAG)) creepie.setGrowingAge(-24000);
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
			if (entity instanceof AbstractGolem && !(entity instanceof Shulker) && target instanceof IOwnableMob)
				if (((IOwnableMob) target).getOwner() instanceof Player && ((Mob) target).getTarget() != entity)
					((AbstractGolem) entity).setTarget(null);
			if (entity instanceof Evoker && SRConfig.COMMON.evokersUseTotems.get() && TrackedDataManager.INSTANCE.getValue(entity, SRDataProcessors.TOTEM_SHIELD_TIME) > 0)
				((Evoker) entity).setTarget(null);
		}
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event) {
		Level world = event.getWorld();
		Explosion explosion = event.getExplosion();
		LivingEntity sourceEntity = explosion.getSourceMob();
		boolean isCreeper = sourceEntity != null && sourceEntity.getType() == EntityType.CREEPER;
		boolean isCreepie = sourceEntity != null && sourceEntity.getType() == SREntities.CREEPIE.get();
		if (isCreeper) {
			if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get())
				event.getAffectedBlocks().clear();
			if (SRConfig.COMMON.creeperExplosionsSpawnCreepies.get()) {
				Creeper creeper = (Creeper) explosion.getSourceMob();
				SporeCloudEntity spores = SREntities.SPORE_CLOUD.get().create(world);
				if (spores == null)
					return;
				spores.setSpawnCloudInstantly(true);
				spores.creepiesAttackPlayersOnly(true);
				if (creeper.isPowered()) {
					spores.setCharged(true);
				}
				spores.setCloudSize((int) (creeper.getHealth() / creeper.getMaxHealth()) * (creeper.isPowered() ? 10 : 4));
				spores.copyPosition(creeper);
				creeper.level.addFreshEntity(spores);
			}
		}

		if (explosion.getExploder() != null && explosion.getExploder().getType() == SREntities.SPORE_BOMB.get()) {
			for (BlockPos pos : event.getAffectedBlocks()) {
				if (world.getBlockState(pos).getBlock() == SRBlocks.SPORE_BOMB.get()) {
					world.removeBlock(pos, false);
					SporeBombEntity sporebomb = new SporeBombEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, explosion.getSourceMob());
					sporebomb.setFuse((short) (world.getRandom().nextInt(sporebomb.getFuse() / 4) + sporebomb.getFuse() / 8));
					world.addFreshEntity(sporebomb);
				}
			}
		}

		List<Entity> safeItems = new ArrayList<>();
		for (Entity entity : event.getAffectedEntities())
			if (entitySafeFromExplosion(entity, isCreeper ? !SRConfig.COMMON.creeperExplosionsDestroyBlocks.get() : isCreepie && !SRConfig.COMMON.creepieExplosionsDestroyBlocks.get()))
				safeItems.add(entity);
		event.getAffectedEntities().removeAll(safeItems);
	}

	public static boolean entitySafeFromExplosion(Entity entity, boolean creeperTypeNoGriefing) {
		if (creeperTypeNoGriefing && entity.getType().is(SREntityTypeTags.CREEPER_IMMUNE))
			return true;
		else if (entity instanceof ItemEntity) {
			ItemStack stack = ((ItemEntity) entity).getItem();
			return creeperTypeNoGriefing || stack.is(SRItemTags.EXPLOSION_IMMUNE);
		}
		return false;
	}

	@SubscribeEvent
	public static void onAttackEntity(AttackEntityEvent event) {
		Player player = event.getPlayer();
		Entity target = event.getTarget();
		Level world = player.level;
		if (target instanceof LivingEntity) {
			ItemStack mainHandStack = player.getMainHandItem();
			if (mainHandStack.getItem() == SRItems.CLEAVER_OF_BEHEADING.get()) {
				float attackDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
				float knockback = (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
				knockback += EnchantmentHelper.getKnockbackBonus(player);
				float enchantDamageBonus = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), ((LivingEntity) target).getMobType());
				float attackStrength = player.getAttackStrengthScale(0.5F);
				attackDamage = attackDamage * (0.2F + attackStrength * attackStrength * 0.8F);
				enchantDamageBonus = enchantDamageBonus * attackStrength;
				if ((attackDamage > 0.0F || enchantDamageBonus > 0.0F) && attackStrength > 0.9F) {
					if (!player.isSprinting() && player.isOnGround() && (player.walkDist - player.walkDistO) < (double) player.getSpeed()) {
						boolean shouldCrit = player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger();
						if (ForgeHooks.getCriticalHit(player, target, shouldCrit, shouldCrit ? 1.5F : 1.0F) == null) {
							target.getPersistentData().putBoolean(NO_KNOCKBACK_KEY, true);
							AABB targetBox = target.getBoundingBox().inflate(1.5D, 0.25D, 1.5D);
							AABB shockwaveBox = new AABB(targetBox.minX, targetBox.minY, targetBox.minZ, targetBox.maxX, targetBox.minY + 3.25D, targetBox.maxZ);
							for (LivingEntity pushed : world.getEntitiesOfClass(LivingEntity.class, shockwaveBox)) {
								if (pushed != player && pushed != target && !player.isAlliedTo(pushed) && (!(pushed instanceof ArmorStand) || !((ArmorStand) pushed).isMarker())) {
									pushed.knockback(0.4F + (knockback * 0.5F), Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
									if (pushed.isAffectedByPotions() && !pushed.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))
										pushed.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
								}
							}
							BlockPos.MutableBlockPos checkingPos = new BlockPos.MutableBlockPos();
							Random random = world.getRandom();
							if (!world.isClientSide) {
								for (int i = 0; i < 100; i++) {
									double x = shockwaveBox.minX + (random.nextDouble() * (shockwaveBox.maxX - shockwaveBox.minX));
									double z = shockwaveBox.minZ + (random.nextDouble() * (shockwaveBox.maxZ - shockwaveBox.minZ));
									int minY = Mth.floor(shockwaveBox.minY);
									for (int y = minY; y < Mth.floor(shockwaveBox.maxY); y++) {
										checkingPos.set(Mth.floor(x), Mth.floor(y), Mth.floor(z));
										if (!isEmptySpace(world, checkingPos)) {
											checkingPos.move(Direction.UP);
											if (isEmptySpace(world, checkingPos)) {
												checkingPos.move(Direction.DOWN);
												BlockState state = world.getBlockState(checkingPos);
												((ServerLevel) world).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(checkingPos), x, y + state.getShape(world, checkingPos).max(Direction.Axis.Y), z, 0, 0.0D, 0.0D, 0.0D, 0.15D);
												break;
											}
										}
									}
								}
								double xAngle = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
								double zAngle = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
								((ServerLevel) world).sendParticles(SRParticles.CLEAVER_SWEEP.get(), player.getX() + xAngle, player.getY(0.5D), player.getZ() + zAngle, 0, xAngle, 0.0D, zAngle, 0.0D);
							}
							world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}

	public static boolean isEmptySpace(BlockGetter world, BlockPos pos) {
		return world.getBlockState(pos).isAir() || !world.getFluidState(pos).isEmpty();
	}

	@SubscribeEvent
	public static void onLivingKnockback(LivingKnockBackEvent event) {
		if (event.getEntity().getPersistentData().getBoolean(NO_KNOCKBACK_KEY))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Evoker && SRConfig.COMMON.evokersUseTotems.get()) {
			if (TrackedDataManager.INSTANCE.getValue(entity, SRDataProcessors.TOTEM_SHIELD_TIME) > 0) {
				if (event.getSource().getDirectEntity() instanceof Projectile)
					event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event) {
		LivingEntity target = event.getEntityLiving();
		if (event.getSource().isExplosion()) {
			double decrease = 0;
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = target.getItemBySlot(slot);
				Collection<AttributeModifier> modifiers = stack.getAttributeModifiers(slot).get(SRAttributes.EXPLOSIVE_DAMAGE_REDUCTION.get());
				if (modifiers.isEmpty())
					continue;

				decrease += modifiers.stream().mapToDouble(AttributeModifier::getAmount).sum();
				stack.hurtAndBreak(22 - EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack) * 8, target, onBroken -> onBroken.broadcastBreakEvent(slot));
			}

			if (decrease != 0)
				event.setAmount(event.getAmount() - (float) (event.getAmount() * decrease));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingDamageDelayed(LivingDamageEvent event) {
		LivingEntity target = event.getEntityLiving();
		Entity attacker = event.getSource().getEntity();
		if (attacker instanceof LivingEntity && ((LivingEntity) attacker).getMainHandItem().getItem() == SRItems.CLEAVER_OF_BEHEADING.get()) {
			if (target instanceof Player) {
				Player targetPlayer = (Player) event.getEntity();
				if (targetPlayer != null && targetPlayer.getHealth() - event.getAmount() <= 0) {
					ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
					stack.addTagElement("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), targetPlayer.getGameProfile()));
					target.spawnAtLocation(stack);
				}
			}
		}
		if (target instanceof Evoker && SRConfig.COMMON.evokersUseTotems.get()) {
			IDataManager data = (IDataManager) target;
			if (target.getHealth() - event.getAmount() <= 0 && event.getSource().getDirectEntity() instanceof Projectile) {
				if (data.getValue(SRDataProcessors.TOTEM_SHIELD_TIME) <= 0 && data.getValue(SRDataProcessors.TOTEM_SHIELD_COOLDOWN) <= 0) {
					event.setCanceled(true);
					target.setHealth(2.0F);
					data.setValue(SRDataProcessors.TOTEM_SHIELD_TIME, 600);
					if (!target.level.isClientSide())
						target.level.broadcastEntityEvent(target, (byte) 35);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onProjectileImpact(ProjectileImpactEvent event) {
		HitResult result = event.getRayTraceResult();
		if (result instanceof BlockHitResult blockResult) {
			Entity entity = event.getEntity();
			if (entity.level.getBlockState(blockResult.getBlockPos()).is(Blocks.TARGET)) {
				if (!entity.level.isClientSide()) {
					IDataManager data = (IDataManager) entity;
					UUID id = data.getValue(SRDataProcessors.CROSSBOW_OWNER).orElse(null);
					if (id != null) {
						Entity crossbowOwner = ((ServerLevel) entity.level).getEntity(id);
						if (crossbowOwner instanceof Raider)
							TrackedDataManager.INSTANCE.setValue(crossbowOwner, SRDataProcessors.TARGET_HIT, true);
						data.setValue(SRDataProcessors.CROSSBOW_OWNER, Optional.empty());
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
		ItemStack stack = event.getItemStack();
		Entity target = event.getTarget();
		if (target.getType() == EntityType.CREEPER || target.getType() == SREntities.CREEPIE.get()) {
			if (stack.getItem() == Items.CREEPER_SPAWN_EGG) {
				Level world = event.getWorld();
				CreepieEntity creepie = SREntities.CREEPIE.get().create(world);
				if (creepie != null) {
					creepie.copyPosition(target);
					if (stack.hasCustomHoverName()) creepie.setCustomName(stack.getHoverName());
					if (!event.getPlayer().isCreative()) stack.shrink(1);
					creepie.attackPlayersOnly = true;
					world.addFreshEntity(creepie);
					event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide()));
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		Level world = event.getWorld();
		BlockPos pos = event.getPos();
		if (world.getBlockState(pos).getBlock() instanceof AbstractBannerBlock) {
			List<BurningBannerEntity> burningBanners = world.getEntitiesOfClass(BurningBannerEntity.class, new AABB(pos));
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
		Player player = event.getPlayer();
		BlockPos pos = event.getPos();
		Level world = event.getWorld();

		if (stack.getItem() instanceof IPottableItem && world.getBlockState(pos).getBlock() == Blocks.FLOWER_POT) {
			BlockState pottedState = ((IPottableItem) stack.getItem()).getPottedState(player.getDirection().getOpposite());
			if (pottedState == null)
				return;
			world.setBlockAndUpdate(pos, pottedState);
			player.awardStat(Stats.POT_FLOWER);
			if (!event.getPlayer().isCreative()) stack.shrink(1);
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		} else if (isValidBurningBannerPos(world, pos)) {
			boolean isFlintAndSteel = stack.getItem() instanceof FlintAndSteelItem;
			if ((isFlintAndSteel || stack.getItem() instanceof FireChargeItem)) {
				SoundEvent sound = isFlintAndSteel ? SoundEvents.FLINTANDSTEEL_USE : SoundEvents.FIRECHARGE_USE;
				float pitch = isFlintAndSteel ? new Random().nextFloat() * 0.4F + 0.8F : (new Random().nextFloat() - new Random().nextFloat()) * 0.2F + 1.0F;
				world.playSound(player, pos, sound, SoundSource.BLOCKS, 1.0F, pitch);

				if (isFlintAndSteel) {
					stack.hurtAndBreak(1, player, (p_219998_1_) -> p_219998_1_.broadcastBreakEvent(event.getHand()));
				} else if (!player.isCreative()) {
					stack.shrink(1);
				}

				world.addFreshEntity(new BurningBannerEntity(world, pos, player));
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void livingUpdate(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		Level world = entity.level;
		IDataManager data = (IDataManager) entity;
		if (entity.getRemainingFireTicks() > 0 && entity.getEffect(SREffects.FROSTBITE.get()) != null)
			entity.removeEffect(SREffects.FROSTBITE.get());
		if (!world.isClientSide()) {
			CompoundTag persistentData = entity.getPersistentData();
			if (persistentData.getBoolean(NO_KNOCKBACK_KEY))
				persistentData.putBoolean(NO_KNOCKBACK_KEY, false);
			if (entity instanceof Raider) {
				int celebrationTime = data.getValue(SRDataProcessors.CELEBRATION_TIME);
				if (celebrationTime > 0)
					data.setValue(SRDataProcessors.CELEBRATION_TIME, celebrationTime - 1);
			}
			boolean canBeInvisible = maskCanMakeInvisible(entity);
			boolean invisibleDueToMask = data.getValue(SRDataProcessors.INVISIBLE_DUE_TO_MASK);
			boolean maskStateChanged = canBeInvisible != invisibleDueToMask;
			if (maskStateChanged) {
				data.setValue(SRDataProcessors.INVISIBLE_DUE_TO_MASK, canBeInvisible);
				spawnMaskParticles(world, entity.getBoundingBox(), 3);
			}
			if (maskStateChanged || (canBeInvisible && !entity.isInvisible()))
				entity.setInvisible(canBeInvisible || entity.hasEffect(MobEffects.INVISIBILITY));
		}
		if (entity instanceof Evoker) {
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

	private static boolean maskCanMakeInvisible(LivingEntity entity) {
		if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == SRItems.MASK_OF_DISHONESTY.get())
			return entity.isCrouching();
		return false;
	}

	public static void spawnMaskParticles(Level world, AABB box, int loops) {
		if (!world.isClientSide) {
			Random random = world.getRandom();
			for (int i = 0; i < loops; i++) {
				double x = box.min(Direction.Axis.X) + (random.nextFloat() * box.getXsize());
				double y = box.min(Direction.Axis.Y) + (random.nextFloat() * box.getYsize());
				double z = box.min(Direction.Axis.Z) + (random.nextFloat() * box.getZsize());
				NetworkUtil.spawnParticle(POOF_KEY, world.dimension(), x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public static boolean isValidBurningBannerPos(Level world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() instanceof AbstractBannerBlock) {
			List<BurningBannerEntity> banners = world.getEntitiesOfClass(BurningBannerEntity.class, new AABB(pos));
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
		CompoundTag fireworks = rocket.getOrCreateTagElement("Fireworks");
		ListTag explosions = new ListTag();
		CompoundTag explosion = new CompoundTag();
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
