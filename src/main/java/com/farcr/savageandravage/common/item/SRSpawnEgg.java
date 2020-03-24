package com.farcr.savageandravage.common.item;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Supplier;

//Imported from Buzzier Bees
public class SRSpawnEgg extends SpawnEggItem {
    private Supplier<EntityType<?>> entity;

    public SRSpawnEgg(Supplier<EntityType<?>> entityTypeIn, int primaryColorIn, int secondaryColorIn, Item.Properties builder) {
        super(entityTypeIn.get(), primaryColorIn, secondaryColorIn, builder);
        entity = entityTypeIn;
    }

    @Override
    public EntityType<?> getType(CompoundNBT compound) {
        if (compound != null && compound.contains("EntityTag", 10)) {
            CompoundNBT entityTag = compound.getCompound("EntityTag");

            if (entityTag.contains("id", 8)) {
                return EntityType.byKey(entityTag.getString("id")).orElse(entity.get());
            }
        }
        return entity.get();
    }
}
