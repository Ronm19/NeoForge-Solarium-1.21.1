package net.ronm19.solariummod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SolarPylonBlock extends Block {
    public static final IntegerProperty POWER = BlockStateProperties.POWER; // 0..15
    private static final Logger log = LoggerFactory.getLogger(SolarPylonBlock.class);

    public SolarPylonBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    // Called when placed: compute initial power and start ticking
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            updatePower(level, pos, state);
            level.scheduleTick(pos, this, 20); // update every second
        }
    }

    // Our scheduled tick — no BlockEntity needed
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        updatePower(level, pos, state);
        level.getBlockTicks();
        int skylight = level.getBrightness(LightLayer.SKY, pos.above());


        // Store sunlight during day
        int newPower = skylight;

        // At night, you can still use the stored value (optional: reduce by 1 each tick to simulate decay)
        if (skylight == 0) {
            newPower = Math.max(0, state.getValue(POWER) - 1);
        }

        level.setBlock(pos, state.setValue(POWER, newPower), 3);

        // Notify redstone neighbors
        level.updateNeighborsAt(pos, this);

        // Schedule next tick
        level.getBlockTicks().hasScheduledTick(pos, this); // every second
    }


    // Also update quickly if neighbors change (optional but snappier)
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
        super.neighborChanged(state, level, pos, block, fromPos, moving);
        if (!level.isClientSide) {
            updatePower(level, pos, state);
        }
    }

    private void updatePower(Level level, BlockPos pos, BlockState state) {
        // Measure skylight just above the pylon; require direct sky if you like
        int sky = level.getBrightness(LightLayer.SKY, pos.above());
        if (!level.canSeeSky(pos.above())) {
            sky = 0; // comment this line out if you want indirect skylight to count
        }

        int current = state.getValue(POWER);
        if (current != sky) {
            level.setBlock(pos, state.setValue(POWER, sky), Block.UPDATE_CLIENTS);
            level.updateNeighborsAt(pos, this); // push redstone update
        }
    }

    // Make this a redstone source
    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return state.getValue(POWER);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return getSignal(state, level, pos, side);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter lvl, BlockPos pos) {
        return state.getValue(POWER); // or some mapping you prefer
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

       tooltipComponents.add(Component.translatable("tooltip.solariummod.solar_pylon_block.tooltip.1"));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
