package com.teamabnormals.savage_and_ravage.core.mixin;

import com.teamabnormals.savage_and_ravage.core.other.SREvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pillager.class)
public abstract class PillagerMixin extends AbstractIllager implements CrossbowAttackMob {
	protected PillagerMixin(EntityType<? extends AbstractIllager> type, Level world) {
		super(type, world);
	}

	@Inject(method = "finalizeSpawn", at = @At(value = "HEAD"))
	public void addFirework(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data, CompoundTag nbt, CallbackInfoReturnable<SpawnGroupData> info) {
		if (this.level.random.nextInt(100) == 0) {
			this.setItemSlot(EquipmentSlot.OFFHAND, SREvents.createRocket(this.getRandom()));
			this.startUsingItem(InteractionHand.OFF_HAND);
			this.setDropChance(EquipmentSlot.OFFHAND, 2.0F);
		}
	}
}
