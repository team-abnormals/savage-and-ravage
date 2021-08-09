
package com.minecraftabnormals.savageandravage.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class CreeperSporeSprinklesParticle extends SpriteTexturedParticle {

    protected CreeperSporeSprinklesParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite sprite) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setSpriteFromAge(sprite);
        this.gravity = 1.0F;
        this.quadSize *= 1.875F;
        this.xd *= 2.5F;
        this.yd *= 2.5F;
        this.zd *= 2.5F;
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new CreeperSporeSprinklesParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        }
    }
}