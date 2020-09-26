package com.minecraftabnormals.savageandravage.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecraftabnormals.savageandravage.core.SRConfig;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.util.EntityPredicates;

@Mixin(ZoglinEntity.class)
public class ZoglinEntityMixin {

    @Inject(at = @At("RETURN"), method = "func_234337_j_(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    private static boolean func_234337_j_(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        return SRConfig.COMMON.creeperExplosionsDestroyBlocks.get() ? entity.getType() != EntityType.ZOGLIN && entity.getType() != EntityType.CREEPER && EntityPredicates.field_233583_f_.test(entity) : entity.getType() != EntityType.ZOGLIN && EntityPredicates.field_233583_f_.test(entity) ;
    }
}
