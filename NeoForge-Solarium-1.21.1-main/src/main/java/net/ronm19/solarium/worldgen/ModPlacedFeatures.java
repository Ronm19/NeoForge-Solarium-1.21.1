package net.ronm19.solarium.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SOLAR_AMBER_PLACED_KEY = registerKey("solar_amber_placed");
    public static final ResourceKey<PlacedFeature> SOLAR_ASH_PLACED_KEY = registerKey("solar_ash_placed");

    public static final ResourceKey<PlacedFeature> SOLARIUM_ORE_PLACED_KEY = registerKey("solarium_ore_placed");
    public static final ResourceKey<PlacedFeature> DEEPSLATE_SOLARIUM_ORE_PLACED_KEY = registerKey("deepslate_solarium_ore_placed");

    public static final ResourceKey<PlacedFeature> SOLAR_ROSE_PLACED_KEY = registerKey("solar_rose_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, SOLAR_AMBER_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SOLAR_AMBER_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),
                        ModBlocks.SOLAR_AMBER_SAPLING.get()));

        register(context,
                SOLAR_ASH_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.SOLAR_ASH_KEY),
                List.of(RarityFilter.onAverageOnceEvery(1),   // How often the tree spawns (low = frequent, high = rare)InSquarePlacement.spread(),            // Spread it in the chunk
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                        // Place on top of terrain (Nether safe)
                        BiomeFilter.biome()                     // Only in the biome it's registered for
                )
        );




        // Regular stone Solarium Ore
        register(context, SOLARIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_SOLARIUM_ORE_KEY),
                ModOrePlacements.rareOrePlacement(7, // veins per chunk
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(8),  // avoid bedrock
                                VerticalAnchor.absolute(64)  // up to surface stone
                        )));

// Deepslate Solarium Ore
        register(context, DEEPSLATE_SOLARIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_DEEPSLATE_SOLARIUM_ORE_KEY),
                ModOrePlacements.rareOrePlacement(7, // veins per chunk
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), // deep underground
                                VerticalAnchor.absolute(-1)   // just below stone layers
                        )));


        register(context, SOLAR_ROSE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SOLAR_ROSE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(16), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));



    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
