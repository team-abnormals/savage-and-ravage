package com.farcr.savageandravage.client.model;

import com.farcr.savageandravage.common.entity.CreepieEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * CreepieModel - TallestEgg
 * Created using Tabula 7.1.0
 */
public class CreepieModel extends SegmentedModel<CreepieEntity> {
	public ModelRenderer field_78134_c;
	public ModelRenderer field_78132_e;
	public ModelRenderer field_78130_g;
	public ModelRenderer field_78129_f;
	public ModelRenderer field_78135_a;
	public ModelRenderer field_78131_d;

	public CreepieModel() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.field_78129_f = new ModelRenderer(this, 0, 16);
		this.field_78129_f.setRotationPoint(-2.0F, 18.0F, -4.0F);
		this.field_78129_f.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.field_78135_a = new ModelRenderer(this, 0, 0);
		this.field_78135_a.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.field_78135_a.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 2.0F);
		this.field_78134_c = new ModelRenderer(this, 16, 16);
		this.field_78134_c.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.field_78134_c.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.field_78132_e = new ModelRenderer(this, 0, 16);
		this.field_78132_e.setRotationPoint(2.0F, 18.0F, 4.0F);
		this.field_78132_e.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.field_78130_g = new ModelRenderer(this, 0, 16);
		this.field_78130_g.setRotationPoint(2.0F, 18.0F, -4.0F);
		this.field_78130_g.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.field_78131_d = new ModelRenderer(this, 0, 16);
		this.field_78131_d.setRotationPoint(-2.0F, 18.0F, 4.0F);
		this.field_78131_d.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
	}

	@Override
	public void setRotationAngles(CreepieEntity creepieEntity, float v, float v1, float v2, float v3, float v4) {

	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.field_78129_f, this.field_78135_a, this.field_78134_c, this.field_78132_e, this.field_78130_g, this.field_78131_d);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}

