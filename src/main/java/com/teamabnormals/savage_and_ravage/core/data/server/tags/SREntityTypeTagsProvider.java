package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SREntityTypeTags;
import com.teamabnormals.savage_and_ravage.core.registry.SREntityTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SREntityTypeTagsProvider extends EntityTypeTagsProvider {

	public SREntityTypeTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SavageAndRavage.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(EntityTypeTags.ARROWS).add(SREntityTypes.MISCHIEF_ARROW.get());
		this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(SREntityTypes.SPORE_CLOUD.get(), SREntityTypes.CONFUSION_BOLT.get());
		this.tag(EntityTypeTags.RAIDERS).add(SREntityTypes.GRIEFER.get(), SREntityTypes.EXECUTIONER.get(), SREntityTypes.ICEOLOGER.get(), SREntityTypes.TRICKSTER.get());
		this.tag(EntityTypeTags.SKELETONS).add(SREntityTypes.SKELETON_VILLAGER.get());
		this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(SREntityTypes.ICEOLOGER.get());

		this.tag(SREntityTypeTags.CREEPER_IMMUNE).add(EntityType.PAINTING, EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME, EntityType.FALLING_BLOCK, EntityType.LEASH_KNOT, EntityType.ARMOR_STAND);
		this.tag(SREntityTypeTags.CREEPER).add(EntityType.CREEPER);
	}
}