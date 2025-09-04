package net.ronm19.solariummod.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface OperateableMineBlock {
    boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player);
}
