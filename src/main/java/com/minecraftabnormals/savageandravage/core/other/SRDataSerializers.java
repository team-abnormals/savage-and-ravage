package com.minecraftabnormals.savageandravage.core.other;

import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class SRDataSerializers {
	public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, SavageAndRavage.MOD_ID);

	public static final IDataSerializer<Optional<Vector3d>> OPTIONAL_VECTOR3D = new IDataSerializer<Optional<Vector3d>>() {
		@Override
		public void write(PacketBuffer buffer, Optional<Vector3d> optionalVector) {
			buffer.writeBoolean(optionalVector.isPresent());
			if (optionalVector.isPresent()) {
				Vector3d vector = optionalVector.get();
				buffer.writeDouble(vector.x);
				buffer.writeDouble(vector.y);
				buffer.writeDouble(vector.z);
			}
		}

		@Override
		public Optional<Vector3d> read(PacketBuffer buffer) {
			return !buffer.readBoolean() ? Optional.empty() : Optional.of(new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
		}

		@Override
		public Optional<Vector3d> copy(Optional<Vector3d> optionalVector) {
			return optionalVector;
		}
	};

	static {
		SERIALIZERS.register("optional_vector3d", () -> new DataSerializerEntry(OPTIONAL_VECTOR3D));
	}
}
