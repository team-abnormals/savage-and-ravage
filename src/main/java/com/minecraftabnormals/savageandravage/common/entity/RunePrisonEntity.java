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
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class RunePrisonEntity extends Entity {
	private static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.createKey(RunePrisonEntity.class, DataSerializers.VARINT);
	private static final DataParameter<Optional<BlockPos>> BLOCK_POS = EntityDataManager.createKey(RunePrisonEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private int currentFrame = 0;
	private boolean isBackwardsFrameCycle = false;

	public RunePrisonEntity(EntityType<? extends RunePrisonEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public RunePrisonEntity(World worldIn, BlockPos positionIn, int ticksTillRemove) {
		super(SREntities.RUNE_PRISON.get(), worldIn);
		this.setBlockPos(positionIn);
		this.setTicksTillRemove(ticksTillRemove);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(BLOCK_POS, Optional.empty());
		this.dataManager.register(TICKS_TILL_REMOVE, 0);
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
		if (compound.contains("GloomyTilePosition", 10)) {
			this.setBlockPos(NBTUtil.readBlockPos(compound.getCompound("GloomyTilePosition")));
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("TicksTillRemove", this.getTicksTillRemove());
		if (this.getBlockPos() != null) {
			compound.put("GloomyTilePosition", NBTUtil.writeBlockPos(this.getBlockPos()));
		}
	}

	public int getTicksTillRemove() {
		return this.dataManager.get(TICKS_TILL_REMOVE);
	}

	public void setTicksTillRemove(int tickCount) {
		this.dataManager.set(TICKS_TILL_REMOVE, tickCount);
	}

	@Nullable
	public BlockPos getBlockPos() {
		return this.dataManager.get(BLOCK_POS).orElse(null);
	}

	private void setBlockPos(@Nullable BlockPos positionIn) {
		this.dataManager.set(BLOCK_POS, Optional.ofNullable(positionIn));
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isRemote() && getTicksTillRemove() % 5 == 0) {
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

		if (getTicksTillRemove() > 0) {
			setTicksTillRemove(getTicksTillRemove() - 1);
		}

		List<LivingEntity> intersectingEntityList = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox());
		for (LivingEntity livingEntity : intersectingEntityList) {
			if (livingEntity.canBeHitWithPotion() && RunedGloomyTilesBlock.shouldTrigger(livingEntity)) {
				livingEntity.addPotionEffect(new EffectInstance(SREffects.WEIGHT.get(), 20, 2));
			}
		}

		if (this.getTicksTillRemove() == 0) {
			this.remove();

			BlockPos pos = this.getBlockPos();
			if (pos == null)
				return;

			if (this.world.getBlockState(pos).getBlock() instanceof RunedGloomyTilesBlock)
				this.world.setBlockState(pos, SRBlocks.GLOOMY_TILES.get().getDefaultState());
		}
	}

	public int getCurrentFrame() {
		return this.currentFrame;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
