package com.minecraftabnormals.savageandravage.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.minecraftabnormals.savageandravage.common.entity.GrieferEntity;
import com.minecraftabnormals.savageandravage.core.SavageAndRavage;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class GrieferModel extends HumanoidModel<GrieferEntity> {
	public ModelPart bipedBodyLayerTwo;
	public ModelPart nose;
	public ModelPart tnt;
	public ModelPart pouch;
	public ModelPart shoulderPad;

	public GrieferModel(ModelPart root) {
		super(root);
		this.hat.visible = false;
		this.bipedBodyLayerTwo = root.getChild("biped_body_layer_two");
		this.tnt = this.body.getChild("tnt");
		this.pouch = this.body.getChild("pouch");
		this.nose = this.head.getChild("nose");
		this.shoulderPad = this.rightArm.getChild("shoulder_pad");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition bipedBodyLayerTwo = root.addOrReplaceChild("biped_body_layer_two", CubeListBuilder.create().texOffs(36, 18).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.03F)), PartPose.ZERO);
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(36, 0).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, false), PartPose.ZERO);
		PartDefinition tnt = body.addOrReplaceChild("tnt", CubeListBuilder.create().texOffs(50, 45).addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 3.0F, false), PartPose.offsetAndRotation(-0.0F, 6.0F, -6.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition pouch = body.addOrReplaceChild("pouch", CubeListBuilder.create().texOffs(46, 36).addBox(0.0F, -0.9F, 0.0F, 6.0F, 6.0F, 3.0F, false), PartPose.offsetAndRotation(-2.9F, 1.7F, 3.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, false), PartPose.ZERO);
		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(16, 34).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, true), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 34).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, false), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition shoulderPad = rightArm.addOrReplaceChild("shoulder_pad", CubeListBuilder.create().texOffs(11, 51).addBox(-4.0F, -2.7F, -3.2F, 5.0F, 6.0F, 6.0F, false), PartPose.ZERO);
		PartDefinition leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, false), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, true), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	protected Iterable<ModelPart> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.bipedBodyLayerTwo));
	}

	@Override
	public void setupAnim(GrieferEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		boolean flag = entityIn.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem;
		this.bipedBodyLayerTwo.copyFrom(this.body);
		this.shoulderPad.visible = !flag;
		if (entityIn.getKickTicks() > 0) {
			float f1 = 1.0F - (float) Mth.abs(10 - 2 * entityIn.getKickTicks()) / 10.0F;
			this.rightLeg.xRot = Mth.lerp(f1, 0.0F, -1.40F);
		}
		AbstractIllager.IllagerArmPose armPose = entityIn.getArmPose();
		if (armPose == AbstractIllager.IllagerArmPose.CELEBRATING) {
			this.head.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
			this.leftArm.z = 0.0F;
			this.leftArm.x = 5.0F;
			this.leftArm.xRot = Mth.cos(ageInTicks * 0.7000F) * 0.05F;
			this.leftArm.zRot = -2.3561945F;
			this.leftArm.yRot = 0.0F;
		}
	}

	@Override
	public void prepareMobModel(GrieferEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		ItemStack itemstack = entityIn.getMainHandItem();
		UseAnim useaction = itemstack.getUseAnimation();
		this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
		this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
		if (entityIn.getMainArm() == HumanoidArm.RIGHT) {
			switch (useaction) {
				case BLOCK:
					this.rightArmPose = HumanoidModel.ArmPose.BLOCK;
					break;
				case BOW:
					this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
					break;
				default:
					this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
					if (!itemstack.isEmpty()) {
						this.rightArmPose = HumanoidModel.ArmPose.ITEM;
					}
					break;
			}
		}
		if (entityIn.getMainArm() == HumanoidArm.LEFT) {
			switch (useaction) {
				case BLOCK:
					this.leftArmPose = HumanoidModel.ArmPose.BLOCK;
					break;
				case BOW:
					this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
					break;
				default:
					this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
					if (!itemstack.isEmpty()) {
						this.leftArmPose = HumanoidModel.ArmPose.ITEM;
					}
					break;
			}
		}
		super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
	}
}