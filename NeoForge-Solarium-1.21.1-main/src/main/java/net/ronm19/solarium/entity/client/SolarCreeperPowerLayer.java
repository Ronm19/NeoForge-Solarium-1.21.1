package net.ronm19.solarium.entity.client;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.entity.custom.SolarCreeperEntity;
import org.jetbrains.annotations.NotNull;

public class SolarCreeperPowerLayer extends EnergySwirlLayer<SolarCreeperEntity, CreeperModel<SolarCreeperEntity>> {
    private static final ResourceLocation POWER_LOCATION =  ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "textures/entity/solar_creeper/solar_creeper_armor.png");
    private final CreeperModel<SolarCreeperEntity> model;

    public SolarCreeperPowerLayer(RenderLayerParent<SolarCreeperEntity, CreeperModel<SolarCreeperEntity>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new CreeperModel<>(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    protected float xOffset(float tickCount) {
        return tickCount * 0.01F;
    }

    protected @NotNull ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected @NotNull EntityModel<SolarCreeperEntity> model() {
        return this.model;
    }
}
