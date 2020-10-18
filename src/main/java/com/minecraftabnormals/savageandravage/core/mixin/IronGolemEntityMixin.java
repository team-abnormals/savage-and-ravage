package com.minecraftabnormals.savageandravage.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecraftabnormals.savageandravage.core.SRConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;

@Mixin(IronGolemEntity.class)
public class IronGolemEntityMixin extends GolemEntity {
	protected IronGolemEntityMixin(EntityType<? extends GolemEntity> entity, World world) {
		super(entity, world);
	}
	
	@Shadow
	public boolean isPlayerCreated() {
		return false;
	}
	
	@Inject(at = @At("RETURN"), method = "collideWithEntity(Lnet/minecraft/entity/Entity;)V", cancellable = true)
    protected void collideWithEntity(Entity entityIn, CallbackInfo info) {
		if (entityIn instanceof IMob && this.getRNG().nextInt(20) == 0) {
			this.setAttackTarget((LivingEntity)entityIn);
		}
		super.collideWithEntity(entityIn);
	}
	
	@Inject(at = @At("RETURN"), method = "canAttack(Lnet/minecraft/entity/EntityType;)Z", cancellable = true)
	public boolean canAttack(EntityType<?> typeIn, CallbackInfoReturnable<Boolean> cir) {
		if (this.isPlayerCreated() && typeIn == EntityType.PLAYER || SRConfig.COMMON.creeperExplosionsDestroyBlocks.get() && typeIn == EntityType.CREEPER) {
			return false;
		} else {
			return super.canAttack(typeIn);
		}
	}
}
