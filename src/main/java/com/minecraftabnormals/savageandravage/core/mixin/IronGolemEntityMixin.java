package com.minecraftabnormals.savageandravage.core.mixin;

import com.minecraftabnormals.savageandravage.core.SRConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends GolemEntity {

	private IronGolemEntityMixin(EntityType<? extends GolemEntity> entity, World world) {
		super(entity, world);
	}

	@Shadow
	public abstract boolean isPlayerCreated();

	@Inject(at = @At("HEAD"), method = "collideWithEntity(Lnet/minecraft/entity/Entity;)V", cancellable = true)
	public void collideWithEntity(Entity entity, CallbackInfo ci) {
		if (entity instanceof IMob && this.getRNG().nextInt(20) == 0) {
			this.setAttackTarget((LivingEntity) entity);
		}
		super.collideWithEntity(entity);
	}

	@Inject(at = @At("RETURN"), method = "canAttack(Lnet/minecraft/entity/EntityType;)Z", cancellable = true)
	public void canAttack(EntityType<?> typeIn, CallbackInfoReturnable<Boolean> ci) {
		if (this.isPlayerCreated() && typeIn == EntityType.PLAYER || SRConfig.COMMON.creeperExplosionsDestroyBlocks.get() && typeIn == EntityType.CREEPER) {
			ci.setReturnValue(false);
		}
		ci.setReturnValue(super.canAttack(typeIn));
	}
}
