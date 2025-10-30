package net.ronm19.solarium.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ronm19.solarium.SolariumMod;
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
    }
}
