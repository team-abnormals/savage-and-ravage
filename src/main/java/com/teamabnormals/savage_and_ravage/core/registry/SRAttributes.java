package com.teamabnormals.savage_and_ravage.core.registry;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SRAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, SavageAndRavage.MOD_ID);

	public static final RegistryObject<Attribute> EXPLOSIVE_DAMAGE_REDUCTION = ATTRIBUTES.register("explosive_damage_reduction", () -> new RangedAttribute("attribute.name.generic.explosive_damage_reduction", 0.0D, 0.0D, 1.0D));
}
