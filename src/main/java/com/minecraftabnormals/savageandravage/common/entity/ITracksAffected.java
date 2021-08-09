package com.minecraftabnormals.savageandravage.common.entity;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

/**
 * Interface for projectile entities or similar, has a method that returns the last entity that was affected by this one.
 */
public interface ITracksAffected {

	/**
	 * 'Affected' entails that something happened e.g. damage or potion effect application - if an entity intersects but
	 * no change occurs then this method doesn't fire.
	 *
	 * @return the last entity affected
	 */
	@Nullable
	Entity getLastAffected();
}
