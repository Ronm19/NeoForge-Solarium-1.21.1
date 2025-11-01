package net.ronm19.solarium.worldgen.biome;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.ronm19.solarium.block.ModBlocks;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource SOLAR_GRASS_BLOCK = makeStateRule(ModBlocks.SOLAR_GRASS_BLOCK.get());
    private static final SurfaceRules.RuleSource SOLAR_DIRT_BLOCK = makeStateRule(ModBlocks.SOLAR_DIRT_BLOCK.get());

    private static final SurfaceRules.RuleSource SOLAR_ASH = makeStateRule(ModBlocks.SOLAR_ASH_BLOCK.get());
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);


    public static SurfaceRules.RuleSource makeSolarForestRules() {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.SOLAR_FOREST),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SOLAR_GRASS_BLOCK), SOLAR_DIRT_BLOCK)),
                // Default to green terracotta
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SOLAR_DIRT_BLOCK)
        );
    }

    public static SurfaceRules.RuleSource makeSolarAshlandsRules() {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK),
                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK),

                // Then apply biome-specific rules
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(ModBiomes.SOLAR_ASHLANDS),
                        SurfaceRules.sequence(
                                // Obsidian on the undersides of ceilings
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, SOLAR_ASH),
                                // Obsidian on the undersides of floors (though less common in Nether caves)
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SOLAR_ASH),
                                SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SOLAR_ASH),
                                // Default to glowstone if not under a ceiling or floor
                                SOLAR_ASH))
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
