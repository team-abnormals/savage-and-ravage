package com.teamabnormals.savage_and_ravage.common.item;

import com.teamabnormals.blueprint.core.util.item.filling.TargetedItemCategoryFiller;
import com.teamabnormals.savage_and_ravage.common.entity.projectile.IceChunk;
import com.teamabnormals.savage_and_ravage.core.registry.SRSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * @author Ocelot
 */
public class WandOfFreezingItem extends Item {
	private static final TargetedItemCategoryFiller FILLER = new TargetedItemCategoryFiller(() -> Items.TOTEM_OF_UNDYING);
	private static final double RAYTRACE_DISTANCE = 16;

	public WandOfFreezingItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		BlockHitResult result = (BlockHitResult) player.pick(RAYTRACE_DISTANCE, 1.0f, false);
		Vec3 startVec = player.getEyePosition(1.0f);
		Vec3 lookVec = player.getViewVector(1.0F);
		Vec3 endVec = startVec.add(lookVec.x * RAYTRACE_DISTANCE, lookVec.y * RAYTRACE_DISTANCE, lookVec.z * RAYTRACE_DISTANCE);
		if (result.getType() != HitResult.Type.MISS)
			endVec = result.getLocation();

		AABB axisalignedbb = player.getBoundingBox().expandTowards(lookVec.scale(RAYTRACE_DISTANCE)).inflate(1.0D, 1.0D, 1.0D);
		EntityHitResult entityraytraceresult = ProjectileUtil.getEntityHitResult(world, player, startVec, endVec, axisalignedbb, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());

		if (result.getType() != HitResult.Type.MISS || entityraytraceresult != null) {
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
			world.playSound(player, player.blockPosition(), SRSounds.ENTITY_PLAYER_CAST_SPELL.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

			player.getCooldowns().addCooldown(this, 20);
			if (!world.isClientSide()) {
				if (entityraytraceresult != null) {
					world.addFreshEntity(new IceChunk(world, player, entityraytraceresult.getEntity()));
				} else {
					BlockPos pos = result.getBlockPos();
					IceChunk iceChunk = new IceChunk(world, player, null);
					iceChunk.absMoveTo(pos.getX() + 0.5, pos.getY() + 1 + IceChunk.HOVER_DISTANCE, pos.getZ() + 0.5, iceChunk.getYRot(), iceChunk.getXRot());
					world.addFreshEntity(iceChunk);
				}
			}

			return InteractionResultHolder.success(stack);
		}

		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		FILLER.fillItem(this, group, items);
	}
}
