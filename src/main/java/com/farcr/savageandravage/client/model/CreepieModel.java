package com.farcr.savageandravage.client.model;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * Creepie - Farcr
 * Created using Tabula 7.1.0
 */
public class CreepieModel<C extends CreepieEntity> extends EntityModel<CreepieEntity> {
	public ModelRenderer head;

	public CreepieModel() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(-3.0F, 9.0F, -3.0F);
		this.head.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6, 0.0F);
	}

	@Override
	public void setRotationAngles(CreepieEntity creepieEntity, float v, float v1, float v2, float v3, float v4) {

	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int i, int i1, float v, float v1, float v2, float v3) {
		/**auto-implemented by IntelliJ, review this later*/
	}
}
