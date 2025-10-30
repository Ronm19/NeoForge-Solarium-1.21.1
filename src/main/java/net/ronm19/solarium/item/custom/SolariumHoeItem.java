package net.ronm19.solarium.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.ronm19.solariummod.util.ModDataComponents;

import java.util.Optional;

public class SolariumHoeItem extends HoeItem {

    private final Tier tier;

    public SolariumHoeItem(Tier tier, Properties properties) {
        super(tier, properties);
        this.tier = tier;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);

        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);

        // Check if the clicked block is a crop
        if (charge >= 50 && state.getBlock() instanceof CropBlock cropBlock) {

            // Try to find the AGE property dynamically
            Optional<IntegerProperty> agePropertyOpt = state.getProperties().stream()
                    .filter(p -> p instanceof IntegerProperty)
                    .map(p -> (IntegerProperty) p)
                    .filter(p -> p.getName().equals("age"))
                    .findFirst();

            if (agePropertyOpt.isPresent()) {
                IntegerProperty ageProperty = agePropertyOpt.get();
                int maxAge = state.getValue(ageProperty).intValue() == ageProperty.getPossibleValues().stream().max(Integer::compare).orElse(7) ? 7 : ageProperty.getPossibleValues().stream().max(Integer::compare).orElse(7);
                int currentAge = state.getValue(ageProperty);

                // If the crop isn't fully grown, grow it instantly
                if (currentAge < maxAge) {
                    level.setBlock(pos, state.setValue(ageProperty, maxAge), 3);

                    // Consume solar energy
                    stack.set(ModDataComponents.SOLAR_CHARGE.get(), Math.max(charge - 50, 0));

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useOn(context);
    }
}
