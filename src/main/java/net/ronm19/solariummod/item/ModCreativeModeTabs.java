package net.ronm19.solariummod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solariummod.SolariumMod;
import net.ronm19.solariummod.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SolariumMod.MOD_ID);

    public static final Supplier<CreativeModeTab> SOLARIUM_ITEMS_TAB =
            CREATIVE_MODE_TABS.register("solarium_items_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.solariummod.solarium_items_tab"))
                    .icon(() -> new ItemStack(ModItems.SOLARIUM_INGOT.get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.SOLARIUM_INGOT);
                        output.accept(ModItems.RAW_SOLARIUM_INGOT);

                        output.accept(ModItems.SOLAR_FRUIT);

                        output.accept(ModItems.SOLAR_EMBER);

                        output.accept(ModItems.SOLARIUM_CHAINSAW);

                        output.accept(ModItems.SOLARIUM_SWORD);
                        output.accept(ModItems.SOLARIUM_PICKAXE);
                        output.accept(ModItems.SOLARIUM_AXE);
                        output.accept(ModItems.SOLARIUM_SHOVEL);
                        output.accept(ModItems.SOLARIUM_HOE);

                        output.accept(ModItems.SOLARIUM_PAXEL);
                        output.accept(ModItems.SOLARIUM_HAMMER);
                        output.accept(ModItems.SOLARIUM_DAGGER);

                        output.accept(ModItems.SOLARIUM_HELMET);
                        output.accept(ModItems.SOLARIUM_CHESTPLATE);
                        output.accept(ModItems.SOLARIUM_LEGGINGS);
                        output.accept(ModItems.SOLARIUM_BOOTS);

                        output.accept(ModItems.SOLARIUM_HORSE_ARMOR);

                    })

                    .build());

    public static final Supplier<CreativeModeTab> SOLARIUM_BLOCKS_TAB =
            CREATIVE_MODE_TABS.register("solarium_blocks_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.solariummod.solarium_blocks_tab"))
                    .icon(() -> new ItemStack(ModBlocks.SOLARIUM_BLOCK.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solarium_items_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.SOLARIUM_BLOCK);
                        output.accept(ModBlocks.RAW_SOLARIUM_BLOCK);

                        output.accept(ModBlocks.SOLARIUM_ORE);
                        output.accept(ModBlocks.DEEPSLATE_SOLARIUM_ORE);

                        output.accept(ModBlocks.SOLAR_PYLON_BLOCK);

                        output.accept(ModBlocks.SOLARIUM_STAIRS);
                        output.accept(ModBlocks.SOLARIUM_SLAB);

                        output.accept(ModBlocks.SOLARIUM_FENCE);
                        output.accept(ModBlocks.SOLARIUM_FENCE_GATE);
                        output.accept(ModBlocks.SOLARIUM_WALL);

                        output.accept(ModBlocks.SOLARIUM_PRESSURE_PLATE);
                        output.accept(ModBlocks.SOLARIUM_BUTTON);

                        output.accept(ModBlocks.SOLARIUM_DOOR);
                        output.accept(ModBlocks.SOLARIUM_TRAPDOOR);

                    })
                    .build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
