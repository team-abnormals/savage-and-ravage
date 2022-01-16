package com.teamabnormals.savageandravage.core.other;

import com.teamabnormals.blueprint.common.world.storage.tracking.*;
import com.teamabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.UUID;

public class SRDataProcessors {
	public static final IDataProcessor<Optional<UUID>> OPTIONAL_UUID = new IDataProcessor<Optional<UUID>>() {
		@Override
		public CompoundTag write(Optional<UUID> optionalUUID) {
			CompoundTag nbt = new CompoundTag();
			if (optionalUUID.isPresent()) {
				nbt.putUUID("uuid", optionalUUID.get());
			} else nbt.putBoolean("null", true);
			return nbt;
		}

		@Override
		public Optional<UUID> read(CompoundTag nbt) {
			return Optional.ofNullable(nbt.getBoolean("null") ? null : nbt.getUUID("uuid"));
		}
	};

	public static final TrackedData<Integer> TOTEM_SHIELD_TIME = TrackedData.Builder.create(DataProcessors.INT, () -> -1).enableSaving().build();
	public static final TrackedData<Integer> TOTEM_SHIELD_COOLDOWN = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().build();

	public static final TrackedData<Boolean> INVISIBLE_DUE_TO_MASK = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enableSaving().build();

	public static final TrackedData<Boolean> TARGET_HIT = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).setSyncType(SyncType.NOPE).build();
	public static final TrackedData<Optional<UUID>> CROSSBOW_OWNER = TrackedData.Builder.create(SRDataProcessors.OPTIONAL_UUID, Optional::empty).setSyncType(SyncType.NOPE).build();
	public static final TrackedData<Integer> CELEBRATION_TIME = TrackedData.Builder.create(DataProcessors.INT, () -> 0).setSyncType(SyncType.NOPE).build();

	public static void registerTrackedData() {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "evoker_shield_time"), TOTEM_SHIELD_TIME);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "evoker_shield_cooldown"), TOTEM_SHIELD_COOLDOWN);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "invisible_due_to_mask"), INVISIBLE_DUE_TO_MASK);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "target_hit"), TARGET_HIT);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "crossbow_owner"), CROSSBOW_OWNER);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "celebration_time"), CELEBRATION_TIME);
	}
}
