package com.minecraftabnormals.savageandravage.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.registry.SoundSubRegistryHelper;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SavageAndRavage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SRSounds {
    public static final SoundSubRegistryHelper HELPER = SavageAndRavage.REGISTRY_HELPER.getSoundSubHelper();

    public static final RegistryObject<SoundEvent> ENTITY_CREEPIE_HURT = HELPER.createSoundEvent("entity.creepie.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_CREEPIE_DEATH = HELPER.createSoundEvent("entity.creepie.death");
    public static final RegistryObject<SoundEvent> ENTITY_CREEPIE_PRIMED = HELPER.createSoundEvent("entity.creepie.primed");
    public static final RegistryObject<SoundEvent> ENTITY_CREEPIE_CONVERT = HELPER.createSoundEvent("entity.creepie.convert");
    public static final RegistryObject<SoundEvent> ENTITY_CREEPIE_GROW = HELPER.createSoundEvent("entity.creepie.grow");
    public static final RegistryObject<SoundEvent> ENTITY_ICEOLOGER_AMBIENT = HELPER.createSoundEvent("entity.iceologer.ambient");
    public static final RegistryObject<SoundEvent> ENTITY_ICEOLOGER_HURT = HELPER.createSoundEvent("entity.iceologer.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_ICEOLOGER_DEATH = HELPER.createSoundEvent("entity.iceologer.death");
    public static final RegistryObject<SoundEvent> ENTITY_ICEOLOGER_CELEBRATE = HELPER.createSoundEvent("entity.iceologer.celebrate");
    public static final RegistryObject<SoundEvent> ENTITY_ICEOLOGER_CAST_SPELL = HELPER.createSoundEvent("entity.iceologer.cast_spell");
    public static final RegistryObject<SoundEvent> ENTITY_PLAYER_CAST_SPELL = HELPER.createSoundEvent("entity.player.cast_spell");
    public static final RegistryObject<SoundEvent> ENTITY_CREEPER_SPORES_THROW = HELPER.createSoundEvent("entity.creeper_spores.throw");
    public static final RegistryObject<SoundEvent> ENTITY_GENERIC_GROWTH_SUCCESS = HELPER.createSoundEvent("entity.generic.growth_success");
    public static final RegistryObject<SoundEvent> ENTITY_GENERIC_GROWTH_FAILURE = HELPER.createSoundEvent("entity.generic.growth_failure");
    public static final RegistryObject<SoundEvent> BLOCK_RUNED_GLOOMY_TILES_ACTIVATE = HELPER.createSoundEvent("block.runed_gloomy_tiles.activate");
}
