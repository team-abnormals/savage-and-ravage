package com.farcr.savageandravage.core.registry;

import com.farcr.savageandravage.client.render.CreepieRenderer;
import com.farcr.savageandravage.client.render.SkeletonVillagerRenderer;
import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.farcr.savageandravage.common.entity.SkeletonVillagerEntity;
import com.farcr.savageandravage.core.SavageAndRavage;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SREntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, SavageAndRavage.MODID);
    
    public static RegistryObject<EntityType<CreepieEntity>> CREEPIE = ENTITIES.register("creepie", () -> EntityType.Builder.<CreepieEntity>create(CreepieEntity::new, EntityClassification.MONSTER).size(0.51F, 1.02F).build("savageandravage:creepie"));
    public static RegistryObject<EntityType<SkeletonVillagerEntity>> SKELETON_VILLAGER = ENTITIES.register("skeleton_villager", () -> EntityType.Builder.<SkeletonVillagerEntity>create(SkeletonVillagerEntity::new, EntityClassification.MONSTER).size(0.6F, 1.99F).build("savageandravage:skeleton_villager"));
    
    @OnlyIn(Dist.CLIENT)
    public static void registerRendering() {
        RenderingRegistry.registerEntityRenderingHandler((EntityType<? extends CreepieEntity>)CREEPIE.get(), CreepieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler((EntityType<? extends SkeletonVillagerEntity>)SKELETON_VILLAGER.get(), SkeletonVillagerRenderer::new);
    }
}
