package com.teamabnormals.savage_and_ravage.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class CreeperSporeSprinklesParticle extends TextureSheetParticle {

	protected CreeperSporeSprinklesParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.setSpriteFromAge(sprite);
		this.gravity = 1.0F;
		this.quadSize *= 1.875F;
		this.xd *= 2.5F;
		this.yd *= 2.5F;
		this.zd *= 2.5F;
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprite) {
			this.sprite = sprite;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new CreeperSporeSprinklesParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
		}
	}
}