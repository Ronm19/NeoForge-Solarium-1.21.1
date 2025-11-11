package net.ronm19.solarium.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.ronm19.solarium.entity.animations.SolarStalkerAnimations;
import net.ronm19.solarium.entity.custom.SolarStalkerEntity;
import org.jetbrains.annotations.NotNull;

public class SolarStalkerModel<T extends SolarStalkerEntity> extends HierarchicalModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart righthand;
    private final ModelPart lefthand;
    private final ModelPart rightleg;
    private final ModelPart leftleg;
    private final ModelPart root;

    public SolarStalkerModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.righthand = root.getChild("righthand");
        this.lefthand = root.getChild("lefthand");
        this.rightleg = root.getChild("rightleg");
        this.leftleg = root.getChild("leftleg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -21.0F, -3.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-3.0F, -14.0F, -2.0F, 7.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition righthand = partdefinition.addOrReplaceChild("righthand", CubeListBuilder.create().texOffs(24, 14).addBox(4.0F, -13.0F, -1.5F, 3.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition lefthand = partdefinition.addOrReplaceChild("lefthand", CubeListBuilder.create().texOffs(24, 27).addBox(-5.9F, -13.1F, -1.5F, 3.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition rightleg = partdefinition.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(28, 0).addBox(1.6F, -5.0F, -0.7F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leftleg = partdefinition.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(0, 28).addBox(-2.7F, -5.0F, -0.7F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SolarStalkerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(SolarStalkerAnimations.solar_stalker_walking, limbSwing, limbSwingAmount, 2f, 1f);
        this.animate(entity.idleAnimationState, SolarStalkerAnimations.solar_stalker_idle, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer( @NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        righthand.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        lefthand.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        rightleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leftleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}
