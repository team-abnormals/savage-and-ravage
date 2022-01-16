package com.teamabnormals.savageandravage.common.block;

import com.teamabnormals.savageandravage.common.entity.IOwnableMob;
import com.teamabnormals.savageandravage.common.entity.RunePrisonEntity;
import com.teamabnormals.savageandravage.core.registry.SRItems;
import com.teamabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RunedGloomyTilesBlock extends ChiseledGloomyTilesBlock {

	public RunedGloomyTilesBlock(Properties blockProperties) {
		super(blockProperties);
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		super.stepOn(world, pos, state, entity);
		if (!state.getValue(POWERED) && shouldTrigger(entity, true)) {
			world.setBlockAndUpdate(pos, state.setValue(POWERED, true));
			world.playSound(null, pos, SRSounds.GENERIC_PREPARE_ATTACK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			EvokerFangs evokerFangs = EntityType.EVOKER_FANGS.create(world);
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
		if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			if (!fooledByMask || livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() != SRItems.MASK_OF_DISHONESTY.get()) {
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
