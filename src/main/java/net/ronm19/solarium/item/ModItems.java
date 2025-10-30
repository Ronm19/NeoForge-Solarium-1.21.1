package net.ronm19.solarium.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.item.custom.FuelItem;
import net.ronm19.solarium.item.custom.SolariumChainsawItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SolariumMod.MOD_ID);

    public static final DeferredItem<Item> SOLARIUM_INGOT = ITEMS.registerItem("solarium_ingot",
            Item::new, new Item.Properties().fireResistant());
    public static final DeferredItem<Item> RAW_SOLARIUM_INGOT = ITEMS.registerItem("raw_solarium_ingot",
            Item::new, new Item.Properties().fireResistant());

    public static final DeferredItem<Item> SOLAR_EMBER = ITEMS.registerItem("solar_ember", properties ->
            new FuelItem(properties, 800), new Item.Properties());

    public static final DeferredItem<Item> SOLARIUM_CHAINSAW = ITEMS.registerItem("solarium_chainsaw",
            SolariumChainsawItem::new, new Item.Properties().durability(2030).fireResistant());

    public static final DeferredItem<Item> SOLAR_APPLE = ITEMS.registerItem("solar_apple",
            Item::new, new Item.Properties().food(ModFoodProperties.SOLAR_APPLE).fireResistant());
    public static final DeferredItem<Item> SOLAR_TOMATO = ITEMS.registerItem("solar_tomato",
            Item::new, new Item.Properties().food(ModFoodProperties.SOLAR_TOMATO).fireResistant());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
