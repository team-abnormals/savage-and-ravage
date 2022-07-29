package com.teamabnormals.savage_and_ravage.common.entity.decoration;

import com.teamabnormals.savage_and_ravage.core.SRConfig;
import com.teamabnormals.savage_and_ravage.core.registry.SRCriteriaTriggers;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BurningBanner extends Entity implements IEntityAdditionalSpawnData {

	public static final EntityDataAccessor<Integer> TICKS_TILL_REMOVE = SynchedEntityData.defineId(BurningBanner.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Optional<BlockPos>> BLOCK_POS = SynchedEntityData.defineId(BurningBanner.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
	public static final EntityDataAccessor<Optional<UUID>> OFFENDER_UUID = SynchedEntityData.defineId(BurningBanner.class, EntityDataSerializers.OPTIONAL_UUID);

	private static final double[] ROTATED_VERTICES = new double[3];
	private AABB burningBox;
	private double burningBoxRotation;

	public BurningBanner(EntityType<? extends BurningBanner> type, Level world) {
		super(type, world);
		this.burningBox = new AABB(0, 0, 0, 1, 1, 1);
		this.burningBoxRotation = 0;
	}

	public BurningBanner(Level world, BlockPos pos, @Nullable Player player) {
		super(SREntityTypes.BURNING_BANNER.get(), world);
		this.setBannerPosition(pos);
		if (player != null) this.setOffenderId(player.getUUID());
		BlockState state = world.getBlockState(pos);
		this.setPos(pos.getX() + 0.5, pos.getY() - (state.getBlock() instanceof WallBannerBlock ? 1 : 0), pos.getZ() + 0.5);
		this.burningBox = getBurningBox(state);
		this.burningBoxRotation = getBurningBoxRotation(state);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(BLOCK_POS, Optional.empty());
		this.entityData.define(OFFENDER_UUID, Optional.empty());
		this.entityData.define(TICKS_TILL_REMOVE, 110);
	}

	@Override
	public void tick() {
		this.setTicksTillRemove(this.getTicksTillRemove() - 1);
		int ticksRemaining = this.getTicksTillRemove();

		BlockPos bannerPos = this.getBannerPosition();
		if (bannerPos == null || ticksRemaining <= 0) {
			this.discard();
			return;
		} else if (ticksRemaining > 10 && !(this.level.getBlockEntity(bannerPos) instanceof BannerBlockEntity)) {
			extinguishFire();
			return;
		}

		if (this.level.isClientSide()) {
			for (int i = 0; i < 5; i++) {
				double randomPositionX = this.burningBox.min(Direction.Axis.X) + (this.level.getRandom().nextFloat() * this.burningBox.getXsize());
				double randomPositionY = this.burningBox.min(Direction.Axis.Y) + (this.level.getRandom().nextFloat() * this.burningBox.getYsize());
				double randomPositionZ = this.burningBox.min(Direction.Axis.Z) + (this.level.getRandom().nextFloat() * this.burningBox.getZsize());
				double[] rotatedPosition = rotate(randomPositionX, randomPositionY, randomPositionZ, this.burningBoxRotation);
				randomPositionX = bannerPos.getX() + rotatedPosition[0];
				randomPositionY = bannerPos.getY() + rotatedPosition[1];
				randomPositionZ = bannerPos.getZ() + rotatedPosition[2];

				if (ticksRemaining > 10) {
					if (this.random.nextInt(5) == 2)
						this.level.addParticle(ParticleTypes.FLAME, randomPositionX, randomPositionY, randomPositionZ, 0.0D, 0.0D, 0.0D);
					if (this.random.nextInt(5) == 3)
						this.level.addParticle(ParticleTypes.LAVA, randomPositionX, randomPositionY, randomPositionZ, 0.0D, 0.0D, 0.0D);
				} else {
					this.level.addParticle(ParticleTypes.LARGE_SMOKE, randomPositionX, randomPositionY, randomPositionZ, 0.0D, 0.0D, 0.0D);
				}
			}
		} else {
			if (this.getTicksTillRemove() > 10) {
				this.playSound(this.level.getBlockEntity(bannerPos) instanceof BannerBlockEntity ? SoundEvents.FIRE_AMBIENT : SoundEvents.FIRE_EXTINGUISH, 2F, this.level.getRandom().nextFloat() * 0.4F + 0.8F);
			} else if (this.getTicksTillRemove() == 10) {
				this.playSound(SoundEvents.FIRE_EXTINGUISH, 2F, this.level.getRandom().nextFloat() * 0.4F + 0.8F);
				Player offender = this.getOffender();
				if (offender != null && isOminousBanner(this.level, bannerPos) && ((ServerLevel) this.level).getRaidAt(bannerPos) == null) {
					SRCriteriaTriggers.BURN_OMINOUS_BANNER.trigger((ServerPlayer) offender);
					if (SRConfig.COMMON.noBadOmenOnDeath.get() && !this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
						MobEffectInstance effect = offender.getEffect(MobEffects.BAD_OMEN);
						if (effect != null)
							offender.removeEffectNoUpdate(MobEffects.BAD_OMEN);
						offender.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 120000, Mth.clamp(effect == null ? 0 : (effect.getAmplifier() + 1), 0, 4), false, false, true));
					}
				}
				this.level.removeBlock(bannerPos, false);
			}
		}
	}

	public void extinguishFire() {
		this.playSound(SoundEvents.FIRE_EXTINGUISH, 0.5F, this.level.getRandom().nextFloat() * 0.4F + 0.8F);
		this.discard();
	}

	@Override
	public void refreshDimensions() {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("Size", 0);
		compound.putInt("TicksTillRemove", this.getTicksTillRemove());
		if (this.getBannerPosition() != null)
			compound.put("BannerPosition", NbtUtils.writeBlockPos(this.getBannerPosition()));
		if (this.getOffenderId() != null)
			compound.putUUID("Offender", this.getOffenderId());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
		if (compound.contains("BannerPosition", 10))
			this.setBannerPosition(NbtUtils.readBlockPos(compound.getCompound("BannerPosition")));
		if (compound.hasUUID("Offender"))
			this.setOffenderId(compound.getUUID("Offender"));
	}

	public int getTicksTillRemove() {
		return this.entityData.get(TICKS_TILL_REMOVE);
	}

	public void setTicksTillRemove(int tickCount) {
		this.entityData.set(TICKS_TILL_REMOVE, tickCount);
	}

	@Nullable
	public BlockPos getBannerPosition() {
		return this.entityData.get(BLOCK_POS).orElse(null);
	}

	private void setBannerPosition(@Nullable BlockPos positionIn) {
		this.entityData.set(BLOCK_POS, Optional.ofNullable(positionIn));
	}

	@Nullable
	public UUID getOffenderId() {
		return this.entityData.get(OFFENDER_UUID).orElse(null);
	}

	public void setOffenderId(@Nullable UUID ownerId) {
		this.entityData.set(OFFENDER_UUID, Optional.ofNullable(ownerId));
	}

	@Nullable
	public Player getOffender() {
		UUID uuid = this.getOffenderId();
		return uuid == null ? null : this.level.getPlayerByUUID(uuid);
	}

	public AABB getBurningBox() {
		return burningBox;
	}

	public double getBurningBoxRotation() {
		return burningBoxRotation;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public static boolean isOminousBanner(BlockGetter world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof BannerBlockEntity) {
			BannerBlockEntity banner = (BannerBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos));
			if (banner.getName().getContents() instanceof TranslatableContents) {
				return ((TranslatableContents) banner.getName().getContents()).getKey().contains("block.minecraft.ominous_banner");
			}
		}
		return false;
	}

	public static AABB getBurningBox(BlockState state) {
		return state.hasProperty(WallBannerBlock.FACING) ? new AABB(1 * 0.0625, -13.5 * 0.0625, 1.5 * 0.0625, 15 * 0.0625, 14 * 0.0625, 4 * 0.0625) : new AABB(1 * 0.0625, 2.5 * 0.0625, 8.5 * 0.0625, 15 * 0.0625, 29.5 * 0.0625, 11 * 0.0625);
	}

	public static double getBurningBoxRotation(BlockState state) {
		Direction direction = state.hasProperty(WallBannerBlock.FACING) ? state.getValue(WallBannerBlock.FACING) : null;
		return direction != null ? Math.toRadians(direction.toYRot()) : (float) (state.getValue(BannerBlock.ROTATION) / 8.0 * Math.PI);
	}

	public static double[] rotate(double x, double y, double z, double angle) {
		float cos = Mth.cos((float) angle);
		float sin = Mth.sin((float) angle);
		ROTATED_VERTICES[0] = 0.5 + (x - 0.5) * cos - (z - 0.5) * sin;
		ROTATED_VERTICES[1] = y;
		ROTATED_VERTICES[2] = 0.5 + (x - 0.5) * sin + (z - 0.5) * cos;
		return ROTATED_VERTICES;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buf) {
		BlockPos bannerPos = this.getBannerPosition();
		buf.writeBoolean(bannerPos != null);
		if (bannerPos != null)
			buf.writeBlockPos(this.getBannerPosition());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buf) {
		BlockPos bannerPos = buf.readBoolean() ? buf.readBlockPos() : null;
		if (bannerPos != null) {
			BlockState state = this.level.getBlockState(bannerPos);
			this.burningBox = getBurningBox(state);
			this.burningBoxRotation = getBurningBoxRotation(state);
		}
	}

}

