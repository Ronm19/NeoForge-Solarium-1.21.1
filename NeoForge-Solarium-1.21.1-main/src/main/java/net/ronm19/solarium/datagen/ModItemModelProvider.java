package net.ronm19.solarium.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;
import net.ronm19.solarium.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider( PackOutput output, ExistingFileHelper existingFileHelper ) {
        super(output, SolariumMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.SOLARIUM_INGOT.get());
        basicItem(ModItems.SOLARIUM_CHAINSAW.get());
        basicItem(ModItems.RAW_SOLARIUM_INGOT.get());
        basicItem(ModItems.SOLAR_APPLE.get());
        basicItem(ModItems.SOLAR_TOMATO.get());
        basicItem(ModItems.SOLAR_EMBER.get());

        basicItem(ModItems.SOLAR_TOMATO_SEEDS.get());
        flowerItem(ModBlocks.SOLAR_ROSE);

        buttonItem(ModBlocks.SOLARIUM_BUTTON, ModBlocks.SOLARIUM_BLOCK);
        fenceItem(ModBlocks.SOLARIUM_FENCE, ModBlocks.SOLARIUM_BLOCK);
        wallItem(ModBlocks.SOLARIUM_WALL, ModBlocks.SOLARIUM_BLOCK);
        basicItem(ModBlocks.SOLARIUM_DOOR.asItem());

        handheldItem(ModItems.SOLARIUM_SWORD);
        handheldItem(ModItems.SOLARIUM_PICKAXE);
        handheldItem(ModItems.SOLARIUM_AXE);
        handheldItem(ModItems.SOLARIUM_SHOVEL);
        handheldItem(ModItems.SOLARIUM_HOE);
        handheldItem(ModItems.SOLARIUM_PAXEL);
        handheldItem(ModItems.SOLARIUM_HAMMER);
        handheldItem(ModItems.SOLAR_DAGGER);
        handheldItem(ModItems.SOLAR_FANG);

        basicItem(ModItems.SOLARIUM_HELMET.get());
        basicItem(ModItems.SOLARIUM_CHESTPLATE.get());
        basicItem(ModItems.SOLARIUM_LEGGINGS.get());
        basicItem(ModItems.SOLARIUM_BOOTS.get());

        basicItem(ModItems.SOLARIUM_HORSE_ARMOR.get());
    }

    public void flowerItem(DeferredBlock<Block> block) {
        this.withExistingParent(block.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0",  ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID,
                        "block/" + block.getId().getPath()));
    }

    public void buttonItem( DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void fenceItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void wallItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    private ItemModelBuilder handheldItem( DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}
