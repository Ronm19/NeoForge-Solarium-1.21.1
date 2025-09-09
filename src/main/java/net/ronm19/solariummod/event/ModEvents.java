package net.ronm19.solariummod.event;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.ronm19.solariummod.SolariumMod;
import net.ronm19.solariummod.item.custom.SolariumHammerItem;

import java.util.HashSet;
import java.util.Set;



@EventBusSubscriber(modid = SolariumMod.MOD_ID)
public class ModEvents {
    // Done with the help of https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java
    // Don't be a jerk License
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();


    @SubscribeEvent
    public static void onHammerUseage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.getItem() instanceof SolariumHammerItem solariumHammerItem && player instanceof ServerPlayer serverPlayer) {
            BlockPos intitalBlockPos = event.getPos();
            if (HARVESTED_BLOCKS.contains(intitalBlockPos)) {
                return;
            }

            for(BlockPos pos : SolariumHammerItem.getBlocksToBeDestroyed(3, intitalBlockPos, serverPlayer)) {
                if(pos == intitalBlockPos || !solariumHammerItem.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }

                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }
}