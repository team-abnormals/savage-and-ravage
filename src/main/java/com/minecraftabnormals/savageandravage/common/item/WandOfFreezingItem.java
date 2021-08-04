package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.abnormals_core.core.util.item.filling.TargetedItemGroupFiller;
import com.minecraftabnormals.savageandravage.common.entity.IceChunkEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * @author Ocelot
 */
public class WandOfFreezingItem extends Item {
	private static final TargetedItemGroupFiller FILLER = new TargetedItemGroupFiller(() -> Items.TOTEM_OF_UNDYING);
	private static final double RAYTRACE_DISTANCE = 16;

	public WandOfFreezingItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		BlockRayTraceResult result = (BlockRayTraceResult) player.pick(RAYTRACE_DISTANCE, 1.0f, false);
		Vector3d startVec = player.getEyePosition(1.0f);
		Vector3d lookVec = player.getViewVector(1.0F);
		Vector3d endVec = startVec.add(lookVec.x * RAYTRACE_DISTANCE, lookVec.y * RAYTRACE_DISTANCE, lookVec.z * RAYTRACE_DISTANCE);
		if (result.getType() != RayTraceResult.Type.MISS)
			endVec = result.getLocation();

		AxisAlignedBB axisalignedbb = player.getBoundingBox().expandTowards(lookVec.scale(RAYTRACE_DISTANCE)).inflate(1.0D, 1.0D, 1.0D);
		EntityRayTraceResult entityraytraceresult = ProjectileHelper.getEntityHitResult(world, player, startVec, endVec, axisalignedbb, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());

		if (result.getType() != RayTraceResult.Type.MISS || entityraytraceresult != null) {
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
			world.playSound(player, player.blockPosition(), SRSounds.ENTITY_PLAYER_CAST_SPELL.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);

			player.getCooldowns().addCooldown(this, 20);
			if (!world.isClientSide()) {
				if (entityraytraceresult != null) {
					world.addFreshEntity(new IceChunkEntity(world, player, entityraytraceresult.getEntity()));
				} else {
					BlockPos pos = result.getBlockPos();
					IceChunkEntity iceChunk = new IceChunkEntity(world, player, null);
					iceChunk.absMoveTo(pos.getX() + 0.5, pos.getY() + 1 + IceChunkEntity.HOVER_DISTANCE, pos.getZ() + 0.5, iceChunk.yRot, iceChunk.xRot);
					world.addFreshEntity(iceChunk);
				}
			}

			return ActionResult.success(stack);
		}

		return ActionResult.pass(stack);
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
