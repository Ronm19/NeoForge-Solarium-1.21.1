package net.ronm19.solarium.item.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import net.ronm19.solariummod.util.ModDataComponents;

public class SolariumShovelItem extends ShovelItem {

    private final Tier tier;

    public SolariumShovelItem(Tier tier, Properties properties) {
        super(tier, properties);
        this.tier = tier;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);

        // Boosted digging speed when full of solar energy
        if (charge >= 150) {
            return tier.getSpeed() * 1.8f;
        }
        return tier.getSpeed();
    }
}
