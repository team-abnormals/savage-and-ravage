package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SRAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, SavageAndRavage.MODID);

    public static final RegistryObject<Attribute> EXPLOSIVE_DAMAGE_REDUCTION = ATTRIBUTES.register("explosive_damage_reduction", () -> new RangedAttribute("attribute.name.generic.explosive_damage_reduction", 0.0D, 0.0D, 1.0D));
}
