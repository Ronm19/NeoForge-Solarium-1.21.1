package net.ronm19.solarium.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper ) {
        super(output, SolariumMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SOLARIUM_BLOCK);
        blockWithItem(ModBlocks.RAW_SOLARIUM_BLOCK);
        blockWithItem(ModBlocks.DEEPSLATE_SOLARIUM_ORE);
        blockWithItem(ModBlocks.SOLARIUM_ORE);

        blockWithItem(ModBlocks.SOLAR_EMBER_BLOCK);
    }

    private void blockWithItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

}
