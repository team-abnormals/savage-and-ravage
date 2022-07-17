package com.teamabnormals.savage_and_ravage.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class CreeperSporesParticle extends TextureSheetParticle {
	private final SpriteSet sprites;

	public CreeperSporesParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.sprites = sprites;
		this.xd *= 0.1;
		this.yd *= 0.1;
		this.zd *= 0.1;
		this.xd += xSpeed;
		this.yd += ySpeed;
		this.zd += zSpeed;
		float f1 = 1.0F - (float) (Math.random() * (double) 0.3F);
		this.rCol = f1;
		this.gCol = f1;
		this.bCol = f1;
		this.quadSize *= 1.875F;
		int i = (int) (8.0D / (Math.random() * 0.8D + 0.3D));
		this.lifetime = (int) Math.max((float) i * 2.5F, 1.0F);
		this.hasPhysics = false;
		this.setSpriteFromAge(sprites);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public float getQuadSize(float partialTicks) {
		return this.quadSize * Mth.clamp(((float) this.age + partialTicks) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.setSpriteFromAge(this.sprites);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.96F;
			this.yd *= 0.96F;
			this.zd *= 0.96F;

			if (this.onGround) {
				this.xd *= 0.7F;
				this.zd *= 0.7F;
			}

		}
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new CreeperSporesParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}
}