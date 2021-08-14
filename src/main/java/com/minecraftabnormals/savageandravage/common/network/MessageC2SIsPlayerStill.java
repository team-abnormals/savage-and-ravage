package com.minecraftabnormals.savageandravage.common.network;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.savageandravage.core.other.SRDataProcessors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageC2SIsPlayerStill {
	private final UUID playerId;
	private final boolean isStill;

	public MessageC2SIsPlayerStill(UUID playerId, boolean isStill) {
		this.playerId = playerId;
		this.isStill = isStill;
	}

	public void serialize(PacketBuffer buffer) {
		buffer.writeUUID(this.playerId);
		buffer.writeBoolean(this.isStill);
	}

	public static MessageC2SIsPlayerStill deserialize(PacketBuffer buffer) {
		return new MessageC2SIsPlayerStill(buffer.readUUID(), buffer.readBoolean());
	}

	public static void handle(MessageC2SIsPlayerStill message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				if (context.getSender() != null) {
					PlayerEntity player = context.getSender().getLevel().getPlayerByUUID(message.playerId);
					if (player != null)
						((IDataManager) player).setValue(SRDataProcessors.MARK_INVISIBLE, message.isStill);
				}
			});
		}
	}

}
