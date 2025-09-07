package net.ronm19.solariummod.item.custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class SolariumChainsawItem extends Item {
    public SolariumChainsawItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide() && context.getPlayer() instanceof ServerPlayer player) {
            BlockPos clickedPos = context.getClickedPos();
            ItemStack stack = context.getItemInHand();

            if (level.getBlockState(clickedPos).is(BlockTags.LOGS)) {
                // Flood fill to collect all connected logs
                Set<BlockPos> logs = collectConnectedLogs(level, clickedPos, 500); // higher limit for big trees
                for (BlockPos pos : logs) {
                    level.destroyBlock(pos, true, player);
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }

                // Collect leaves around chopped logs
                Set<BlockPos> leaves = collectNearbyLeaves(level, logs, 6);
                for (BlockPos pos : leaves) {
                    // Destroy leaves quickly
                    level.destroyBlock(pos, false, player);

                    // Try placing fire on top
                    BlockPos firePos = pos.above();
                    var fireState = BaseFireBlock.getState(level, firePos);
                    if (fireState != null && fireState.canSurvive(level, firePos)) {
                        level.setBlock(firePos, fireState, 3);
                    }
                }

                return InteractionResult.CONSUME;
            }
        }

        return super.useOn(context);
    }

    /**
     * Collects all connected log blocks using DFS (any direction).
     */
    private Set<BlockPos> collectConnectedLogs(Level level, BlockPos start, int limit) {
        Set<BlockPos> visited = new HashSet<>();
        Stack<BlockPos> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty() && visited.size() < limit) {
            BlockPos pos = stack.pop();
            if (visited.contains(pos)) continue;

            BlockState state = level.getBlockState(pos);
            if (state.is(BlockTags.LOGS)) {
                visited.add(pos);

                // Check surrounding blocks in a 3x3x3 cube
                for (BlockPos offset : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                    if (!visited.contains(offset)) {
                        stack.push(offset.immutable());
                    }
                }
            }
        }

        return visited;
    }

    /**
     * Collects nearby leaves around chopped logs.
     */
    private Set<BlockPos> collectNearbyLeaves(Level level, Set<BlockPos> logs, int radius) {
        Set<BlockPos> leaves = new HashSet<>();

        for (BlockPos log : logs) {
            for (BlockPos pos : BlockPos.betweenClosed(
                    log.offset(-radius, -radius, -radius),
                    log.offset(radius, radius, radius))) {
                BlockState state = level.getBlockState(pos);
                if (state.is(BlockTags.LEAVES)) {
                    leaves.add(pos.immutable());
                }
            }
        }

        return leaves;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.solariummod.solarium_chainsaw.tooltip_1"));
            tooltipComponents.add(Component.translatable("tooltip.solariummod.solarium_chainsaw.tooltip_2"));

        } else {
            tooltipComponents.add(Component.translatable("tooltip.solariummod.solarium_chainsaw.tooltip_shift"));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
