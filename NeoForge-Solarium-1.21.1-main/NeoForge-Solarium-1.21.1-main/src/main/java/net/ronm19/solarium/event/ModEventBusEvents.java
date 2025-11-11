package net.ronm19.solarium.event;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.entity.ModEntities;
import net.ronm19.solarium.entity.client.ModModelLayers;
import net.ronm19.solarium.entity.client.SolarStalkerModel;
import net.ronm19.solarium.entity.custom.SolarAxolotlEntity;
import net.ronm19.solarium.entity.custom.SolarCreeperEntity;
import net.ronm19.solarium.entity.custom.SolarGhastEntity;
import net.ronm19.solarium.entity.custom.SolarStalkerEntity;

@EventBusSubscriber(modid = SolariumMod.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SOLAR_STALKER, SolarStalkerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes( EntityAttributeCreationEvent event) {
        event.put(ModEntities.SOLAR_CREEPER.get(), SolarCreeperEntity.createSolarCreeperAttributes().build());
        event.put(ModEntities.SOLAR_GHAST.get(), SolarGhastEntity.createSolarGhastAttributes().build());
        event.put(ModEntities.SOLAR_STALKER.get(), SolarStalkerEntity.createSolarStalkerAttributes().build());

        event.put(ModEntities.SOLAR_AXOLOTL.get(), SolarAxolotlEntity.createSolarAxolotlAttributes().build());
    }
}
