package net.ronm19.solarium.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.PotionBrewEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;
import net.ronm19.solarium.item.ModItems;
import net.ronm19.solarium.item.custom.SolariumHammerItem;
import net.ronm19.solarium.potion.ModPotions;
import net.ronm19.solarium.villager.ModVillagers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = SolariumMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {


    public static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();
    @SubscribeEvent
    public static void onSolariumHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();

        if(mainHandItem.getItem() instanceof SolariumHammerItem solariumHammer && player instanceof ServerPlayer serverPlayer) {
            BlockPos initialBlockPos = event.getPos();
            if(HARVESTED_BLOCKS.contains(initialBlockPos)) {
                return;
            }

            for(BlockPos pos : SolariumHammerItem.getBlocksToBeDestroyed(2, initialBlockPos, serverPlayer)) {
                if(pos == initialBlockPos || !solariumHammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }

                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, ModBlocks.SOLAR_ROSE.asItem(), ModPotions.SOLAR_BLESSING_POTION);
    }
    @SubscribeEvent
    public static void addCustomTrades( VillagerTradesEvent event) {
        if(event.getType() == ModVillagers.SOLARGER.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

          trades.get(1).add(( entity, randomSource ) -> new MerchantOffer(
                  new ItemCost(Items.EMERALD, 3),
                  new ItemStack(ModItems.SOLARIUM_INGOT.get(), 5), 8, 4, 0.05f
          ));

          trades.get(2).add(( entity, randomSource ) -> new MerchantOffer(
                  new ItemCost(ModItems.SOLARIUM_INGOT, 10),
                  new ItemStack(ModItems.SOLARIUM_SWORD.get(), 1), 7, 8, 0.05f
          ));

          trades.get(3).add(( entity, randomSource ) -> new MerchantOffer(
                  new ItemCost(ModItems.SOLARIUM_INGOT, 20),
                  new ItemStack(ModItems.SOLAR_DAGGER.get(), 1), 2, 11, 0.07f
          ));

          trades.get(4).add(( entity, randomSource ) -> new MerchantOffer(
                  new ItemCost(ModItems.SOLARIUM_INGOT, 40),
                  new ItemStack(ModItems.SOLARIUM_HAMMER.get(), 1), 2, 15, 0.09f
          ));

          trades.get(5).add(( entity, randomSource ) -> new MerchantOffer(
                  new ItemCost(ModItems.SOLARIUM_INGOT, 80),
                  new ItemStack(ModItems.SOLAR_EDGE.get(), 1), 1, 25, 0.11f
          ));




      }
    }
}
