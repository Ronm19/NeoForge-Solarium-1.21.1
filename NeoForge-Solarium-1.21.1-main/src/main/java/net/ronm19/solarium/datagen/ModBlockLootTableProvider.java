package net.ronm19.solarium.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.ronm19.solarium.block.ModBlocks;
import net.ronm19.solarium.item.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider provider ) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.SOLARIUM_BLOCK.get());
        dropSelf(ModBlocks.RAW_SOLARIUM_BLOCK.get());
        dropSelf(ModBlocks.SOLAR_EMBER_BLOCK.get());

        this.add(ModBlocks.SOLARIUM_ORE.get(),
                block -> createOreDrop(ModBlocks.SOLARIUM_ORE.get(), ModItems.RAW_SOLARIUM_INGOT.get()));
        this.add(ModBlocks.DEEPSLATE_SOLARIUM_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.DEEPSLATE_SOLARIUM_ORE.get(), ModItems.RAW_SOLARIUM_INGOT.get(), 2, 7));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder :: value)::iterator;
    }

    protected LootTable.Builder createMultipleOreDrops( Block block, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder<?>)this.applyExplosionDecay(
                        block, LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                                .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }
}
