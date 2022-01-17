package com.teamabnormals.savage_and_ravage.core.registry;

import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

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

	public static final RegistryObject<SoundEvent> ENTITY_TRICKSTER_AMBIENT = HELPER.createSoundEvent("entity.trickster.ambient");
	public static final RegistryObject<SoundEvent> ENTITY_TRICKSTER_STEP = HELPER.createSoundEvent("entity.trickster.step");
	public static final RegistryObject<SoundEvent> ENTITY_TRICKSTER_HURT = HELPER.createSoundEvent("entity.trickster.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_TRICKSTER_DEATH = HELPER.createSoundEvent("entity.trickster.death");
	public static final RegistryObject<SoundEvent> ENTITY_TRICKSTER_CELEBRATE = HELPER.createSoundEvent("entity.trickster.celebrate");
	public static final RegistryObject<SoundEvent> ENTITY_TRICKSTER_CAST_SPELL = HELPER.createSoundEvent("entity.trickster.cast_spell");
	public static final RegistryObject<SoundEvent> ENTITY_TRICKSTER_LAUGH = HELPER.createSoundEvent("entity.trickster.laugh");

	public static final RegistryObject<SoundEvent> ENTITY_PLAYER_CAST_SPELL = HELPER.createSoundEvent("entity.player.cast_spell");
	public static final RegistryObject<SoundEvent> ENTITY_CREEPER_SPORES_THROW = HELPER.createSoundEvent("entity.creeper_spores.throw");
	public static final RegistryObject<SoundEvent> GENERIC_PREPARE_ATTACK = HELPER.createSoundEvent("generic.prepare_attack");
	public static final RegistryObject<SoundEvent> GENERIC_PUFF_OF_SMOKE = HELPER.createSoundEvent("generic.puff_of_smoke");

	public static final RegistryObject<SoundEvent> BLOCK_NOTE_BLOCK_HIT_MARKER = HELPER.createSoundEvent("block.note_block.hit_marker");
	public static final RegistryObject<SoundEvent> BLOCK_NOTE_BLOCK_HARPSICHORD = HELPER.createSoundEvent("block.note_block.harpsichord");
	public static final RegistryObject<SoundEvent> BLOCK_NOTE_BLOCK_ORCHESTRAL_HIT = HELPER.createSoundEvent("block.note_block.orchestral_hit");
}
