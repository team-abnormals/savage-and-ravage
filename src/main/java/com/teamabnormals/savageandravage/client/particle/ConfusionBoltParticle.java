package com.teamabnormals.savageandravage.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ConfusionBoltParticle extends TextureSheetParticle {
	private static final Random RANDOM = new Random();
	private final SpriteSet sprites;

	private ConfusionBoltParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
		super(world, x, y, z, 0.5D - RANDOM.nextDouble(), ySpeed, 0.5D - RANDOM.nextDouble());
		this.yd = ySpeed;
		this.sprites = sprite;
		if (xSpeed == 0.0D && zSpeed == 0.0D) {
			this.xd *= 0.1F;
			this.zd *= 0.1F;
		}

		this.quadSize *= 0.75F;
		this.lifetime = (int) (1.0D / (Math.random() * 0.8D + 0.2D));
		this.hasPhysics = false;
		this.setSpriteFromAge(sprite);
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.setSpriteFromAge(this.sprites);
			this.move(this.xd, this.yd, this.zd);
			if (this.y == this.yo) {
				this.xd *= 1.1D;
				this.zd *= 1.1D;
			}

			this.xd *= 0.96F;
			this.zd *= 0.96F;
			if (this.onGround) {
				this.xd *= 0.7F;
				this.zd *= 0.7F;
			}

		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprite) {
			this.sprite = sprite;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ConfusionBoltParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
		}
	}

}
