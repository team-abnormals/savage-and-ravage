package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.common.block.RunedGloomyTilesBlock;
import com.minecraftabnormals.savageandravage.core.registry.SRBlocks;
import com.minecraftabnormals.savageandravage.core.registry.SREffects;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class RunePrisonEntity extends Entity {
	private static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.defineId(RunePrisonEntity.class, DataSerializers.INT);
	private static final DataParameter<Optional<BlockPos>> BLOCK_POS = EntityDataManager.defineId(RunePrisonEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private final boolean fromTrap;
	private UUID casterUUID = null;
	private int casterID = 0;
	private int currentFrame = 0;
	private boolean isBackwardsFrameCycle = false;

	public RunePrisonEntity(EntityType<? extends RunePrisonEntity> type, World world) {
		super(type, world);
		this.fromTrap = false;
	}

	public RunePrisonEntity(World world, BlockPos position, int ticksTillRemove, boolean fromTrap) {
		super(SREntities.RUNE_PRISON.get(), world);
		this.setBlockPos(position);
		this.fromTrap = fromTrap;
		this.setTicksTillRemove(ticksTillRemove);
	}

	public RunePrisonEntity(World world, BlockPos position, int ticksTillRemove, boolean fromTrap, ITracksHits caster) {
		this(world, position, ticksTillRemove, fromTrap);
		this.setCaster(caster.getThisEntity());
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(BLOCK_POS, Optional.empty());
		this.entityData.define(TICKS_TILL_REMOVE, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
		if (compound.contains("GloomyTilePosition", 10))
			this.setBlockPos(NBTUtil.readBlockPos(compound.getCompound("GloomyTilePosition")));
		if (compound.hasUUID("CasterUUID"))
			this.casterUUID = compound.getUUID("CasterID");
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		compound.putInt("TicksTillRemove", this.getTicksTillRemove());
		if (this.getBlockPos() != null)
			compound.put("GloomyTilePosition", NBTUtil.writeBlockPos(this.getBlockPos()));
		if (this.casterUUID != null)
			compound.putUUID("CasterUUID", this.casterUUID);
	}

	@Nullable
	public ITracksHits getCaster() {
		if (this.casterUUID != null && this.level instanceof ServerWorld)
			return (ITracksHits) ((ServerWorld) this.level).getEntity(this.casterUUID);
		else return this.casterID != 0 ? (ITracksHits) this.level.getEntity(this.casterID) : null;
	}

	public void setCaster(@Nullable Entity caster) {
		this.casterID = caster == null ? 0 : caster.getId();
		this.casterUUID = caster == null ? null : caster.getUUID();
	}

	public int getTicksTillRemove() {
		return this.entityData.get(TICKS_TILL_REMOVE);
	}

	public void setTicksTillRemove(int tickCount) {
		this.entityData.set(TICKS_TILL_REMOVE, tickCount);
	}

	@Nullable
	public BlockPos getBlockPos() {
		return this.entityData.get(BLOCK_POS).orElse(null);
	}

	private void setBlockPos(@Nullable BlockPos positionIn) {
		this.entityData.set(BLOCK_POS, Optional.ofNullable(positionIn));
	}

	@Override
	public void tick() {
		super.tick();

		if (level.isClientSide() && getTicksTillRemove() % 5 == 0) {
			if (!isBackwardsFrameCycle) {
				currentFrame++;
				if (currentFrame == 4) {
					isBackwardsFrameCycle = true;
				}
			} else {
				currentFrame--;
				if (currentFrame == 0) {
					isBackwardsFrameCycle = false;
				}
			}
		}

		if (getTicksTillRemove() > 0)
			setTicksTillRemove(getTicksTillRemove() - 1);

		for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
			if (livingEntity.isAffectedByPotions()) {
				livingEntity.addEffect(new EffectInstance(SREffects.WEIGHT.get(), 60, 2));
				if (this.getCaster() != null)
					this.getCaster().onTrackedHit(this, livingEntity);
			}
		}

		if (this.getTicksTillRemove() == 0) {
			this.remove();

			BlockPos pos = this.getBlockPos();
			if (pos != null && this.fromTrap) {
				if (this.level.getBlockState(pos).getBlock() instanceof RunedGloomyTilesBlock)
					this.level.setBlockAndUpdate(pos, SRBlocks.GLOOMY_TILES.get().defaultBlockState());
			}
		}
	}

	public int getCurrentFrame() {
		return this.currentFrame;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
