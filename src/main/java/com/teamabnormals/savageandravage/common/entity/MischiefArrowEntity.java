package com.teamabnormals.savageandravage.common.entity;

import com.teamabnormals.savageandravage.core.registry.SREntities;
import com.teamabnormals.savageandravage.core.registry.SRItems;
import com.teamabnormals.savageandravage.core.registry.SRParticles;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class MischiefArrowEntity extends AbstractArrow {
	public boolean finished = false;

	public MischiefArrowEntity(EntityType<? extends MischiefArrowEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	public MischiefArrowEntity(Level worldIn, double x, double y, double z) {
		super(SREntities.MISCHIEF_ARROW.get(), x, y, z, worldIn);
	}

	public MischiefArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(SREntities.MISCHIEF_ARROW.get(), world);
	}

	public MischiefArrowEntity(Level worldIn, LivingEntity shooter) {
		super(SREntities.MISCHIEF_ARROW.get(), shooter, worldIn);
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!finished) {
			this.pickup = Pickup.DISALLOWED;

			CreepieEntity creepie = SREntities.CREEPIE.get().create(level);
			if (creepie != null) {
				creepie.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);

				Entity thrower = this.getOwner();
				if (thrower instanceof LivingEntity) {
					if (!thrower.isInvisible())
						creepie.setOwnerId(thrower.getUUID());
				}

				this.level.addFreshEntity(creepie);
			}
			this.finished = true;
		}
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!finished) {
			this.pickup = Pickup.DISALLOWED;

			CreepieEntity creepie = SREntities.CREEPIE.get().create(level);
			if (creepie != null) {
				creepie.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);

				Entity thrower = this.getOwner();
				if (thrower instanceof LivingEntity) {
					if (!thrower.isInvisible())
						creepie.setOwnerId(thrower.getUUID());
				}

				if (result instanceof EntityHitResult) {
					Entity hit = ((EntityHitResult) result).getEntity();
					if (hit instanceof LivingEntity)
						creepie.setTarget((LivingEntity) hit);
				}

				this.level.addFreshEntity(creepie);
			}

			this.finished = true;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.finished)
			this.level.addParticle(SRParticles.CREEPER_SPORES.get(), this.getX(), this.getY(), this.getZ() - 0.0D, 0.0D, 0.0D, 0.0D);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(SRItems.MISCHIEF_ARROW.get());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}