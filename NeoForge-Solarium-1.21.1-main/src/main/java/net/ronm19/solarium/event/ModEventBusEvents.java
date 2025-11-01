package net.ronm19.solarium.event;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.entity.ModEntities;
import net.ronm19.solarium.entity.custom.SolarCreeperEntity;
import net.ronm19.solarium.entity.custom.SolarGhastEntity;

@EventBusSubscriber(modid = SolariumMod.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public static void registerAttributes( EntityAttributeCreationEvent event) {
        event.put(ModEntities.SOLAR_CREEPER.get(), SolarCreeperEntity.createSolarCreeperAttributes().build());
        event.put(ModEntities.SOLAR_GHAST.get(), SolarGhastEntity.createSolarGhastAttributes().build());
    }
}
