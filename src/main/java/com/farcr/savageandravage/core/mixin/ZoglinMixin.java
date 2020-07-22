package com.farcr.savageandravage.core.registry.other.mixin;

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
public class ZoglinMixin extends MonsterEntity implements IMob, IFlinging
{

	@Shadow 
	public int field_234325_bu_;
	
	protected ZoglinMixin(EntityType<? extends MonsterEntity> p_i48553_1_, World p_i48553_2_) {
		super(p_i48553_1_, p_i48553_2_);
		// TODO Auto-generated constructor stub
	}

	public int func_230290_eL_() {
		return field_234325_bu_;
	}
	
	@Overwrite(remap = true)
	private static boolean func_234337_j_(LivingEntity p_234337_0_) 
	{
		 EntityType<?> entitytype = p_234337_0_.getType();
	     return entitytype != EntityType.ZOGLIN && EntityPredicates.field_233583_f_.test(p_234337_0_);
	}

}
