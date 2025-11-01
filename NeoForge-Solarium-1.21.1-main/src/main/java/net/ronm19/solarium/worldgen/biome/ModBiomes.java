package net.ronm19.solarium.worldgen.biome;

import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.worldgen.biome.region.NetherRegion;
import net.ronm19.solarium.worldgen.biome.region.OverworldRegion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import terrablender.api.EndBiomeRegistry;
import terrablender.api.Regions;

public class ModBiomes {
    public static final ResourceKey<Biome> SOLAR_FOREST = registerBiomeKey("solar_forest");
    public static final ResourceKey<Biome> SOLAR_ASHLANDS = registerBiomeKey("solar_ashlands");

    public static void registerBiomes() {
        Regions.register(new OverworldRegion(ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solarium_overworld"), 40));
        Regions.register(new NetherRegion(ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solarium_nether"), 40));
    }

    public static void bootstrap(BootstrapContext<Biome> context) {
        var carver = context.lookup(Registries.CONFIGURED_CARVER);
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        register(context, SOLAR_FOREST, ModOverworldBiomes.solarForest(placedFeatures, carver));
        register(context, SOLAR_ASHLANDS, ModNetherBiomes.solarAshlands(placedFeatures, carver));
    }


    private static void register(BootstrapContext<Biome> context, ResourceKey<Biome> key, Biome biome) {
        context.register(key, biome);
    }

    private static ResourceKey<Biome> registerBiomeKey(String name) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, name));
    }
}
