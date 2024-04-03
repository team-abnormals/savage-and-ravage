package com.teamabnormals.savage_and_ravage.core.data.server;

import com.teamabnormals.savage_and_ravage.core.SavageAndRavage;
import com.teamabnormals.savage_and_ravage.core.other.SRBiomeModifiers;
import com.teamabnormals.savage_and_ravage.core.other.SRFeatures.SRConfiguredFeatures;
import com.teamabnormals.savage_and_ravage.core.other.SRFeatures.SRPlacedFeatures;
import com.teamabnormals.savage_and_ravage.core.other.SRFeatures.SRTemplatePools;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SRDatapackBuiltinEntriesProvider extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, SRConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, SRPlacedFeatures::bootstrap)
			.add(Registries.TEMPLATE_POOL, SRTemplatePools::bootstrap)
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, SRBiomeModifiers::bootstrap);

	public SRDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(output, provider, BUILDER, Set.of(SavageAndRavage.MOD_ID));
	}
}