package com.teamabnormals.savage_and_ravage.common.block;

import com.teamabnormals.savage_and_ravage.common.entity.OwnableMob;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.RunePrison;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import com.teamabnormals.savage_and_ravage.core.registry.SRSounds;
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
			RunePrison runePrison = new RunePrison(world, pos, 25, true);
			runePrison.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
			world.addFreshEntity(runePrison);
		}
	}

	public static boolean shouldTrigger(Entity entity, boolean fooledByMask) {
		if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && entity instanceof LivingEntity livingEntity) {
			if (!fooledByMask || livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() != SRItems.MASK_OF_DISHONESTY.get()) {
				if (!livingEntity.getType().is(EntityTypeTags.RAIDERS)) {
					if (livingEntity instanceof OwnableMob) {
						LivingEntity owner = ((OwnableMob) livingEntity).getOwner();
						return owner == null || !owner.getType().is(EntityTypeTags.RAIDERS);
					} else return livingEntity.isAffectedByPotions();
				}
			}
		}
		return false;
	}
}
