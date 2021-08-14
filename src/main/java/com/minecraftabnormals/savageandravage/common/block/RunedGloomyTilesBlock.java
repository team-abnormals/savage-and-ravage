package com.minecraftabnormals.savageandravage.common.block;

import com.minecraftabnormals.savageandravage.common.entity.IOwnableMob;
import com.minecraftabnormals.savageandravage.common.entity.RunePrisonEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RunedGloomyTilesBlock extends ChiseledGloomyTilesBlock {

	public RunedGloomyTilesBlock(Properties blockProperties) {
		super(blockProperties);
	}

	@Override
	public void stepOn(World world, BlockPos pos, Entity entity) {
		super.stepOn(world, pos, entity);
		BlockState state = world.getBlockState(pos);
		if (!state.getValue(POWERED) && shouldTrigger(entity, true)) {
			world.setBlockAndUpdate(pos, state.setValue(POWERED, true));
			world.playSound(null, pos, SRSounds.GENERIC_PREPARE_ATTACK.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
			EvokerFangsEntity evokerFangs = EntityType.EVOKER_FANGS.create(world);
			if (evokerFangs != null) {
				evokerFangs.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
				world.addFreshEntity(evokerFangs);
			}
			RunePrisonEntity runePrison = new RunePrisonEntity(world, pos, 25, true);
			runePrison.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
			world.addFreshEntity(runePrison);
		}
	}

	public static boolean shouldTrigger(Entity entity, boolean fooledByMask) {
		if (EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(entity) && entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			if (!fooledByMask || livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() != SRItems.MASK_OF_DISHONESTY.get()) {
				if (!EntityTypeTags.RAIDERS.contains(livingEntity.getType())) {
					if (livingEntity instanceof IOwnableMob) {
						LivingEntity owner = ((IOwnableMob) livingEntity).getOwner();
						return owner == null || !EntityTypeTags.RAIDERS.contains(owner.getType());
					} else return livingEntity.isAffectedByPotions();
				}
			}
		}
		return false;
	}
}
