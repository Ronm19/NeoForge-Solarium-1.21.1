package net.ronm19.solariummod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ronm19.solariummod.SolariumMod;
import net.ronm19.solariummod.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SolariumMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SOLARIUM_BLOCK.get())
                .add(ModBlocks.RAW_SOLARIUM_BLOCK.get())
                .add(ModBlocks.SOLAR_PYLON_BLOCK.get())
                .add(ModBlocks.SOLARIUM_ORE.get())
                .add(ModBlocks.DEEPSLATE_SOLARIUM_ORE.get())
                .add(ModBlocks.SOLARIUM_STAIRS.get())
                .add(ModBlocks.SOLARIUM_SLAB.get())
                .add(ModBlocks.SOLARIUM_PRESSURE_PLATE.get())
                .add(ModBlocks.SOLARIUM_BUTTON.get())
                .add(ModBlocks.SOLARIUM_WALL.get())
                .add(ModBlocks.SOLARIUM_FENCE.get())
                .add(ModBlocks.SOLARIUM_FENCE_GATE.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SOLARIUM_BLOCK.get())
                .add(ModBlocks.RAW_SOLARIUM_BLOCK.get())
                .add(ModBlocks.SOLAR_PYLON_BLOCK.get())
                .add(ModBlocks.SOLARIUM_ORE.get())
                .add(ModBlocks.DEEPSLATE_SOLARIUM_ORE.get());

        tag(BlockTags.FENCES).add(ModBlocks.SOLARIUM_FENCE.get());
        tag(BlockTags.FENCE_GATES).add(ModBlocks.SOLARIUM_FENCE_GATE.get());
        tag(BlockTags.WALLS).add(ModBlocks.SOLARIUM_WALL.get());

    }
}
