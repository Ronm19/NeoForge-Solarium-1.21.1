package net.ronm19.solariummod.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface TickableBlock {
    void tick(BlockState state, ServerLevel level, BlockPos pos, java.util.Random rand);
}
