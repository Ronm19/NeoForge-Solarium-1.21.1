package net.ronm19.solarium.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.custom.SolarLampBlock;
import net.ronm19.solarium.block.custom.SolarTomatoCropBlock;
import net.ronm19.solarium.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(SolariumMod.MOD_ID);


    public static final DeferredBlock<Block> SOLARIUM_BLOCK = registerBlock("solarium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> RAW_SOLARIUM_BLOCK = registerBlock("raw_solarium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SOLAR_EMBER_BLOCK = registerBlock("solar_ember_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SOLARIUM_ORE = registerBlock("solarium_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 5),BlockBehaviour.Properties.of().strength(4f).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DEEPSLATE_SOLARIUM_ORE = registerBlock("deepslate_solarium_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 7),BlockBehaviour.Properties.of().strength(4f).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SOLAR_ASH_BLOCK = registerBlock("solar_ash_block",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL)));
    public static final DeferredBlock<Block> SOLAR_GRASS_BLOCK = registerBlock("solar_grass_block",
            () -> new GrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> SOLAR_DIRT_BLOCK = registerBlock("solar_dirt_block",
            () -> new RootedDirtBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.ROOTED_DIRT)));

    public static final DeferredBlock<Block> SOLAR_LAMP_BLOCK = registerBlock("solar_lamp_block",
            () -> new SolarLampBlock(BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)
                    .lightLevel(state -> state.getValue(SolarLampBlock.CLICKED) ? 15 : 0)));

    public static final DeferredBlock<Block> SOLAR_TOMATO_CROP = BLOCKS.register("solar_tomato_crop",
            () -> new SolarTomatoCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)));

    public static final DeferredBlock<Block> SOLAR_ROSE = registerBlock("solar_rose",
            () -> new FlowerBlock(MobEffects.FIRE_RESISTANCE, 15, BlockBehaviour.Properties.ofFullCopy(Blocks.WITHER_ROSE)));
    public static final DeferredBlock<Block> POTTED_SOLAR_ROSE = BLOCKS.register("potted_solar_rose",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), SOLAR_ROSE, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_WITHER_ROSE)));

    public static final DeferredBlock<Block> SOLAR_WORKBENCH_BLOCK = registerBlock("solar_workbench_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> SOLARIUM_STAIRS = registerBlock("solarium_stairs",
            () -> new StairBlock(ModBlocks.SOLARIUM_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SOLARIUM_SLAB = registerBlock("solarium_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> SOLARIUM_PRESSURE_PLATE = registerBlock("solarium_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).sound( SoundType.STONE )));
    public static final DeferredBlock<Block> SOLARIUM_BUTTON = registerBlock("solarium_button",
            () -> new ButtonBlock(BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON).sound(SoundType.STONE).noCollission()));

    public static final DeferredBlock<Block> SOLARIUM_FENCE = registerBlock("solarium_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SOLARIUM_FENCE_GATE = registerBlock("solarium_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SOLARIUM_WALL = registerBlock("solarium_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE_WALL).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SOLARIUM_TRAPDOOR = registerBlock("solarium_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<Block> SOLARIUM_DOOR = registerBlock("solarium_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).sound(SoundType.STONE).noOcclusion()));







    private static <T extends Block>DeferredBlock<T> registerBlock( String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;

    }

    private static <T extends Block> void registerBlockItem( String name, DeferredBlock<T> block ) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
