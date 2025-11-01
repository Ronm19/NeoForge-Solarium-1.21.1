package net.ronm19.solarium;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.ronm19.solarium.block.ModBlocks;
import net.ronm19.solarium.effect.ModEffects;
import net.ronm19.solarium.enchantment.ModEnchantmentEffects;
import net.ronm19.solarium.enchantment.ModEnchantments;
import net.ronm19.solarium.item.ModArmorMaterials;
import net.ronm19.solarium.item.ModCreativeModeTabs;
import net.ronm19.solarium.item.ModItems;
import net.ronm19.solarium.potion.ModPotions;
import net.ronm19.solarium.sound.ModSounds;
import net.ronm19.solarium.villager.ModVillagers;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SolariumMod.MOD_ID)
public class SolariumMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "solarium";
    // Directly reference a slf4j logger

    public static final Logger LOGGER = LogUtils.getLogger();
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.

    public SolariumMod( IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModArmorMaterials.register(modEventBus);

        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);

        ModVillagers.register(modEventBus);

        ModEnchantmentEffects.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
          ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.SOLAR_ROSE.getId(), ModBlocks.POTTED_SOLAR_ROSE);
      });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = SolariumMod.MOD_ID)
    static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
