package com.farcr.savageandravage.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IFlinging;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;

@Mixin(ZoglinEntity.class)
public class ZoglinMixin extends MonsterEntity implements IMob, IFlinging {
	@Shadow 
	public int field_234325_bu_;
	
	protected ZoglinMixin(EntityType<? extends MonsterEntity> entity, World world) {
		super(entity, world);
	}

	public int func_230290_eL_() {
		return field_234325_bu_;
	}
	
	@Overwrite(remap = true)
	private static boolean func_234337_j_(LivingEntity entity) {
		 EntityType<?> entitytype = entity.getType();
	     return entitytype != EntityType.ZOGLIN && EntityPredicates.field_233583_f_.test(entity);
	}

}
