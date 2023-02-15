package com.teamabnormals.savage_and_ravage.core.mixin;

import com.teamabnormals.savage_and_ravage.core.SRConfig;
import com.teamabnormals.savage_and_ravage.core.other.tags.SREntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zoglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zoglin.class)
public abstract class ZoglinMixin {

	@Inject(at = @At("RETURN"), method = "isTargetable", cancellable = true)
	private void isTargetable(LivingEntity entity, CallbackInfoReturnable<Boolean> ci) {
		if (!SRConfig.COMMON.creeperExplosionsDestroyBlocks.get() && entity.getType().is(SREntityTypeTags.CREEPERS))
			ci.setReturnValue(true);
	}
}
