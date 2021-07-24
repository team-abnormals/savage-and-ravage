package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import com.minecraftabnormals.savageandravage.core.registry.SRParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.entity.projectile.AbstractArrowEntity.PickupStatus;

public class MischiefArrowEntity extends AbstractArrowEntity {
	public boolean finished = false;

	public MischiefArrowEntity(EntityType<? extends MischiefArrowEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public MischiefArrowEntity(World worldIn, double x, double y, double z) {
		super(SREntities.MISCHIEF_ARROW.get(), x, y, z, worldIn);
	}

	public MischiefArrowEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(SREntities.MISCHIEF_ARROW.get(), world);
	}

	public MischiefArrowEntity(World worldIn, LivingEntity shooter) {
		super(SREntities.MISCHIEF_ARROW.get(), shooter, worldIn);
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		if (!finished) {
			this.pickup = PickupStatus.DISALLOWED;

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
	protected void onHitEntity(EntityRayTraceResult result) {
		super.onHitEntity(result);
		if (!finished) {
			CreepieEntity creepie = SREntities.CREEPIE.get().create(level);
			if (creepie == null)
				return;

			creepie.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);

			Entity thrower = this.getOwner();
			if (thrower instanceof LivingEntity) {
				if (!thrower.isInvisible())
					creepie.setOwnerId(thrower.getUUID());
			}

			if (result.getEntity() instanceof LivingEntity) {
				creepie.setTarget((LivingEntity) result.getEntity());
			}

			this.level.addFreshEntity(creepie);
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
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}