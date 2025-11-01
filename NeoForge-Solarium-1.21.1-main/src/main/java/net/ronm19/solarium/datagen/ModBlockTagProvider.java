package net.ronm19.solarium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;
import net.ronm19.solarium.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider( PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper ) {
        super(output, lookupProvider, SolariumMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags( HolderLookup.@NotNull Provider provider ) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SOLARIUM_BLOCK.get())
                .add(ModBlocks.RAW_SOLARIUM_BLOCK.get())
                .add(ModBlocks.SOLARIUM_ORE.get())
                .add(ModBlocks.DEEPSLATE_SOLARIUM_ORE.get())
                .add(ModBlocks.SOLARIUM_WALL.get())
                .add(ModBlocks.SOLAR_EMBER_BLOCK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SOLARIUM_BLOCK.get())
                .add(ModBlocks.RAW_SOLARIUM_BLOCK.get())
                .add(ModBlocks.SOLARIUM_ORE.get())
                .add(ModBlocks.SOLARIUM_WALL.get())
                .add(ModBlocks.DEEPSLATE_SOLARIUM_ORE.get())
                .add(ModBlocks.SOLAR_EMBER_BLOCK.get());

        this.tag(ModTags.Blocks.SOLARIUM_PAXEL_MINEABLE)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BlockTags.MINEABLE_WITH_SHOVEL)
                .addTag(BlockTags.MINEABLE_WITH_AXE);

        this.tag(BlockTags.DIRT)
                .add(ModBlocks.SOLAR_GRASS_BLOCK.get())
                .add(ModBlocks.SOLAR_DIRT_BLOCK.get());

        this.tag(BlockTags.CRIMSON_STEMS)
                .add(ModBlocks.SOLAR_ASH_BLOCK.get());

        this.tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.SOLARIUM_FENCE.get());

        this.tag(BlockTags.LOGS)
                .add(ModBlocks.SOLAR_AMBER_LOG.get())
                .add(ModBlocks.SOLAR_AMBER_WOOD.get())
                .add(ModBlocks.STRIPPED_SOLAR_AMBER_LOG.get())
                .add(ModBlocks.STRIPPED_SOLAR_AMBER_WOOD.get())
                .add(ModBlocks.SOLAR_ASH_LOG.get())
                .add(ModBlocks.SOLAR_ASH_WOOD.get())
                .add(ModBlocks.STRIPPED_SOLAR_ASH_LOG.get())
                .add(ModBlocks.STRIPPED_SOLAR_ASH_WOOD.get());


        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.SOLAR_AMBER_PLANKS.get())
                .add(ModBlocks.SOLAR_ASH_PLANKS.get());

        this.tag(BlockTags.FENCES);

        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.SOLARIUM_FENCE_GATE.get());

        this.tag(BlockTags.WALLS)
                .add(ModBlocks.SOLARIUM_WALL.get());

    }
}
