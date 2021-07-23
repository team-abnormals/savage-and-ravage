package com.minecraftabnormals.savageandravage.client.render;

import com.minecraftabnormals.savageandravage.common.entity.BurningBannerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.World;

public class BurningBannerRenderer extends EntityRenderer<BurningBannerEntity> {

	public BurningBannerRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(BurningBannerEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn) {
		if (!Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes())
			return;

		World world = entity.level;
		BlockPos bannerPos = entity.getBannerPosition();
		if (bannerPos == null || !(world.getBlockEntity(bannerPos) instanceof BannerTileEntity))
			return;

		matrixStack.pushPose();
		matrixStack.translate(bannerPos.getX() - entity.getX(), bannerPos.getY() - entity.getY(), bannerPos.getZ() - entity.getZ());

		IVertexBuilder builder = buffer.getBuffer(RenderType.lines());
		Matrix4f matrix4f = matrixStack.last().pose();
		AxisAlignedBB box = entity.getBurningBox();
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

	private static void pos(IVertexBuilder builder, Matrix4f matrix4f, double x, double y, double z, double rotation) {
		double[] rotatedVertices = BurningBannerEntity.rotate(x, y, z, rotation);
		builder.vertex(matrix4f, (float) rotatedVertices[0], (float) rotatedVertices[1], (float) rotatedVertices[2]).color(255, 0, 255, 255).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(BurningBannerEntity entity) {
		return PlayerContainer.BLOCK_ATLAS;
	}
}
