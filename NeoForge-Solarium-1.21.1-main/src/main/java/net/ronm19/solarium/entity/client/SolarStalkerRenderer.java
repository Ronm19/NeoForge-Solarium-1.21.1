package net.ronm19.solarium.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.entity.custom.SolarStalkerEntity;
import org.jetbrains.annotations.NotNull;

public class SolarStalkerRenderer extends MobRenderer<SolarStalkerEntity, SolarStalkerModel<SolarStalkerEntity>> {
    public SolarStalkerRenderer( EntityRendererProvider.Context context) {
        super(context, new SolarStalkerModel<>(context.bakeLayer(ModModelLayers.SOLAR_STALKER)), 0.6f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation( @NotNull SolarStalkerEntity solarStalkerEntity ) {
        return ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "textures/entity/solar_stalker/solar_stalker.png");
    }
}
