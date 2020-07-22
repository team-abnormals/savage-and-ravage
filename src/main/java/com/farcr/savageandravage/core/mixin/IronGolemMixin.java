package com.farcr.savageandravage.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;

@Mixin(IronGolemEntity.class)
public class IronGolemMixin extends GolemEntity {
	protected IronGolemMixin(EntityType<? extends GolemEntity> entity, World world) {
		super(entity, world);
	}
	
	@Shadow
	public boolean isPlayerCreated() {
		return false;
	}
	
	@Overwrite(remap = true)
    protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof IMob && this.getRNG().nextInt(20) == 0) {
			this.setAttackTarget((LivingEntity)entityIn);
		}
		super.collideWithEntity(entityIn);
	}
	
	@Overwrite(remap = true)
	public boolean canAttack(EntityType<?> typeIn) {
		if (this.isPlayerCreated() && typeIn == EntityType.PLAYER) {
			return false;
		} else {
			return super.canAttack(typeIn);
		}
	}
}
