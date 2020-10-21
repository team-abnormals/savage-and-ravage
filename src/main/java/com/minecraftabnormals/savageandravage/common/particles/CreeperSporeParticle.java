package com.minecraftabnormals.savageandravage.common.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;

public class CreeperSporeParticle extends SpriteTexturedParticle {
    private final IAnimatedSprite field_217583_C;

    public CreeperSporeParticle(ClientWorld p_i51015_1_, double p_i51015_2_, double p_i51015_4_, double p_i51015_6_, double p_i51015_8_, double p_i51015_10_, double p_i51015_12_, IAnimatedSprite p_i51015_14_) {
        super(p_i51015_1_, p_i51015_2_, p_i51015_4_, p_i51015_6_, 0.0D, 0.0D, 0.0D);
        this.field_217583_C = p_i51015_14_;
        this.motionX *= 0.1;
        this.motionY *= 0.1;
        this.motionZ *= 0.1;
        this.motionX += p_i51015_8_;
        this.motionY += p_i51015_10_;
        this.motionZ += p_i51015_12_;
        float f1 = 1.0F - (float) (Math.random() * (double) 0.3F);
        this.particleRed = f1;
        this.particleGreen = f1;
        this.particleBlue = f1;
        this.particleScale *= 1.875F;
        int i = (int) (8.0D / (Math.random() * 0.8D + 0.3D));
        this.maxAge = (int) Math.max((float) i * 2.5F, 1.0F);
        this.canCollide = false;
        this.selectSpriteWithAge(p_i51015_14_);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getScale(float partialTicks) {
        return this.particleScale * MathHelper.clamp(((float) this.age + partialTicks) / (float) this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.selectSpriteWithAge(this.field_217583_C);
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.96F;
            this.motionY *= 0.96F;
            this.motionZ *= 0.96F;
            PlayerEntity playerentity = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 2.0D, false);
            if (playerentity != null) {
                double d0 = playerentity.getPosY();
                if (this.posY > d0) {
                    this.posY += (d0 - this.posY) * 0.2D;
                    this.motionY += (playerentity.getMotion().y - this.motionY) * 0.2D;
                    this.setPosition(this.posX, this.posY, this.posZ);
                }
            }

            if (this.onGround) {
                this.motionX *= 0.7F;
                this.motionZ *= 0.7F;
            }

        }
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new CreeperSporeParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}