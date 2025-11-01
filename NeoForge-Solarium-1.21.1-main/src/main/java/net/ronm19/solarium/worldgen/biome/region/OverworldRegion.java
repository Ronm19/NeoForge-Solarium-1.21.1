package net.ronm19.solarium.worldgen.biome.region;

import com.mojang.datafixers.util.Pair;
import net.ronm19.solarium.worldgen.biome.ModBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

import static terrablender.api.ParameterUtils.*;

public class OverworldRegion extends Region {
    public OverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        // Define the climate parameters for our SOLAR_FOREST biome.
        // This biome is hot, dry, and bright — typically found in warm inland regions.
        new ParameterPointListBuilder()
                .temperature(Temperature.span(Temperature.WARM, Temperature.HOT)) // flipped
                .humidity(Humidity.span(Humidity.DRY, Humidity.ARID))             // flipped
                .continentalness(Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND))
                .erosion(Erosion.span(Erosion.EROSION_0, Erosion.EROSION_3))
                .depth(Depth.SURFACE)
                .weirdness(Weirdness.span(Weirdness.LOW_SLICE_VARIANT_ASCENDING, Weirdness.HIGH_SLICE_VARIANT_ASCENDING))
                .build().forEach(point -> builder.add(point, ModBiomes.SOLAR_FOREST));

        // offset
        float offset = 0.6F;

        // Register our Solar Forest biome in the climate mapper
        builder.build().forEach(mapper);
    }
}
