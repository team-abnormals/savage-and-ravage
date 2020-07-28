package com.minecraftabnormals.savageandravage.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.util.EntityPredicates;

@Mixin(ZoglinEntity.class)
public class ZoglinMixin {
	@Overwrite(remap = true)
	private static boolean func_234337_j_(LivingEntity entity) {
		 EntityType<?> entitytype = entity.getType();
	     return entitytype != EntityType.ZOGLIN && EntityPredicates.field_233583_f_.test(entity);
	}

}
