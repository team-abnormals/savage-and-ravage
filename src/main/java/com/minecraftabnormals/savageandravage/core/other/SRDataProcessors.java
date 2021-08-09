package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.*;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Optional;

public class SRDataProcessors {
	public static final IDataProcessor<Optional<Vector3d>> OPTIONAL_VECTOR3D = new IDataProcessor<Optional<Vector3d>>() {
		@Override
		public CompoundNBT write(Optional<Vector3d> optionalVector) {
			CompoundNBT nbt = new CompoundNBT();
			if (optionalVector.isPresent()) {
				Vector3d vector = optionalVector.get();
				nbt.putDouble("x", vector.x);
				nbt.putDouble("y", vector.y);
				nbt.putDouble("z", vector.z);
			} else nbt.putBoolean("null", true);
			return nbt;
		}

		@Override
		public Optional<Vector3d> read(CompoundNBT nbt) {
			return Optional.ofNullable(nbt.getBoolean("null") ? null : new Vector3d(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z")));
		}
	};

	public static final TrackedData<Integer> TOTEM_SHIELD_TIME = TrackedData.Builder.create(DataProcessors.INT, () -> -1).enableSaving().build();
	public static final TrackedData<Integer> TOTEM_SHIELD_COOLDOWN = TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().build();
	public static final TrackedData<Boolean> MARK_INVISIBLE = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enableSaving().setSyncType(SyncType.TO_CLIENTS).build();
	public static final TrackedData<Boolean> INVISIBLE_DUE_TO_MASK = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enableSaving().setSyncType(SyncType.TO_CLIENTS).build();
	public static final TrackedData<Optional<Vector3d>> PREVIOUS_POSITION = TrackedData.Builder.create(OPTIONAL_VECTOR3D, Optional::empty).setSyncType(SyncType.NOPE).build();
	public static final TrackedData<Integer> ILLEGAL_MASK_TICKS = TrackedData.Builder.create(DataProcessors.INT, () -> 0).setSyncType(SyncType.NOPE).build();

	public static void registerTrackedData() {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "evoker_shield_time"), TOTEM_SHIELD_TIME);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "evoker_shield_cooldown"), TOTEM_SHIELD_COOLDOWN);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "mark_invisible"), MARK_INVISIBLE);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "invisible_due_to_mask"), INVISIBLE_DUE_TO_MASK);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "previous_position"), PREVIOUS_POSITION);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(SavageAndRavage.MOD_ID, "illegal_mask_ticks"), ILLEGAL_MASK_TICKS);
	}
}
