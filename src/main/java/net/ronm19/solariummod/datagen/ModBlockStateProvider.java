package net.ronm19.solariummod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.ronm19.solariummod.SolariumMod;
import net.ronm19.solariummod.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SolariumMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SOLARIUM_BLOCK);
        blockWithItem(ModBlocks.RAW_SOLARIUM_BLOCK);
        blockWithItem(ModBlocks.SOLAR_PYLON_BLOCK);

        blockWithItem(ModBlocks.SOLARIUM_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SOLARIUM_ORE);

        stairsBlock(((StairBlock) ModBlocks.SOLARIUM_STAIRS.get()), blockTexture(ModBlocks.SOLARIUM_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.SOLARIUM_SLAB.get()), blockTexture(ModBlocks.SOLARIUM_BLOCK.get()), blockTexture(ModBlocks.SOLARIUM_BLOCK.get()));

        fenceBlock(((FenceBlock) ModBlocks.SOLARIUM_FENCE.get()), blockTexture(ModBlocks.SOLARIUM_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.SOLARIUM_FENCE_GATE.get()), blockTexture(ModBlocks.SOLARIUM_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.SOLARIUM_WALL.get()), blockTexture(ModBlocks.SOLARIUM_BLOCK.get()));

        doorBlockWithRenderType(((DoorBlock) ModBlocks.SOLARIUM_DOOR.get()), modLoc("block/solarium_door_bottom"), modLoc("block/solarium_door_top"), "cutout");
        trapdoorBlockWithRenderType(((TrapDoorBlock) ModBlocks.SOLARIUM_TRAPDOOR.get()), modLoc("block/solarium_trapdoor"), true, "cutout");

        pressurePlateBlock(((PressurePlateBlock) ModBlocks.SOLARIUM_PRESSURE_PLATE.get()), blockTexture(ModBlocks.SOLARIUM_BLOCK.get()));
        buttonBlock(((ButtonBlock) ModBlocks.SOLARIUM_BUTTON.get()), blockTexture(ModBlocks.SOLARIUM_BLOCK.get()));

        blockItem(ModBlocks.SOLARIUM_STAIRS);
        blockItem(ModBlocks.SOLARIUM_SLAB);

        blockItem(ModBlocks.SOLARIUM_PRESSURE_PLATE);

        blockItem(ModBlocks.SOLARIUM_TRAPDOOR, "_bottom");
    }

    private void blockWithItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("solariummod:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<Block> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("solariummod:block/" + deferredBlock.getId().getPath() + appendix));
    }
}
