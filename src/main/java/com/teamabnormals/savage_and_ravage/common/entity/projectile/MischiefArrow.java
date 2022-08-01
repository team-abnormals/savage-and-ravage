package com.teamabnormals.savage_and_ravage.common.entity.projectile;

import com.teamabnormals.savage_and_ravage.common.entity.monster.Creepie;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import com.teamabnormals.savage_and_ravage.core.registry.SRItems;
import com.teamabnormals.savage_and_ravage.core.registry.SRParticleTypes;
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

public class MischiefArrow extends AbstractArrow {
	public boolean finished = false;

	public MischiefArrow(EntityType<? extends MischiefArrow> type, Level worldIn) {
		super(type, worldIn);
	}

	public MischiefArrow(Level worldIn, double x, double y, double z) {
		super(SREntityTypes.MISCHIEF_ARROW.get(), x, y, z, worldIn);
	}

	public MischiefArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(SREntityTypes.MISCHIEF_ARROW.get(), world);
	}

	public MischiefArrow(Level worldIn, LivingEntity shooter) {
		super(SREntityTypes.MISCHIEF_ARROW.get(), shooter, worldIn);
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!finished) {
			this.pickup = Pickup.DISALLOWED;

			Creepie creepie = SREntityTypes.CREEPIE.get().create(level);
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

			Creepie creepie = SREntityTypes.CREEPIE.get().create(level);
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
			this.level.addParticle(SRParticleTypes.CREEPER_SPORES.get(), this.getX(), this.getY(), this.getZ() - 0.0D, 0.0D, 0.0D, 0.0D);
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