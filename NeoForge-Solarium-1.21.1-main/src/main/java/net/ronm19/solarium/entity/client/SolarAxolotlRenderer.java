package net.ronm19.solarium.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.entity.custom.SolarAxolotlEntity;
import org.jetbrains.annotations.NotNull;

public class SolarAxolotlRenderer extends MobRenderer<SolarAxolotlEntity, AxolotlModel<SolarAxolotlEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "textures/entity/solar_axolotl/solar_axolotl.png");

    public SolarAxolotlRenderer( EntityRendererProvider.Context context ) {
        super(context, new AxolotlModel<>(context.bakeLayer(ModelLayers.AXOLOTL)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation( @NotNull SolarAxolotlEntity solarAxolotlEntity ) {
        return TEXTURE;
    }

    @Override
    public void render( SolarAxolotlEntity entity, float entityYaw, float partialTicks,
                        PoseStack poseStack, MultiBufferSource buffer, int packedLight ) {
        // If the entity is a baby, make it smaller
        if (entity.isBaby()) {
            poseStack.pushPose();
            // Scale down to 50% size (typical for baby mobs)
            poseStack.scale(0.5F, 0.5F, 0.5F);
            // Move it upward slightly so it doesn't sink into the ground
            poseStack.translate(0.0D, 1.5D, 0.0D);

            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
            poseStack.popPose();
        } else {
            // Normal render for adults
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }
}