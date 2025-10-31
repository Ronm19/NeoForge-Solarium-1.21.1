package net.ronm19.solarium.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.item.custom.*;

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

    public static final DeferredItem<Item> SOLARIUM_SWORD = ITEMS.register("solarium_sword",
            () -> new SwordItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.SOLARIUM, 8, -2.4f)).fireResistant()));
    public static final DeferredItem<Item> SOLARIUM_PICKAXE = ITEMS.register("solarium_pickaxe",
            () -> new PickaxeItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.SOLARIUM, 2, -2.8f)).fireResistant()));
    public static final DeferredItem<Item> SOLARIUM_AXE = ITEMS.register("solarium_axe",
            () -> new AxeItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.SOLARIUM, 6, -3.2f)).fireResistant()));
    public static final DeferredItem<Item> SOLARIUM_SHOVEL = ITEMS.register("solarium_shovel",
            () -> new ShovelItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.SOLARIUM, 1.5f, -3.0f)).fireResistant()));
    public static final DeferredItem<Item> SOLARIUM_HOE = ITEMS.register("solarium_hoe",
            () -> new HoeItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.SOLARIUM, 0, -3.0f)).fireResistant()));

    public static final DeferredItem<Item> SOLARIUM_PAXEL = ITEMS.register("solarium_paxel",
            () -> new SolariumPaxelItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.SOLARIUM, 5, -2.5f)).fireResistant()));
    public static final DeferredItem<Item> SOLARIUM_HAMMER = ITEMS.register("solarium_hammer",
            () -> new SolariumHammerItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.SOLARIUM, 10, -2.5f)).fireResistant()));
    public static final DeferredItem<Item> SOLAR_DAGGER = ITEMS.register("solar_dagger",
            () -> new SolarDaggerItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.SOLARIUM, 4, -2.1f)).fireResistant(), MobEffects.HARM));
    public static final DeferredItem<Item> SOLAR_FANG = ITEMS.register("solar_fang",
            () -> new SolarFangItem(ModToolTiers.SOLARIUM, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.SOLARIUM, 6, -2.1f)).fireResistant()));
    public static final DeferredItem<Item> SOLAR_BOW = ITEMS.register("solar_bow",
            () -> new BowItem(new Item.Properties().durability(2500).fireResistant()));

    public static final DeferredItem<Item> SOLARIUM_HELMET = ITEMS.register("solarium_helmet",
            () -> new ModArmorItem(ModArmorMaterials.SOLARIUM, ArmorItem.Type.HELMET,
                    new Item.Properties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(47))));
    public static final DeferredItem<Item> SOLARIUM_CHESTPLATE = ITEMS.register("solarium_chestplate",
            () -> new ArmorItem(ModArmorMaterials.SOLARIUM, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(47))));
    public static final DeferredItem<Item> SOLARIUM_LEGGINGS = ITEMS.register("solarium_leggings",
            () -> new ArmorItem(ModArmorMaterials.SOLARIUM, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(47))));
    public static final DeferredItem<Item> SOLARIUM_BOOTS = ITEMS.register("solarium_boots",
            () -> new ArmorItem(ModArmorMaterials.SOLARIUM, ArmorItem.Type.BOOTS,
                    new Item.Properties().fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(47))));

    public static final DeferredItem<Item> SOLARIUM_HORSE_ARMOR = ITEMS.register("solarium_horse_armor",
            () -> new AnimalArmorItem(ModArmorMaterials.SOLARIUM, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().fireResistant().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
