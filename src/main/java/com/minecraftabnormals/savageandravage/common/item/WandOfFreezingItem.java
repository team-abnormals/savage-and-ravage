package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.savageandravage.common.entity.IceChunkEntity;
import com.minecraftabnormals.savageandravage.core.registry.SRSounds;
import com.teamabnormals.abnormals_core.core.utils.ItemStackUtils;

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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * @author Ocelot
 */
public class WandOfFreezingItem extends Item {

    private static final double RAYTRACE_DISTANCE = 16;

    public WandOfFreezingItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        BlockRayTraceResult result = (BlockRayTraceResult) player.pick(RAYTRACE_DISTANCE, 1.0f, false);
        Vector3d startVec = player.getEyePosition(1.0f);
        Vector3d lookVec = player.getLook(1.0F);
        Vector3d endVec = startVec.add(lookVec.x * RAYTRACE_DISTANCE, lookVec.y * RAYTRACE_DISTANCE, lookVec.z * RAYTRACE_DISTANCE);
        if (result.getType() != RayTraceResult.Type.MISS)
            endVec = result.getHitVec();

        AxisAlignedBB axisalignedbb = player.getBoundingBox().expand(lookVec.scale(RAYTRACE_DISTANCE)).grow(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(world, player, startVec, endVec, axisalignedbb, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.canBeCollidedWith());

        if (result.getType() != RayTraceResult.Type.MISS || entityraytraceresult != null) {
            stack.damageItem(1, player, p -> p.sendBreakAnimation(hand));
            world.playSound(player, player.getPosition(), SRSounds.ENTITY_PLAYER_CAST_SPELL.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);

            player.getCooldownTracker().setCooldown(this, 20);
            if (!world.isRemote()) {
                if (entityraytraceresult != null) {
                    world.addEntity(new IceChunkEntity(world, player, entityraytraceresult.getEntity()));
                } else {
                    BlockPos pos = result.getPos();
                    IceChunkEntity iceChunk = new IceChunkEntity(world, player, null);
                    iceChunk.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 1 + IceChunkEntity.HOVER_DISTANCE, pos.getZ() + 0.5, iceChunk.rotationYaw, iceChunk.rotationPitch);
                    world.addEntity(iceChunk);
                }
            }

            return ActionResult.resultSuccess(stack);
        }

        return ActionResult.resultPass(stack);
    }
    
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        ItemStackUtils.fillAfterItemForGroup(this.asItem(), Items.TOTEM_OF_UNDYING, group, items);
    }
}
