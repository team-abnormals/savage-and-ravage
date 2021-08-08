package com.minecraftabnormals.savageandravage.core.mixin;

import com.minecraftabnormals.savageandravage.common.entity.TricksterEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpellcastingIllagerEntity.class)
public abstract class SpellcastingIllagerEntityMixin {
    @SuppressWarnings("all")
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/SpellcastingIllagerEntity;isCastingSpell()Z", shift = At.Shift.BEFORE), cancellable = true)
    public void cancelParticles(CallbackInfo info) {
        if (((Object) this) instanceof TricksterEntity)
            info.cancel();
    }
}
