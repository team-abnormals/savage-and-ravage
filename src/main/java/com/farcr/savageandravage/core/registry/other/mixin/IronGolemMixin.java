package com.farcr.savageandravage.core.registry.other.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

@Mixin(IronGolemEntity.class)
public class IronGolemMixin extends GolemEntity 
{
	protected IronGolemMixin(EntityType<? extends GolemEntity> p_i48569_1_, World p_i48569_2_) {
		super(p_i48569_1_, p_i48569_2_);
		// TODO Auto-generated constructor stub
	}
	
	@Shadow
	public boolean isPlayerCreated() {
		return false;
	}
	
	@Overwrite(remap = true)
	public boolean canAttack(EntityType<?> typeIn) 
	{
	 if (this.isPlayerCreated() && typeIn == EntityType.PLAYER) {
	         return false;
	      } else {
	         return super.canAttack(typeIn);
	      }
	 }
	

}
