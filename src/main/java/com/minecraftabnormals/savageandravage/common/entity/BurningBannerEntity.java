package com.minecraftabnormals.savageandravage.common.entity;

import com.minecraftabnormals.savageandravage.core.SRConfig;
import com.minecraftabnormals.savageandravage.core.registry.SREntities;
import com.minecraftabnormals.savageandravage.core.registry.SRTriggers;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BurningBannerEntity extends Entity implements IEntityAdditionalSpawnData {

	public static final DataParameter<Integer> TICKS_TILL_REMOVE = EntityDataManager.defineId(BurningBannerEntity.class, DataSerializers.INT);
	public static final DataParameter<Optional<BlockPos>> BLOCK_POS = EntityDataManager.defineId(BurningBannerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
	public static final DataParameter<Optional<UUID>> OFFENDER_UUID = EntityDataManager.defineId(BurningBannerEntity.class, DataSerializers.OPTIONAL_UUID);

	private static final double[] ROTATED_VERTICES = new double[3];
	private AxisAlignedBB burningBox;
	private double burningBoxRotation;

	public BurningBannerEntity(EntityType<? extends BurningBannerEntity> type, World world) {
		super(type, world);
		this.burningBox = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
		this.burningBoxRotation = 0;
	}

	public BurningBannerEntity(World world, BlockPos pos, @Nullable PlayerEntity player) {
		super(SREntities.BURNING_BANNER.get(), world);
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
			this.remove();
			return;
		} else if (ticksRemaining > 10 && !(this.level.getBlockEntity(bannerPos) instanceof BannerTileEntity)) {
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
				this.playSound(this.level.getBlockEntity(bannerPos) instanceof BannerTileEntity ? SoundEvents.FIRE_AMBIENT : SoundEvents.FIRE_EXTINGUISH, 2F, this.level.getRandom().nextFloat() * 0.4F + 0.8F);
			} else if (this.getTicksTillRemove() == 10) {
				this.playSound(SoundEvents.FIRE_EXTINGUISH, 2F, this.level.getRandom().nextFloat() * 0.4F + 0.8F);
				PlayerEntity offender = this.getOffender();
				if (offender != null && isOminousBanner(this.level, bannerPos) && ((ServerWorld) this.level).getRaidAt(bannerPos) == null) {
					SRTriggers.BURN_OMINOUS_BANNER.trigger((ServerPlayerEntity) offender);
					if (SRConfig.COMMON.noBadOmenOnDeath.get() && !this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
						EffectInstance effect = offender.getEffect(Effects.BAD_OMEN);
						if (effect != null)
							offender.removeEffectNoUpdate(Effects.BAD_OMEN);
						offender.addEffect(new EffectInstance(Effects.BAD_OMEN, 120000, MathHelper.clamp(effect == null ? 0 : (effect.getAmplifier() + 1), 0, 4), false, false, true));
					}
				}
				this.level.removeBlock(bannerPos, false);
			}
		}
	}

	public void extinguishFire() {
		this.playSound(SoundEvents.FIRE_EXTINGUISH, 0.5F, this.level.getRandom().nextFloat() * 0.4F + 0.8F);
		this.remove();
	}

	@Override
	public void refreshDimensions() {
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		compound.putInt("Size", 0);
		compound.putInt("TicksTillRemove", this.getTicksTillRemove());
		if (this.getBannerPosition() != null)
			compound.put("BannerPosition", NBTUtil.writeBlockPos(this.getBannerPosition()));
		if (this.getOffenderId() != null)
			compound.putUUID("Offender", this.getOffenderId());
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		this.setTicksTillRemove(compound.getInt("TicksTillRemove"));
		if (compound.contains("BannerPosition", 10))
			this.setBannerPosition(NBTUtil.readBlockPos(compound.getCompound("BannerPosition")));
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
	public PlayerEntity getOffender() {
		UUID uuid = this.getOffenderId();
		return uuid == null ? null : this.level.getPlayerByUUID(uuid);
	}

	public AxisAlignedBB getBurningBox() {
		return burningBox;
	}

	public double getBurningBoxRotation() {
		return burningBoxRotation;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public static boolean isOminousBanner(IBlockReader world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof BannerTileEntity) {
			BannerTileEntity banner = (BannerTileEntity) Objects.requireNonNull(world.getBlockEntity(pos));
			if (banner.getName() instanceof TranslationTextComponent) {
				return ((TranslationTextComponent) banner.getName()).getKey().contains("block.minecraft.ominous_banner");
			}
		}
		return false;
	}

	public static AxisAlignedBB getBurningBox(BlockState state) {
		return state.hasProperty(WallBannerBlock.FACING) ? new AxisAlignedBB(1 * 0.0625, -13.5 * 0.0625, 1.5 * 0.0625, 15 * 0.0625, 14 * 0.0625, 4 * 0.0625) : new AxisAlignedBB(1 * 0.0625, 2.5 * 0.0625, 8.5 * 0.0625, 15 * 0.0625, 29.5 * 0.0625, 11 * 0.0625);
	}

	public static double getBurningBoxRotation(BlockState state) {
		Direction direction = state.hasProperty(WallBannerBlock.FACING) ? state.getValue(WallBannerBlock.FACING) : null;
		return direction != null ? Math.toRadians(direction.toYRot()) : (float) (state.getValue(BannerBlock.ROTATION) / 8.0 * Math.PI);
	}

	public static double[] rotate(double x, double y, double z, double angle) {
		float cos = MathHelper.cos((float) angle);
		float sin = MathHelper.sin((float) angle);
		ROTATED_VERTICES[0] = 0.5 + (x - 0.5) * cos - (z - 0.5) * sin;
		ROTATED_VERTICES[1] = y;
		ROTATED_VERTICES[2] = 0.5 + (x - 0.5) * sin + (z - 0.5) * cos;
		return ROTATED_VERTICES;
	}

	@Override
	public void writeSpawnData(PacketBuffer buf) {
		BlockPos bannerPos = this.getBannerPosition();
		buf.writeBoolean(bannerPos != null);
		if (bannerPos != null)
			buf.writeBlockPos(this.getBannerPosition());
	}

	@Override
	public void readSpawnData(PacketBuffer buf) {
		BlockPos bannerPos = buf.readBoolean() ? buf.readBlockPos() : null;
		if (bannerPos != null) {
			BlockState state = this.level.getBlockState(bannerPos);
			this.burningBox = getBurningBox(state);
			this.burningBoxRotation = getBurningBoxRotation(state);
		}
	}

}

