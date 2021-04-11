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
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

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
	protected void func_230299_a_(BlockRayTraceResult result) {
		super.func_230299_a_(result);
		if (!finished) {
			this.pickupStatus = PickupStatus.DISALLOWED;

			CreepieEntity creepie = SREntities.CREEPIE.get().create(world);
			if (creepie != null) {
				creepie.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);

				Entity thrower = this.func_234616_v_();
				if (thrower instanceof LivingEntity) {
					if (!((LivingEntity) thrower).isPotionActive(Effects.INVISIBILITY))
						creepie.setOwnerId(thrower.getUniqueID());
				}

				this.world.addEntity(creepie);
			}
			this.finished = true;
		}
	}

	@Override
	protected void onEntityHit(EntityRayTraceResult result) {
		super.onEntityHit(result);
		if (!finished) {
			CreepieEntity creepie = SREntities.CREEPIE.get().create(world);
			if (creepie == null)
				return;

			creepie.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);

			Entity thrower = this.func_234616_v_();
			if (thrower instanceof LivingEntity) {
				if (!((LivingEntity) thrower).isPotionActive(Effects.INVISIBILITY))
					creepie.setOwnerId(thrower.getUniqueID());
			}

			if (result.getEntity() instanceof LivingEntity) {
				creepie.setAttackTarget((LivingEntity) result.getEntity());
			}

			this.world.addEntity(creepie);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.finished)
			this.world.addParticle(SRParticles.CREEPER_SPORES.get(), this.getPosX(), this.getPosY(), this.getPosZ() - 0.0D, 0.0D, 0.0D, 0.0D);
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(SRItems.MISCHIEF_ARROW.get());
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}