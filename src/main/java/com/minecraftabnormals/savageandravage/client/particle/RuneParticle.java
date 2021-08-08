package com.minecraftabnormals.savageandravage.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;

public class RuneParticle extends SpriteTexturedParticle {

	public RuneParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.xd *= 0.01;
		this.yd *= 0.01;
		this.zd *= 0.01;
		this.xd += xSpeed;
		this.yd += ySpeed;
		this.zd += zSpeed;
		float f1 = 1.0F - (float) (Math.random() * (double) 0.3F);
		this.rCol = f1;
		this.gCol = f1;
		this.bCol = f1;
		this.quadSize *= 1.5;
		int i = (int) (8.0D / (Math.random() * 0.8D + 0.3D));
		this.lifetime = (int) Math.max((float) i * 2.5F, 1.0F);
		this.hasPhysics = false;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public float getQuadSize(float partialTicks) {
		return this.quadSize * MathHelper.clamp(((float) this.age + partialTicks) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.98F;
			this.yd *= 0.98F;
			this.zd *= 0.98F;
		}
	}

	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			RuneParticle rune = new RuneParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
			rune.pickSprite(this.spriteSet);
			return rune;
		}
	}
}
