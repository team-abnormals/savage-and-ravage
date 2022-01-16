package com.teamabnormals.savageandravage.core.mixin;

import com.teamabnormals.savageandravage.core.SRConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author - SmellyModder (Luke Tonon)
 */
@Mixin(Raider.class)
public abstract class RaiderMixin extends PatrollingMonster {
	private RaiderMixin(EntityType<? extends PatrollingMonster> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z", shift = At.Shift.AFTER), cancellable = true)
	private void cancelBadOmenEffect(DamageSource source, CallbackInfo info) {
		if (SRConfig.COMMON.noBadOmenOnDeath.get()) {
			super.die(source);
			info.cancel();
		}
	}
}