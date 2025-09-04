package net.ronm19.solariummod.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solariummod.SolariumMod;
import net.ronm19.solariummod.block.custom.SolarPylonBlock;
import net.ronm19.solariummod.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SolariumMod.MOD_ID);

    public static final DeferredBlock<Block> SOLARIUM_BLOCK = registerBlock("solarium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> RAW_SOLARIUM_BLOCK = registerBlock("raw_solarium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));

public static final DeferredBlock<Block> SOLARIUM_ORE = registerBlock("solarium_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 5),
                    BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DEEPSLATE_SOLARIUM_ORE = registerBlock("deepslate_solarium_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 7),
                    BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));


    public static final DeferredBlock<Block> SOLAR_PYLON_BLOCK = registerBlock("solar_pylon_block",
            () -> new SolarPylonBlock(BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops()));

    private static <T extends Block>DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;

    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
