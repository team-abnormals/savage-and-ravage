package com.minecraftabnormals.savageandravage.core.mixin;

import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.network.datasync.DataParameter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractRaiderEntity.class)
public interface IRaiderAccessor {
    @Accessor("IS_CELEBRATING")
    DataParameter<Boolean> getIsCelebrating();
}
