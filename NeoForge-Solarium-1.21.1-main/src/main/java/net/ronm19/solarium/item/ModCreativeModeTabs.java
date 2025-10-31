package net.ronm19.solarium.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SolariumMod.MOD_ID);

    public static Supplier<CreativeModeTab> SOLARIUM_MOD_ITEMS_TAB =
            CREATIVE_MODE_TABS.register("solarium_mod_items_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.solarium.solarium_mod_items_tab"))
                    .icon(() -> new ItemStack(ModItems.SOLARIUM_INGOT.get()))
                    .displayItems(( parameters, output ) -> {

                        output.accept(ModItems.SOLARIUM_INGOT);
                        output.accept(ModItems.RAW_SOLARIUM_INGOT);

                        output.accept(ModItems.SOLARIUM_CHAINSAW);

                        output.accept(ModItems.SOLAR_EMBER);

                        output.accept(ModItems.SOLAR_APPLE);
                        output.accept(ModItems.SOLAR_TOMATO);

                    }).build());


    public static Supplier<CreativeModeTab> SOLARIUM_MOD_BLOCKS_TAB =
            CREATIVE_MODE_TABS.register("solarium_mod_blocks_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.solarium.solarium_mod_blocks_tab"))
                    .icon(() -> new ItemStack(ModBlocks.RAW_SOLARIUM_BLOCK.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solarium_mod_items_tab"))
                    .displayItems(( parameters, output ) -> {

                       output.accept(ModBlocks.SOLARIUM_BLOCK);
                       output.accept(ModBlocks.RAW_SOLARIUM_BLOCK);

                       output.accept(ModBlocks.SOLARIUM_ORE);
                       output.accept(ModBlocks.DEEPSLATE_SOLARIUM_ORE);

                       output.accept(ModBlocks.SOLAR_EMBER_BLOCK);

                       output.accept(ModBlocks.SOLAR_ASH_BLOCK);
                       output.accept(ModBlocks.SOLAR_GRASS_BLOCK);
                       output.accept(ModBlocks.SOLAR_DIRT_BLOCK);

                       output.accept(ModBlocks.SOLAR_WORKBENCH_BLOCK);

                       output.accept(ModBlocks.SOLARIUM_STAIRS);
                       output.accept(ModBlocks.SOLARIUM_SLAB);
                       output.accept(ModBlocks.SOLARIUM_BUTTON);
                       output.accept(ModBlocks.SOLARIUM_PRESSURE_PLATE);
                       output.accept(ModBlocks.SOLARIUM_FENCE);
                       output.accept(ModBlocks.SOLARIUM_FENCE_GATE);
                       output.accept(ModBlocks.SOLARIUM_WALL);
                       output.accept(ModBlocks.SOLARIUM_TRAPDOOR);
                       output.accept(ModBlocks.SOLARIUM_DOOR);

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
