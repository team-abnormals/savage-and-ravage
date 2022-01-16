package com.teamabnormals.savage_and_ravage.core.mixin;

import net.minecraft.world.entity.raid.Raider;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Raider.class)
public interface RaiderAccessor {
	@Accessor("IS_CELEBRATING")
	EntityDataAccessor<Boolean> getIsCelebrating();
}
