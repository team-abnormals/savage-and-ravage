package com.teamabnormals.savage_and_ravage.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class CleaverSweepParticle extends TextureSheetParticle {
	private static final float NINETY_DEGREES_IN_RADIANS = -1.5707964F;
	private final SpriteSet sprites;

	private CleaverSweepParticle(ClientLevel p_i232341_1_, double p_i232341_2_, double p_i232341_4_, double p_i232341_6_, double p_i232341_8_, SpriteSet p_i232341_10_) {
		super(p_i232341_1_, p_i232341_2_, p_i232341_4_, p_i232341_6_, 0.0D, 0.0D, 0.0D);
		this.sprites = p_i232341_10_;
		this.lifetime = 4;
		float f = this.random.nextFloat() * 0.6F + 0.4F;
		this.rCol = f;
		this.gCol = f;
		this.bCol = f;
		this.oRoll = NINETY_DEGREES_IN_RADIANS;
		this.roll = NINETY_DEGREES_IN_RADIANS;
		this.quadSize = 1.0F - (float) p_i232341_8_ * 0.5F;
		this.setSpriteFromAge(p_i232341_10_);
	}

	public int getLightColor(float p_189214_1_) {
		return 15728880;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.setSpriteFromAge(this.sprites);
		}
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_LIT;
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Factory(SpriteSet p_i50563_1_) {
			this.sprites = p_i50563_1_;
		}

		public Particle createParticle(SimpleParticleType p_199234_1_, ClientLevel p_199234_2_, double p_199234_3_, double p_199234_5_, double p_199234_7_, double p_199234_9_, double p_199234_11_, double p_199234_13_) {
			return new CleaverSweepParticle(p_199234_2_, p_199234_3_, p_199234_5_, p_199234_7_, p_199234_9_, this.sprites);
		}
	}
}
