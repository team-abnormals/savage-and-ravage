package com.teamabnormals.savage_and_ravage.core.other;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class SRDataSerializers {
	public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, SavageAndRavage.MOD_ID);

	public static final EntityDataSerializer<Optional<Vec3>> OPTIONAL_VECTOR3D = new EntityDataSerializer<Optional<Vec3>>() {
		@Override
		public void write(FriendlyByteBuf buffer, Optional<Vec3> optionalVector) {
			buffer.writeBoolean(optionalVector.isPresent());
			if (optionalVector.isPresent()) {
				Vec3 vector = optionalVector.get();
				buffer.writeDouble(vector.x);
				buffer.writeDouble(vector.y);
				buffer.writeDouble(vector.z);
			}
		}

		@Override
		public Optional<Vec3> read(FriendlyByteBuf buffer) {
			return !buffer.readBoolean() ? Optional.empty() : Optional.of(new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
		}

		@Override
		public Optional<Vec3> copy(Optional<Vec3> optionalVector) {
			return optionalVector;
		}
	};

	static {
		SERIALIZERS.register("optional_vector3d", () -> new DataSerializerEntry(OPTIONAL_VECTOR3D));
	}
}
