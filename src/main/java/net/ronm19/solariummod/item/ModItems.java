package net.ronm19.solariummod.item;

import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solariummod.SolariumMod;
import net.ronm19.solariummod.item.custom.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SolariumMod.MOD_ID);

    public static final DeferredItem<Item> SOLARIUM_INGOT = ITEMS.registerSimpleItem("solarium_ingot");
    public static final DeferredItem<Item> RAW_SOLARIUM_INGOT = ITEMS.registerItem("raw_solarium_ingot",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> SOLAR_FRUIT =
            ITEMS.registerItem("solar_fruit", Item::new, new Item.Properties().food(ModFoodProperties.SOLAR_FRUIT));
public static final DeferredItem<Item> SOLAR_EMBER =
            ITEMS.registerItem("solar_ember", properties -> new FuelItem(properties, 1600), new Item.Properties());

    public static final DeferredItem<Item> SOLARIUM_CHAINSAW =
            ITEMS.registerItem("solarium_chainsaw", SolariumChainsawItem::new, new Item.Properties().durability(2030));

    public static final DeferredItem<Item> SOLARIUM_SWORD = ITEMS.register("solarium_sword",
            () -> new SolariumSwordItem(ModToolTiers.SOLARIUM_INGOT, new Item.Properties()
                    .fireResistant().attributes(SwordItem.createAttributes(ModToolTiers.SOLARIUM_INGOT, 9, -2.4f))));

    public static final DeferredItem<Item> SOLARIUM_PICKAXE = ITEMS.register("solarium_pickaxe",
            () -> new SolariumPickaxeItem(ModToolTiers.SOLARIUM_INGOT, new Item.Properties()
                    .fireResistant().attributes(PickaxeItem.createAttributes(ModToolTiers.SOLARIUM_INGOT, 3, -2.8f))));

    public static final DeferredItem<Item> SOLARIUM_AXE = ITEMS.register("solarium_axe",
            () -> new SolariumAxeItem(ModToolTiers.SOLARIUM_INGOT, new Item.Properties()
                    .fireResistant().attributes(AxeItem.createAttributes(ModToolTiers.SOLARIUM_INGOT, 6, -3.0f))));

    public static final DeferredItem<Item> SOLARIUM_SHOVEL = ITEMS.register("solarium_shovel",
            () -> new SolariumShovelItem(ModToolTiers.SOLARIUM_INGOT, new Item.Properties()
                    .fireResistant().attributes(ShovelItem.createAttributes(ModToolTiers.SOLARIUM_INGOT, 1.5f, -3.2f))));

    public static final DeferredItem<Item> SOLARIUM_HOE = ITEMS.register("solarium_hoe",
            () -> new SolariumHoeItem(ModToolTiers.SOLARIUM_INGOT, new Item.Properties()
                    .fireResistant().attributes(HoeItem.createAttributes(ModToolTiers.SOLARIUM_INGOT, 0, -3.0f))));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
