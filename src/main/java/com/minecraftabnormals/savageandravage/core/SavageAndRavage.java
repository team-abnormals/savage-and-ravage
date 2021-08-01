package com.minecraftabnormals.savageandravage.core;

import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.abnormals_core.core.util.registry.RegistryHelper;
import com.minecraftabnormals.savageandravage.client.render.IceChunkRenderer;
import com.minecraftabnormals.savageandravage.client.render.layer.TotemShieldLayer;
import com.minecraftabnormals.savageandravage.common.network.MessageC2SIsPlayerStill;
import com.minecraftabnormals.savageandravage.core.other.SRCompat;
import com.minecraftabnormals.savageandravage.core.other.SRFeatures;
import com.minecraftabnormals.savageandravage.core.other.SRLoot;
import com.minecraftabnormals.savageandravage.core.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(SavageAndRavage.MOD_ID)
public class SavageAndRavage {
	public static final String MOD_ID = "savageandravage";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
	public static final String NETWORK_PROTOCOL = "SR1";

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net"))
			.networkProtocolVersion(() -> NETWORK_PROTOCOL)
			.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
			.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
			.simpleChannel();

	public SavageAndRavage() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		REGISTRY_HELPER.register(bus);
		SREntities.ENTITIES.register(bus);
		SRParticles.PARTICLES.register(bus);
		SREffects.EFFECTS.register(bus);
		SRFeatures.FEATURES.register(bus);
		SRAttributes.ATTRIBUTES.register(bus);

		MinecraftForge.EVENT_BUS.register(this);

		bus.addListener(this::commonSetup);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::clientSetup);
			bus.addListener(this::registerModels);
			bus.addListener(this::finish);
		});

		this.setupMessages();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SRConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SRConfig.CLIENT_SPEC);
		DataUtil.registerConfigCondition(SavageAndRavage.MOD_ID, SRConfig.COMMON, SRConfig.CLIENT);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SREntities.registerEntitySpawns();
			SREntities.registerTrackedData();
			SRFeatures.registerPools();
			SRFeatures.registerBiomeModifications();
			SREntities.registerWaveMembers();
			SRCompat.registerFlammables();
			SRCompat.registerDispenserBehaviors();
			SRCompat.registerDataSerializers();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		SREntities.registerRendering();
		event.enqueueWork(() -> {
			SRItems.registerItemProperties();
		});
	}

	private void registerModels(ModelRegistryEvent event) {
		ModelLoader.addSpecialModel(IceChunkRenderer.MODEL_LOCATION);
	}

	private void setupMessages() {
		CHANNEL.registerMessage(-1, MessageC2SIsPlayerStill.class, MessageC2SIsPlayerStill::serialize, MessageC2SIsPlayerStill::deserialize, MessageC2SIsPlayerStill::handle);
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	private void finish(FMLLoadCompleteEvent event) {
		event.enqueueWork(() -> {
			EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
			EntityRenderer<?> renderer = manager.renderers.get(EntityType.EVOKER);
			if (renderer instanceof EvokerRenderer) {
				EvokerRenderer<EvokerEntity> livingRenderer = (EvokerRenderer<EvokerEntity>) renderer;
				livingRenderer.addLayer(new TotemShieldLayer<>(livingRenderer, new IllagerModel<>(2.0F, 0.0F, 64, 64)));
			}
		});
	}
}
