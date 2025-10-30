package net.ronm19.solarium.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.ronm19.solariummod.util.ModDataComponents;

public class SolariumPickaxeItem extends PickaxeItem {

    private static final int MAX_CHARGE = 200; // Full solar power

    public SolariumPickaxeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, net.minecraft.world.level.block.state.BlockState state) {
        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);
        float baseSpeed = super.getDestroySpeed(stack, state);

        // If fully charged, boost mining speed x2.5
        if (charge > 0) {
            return baseSpeed * (1.0f + (charge / (float) MAX_CHARGE) * 1.5f);
        }

        return baseSpeed; // Normal speed if no solar energy
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, net.minecraft.world.level.block.state.BlockState state,
                             net.minecraft.core.BlockPos pos, LivingEntity entity) {
        if (entity instanceof Player player) {
            int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);

            // Consume 1 solar charge per mined block
            if (charge > 0) {
                stack.set(ModDataComponents.SOLAR_CHARGE.get(), Math.max(charge - 1, 0));
            }
        }

        return super.mineBlock(stack, level, state, pos, entity);
    }
}
