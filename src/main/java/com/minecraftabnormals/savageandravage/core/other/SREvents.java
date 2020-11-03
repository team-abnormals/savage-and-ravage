package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.savageandravage.common.effect.GrowingEffect;
import com.minecraftabnormals.savageandravage.common.effect.ShrinkingEffect;
import com.minecraftabnormals.savageandravage.common.entity.BurningBannerEntity;
import com.minecraftabnormals.savageandravage.common.entity.SporeCloudEntity;
import com.minecraftabnormals.savageandravage.common.entity.CreepieEntity;
import com.minecraftabnormals.savageandravage.common.entity.GrieferEntity;
import com.minecraftabnormals.savageandravage.common.entity.IOwnableMob;
import com.minecraftabnormals.savageandravage.common.entity.SkeletonVillagerEntity;
import com.minecraftabnormals.savageandravage.common.entity.goals.AvoidGrieferOwnedCreepiesGoal;
import com.minecraftabnormals.savageandravage.common.entity.goals.ImprovedCrossbowGoal;
import com.minecraftabnormals.savageandravage.common.item.PottableItem;
import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import com.minecraftabnormals.savageandravage.core.registry.SRAttributes;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID)
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
            if (event.getWorld().rand.nextInt(100) == 0 && !event.getWorld().isRemote) {
                pillager.setItemStackToSlot(EquipmentSlotType.OFFHAND, createRocket());
                pillager.setActiveHand(Hand.OFF_HAND);
                pillager.setDropChance(EquipmentSlotType.OFFHAND, 2.0F);
            }
        }

        // Attempted to make golems attack creepers, didnt work
        if (event.getEntity() instanceof IronGolemEntity && !SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
            IronGolemEntity golem = (IronGolemEntity) event.getEntity();
            golem.targetSelector.goals.stream().map(it -> it.inner).filter(it -> it instanceof NearestAttackableTargetGoal<?>).findFirst().ifPresent(noAngryAtCreeper -> {
                golem.targetSelector.removeGoal(noAngryAtCreeper);
                golem.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(golem, MobEntity.class, 5, false, false, (p_213619_0_) -> p_213619_0_ instanceof IMob));
            });
        }
        if (event.getEntity() instanceof CreeperEntity && !SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
            CreeperEntity creeper = (CreeperEntity) event.getEntity();
            creeper.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(creeper, IronGolemEntity.class, true));
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
        //TODO can this not just be a loot table? check with tallest
        if (event.getEntity() instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity) event.getEntity();
            if (event.getSource().isExplosion() && SRConfig.COMMON.creepersDropSporesAfterExplosionDeath.get()) {
                creeper.entityDropItem(new ItemStack(SRItems.CREEPER_SPORES.get(), 1 + creeper.world.rand.nextInt(3)));
            }
        }
        // kinda messy rn will clean it up later
        else if (event.getEntity() instanceof PillagerEntity) {
            PillagerEntity pillager = (PillagerEntity) event.getEntity();
            if (pillager.isServerWorld() && ((ServerWorld) pillager.getEntityWorld()).findRaid(pillager.getPosition()) != null) {
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
        if (event.getExplosion().getExplosivePlacedBy() instanceof CreeperEntity && !(event.getExplosion().getExplosivePlacedBy() instanceof CreepieEntity)) {
            if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get()) {
                event.getAffectedBlocks().clear();
            }
            if (SRConfig.COMMON.creeperExplosionsSpawnCreepies.get()) {
                CreeperEntity creeper = (CreeperEntity) event.getExplosion().getExplosivePlacedBy();
                SporeCloudEntity spores = SREntities.SPORE_CLOUD.get().create(event.getWorld());
                if (spores == null)
                    return;

                spores.setSporeBomb(true);
                spores.setHaveCreepiesAttackPlayersOnly(true);
                spores.setCloudSize(creeper.isCharged() ? (int) (creeper.getHealth() / 4) : (int) (creeper.getHealth() / 5));
                spores.copyLocationAndAnglesFrom(creeper);
                creeper.world.addEntity(spores);
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
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        if(world.getBlockState(pos).getBlock() instanceof AbstractBannerBlock) {
            List<BurningBannerEntity> burningBanners = world.getEntitiesWithinAABB(BurningBannerEntity.class, new AxisAlignedBB(pos));
            for (BurningBannerEntity burningBanner : burningBanners) {
                if(burningBanner.getBannerPosition() != null && burningBanner.getBannerPosition().equals(pos)) {
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
        } else if (isValidBannerPos(event)) {
            TileEntity te = world.getTileEntity(pos);
            boolean isFlintAndSteel = stack.getItem() instanceof FlintAndSteelItem;

            if (te instanceof BannerTileEntity && (isFlintAndSteel || stack.getItem() instanceof FireChargeItem)) {
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

    private static boolean isValidBannerPos(PlayerInteractEvent event) {
        boolean isValid = false;
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        if (world.getBlockState(pos).getBlock() instanceof AbstractBannerBlock) {
            if (world.getEntitiesWithinAABB(BurningBannerEntity.class, new AxisAlignedBB(pos)).isEmpty()) {
                isValid = true;
            } else {
                List<BurningBannerEntity> burningBanners = world.getEntitiesWithinAABB(BurningBannerEntity.class, new AxisAlignedBB(pos));
                isValid = true;
                for (BurningBannerEntity burningBanner : burningBanners) {
                    if (burningBanner.getBannerPosition() != null && burningBanner.getBannerPosition().equals(pos)) isValid = false;
                }
            }
        }
        return isValid;
    }

    @SubscribeEvent
    public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) throws InvocationTargetException, IllegalAccessException {
        LivingEntity affected = event.getEntityLiving();
        boolean shouldSetChild = false;
        int growingAgeValue = 0;
        if(event.getPotionEffect() != null) {
            if (event.getPotionEffect().getPotion() instanceof ShrinkingEffect) {
                shouldSetChild = true;
                growingAgeValue = -24000;
            }
            if (event.getPotionEffect().getPotion() instanceof GrowingEffect || shouldSetChild) {
                boolean canChange = false;
                if (affected instanceof SlimeEntity) {
                    SlimeEntity slime = (SlimeEntity) affected;
                    int size = slime.getSlimeSize();
                    if (shouldSetChild ? size > 1 : size < 3) {
                        canChange = true;
                        Method setSize = ObfuscationReflectionHelper.findMethod(SlimeEntity.class, "func_70799_a", int.class, boolean.class);
                        setSize.invoke(slime, (size + (shouldSetChild ? (size < 4 ? -1 : -2) : (size < 2 ? 1 : 2))), false);
                    }
                } else if (checkBooflo(affected, shouldSetChild))
                    canChange = true;
                else if (shouldSetChild != affected.isChild()) {
                    canChange = true;
                    if (affected instanceof AgeableEntity && !(affected instanceof ParrotEntity))
                        ((AgeableEntity) affected).setGrowingAge(growingAgeValue);
                    else if (shouldSetChild && affected instanceof CreeperEntity)
                        convertCreeper((CreeperEntity) affected);
                    else if (!shouldSetChild && affected instanceof CreepieEntity)
                        ((CreepieEntity) affected).setGrowingAge(growingAgeValue);
                    else if (affected instanceof ZombieEntity)
                        ((ZombieEntity) affected).setChild(shouldSetChild);
                    else if (affected instanceof PiglinEntity)
                        ((PiglinEntity) affected).setChild(shouldSetChild);
                    else if (affected instanceof ZoglinEntity)
                        ((ZoglinEntity) affected).setChild(shouldSetChild);
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
                if (affected.isServerWorld()) {
                    ((ServerWorld) affected.world).spawnParticle(canChange ? (shouldSetChild ? ParticleTypes.TOTEM_OF_UNDYING : ParticleTypes.HAPPY_VILLAGER) : ParticleTypes.LARGE_SMOKE, affected.getPosXRandom(0.3D), affected.getPosYRandom() - 0.1D, affected.getPosZRandom(0.3D), canChange ? 40 : 20, 0.3D, 0.6D, 0.3D, canChange ? 0.2D : 0.01D);
                    affected.playSound(canChange ? SRSounds.GROWTH_MODIFICATION_SUCCESS.get() : SRSounds.GROWTH_MODIFICATION_FAILURE.get(), 1.0F, 1.0F);
                }
            }
        }
    }

    public static boolean checkBooflo(LivingEntity affected, boolean isBabyPotion) {
        if (ModList.get().isLoaded("endergetic")) {
            return ((!isBabyPotion && affected.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("endergetic:booflo_baby")) || affected.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("endergetic:booflo_adolescent")) || (isBabyPotion && affected.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("endergetic:booflo")))));
        }
        return false;
    }

    public static void convertCreeper(CreeperEntity creeper) {
        CreepieEntity creepie = SREntities.CREEPIE.get().create(creeper.world);
        if (creepie == null)
            return;

        creepie.copyLocationAndAnglesFrom(creeper.getEntity());
        creepie.onInitialSpawn(creeper.world, creeper.world.getDifficultyForLocation(new BlockPos(creepie.getPositionVec())), SpawnReason.CONVERSION, null, null);
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
