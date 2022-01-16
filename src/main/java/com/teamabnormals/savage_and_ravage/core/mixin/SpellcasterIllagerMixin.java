package com.teamabnormals.savage_and_ravage.core.mixin;

import com.teamabnormals.savage_and_ravage.common.entity.TricksterEntity;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpellcasterIllager.class)
public abstract class SpellcasterIllagerMixin {

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/SpellcasterIllager;isCastingSpell()Z", shift = At.Shift.BEFORE), cancellable = true)
	public void cancelParticles(CallbackInfo info) {
		if (((Object) this) instanceof TricksterEntity)
			info.cancel();
	}
}
