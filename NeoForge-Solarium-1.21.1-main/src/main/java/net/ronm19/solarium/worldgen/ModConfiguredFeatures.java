package net.ronm19.solarium.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SOLAR_AMBER_KEY = registerKey("solar_amber");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SOLARIUM_ORE_KEY = registerKey("solarium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_DEEPSLATE_SOLARIUM_ORE_KEY = registerKey("deepslate_solarium_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SOLAR_ROSE_KEY = registerKey("solar_rose");


    public static void bootstrap( BootstrapContext<ConfiguredFeature<?, ?>> context) {

        register(context, SOLAR_AMBER_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.SOLAR_AMBER_LOG.get()),
                new StraightTrunkPlacer(4, 5, 3),
                BlockStateProvider.simple(ModBlocks.SOLAR_AMBER_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(2), 4),
                new TwoLayersFeatureSize(1, 0, 2)).build());

        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldSolariumOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.SOLARIUM_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> overworldDeepslateSolariumOres = List.of(
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_SOLARIUM_ORE.get().defaultBlockState()));

        register(context, OVERWORLD_SOLARIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSolariumOres, 9));
        register(context, OVERWORLD_DEEPSLATE_SOLARIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldDeepslateSolariumOres, 9));

        register(context, SOLAR_ROSE_KEY, Feature.FLOWER, new RandomPatchConfiguration(32, 6, 2,
                PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.SOLAR_ROSE.get())))));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register( BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                           ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}