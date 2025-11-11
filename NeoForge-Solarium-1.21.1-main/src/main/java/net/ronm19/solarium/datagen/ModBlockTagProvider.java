package net.ronm19.solarium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;
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
                .add(ModBlocks.SOLAR_EMBER_BLOCK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SOLARIUM_BLOCK.get())
                .add(ModBlocks.RAW_SOLARIUM_BLOCK.get())
                .add(ModBlocks.SOLARIUM_ORE.get())
                .add(ModBlocks.DEEPSLATE_SOLARIUM_ORE.get())
                .add(ModBlocks.SOLAR_EMBER_BLOCK.get());
    }
}
