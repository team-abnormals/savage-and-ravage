package com.minecraftabnormals.savageandravage.core.mixin;

import com.minecraftabnormals.savageandravage.core.SRConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZoglinEntity.class)
public abstract class ZoglinEntityMixin {

    @Inject(at = @At("RETURN"), method = "func_234337_j_", cancellable = true)
    private static void func_234337_j_(LivingEntity entity, CallbackInfoReturnable<Boolean> ci) {
        if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get() && entity.getType() == EntityType.CREEPER)
            ci.setReturnValue(true);
    }
}
