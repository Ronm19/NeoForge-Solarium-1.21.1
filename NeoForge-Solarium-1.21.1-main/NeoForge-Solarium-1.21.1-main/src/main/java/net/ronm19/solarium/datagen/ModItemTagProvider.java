package net.ronm19.solarium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider( PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags,
                               @Nullable ExistingFileHelper existingFileHelper ) {
        super(output, lookupProvider, blockTags, SolariumMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags( HolderLookup.Provider provider ) {
        tag(ItemTags.LOGS)
                .add(ModBlocks.SOLAR_AMBER_LOG.asItem())
                .add(ModBlocks.SOLAR_AMBER_WOOD.asItem())
                .add(ModBlocks.STRIPPED_SOLAR_AMBER_LOG.asItem())
                .add(ModBlocks.STRIPPED_SOLAR_AMBER_WOOD.asItem())

                .add(ModBlocks.SOLAR_ASH_LOG.asItem())
                .add(ModBlocks.SOLAR_ASH_WOOD.asItem())
                .add(ModBlocks.STRIPPED_SOLAR_ASH_LOG.asItem())
                .add(ModBlocks.STRIPPED_SOLAR_ASH_WOOD.asItem());

        tag(ItemTags.PLANKS)
                .add(ModBlocks.SOLAR_AMBER_PLANKS.asItem())
                .add(ModBlocks.SOLAR_ASH_PLANKS.asItem());
    }
}
