package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.savageandravage.common.entity.*;
import com.minecraftabnormals.savageandravage.common.entity.block.SporeBombEntity;
import com.minecraftabnormals.savageandravage.common.entity.goals.AvoidGrieferOwnedCreepiesGoal;
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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID)
public class SREvents {

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof PillagerEntity) {
			PillagerEntity pillager = (PillagerEntity) event.getEntity();
			ImprovedCrossbowGoal<PillagerEntity> aiCrossBow = new ImprovedCrossbowGoal<>(pillager, 1.0D, 8.0F, 5.0D);
			pillager.goalSelector.availableGoals.stream().map(it -> it.goal).filter(it -> it instanceof RangedCrossbowAttackGoal<?>).findFirst().ifPresent(crossbowGoal -> {
				pillager.goalSelector.removeGoal(crossbowGoal);
				pillager.goalSelector.addGoal(3, aiCrossBow);
			});
		}
		if (SRConfig.COMMON.evokersUseTotems.get() && event.getEntity() instanceof EvokerEntity) {
			EvokerEntity evoker = (EvokerEntity) event.getEntity();
			evoker.goalSelector.addGoal(1, new AvoidEntityGoal<IronGolemEntity>(evoker, IronGolemEntity.class, 8.0F, 0.6D, 1.0D) {
				@Override
				public boolean canUse() {
					return super.canUse() && SRConfig.COMMON.evokersUseTotems.get() && ((IDataManager) this.mob).getValue(SRDataProcessors.TOTEM_SHIELD_TIME) > 0;
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
		if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
			if (event.getEntity() instanceof IronGolemEntity) {
				IronGolemEntity golem = (IronGolemEntity) event.getEntity();
				golem.targetSelector.availableGoals.stream().map(it -> it.goal).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(noAngryAtCreeper -> {
					golem.targetSelector.removeGoal(noAngryAtCreeper);
					golem.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(golem, MobEntity.class, 5, false, false, (p_213619_0_) -> p_213619_0_ instanceof IMob));
				});
			} else if (event.getEntity().getType() == EntityType.CREEPER) {
				CreeperEntity creeper = (CreeperEntity) event.getEntity();
				creeper.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(creeper, IronGolemEntity.class, true));
			}
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
			villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, TricksterEntity.class, 8.0F, 0.8D, 0.8D));
			villager.goalSelector.addGoal(1, new AvoidGrieferOwnedCreepiesGoal<>(villager, CreepieEntity.class, 8.0F, 0.8D, 0.8D));
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
		if (entity instanceof GolemEntity && !(entity instanceof ShulkerEntity) && target instanceof IOwnableMob) {
			if (((IOwnableMob) target).getOwner() instanceof PlayerEntity && ((MobEntity) target).getTarget() != entity) {
				((GolemEntity) entity).setTarget(null);
			}
		}
		if (entity instanceof EvokerEntity && SRConfig.COMMON.evokersUseTotems.get() && ((IDataManager) entity).getValue(SRDataProcessors.TOTEM_SHIELD_TIME) > 0)
			((MobEntity) entity).setTarget(null);
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event) {
		World world = event.getWorld();
		Explosion explosion = event.getExplosion();
		if (explosion.getSourceMob() != null) {
			if (explosion.getSourceMob().getType() == EntityType.CREEPER) {
				if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
					event.getAffectedBlocks().clear();
				}
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
			IDataManager data = (IDataManager) entity;
			if (data.getValue(SRDataProcessors.TOTEM_SHIELD_TIME) > 0) {
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
			boolean canBeInvisible = maskCanMakeInvisible(entity);
			boolean invisibleDueToMask = data.getValue(SRDataProcessors.INVISIBLE_DUE_TO_MASK);
			boolean maskStateChanged = canBeInvisible != invisibleDueToMask;
			if (maskStateChanged) {
				data.setValue(SRDataProcessors.INVISIBLE_DUE_TO_MASK, canBeInvisible);
				spawnMaskParticles(entity.getRandom(), entity.getBoundingBox(), 3);
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
		if (((IDataManager) entity).getValue(SRDataProcessors.INVISIBLE_DUE_TO_MASK)) {
			double armorCover = entity.getArmorCoverPercentage();
			if (armorCover < 0.1F) {
				armorCover = 0.1F;
			}
			event.modifyVisibility(1 / armorCover); //potentially slightly inaccurate
			event.modifyVisibility(0.1);
		}
	}

	private static boolean maskCanMakeInvisible(LivingEntity entity) {
		if (entity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == SRItems.MASK_OF_DISHONESTY.get())
			return entity.isCrouching();
		return false;
	}

	public static void spawnMaskParticles(Random random, AxisAlignedBB box, int loops) {
		ResourceLocation poofId = ForgeRegistries.PARTICLE_TYPES.getKey(ParticleTypes.POOF);
		if (poofId != null) {
			for (int i = 0; i < loops; i++) {
				double randomPositionX = box.min(Direction.Axis.X) + (random.nextFloat() * box.getXsize());
				double randomPositionY = box.min(Direction.Axis.Y) + (random.nextFloat() * box.getYsize());
				double randomPositionZ = box.min(Direction.Axis.Z) + (random.nextFloat() * box.getZsize());
				NetworkUtil.spawnParticle(poofId.toString(), randomPositionX, randomPositionY, randomPositionZ, 0.0f, 0.0f, 0.0f);
			}
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
