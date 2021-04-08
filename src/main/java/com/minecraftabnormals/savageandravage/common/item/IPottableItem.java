package com.minecraftabnormals.savageandravage.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

/**
 * <p>Used to specify what modded items can be placed into a flower pot.</p>
 *
 * @author Ocelot
 */
public interface IPottableItem {

	/**
	 * @param direction The direction that the pot should face, ignore if not directional
	 * @return The potted state or <code>null</code> if there is no potted state
	 */
	@Nullable
	BlockState getPottedState(Direction direction);

}
