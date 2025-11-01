package net.ronm19.solarium.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.ronm19.solarium.entity.ModEntities;
import net.ronm19.solarium.worldgen.ModPlacedFeatures;

public class ModNetherBiomes {
    private static void addFeature( BiomeGenerationSettings.Builder builder, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature ) {
        builder.addFeature(step, feature);
    }

    public static Biome solarAshlands( HolderGetter<PlacedFeature> placedFeatureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter ) {
        // Mob spawns
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.SOLAR_GHAST.get(), 10, 1, 1));

        // Biome features
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);

        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA);

        addFeature(biomeBuilder, GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SOLAR_ASH_PLACED_KEY);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(4.0F)
                .downfall(0.0F)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x2e1f0f) // molten dark bronze tone
                        .waterFogColor(0x2e1f0f)
                        .fogColor(0x4c2e10)   // deep burnt orange-brown
                        .skyColor(calculateSkyColor(2.0F))
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.00725f))
                        .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                        .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 90000, 8, 2.0D))
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_BASALT_DELTAS))
                        .build()
                )
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }

    protected static int calculateSkyColor( float temperature ) {
        float t = temperature / 3.0F;
        t = Mth.clamp(t, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - t * 0.05F, 0.5F + t * 0.1F, 1.0F);
    }
}
