package com.minecraftabnormals.savageandravage.core.mixin;

import com.minecraftabnormals.savageandravage.core.other.SREvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends AbstractIllagerEntity implements ICrossbowUser {
	protected PillagerEntityMixin(EntityType<? extends AbstractIllagerEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "finalizeSpawn", at = @At(value = "HEAD"))
	public void addFirework(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData data, CompoundNBT nbt, CallbackInfoReturnable<ILivingEntityData> info) {
		if (this.level.random.nextInt(100) == 0) {
			this.setItemSlot(EquipmentSlotType.OFFHAND, SREvents.createRocket(this.getRandom()));
			this.startUsingItem(Hand.OFF_HAND);
			this.setDropChance(EquipmentSlotType.OFFHAND, 2.0F);
		}
	}
}
