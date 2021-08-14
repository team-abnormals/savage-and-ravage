package com.minecraftabnormals.savageandravage.core.mixin;

import com.minecraftabnormals.savageandravage.core.SRConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author - SmellyModder (Luke Tonon)
 */
@Mixin(AbstractRaiderEntity.class)
public abstract class AbstractRaiderEntityMixin extends PatrollerEntity {
	private AbstractRaiderEntityMixin(EntityType<? extends PatrollerEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z", shift = At.Shift.AFTER), cancellable = true)
	private void cancelBadOmenEffect(DamageSource source, CallbackInfo info) {
		if (SRConfig.COMMON.noBadOmenOnDeath.get()) {
			super.die(source);
			info.cancel();
		}
	}
}