package com.teamabnormals.savageandravage.common.entity;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

/**
 * Interface for living entities that launch projectiles or similar entities, has a method that handles what should happen when
 * its projectile hits something.
 */
public interface ITracksHits {

	/**
	 * Defines what should happen when {@code hitter} hit or otherwise affected {@code hit}. This can either be called
	 * in the entity's {@code tick} or by {@code hitter}.
	 *
	 * @param hitter - the projectile or other entity doing the hitting
	 * @param hit    - the {@code LivingEntity} that was hit
	 */
	@Nullable
	void onTrackedHit(Entity hitter, Entity hit);
}
