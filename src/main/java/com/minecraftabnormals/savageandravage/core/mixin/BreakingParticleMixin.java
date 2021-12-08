package com.minecraftabnormals.savageandravage.core.mixin;

import com.minecraftabnormals.savageandravage.core.registry.SRItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.BreakingParticle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BreakingParticle.class)
public abstract class BreakingParticleMixin extends SpriteTexturedParticle {

	protected BreakingParticleMixin(ClientWorld level, double p_i232447_2_, double p_i232447_4_, double p_i232447_6_) {
		super(level, p_i232447_2_, p_i232447_4_, p_i232447_6_);
	}

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDLnet/minecraft/item/ItemStack;)V")
	private void getModel(ClientWorld level, double p_i232348_2_, double p_i232348_4_, double p_i232348_6_, ItemStack stack, CallbackInfo ci) {
		if (stack.getItem() == SRItems.CLEAVER_OF_BEHEADING.get()) {
			IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(Items.AIR);
			if (model != null)
				this.setSprite(model.getParticleIcon());
		}
	}
}