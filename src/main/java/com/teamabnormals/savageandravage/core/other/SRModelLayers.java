package com.teamabnormals.savageandravage.core.other;

import com.teamabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class SRModelLayers {
	public static final ModelLayerLocation CREEPIE = register("creepie");
	public static final ModelLayerLocation CREEPIE_ARMOR = register("creepie", "armor");
	public static final ModelLayerLocation EXECUTIONER = register("executioner");
	public static final ModelLayerLocation GRIEFER = register("griefer");
	public static final ModelLayerLocation GRIEFER_ARMOR = register("griefer_armor", "armor");
	public static final ModelLayerLocation ICEOLOGER = register("iceologer");
	public static final ModelLayerLocation ILLAGER_ARMOR = register("illager", "armor");
	public static final ModelLayerLocation MASK_OF_DISHONESTY = register("mask_of_dishonesty", "armor");
	public static final ModelLayerLocation RUNE_PRISON = register("rune_prison");
	public static final ModelLayerLocation SKELETON_VILLAGER = register("skeleton_villager");
	public static final ModelLayerLocation TRICKSTER = register("trickster");
	public static final ModelLayerLocation VILLAGER_INNER_ARMOR = register("villager_armor", "inner_armor");
	public static final ModelLayerLocation VILLAGER_OUTER_ARMOR = register("villager_armor", "outer_armor");

	public static ModelLayerLocation register(String name) {
		return register(name, "main");
	}

	public static ModelLayerLocation register(String name, String layer) {
		return new ModelLayerLocation(new ResourceLocation(SavageAndRavage.MOD_ID, name), layer);
	}
}
