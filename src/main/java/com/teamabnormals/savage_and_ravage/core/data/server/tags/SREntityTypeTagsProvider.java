package com.teamabnormals.savage_and_ravage.core.data.server.tags;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.tags.SREntityTypeTags;
import com.teamabnormals.savage_and_ravage.core.registry.SREntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SREntityTypeTagsProvider extends EntityTypeTagsProvider {

	public SREntityTypeTagsProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
		super(generator, SavageAndRavage.MOD_ID, fileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(EntityTypeTags.ARROWS).add(SREntities.MISCHIEF_ARROW.get());
		this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(SREntities.SPORE_CLOUD.get(), SREntities.CONFUSION_BOLT.get());
		this.tag(EntityTypeTags.RAIDERS).add(SREntities.GRIEFER.get(), SREntities.EXECUTIONER.get(), SREntities.ICEOLOGER.get(), SREntities.TRICKSTER.get());
		this.tag(EntityTypeTags.SKELETONS).add(SREntities.SKELETON_VILLAGER.get());

		this.tag(SREntityTypeTags.CREEPER_IMMUNE).add(EntityType.PAINTING, EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME, EntityType.FALLING_BLOCK, EntityType.LEASH_KNOT, EntityType.ARMOR_STAND);
	}
}