package net.ronm19.solariummod.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
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

    public static final DeferredBlock<Block> SOLARIUM_STAIRS = registerBlock("solarium_stairs",
            () -> new StairBlock(ModBlocks.SOLARIUM_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f).
                    requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> SOLARIUM_SLAB = registerBlock("solarium_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SOLARIUM_PRESSURE_PLATE = registerBlock("solarium_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.IRON, BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> SOLARIUM_BUTTON = registerBlock("solarium_button",
            () -> new ButtonBlock(BlockSetType.IRON, 10, BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().noCollission()));

    public static final DeferredBlock<Block> SOLARIUM_FENCE = registerBlock("solarium_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> SOLARIUM_FENCE_GATE = registerBlock("solarium_fence_gate",
            () -> new FenceGateBlock(WoodType.ACACIA,BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> SOLARIUM_WALL = registerBlock("solarium_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SOLARIUM_DOOR = registerBlock("solarium_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> SOLARIUM_TRAPDOOR = registerBlock("solarium_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().noOcclusion()));

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
