package com.minecraftabnormals.savageandravage.core.mixin;

import com.minecraftabnormals.savageandravage.core.SRConfig;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author - SmellyModder (Luke Tonon)
 */
@Mixin(AbstractRaiderEntity.class)
public abstract class AbstractRaiderEntityMixin {
	@Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addEffect(Lnet/minecraft/potion/EffectInstance;)Z"))
	private boolean cancelBadOmenEffect(PlayerEntity player, EffectInstance effect) {
		return !(SRConfig.COMMON.noBadOmenOnDeath.get() || player.addEffect(effect));
	}
}
