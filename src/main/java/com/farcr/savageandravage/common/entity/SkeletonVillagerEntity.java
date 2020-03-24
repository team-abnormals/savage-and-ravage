package com.farcr.savageandravage.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class SkeletonVillagerEntity extends AbstractSkeletonEntity {

	public SkeletonVillagerEntity(EntityType<? extends SkeletonVillagerEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected SoundEvent getStepSound() {
		// TODO Auto-generated method stub
		return null;
	}

}
