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
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * ModelGrieferArmor - MCVinnyQ & Farcr
 * Created using Tabula 7.1.0
 */
public class GrieferArmorModel<T extends LivingEntity> extends BipedModel<T> {

	private static final Map<Integer, GrieferArmorModel<? extends LivingEntity>> CACHE = new HashMap<>();
	private static final ResourceLocation GUARD_VILLAGER_NAME = new ResourceLocation("guardvillagers:guard");

	public ModelRenderer helmet1;
	public ModelRenderer helmet2;
	public ModelRenderer chestplate1;
	public ModelRenderer chestplate2;

	public ModelRenderer leggingsLeft;
	public ModelRenderer leggingsRight;

	public ModelRenderer bootsLeft;
	public ModelRenderer bootsRight;

	public ModelRenderer shoulderPadRight;
	public ModelRenderer shoulderPadLeft;

	public ModelRenderer piglinHelmet1;
	public ModelRenderer piglinHelmet2;
	public ModelRenderer piglinHelmet3;
	public ModelRenderer piglinHelmet4;
	public ModelRenderer piglinHelmet5;

	public ModelRenderer illagerHelmet1;
	public ModelRenderer illagerHelmet2;
	public ModelRenderer illagerHelmet3;

	private final EquipmentSlotType slot;
	private final byte entityFlag;

	private GrieferArmorModel(int entityFlag) {
		super(1.0F, 0.0F, 64, 64);
		this.slot = EquipmentSlotType.values()[entityFlag & 15];
		this.entityFlag = (byte) (entityFlag >> 4);

		this.texWidth = 128;
		this.texHeight = 64;

		this.helmet1 = new ModelRenderer(this, 0, 0);
		this.helmet1.setPos(0.0F, 0.0F, 0.0F);
		this.helmet1.addBox(-4.0F, -8.0F, -4.0F, 8, 9, 8, 0.6F);

		this.helmet2 = new ModelRenderer(this, 36, 0);
		this.helmet2.setPos(0.0F, 0.0F, 0.0F);
		this.helmet2.addBox(-1.0F, -11.0F, -6.1F, 2, 8, 12, 0.125F);

		this.chestplate1 = new ModelRenderer(this, 0, 17);
		this.chestplate1.setPos(0.0F, 0.0F, 0.0F);
		this.chestplate1.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, 0.25F);

		this.chestplate2 = new ModelRenderer(this, 0, 34);
		this.chestplate2.setPos(0.0F, 0.0F, 0.0F);
		this.chestplate2.addBox(-4.0F, 0.0F, -2.0F, 8, 15, 4, 0.3F);

		this.leggingsLeft = new ModelRenderer(this, 48, 50);
		this.leggingsLeft.mirror = true;
		this.leggingsLeft.setPos(2.0F, 12.0F, 0.0F);
		this.leggingsLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.25F);

		this.leggingsRight = new ModelRenderer(this, 48, 50);
		this.leggingsRight.setPos(-2.0F, 12.0F, 0.0F);
		this.leggingsRight.addBox(-2.0F, 0.0F, -2.0F, 4, 10, 4, 0.25F);

		this.bootsLeft = new ModelRenderer(this, 32, 48);
		this.bootsLeft.mirror = true;
		this.bootsLeft.setPos(2.0F, 12.0F, 0.0F);
		this.bootsLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F);

		this.bootsRight = new ModelRenderer(this, 32, 48);
		this.bootsRight.setPos(-2.0F, 12.0F, 0.0F);
		this.bootsRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.5F);

		this.shoulderPadLeft = new ModelRenderer(this, 42, 20);
		this.shoulderPadLeft.mirror = true;
		this.shoulderPadLeft.setPos(5.0F, 1.0F, 0.0F);
		this.shoulderPadLeft.addBox(-1.0F, -2.0F, -3.0F, 5, 6, 6, 0.3F);

		this.shoulderPadRight = new ModelRenderer(this, 42, 20);
		this.shoulderPadRight.setPos(-5.0F, 1.0F, 0.0F);
		this.shoulderPadRight.addBox(-4.0F, -2.0F, -3.0F, 5, 6, 6, 0.3F);

		this.piglinHelmet1 = new ModelRenderer(this, 64, 0);
		this.piglinHelmet1.setPos(0.0F, 0.0F, 0.0F);
		this.piglinHelmet1.addBox(-5.0F, -8.5F, -4.0F, 10.0F, 9.0F, 8.0F, 0.6F, false);

		this.piglinHelmet2 = new ModelRenderer(this, 36, 0);
		this.piglinHelmet2.setPos(0.0F, 0.0F, 0.0F);
		this.piglinHelmet2.addBox(-1.0F, -12.5F, -6.1F, 2.0F, 8.0F, 12.0F, 0.125F, false);

		this.piglinHelmet3 = new ModelRenderer(this, 64, 20);
		this.piglinHelmet3.setPos(0.0F, 0.0F, 0.0F);
		this.piglinHelmet3.addBox(-2.0F, -4.2F, -5.7F, 4.0F, 4.0F, 1.0F, 0.35F, false);

		this.piglinHelmet4 = new ModelRenderer(this, 64, 25);
		this.piglinHelmet4.setPos(0.0F, 0.0F, 0.0F);
		this.piglinHelmet4.addBox(2.5F, -2F, -5.9F, 1.0F, 2.0F, 1.0F, 0.15F, false);

		this.piglinHelmet5 = new ModelRenderer(this, 64, 25);
		this.piglinHelmet5.setPos(0.0F, 0.0F, 0.0F);
		this.piglinHelmet5.addBox(-3.5F, -2F, -5.9F, 1.0F, 2.0F, 1.0F, 0.15F, false);

		this.illagerHelmet1 = new ModelRenderer(this, 64, 28);
		this.illagerHelmet1.setPos(0.0F, 0.0F, 0.0F);
		this.illagerHelmet1.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 11.0F, 8.0F, 0.6F, false);

		this.illagerHelmet2 = new ModelRenderer(this, 36, 0);
		this.illagerHelmet2.setPos(0.0F, 0.0F, 0.0F);
		this.illagerHelmet2.addBox(-1.0F, -13.0F, -6.1F, 2.0F, 8.0F, 12.0F, 0.125F, false);

		this.illagerHelmet3 = new ModelRenderer(this, 64, 30);
		this.illagerHelmet3.setPos(0.0F, 0.0F, 0.0F);
		this.illagerHelmet3.addBox(-1.0F, -3.0F, -6.1F, 2.0F, 4.0F, 2.0F, 0.125F, false);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		boolean illager = (this.entityFlag & 1) > 0;
		boolean piglin = (this.entityFlag & 2) > 0;
		boolean child = (this.entityFlag & 4) > 0;

		if (this.slot == EquipmentSlotType.HEAD) {
			if (piglin) {
				matrixStack.pushPose();
				this.piglinHelmet1.copyFrom(this.head);
				this.piglinHelmet2.copyFrom(this.head);
				this.piglinHelmet3.copyFrom(this.head);
				this.piglinHelmet4.copyFrom(this.head);
				this.piglinHelmet5.copyFrom(this.head);
				if (child) {
					matrixStack.scale(0.8F, 0.8F, 0.8F);
					this.piglinHelmet1.setPos(0.0F, 15.0F, 0.0F);
					this.piglinHelmet2.setPos(0.0F, 15.0F, 0.0F);
					this.piglinHelmet3.setPos(0.0F, 15.0F, 0.0F);
					this.piglinHelmet4.setPos(0.0F, 15.0F, 0.0F);
					this.piglinHelmet5.setPos(0.0F, 15.0F, 0.0F);
				}
				this.piglinHelmet1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.piglinHelmet2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.piglinHelmet3.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.piglinHelmet4.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.piglinHelmet5.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				matrixStack.popPose();

			} else if (illager) {
				matrixStack.pushPose();
				this.illagerHelmet1.copyFrom(this.head);
				this.illagerHelmet2.copyFrom(this.head);
				this.illagerHelmet3.copyFrom(this.head);
				if (child) {
					matrixStack.scale(0.8F, 0.8F, 0.8F);
					this.illagerHelmet1.setPos(0.0F, 15.0F, 0.0F);
					this.illagerHelmet2.setPos(0.0F, 15.0F, 0.0F);
					this.illagerHelmet3.setPos(0.0F, 15.0F, 0.0F);
				}
				this.illagerHelmet1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.illagerHelmet2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.illagerHelmet3.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				matrixStack.popPose();
			} else {
				matrixStack.pushPose();
				this.helmet1.copyFrom(this.head);
				this.helmet2.copyFrom(this.head);
				if (child) {
					matrixStack.scale(0.8F, 0.8F, 0.8F);
					this.helmet1.setPos(0.0F, 15.0F, 0.0F);
					this.helmet2.setPos(0.0F, 15.0F, 0.0F);
				}
				this.helmet1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				this.helmet2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				matrixStack.popPose();
			}
		}

		if (this.slot == EquipmentSlotType.CHEST) {
			matrixStack.pushPose();

			this.chestplate1.copyFrom(this.body);
			this.shoulderPadLeft.copyFrom(this.leftArm);
			this.shoulderPadRight.copyFrom(this.rightArm);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.chestplate1.setPos(0.0F, 24.0F, 0.0F);
				this.shoulderPadLeft.setPos(5.0F, 24.0F, 0.0F);
				this.shoulderPadRight.setPos(-5.0F, 24.0F, 0.0F);
			}
			this.shoulderPadLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.shoulderPadRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			if (illager) {
				matrixStack.scale(1.0F, 1.0F, 1.3F);
			}
			this.chestplate1.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.popPose();
		}

		if (this.slot == EquipmentSlotType.LEGS) {
			matrixStack.pushPose();
			matrixStack.scale(1.01F, 1.0F, 1.01F);
			this.chestplate2.copyFrom(this.body);
			this.leggingsLeft.copyFrom(this.leftLeg);
			this.leggingsRight.copyFrom(this.rightLeg);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.leggingsLeft.setPos(2.0F, 36.0F, 0.0F);
				this.leggingsRight.setPos(-2.0F, 36.0F, 0.0F);
				this.chestplate2.setPos(0.0F, 24.0F, 0.0F);
			}
			this.leggingsLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.leggingsRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			if (illager) {
				matrixStack.scale(1.0F, 1.0F, 1.32F);
			}
			this.chestplate2.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.popPose();
		}

		if (this.slot == EquipmentSlotType.FEET) {
			matrixStack.pushPose();
			matrixStack.scale(1.05F, 1.0F, 1.05F);

			this.bootsLeft.copyFrom(this.leftLeg);
			this.bootsRight.copyFrom(this.rightLeg);
			if (child) {
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.bootsLeft.setPos(2.0F, 37.0F, 0.0F);
				this.bootsRight.setPos(-2.0F, 37.0F, 0.0F);
			}
			this.bootsLeft.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			this.bootsRight.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStack.popPose();
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
		int entityFlag = (slot.ordinal() & 15) | (illager ? 1 : 0) << 4 | (piglin ? 1 : 0) << 5 | (entity.isBaby() ? 1 : 0) << 6;
		return (A) CACHE.computeIfAbsent(entityFlag, GrieferArmorModel::new);
	}
}
