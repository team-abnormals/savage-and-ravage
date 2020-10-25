package com.minecraftabnormals.savageandravage.common.item;

import com.minecraftabnormals.savageandravage.common.entity.IceologerIceChunkEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * @author Ocelot
 */
public class IceWandItem extends Item {

    private static final double RAYTRACE_DISTANCE = 16;

    public IceWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        RayTraceResult result = player.pick(RAYTRACE_DISTANCE, 1.0f, false);
        Vector3d vector3d = player.getEyePosition(1.0f);
        Vector3d vector3d1 = player.getLook(1.0F);
        Vector3d vector3d2 = vector3d.add(vector3d1.x * RAYTRACE_DISTANCE, vector3d1.y * RAYTRACE_DISTANCE, vector3d1.z * RAYTRACE_DISTANCE);
        AxisAlignedBB axisalignedbb = player.getBoundingBox().expand(vector3d1.scale(RAYTRACE_DISTANCE)).grow(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(world, player, vector3d, vector3d2, axisalignedbb, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.canBeCollidedWith());
        if (entityraytraceresult != null && result.getHitVec().squareDistanceTo(vector3d) > entityraytraceresult.getHitVec().squareDistanceTo(vector3d)) {
            player.getHeldItem(hand).damageItem(1, player, p -> p.sendBreakAnimation(hand));
            world.playSound(player, player.getPosition(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
            if (!world.isRemote()) {
                Entity entity = entityraytraceresult.getEntity();
                world.addEntity(new IceologerIceChunkEntity(world, player, entity));
            }
            return ActionResult.resultSuccess(player.getHeldItem(hand));
        }
        return ActionResult.resultPass(player.getHeldItem(hand));
    }
}
