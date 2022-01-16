package com.teamabnormals.savage_and_ravage.client.render;

import com.teamabnormals.savage_and_ravage.common.entity.BurningBannerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.phys.AABB;

public class BurningBannerRenderer extends EntityRenderer<BurningBannerEntity> {

	public BurningBannerRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(BurningBannerEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLightIn) {
		if (!Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes())
			return;

		Level world = entity.level;
		BlockPos bannerPos = entity.getBannerPosition();
		if (bannerPos == null || !(world.getBlockEntity(bannerPos) instanceof BannerBlockEntity))
			return;

		matrixStack.pushPose();
		matrixStack.translate(bannerPos.getX() - entity.getX(), bannerPos.getY() - entity.getY(), bannerPos.getZ() - entity.getZ());

		VertexConsumer builder = buffer.getBuffer(RenderType.lines());
		Matrix4f matrix4f = matrixStack.last().pose();
		AABB box = entity.getBurningBox();
		double rotation = entity.getBurningBoxRotation();

		double minX = box.min(Direction.Axis.X);
		double minY = box.min(Direction.Axis.Y);
		double minZ = box.min(Direction.Axis.Z);
		double maxX = box.max(Direction.Axis.X);
		double maxY = box.max(Direction.Axis.Y);
		double maxZ = box.max(Direction.Axis.Z);

		// back
		pos(builder, matrix4f, minX, minY, minZ, rotation);
		pos(builder, matrix4f, minX, maxY, minZ, rotation);
		pos(builder, matrix4f, minX, maxY, minZ, rotation);
		pos(builder, matrix4f, maxX, maxY, minZ, rotation);
		pos(builder, matrix4f, maxX, maxY, minZ, rotation);
		pos(builder, matrix4f, maxX, minY, minZ, rotation);
		pos(builder, matrix4f, minX, minY, minZ, rotation);
		pos(builder, matrix4f, minX, minY, minZ, rotation);

		// front
		pos(builder, matrix4f, minX, minY, maxZ, rotation);
		pos(builder, matrix4f, minX, maxY, maxZ, rotation);
		pos(builder, matrix4f, minX, maxY, maxZ, rotation);
		pos(builder, matrix4f, maxX, maxY, maxZ, rotation);
		pos(builder, matrix4f, maxX, maxY, maxZ, rotation);
		pos(builder, matrix4f, maxX, minY, maxZ, rotation);
		pos(builder, matrix4f, minX, minY, maxZ, rotation);
		pos(builder, matrix4f, minX, minY, maxZ, rotation);

		// sides
		pos(builder, matrix4f, minX, minY, minZ, rotation);
		pos(builder, matrix4f, minX, minY, maxZ, rotation);
		pos(builder, matrix4f, minX, maxY, minZ, rotation);
		pos(builder, matrix4f, minX, maxY, maxZ, rotation);
		pos(builder, matrix4f, maxX, maxY, minZ, rotation);
		pos(builder, matrix4f, maxX, maxY, maxZ, rotation);
		pos(builder, matrix4f, minX, minY, minZ, rotation);
		pos(builder, matrix4f, minX, minY, maxZ, rotation);

		matrixStack.popPose();
	}

	private static void pos(VertexConsumer builder, Matrix4f matrix4f, double x, double y, double z, double rotation) {
		double[] rotatedVertices = BurningBannerEntity.rotate(x, y, z, rotation);
		builder.vertex(matrix4f, (float) rotatedVertices[0], (float) rotatedVertices[1], (float) rotatedVertices[2]).color(255, 0, 255, 255).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(BurningBannerEntity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
