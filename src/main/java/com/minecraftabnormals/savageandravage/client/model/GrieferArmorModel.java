package com.minecraftabnormals.savageandravage.client.model;

import com.minecraftabnormals.savageandravage.common.entity.SkeletonVillagerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * ModelGrieferArmor - MCVinnyq & Farcr
 * Created using Tabula 7.1.0
 */
public class GrieferArmorModel<T extends LivingEntity> extends BipedModel<T> {

	private static final Map<Integer, GrieferArmorModel<? extends LivingEntity>> CACHE = new HashMap<>();
	private static final ResourceLocation GUARD_VILLAGER_NAME = new ResourceLocation("guardvillagers:guard");

	public ModelRenderer helmet1;
	public ModelRenderer helmet2;
	public ModelRenderer chestplate1;
	public ModelRenderer chestplate2;

	public ModelRenderer leggingsleft;
	public ModelRenderer leggingsright;

	public ModelRenderer bootsleft;
	public ModelRenderer bootsright;

	public ModelRenderer shoulderpadright;
	public ModelRenderer shoulderpadleft;

	public ModelRenderer piglin_helmet1;
	public ModelRenderer piglin_helmet2;
	public ModelRenderer piglin_helmet3;
	public ModelRenderer piglin_helmet4;
	public ModelRenderer piglin_helmet5;

	public ModelRenderer illager_helmet1;
	public ModelRenderer illager_helmet2;
	public ModelRenderer illager_helmet3;

	private final EquipmentSlotType slot;
	private final byte entityFlag;

	private GrieferArmorModel(int entityFlag) {
		super(1.0F, 0.0F, 64, 64);
		this.slot = EquipmentSlotType.values()[entityFlag & 15];
		this.entityFlag = (byte) (entityFlag >> 4);

		this.textureWidth = 128;
		this.textureHeight = 64;

		this.helmet1 = new ModelRenderer(this, 0, 0);
		this.helmet1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.helmet1.addBox(-4.0F, -8.0F, -4.0F, 8, 9, 8, 0.6F);

		this.helmet2 = new ModelRenderer(this, 36, 0);
		this.helmet2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.helmet2.addBox(-1.0F, -11.0F, -6.1F, 2, 8, 12, 0.125F);

		this.chestplate1 = new ModelRenderer(this, 0, 17);
		this.chestplate1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.chestplate1.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, 0.25F);

		this.chestplate2 = new ModelRenderer(this, 0, 34);
		this.chestplate2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.chestplate2.addBox(-4.0F, 0.0F, -2.0F, 8, 15, 4, 0.3F);

		this.leggingsleft = new ModelRenderer(this, 48, 50);
		this.leggingsleft.mirror = true;
		this.leggingsleft.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.leggingsleft.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.25F);

		this.leggingsright = new ModelRenderer(this, 48, 50);
		this.leggingsright.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.leggingsright.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.25F);

		this.bootsleft = new ModelRenderer(this, 32, 48);
		this.bootsleft.mirror = true;
		this.bootsleft.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.bootsleft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F);

		this.bootsright = new ModelRenderer(this, 32, 48);
		this.bootsright.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bootsright.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F);

		this.shoulderpadleft = new ModelRenderer(this, 42, 20);
		this.shoulderpadleft.mirror = true;
		this.shoulderpadleft.setRotationPoint(5.0F, 1.0F, 0.0F);
		this.shoulderpadleft.addBox(-1.0F, -2.0F, -3.0F, 5, 6, 6, 0.3F);

		this.shoulderpadright = new ModelRenderer(this, 42, 20);
		this.shoulderpadright.setRotationPoint(-5.0F, 1.0F, 0.0F);
		this.shoulderpadright.addBox(-4.0F, -2.0F, -3.0F, 5, 6, 6, 0.3F);

		this.piglin_helmet1 = new ModelRenderer(this, 64, 0);
		this.piglin_helmet1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.piglin_helmet1.addBox(-5.0F, -8.5F, -4.0F, 10.0F, 9.0F, 8.0F, 0.6F, false);

		this.piglin_helmet2 = new ModelRenderer(this, 36, 0);
		this.piglin_helmet2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.piglin_helmet2.addBox(-1.0F, -12.5F, -6.1F, 2.0F, 8.0F, 12.0F, 0.125F, false);

		this.piglin_helmet3 = new ModelRenderer(this, 64, 20);
		this.piglin_helmet3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.piglin_helmet3.addBox(-2.0F, -4.2F, -5.7F, 4.0F, 4.0F, 1.0F, 0.35F, false);

		this.piglin_helmet4 = new ModelRenderer(this, 64, 25);
		this.piglin_helmet4.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.piglin_helmet4.addBox(2.5F, -2F, -5.9F, 1.0F, 2.0F, 1.0F, 0.15F, false);

		this.piglin_helmet5 = new ModelRenderer(this, 64, 25);
		this.piglin_helmet5.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.piglin_helmet5.addBox(-3.5F, -2F, -5.9F, 1.0F, 2.0F, 1.0F, 0.15F, false);

		this.illager_helmet1 = new ModelRenderer(this, 64, 28);
		this.illager_helmet1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.illager_helmet1.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 11.0F, 8.0F, 0.6F, false);

		this.illager_helmet2 = new ModelRenderer(this, 36, 0);
		this.illager_helmet2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.illager_helmet2.addBox(-1.0F, -13.0F, -6.1F, 2.0F, 8.0F, 12.0F, 0.125F, false);

		this.illager_helmet3 = new ModelRenderer(this, 64, 30);
		this.illager_helmet3.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.illager_helmet3.addBox(-1.0F, -3.0F, -6.1F, 2.0F, 4.0F, 2.0F, 0.125F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		boolean illager = (this.entityFlag & 1) > 0;
		boolean piglin = (this.entityFlag & 2) > 0;
		boolean child = (this.entityFlag & 4) > 0;

		if (this.slot == EquipmentSlotType.HEAD) {
			if (piglin) {
				matrixStack.push();
				this.piglin_helmet1.copyModelAngles(this.bipedHead);
				this.piglin_helmet2.copyModelAngles(this.bipedHead);
				this.piglin_helmet3.copyModelAngles(this.bipedHead);
				this.piglin_helmet4.copyModelAngles(this.bipedHead);
				this.piglin_helmet5.copyModelAngles(this.bipedHead);
				if (child) {
					matrixStack.scale(0.8F, 0.8F, 0.8F);
					this.piglin_helmet1.setRotationPoint(0.0F, 15.0F, 0.0F);
					this.piglin_helmet2.setRotationPoint(0.0F, 15.0F, 0.0F);
					this.piglin_helmet3.setRotationPoint(0.0F, 15.0F, 0.0F);
					this.piglin_helmet4.setRotationPoint(0.0F, 15.0F, 0.0F);
					this.piglin_helmet5.setRotationPoint(0.0F, 15.0F, 0.0F);
				}
				this.piglin_helmet1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.piglin_helmet2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.piglin_helmet3.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.piglin_helmet4.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.piglin_helmet5.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				matrixStack.pop();

			} else if (illager) {
				matrixStack.push();
				this.illager_helmet1.copyModelAngles(this.bipedHead);
				this.illager_helmet2.copyModelAngles(this.bipedHead);
				this.illager_helmet3.copyModelAngles(this.bipedHead);
				if (child) {
					matrixStack.scale(0.8F, 0.8F, 0.8F);
					this.illager_helmet1.setRotationPoint(0.0F, 15.0F, 0.0F);
					this.illager_helmet2.setRotationPoint(0.0F, 15.0F, 0.0F);
					this.illager_helmet3.setRotationPoint(0.0F, 15.0F, 0.0F);
				}
				this.illager_helmet1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.illager_helmet2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.illager_helmet3.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				matrixStack.pop();
			} else {
				matrixStack.push();
				this.helmet1.copyModelAngles(this.bipedHead);
				this.helmet2.copyModelAngles(this.bipedHead);
				if (child) {
					matrixStack.scale(0.8F, 0.8F, 0.8F);
					this.helmet1.setRotationPoint(0.0F, 15.0F, 0.0F);
					this.helmet2.setRotationPoint(0.0F, 15.0F, 0.0F);
				}
				this.helmet1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.helmet2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				matrixStack.pop();
			}
		}

		if (this.slot == EquipmentSlotType.CHEST) {
			matrixStack.push();

			this.chestplate1.copyModelAngles(this.bipedBody);
			this.shoulderpadleft.copyModelAngles(this.bipedLeftArm);
			this.shoulderpadright.copyModelAngles(this.bipedRightArm);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.chestplate1.setRotationPoint(0.0F, 24.0F, 0.0F);
				this.shoulderpadleft.setRotationPoint(5.0F, 24.0F, 0.0F);
				this.shoulderpadright.setRotationPoint(-5.0F, 24.0F, 0.0F);
			}
			this.shoulderpadleft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.shoulderpadright.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			if (illager) {
				matrixStack.scale(1.0F, 1.0F, 1.3F);
			}
			this.chestplate1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.pop();
		}

		if (this.slot == EquipmentSlotType.LEGS) {
			matrixStack.push();
			matrixStack.scale(1.01F, 1.0F, 1.01F);
			this.chestplate2.copyModelAngles(this.bipedBody);
			this.leggingsleft.copyModelAngles(this.bipedLeftLeg);
			this.leggingsright.copyModelAngles(this.bipedRightLeg);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.leggingsleft.setRotationPoint(2.0F, 36.0F, 0.0F);
				this.leggingsright.setRotationPoint(-2.0F, 36.0F, 0.0F);
				this.chestplate2.setRotationPoint(0.0F, 24.0F, 0.0F);
			}
			this.leggingsleft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.leggingsright.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			if (illager) {
				matrixStack.scale(1.0F, 1.0F, 1.32F);
			}
			this.chestplate2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.pop();
		}

		if (this.slot == EquipmentSlotType.FEET) {
			matrixStack.push();
			matrixStack.scale(1.05F, 1.0F, 1.05F);

			this.bootsleft.copyModelAngles(this.bipedLeftLeg);
			this.bootsright.copyModelAngles(this.bipedRightLeg);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.bootsleft.setRotationPoint(2.0F, 37.0F, 0.0F);
				this.bootsright.setRotationPoint(-2.0F, 37.0F, 0.0F);
			}
			this.bootsleft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.bootsright.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.pop();
		}
	}

	/**
	 * Fetches or creates a new model based on the provided entity.
	 *
	 * @param slot   The slot the armor model is in
	 * @param entity The entity to get the context for
	 * @param <A>    The type of model to fetch
	 * @return A new model or a previously cached one
	 */
	@SuppressWarnings("unchecked")
	public static <A extends BipedModel<?>> A getModel(EquipmentSlotType slot, LivingEntity entity) {
		boolean illager = entity instanceof AbstractIllagerEntity ||
				entity instanceof SkeletonVillagerEntity ||
				entity instanceof ZombieVillagerEntity ||
				entity instanceof AbstractVillagerEntity ||
				entity.getType() == ForgeRegistries.ENTITIES.getValue(GUARD_VILLAGER_NAME);
		boolean piglin = entity instanceof AbstractPiglinEntity ||
				entity instanceof ZombifiedPiglinEntity;
		int entityFlag = (slot.ordinal() & 15) | (illager ? 1 : 0) << 4 | (piglin ? 1 : 0) << 5 | (entity.isChild() ? 1 : 0) << 6;
		return (A) CACHE.computeIfAbsent(entityFlag, GrieferArmorModel::new);
	}
}
