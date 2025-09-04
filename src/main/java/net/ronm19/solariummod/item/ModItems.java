package net.ronm19.solariummod.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solariummod.SolariumMod;
import net.ronm19.solariummod.item.custom.SolariumChainsawItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SolariumMod.MOD_ID);

    public static final DeferredItem<Item> SOLARIUM_INGOT = ITEMS.registerSimpleItem("solarium_ingot");
    public static final DeferredItem<Item> RAW_SOLARIUM_INGOT = ITEMS.registerItem("raw_solarium_ingot",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> SOLAR_FRUIT =
            ITEMS.registerItem("solar_fruit", Item::new, new Item.Properties().food(ModFoodProperties.SOLAR_FRUIT));

    public static final DeferredItem<Item> SOLARIUM_CHAINSAW =
            ITEMS.registerItem("solarium_chainsaw", SolariumChainsawItem::new, new Item.Properties().durability(2030));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
