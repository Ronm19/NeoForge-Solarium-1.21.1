package net.ronm19.solarium.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class SolariumChainsawItem extends Item {

    public SolariumChainsawItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide()) {
            BlockPos startPos = context.getClickedPos();
            Player player = context.getPlayer();

            if (level.getBlockState(startPos).is(BlockTags.LOGS)) {
                // Recursively destroy the tree
                destroyTree(level, startPos, player);

                // Damage the chainsaw
                context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), ((ServerPlayer) context.getPlayer()),
                        item -> Objects.requireNonNull(context.getPlayer()).onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

                return InteractionResult.CONSUME;
            }
        }

        return super.useOn(context);
    }

    private void destroyTree( Level level, BlockPos pos, Player player) {
        // Avoid infinite recursion: only consider logs and leaves
        if (!level.isLoaded(pos)) return;

        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES)) {
            // Destroy block and drop items
            level.destroyBlock(pos, true, player);

            // Set fire where the block was
            level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);

            // Check all 6 neighboring blocks (including diagonals for leaves if needed)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx != 0 || dy != 0 || dz != 0) {
                            destroyTree(level, pos.offset(dx, dy, dz), player);
                        }
                    }
                }
            }
        }
    }
}
