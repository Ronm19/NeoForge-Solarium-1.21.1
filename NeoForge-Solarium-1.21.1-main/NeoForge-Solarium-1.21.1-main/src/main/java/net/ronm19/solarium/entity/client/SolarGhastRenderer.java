package net.ronm19.solarium.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.entity.custom.SolarGhastEntity;
import org.jetbrains.annotations.NotNull;

public class SolarGhastRenderer extends MobRenderer<SolarGhastEntity, GhastModel<SolarGhastEntity>> {
    private static final ResourceLocation SOLAR_GHAST_LOCATION = ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "textures/entity/solar_ghast/solar_ghast.png");
    private static final ResourceLocation SOLAR_GHAST_SHOOTING_LOCATION = ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "textures/entity/solar_ghast/solar_ghast_shooting.png");

    public SolarGhastRenderer(EntityRendererProvider.Context context) {
        super(context, new GhastModel<>(context.bakeLayer(ModelLayers.GHAST)), 1.5F);
    }

    public @NotNull ResourceLocation getTextureLocation( SolarGhastEntity entity) {
        return entity.isCharging() ? SOLAR_GHAST_SHOOTING_LOCATION : SOLAR_GHAST_LOCATION;
    }

    protected void scale( @NotNull SolarGhastEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        float f = 1.0F;
        float f1 = 4.5F;
        float f2 = 4.5F;
        poseStack.scale(4.5F, 4.5F, 4.5F);
    }
}

