package com.teamabnormals.savage_and_ravage.common.entity.projectile;

import com.teamabnormals.savage_and_ravage.common.block.RunedGloomyTilesBlock;
import com.teamabnormals.savage_and_ravage.common.entity.TracksHits;
import com.teamabnormals.savage_and_ravage.core.registry.SRBlocks;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import com.teamabnormals.savage_and_ravage.core.registry.SRMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class RunePrison extends Entity {
	private static final EntityDataAccessor<Integer> TICKS_TILL_REMOVE = SynchedEntityData.defineId(RunePrison.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Optional<BlockPos>> BLOCK_POS = SynchedEntityData.defineId(RunePrison.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
	private final boolean fromTrap;
	private UUID casterUUID = null;
	private int casterID = 0;
	private int currentFrame = 0;
	private boolean isBackwardsFrameCycle = false;

	public RunePrison(EntityType<? extends RunePrison> type, Level world) {
		super(type, world);
		this.fromTrap = false;
	}

	public RunePrison(Level world, BlockPos position, int ticksTillRemove, boolean fromTrap) {
		super(SREntityTypes.RUNE_PRISON.get(), world);
		this.setBlockPos(position);
		this.fromTrap = fromTrap;
		this.setTicksTillRemove(ticksTillRemove);
	}

	public <T extends Entity & TracksHits> RunePrison(Level world, BlockPos position, int ticksTillRemove, boolean fromTrap, T caster) {
		this(world, position, ticksTillRemove, fromTrap);
		this.setCaster(caster);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(BLOCK_POS, Optional.empty());
		this.entityData.define(TICKS_TILL_REMOVE, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
		if (compound.contains("GloomyTilePosition", 10))
			this.setBlockPos(NbtUtils.readBlockPos(compound.getCompound("GloomyTilePosition")));
		if (compound.hasUUID("CasterUUID"))
			this.casterUUID = compound.getUUID("CasterID");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("TicksTillRemove", this.getTicksTillRemove());
		if (this.getBlockPos() != null)
			compound.put("GloomyTilePosition", NbtUtils.writeBlockPos(this.getBlockPos()));
		if (this.casterUUID != null)
			compound.putUUID("CasterUUID", this.casterUUID);
	}

	@Nullable
	public TracksHits getCaster() {
		if (this.casterUUID != null && this.level instanceof ServerLevel)
			return (TracksHits) ((ServerLevel) this.level).getEntity(this.casterUUID);
		else return this.casterID != 0 ? (TracksHits) this.level.getEntity(this.casterID) : null;
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
				livingEntity.addEffect(new MobEffectInstance(SRMobEffects.WEIGHT.get(), 60, 2));
				if (this.getCaster() != null)
					this.getCaster().onTrackedHit(this, livingEntity);
			}
		}

		if (this.getTicksTillRemove() == 0) {
			this.discard();

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
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
