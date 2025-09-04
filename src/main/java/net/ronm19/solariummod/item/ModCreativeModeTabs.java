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

                        output.accept(ModItems.SOLARIUM_CHAINSAW);

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

                    })
                    .build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
