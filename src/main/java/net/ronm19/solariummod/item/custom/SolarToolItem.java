package net.ronm19.solariummod.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.ronm19.solariummod.util.ModDataComponents;

public abstract class SolarToolItem extends Item {

    private static final int MAX_CHARGE = 200; // Maximum stored sunlight energy

    public SolarToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        if (!(entity instanceof Player player)) return;

        // Get current solar charge from Data Component, default to 0 if missing
        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);

        // Recharge during the day when in direct sunlight
        if (level.isDay() && level.canSeeSky(player.blockPosition())) {
            if (charge < MAX_CHARGE) {
                stack.set(ModDataComponents.SOLAR_CHARGE.get(), Math.min(charge + 2, MAX_CHARGE));
            }
        } else {
            // Slowly drain charge at night or underground
            if (charge > 0) {
                stack.set(ModDataComponents.SOLAR_CHARGE.get(), Math.max(charge - 1, 0));
            }
        }
    }

    /** Returns the current solar charge of the tool */
    public int getSolarCharge(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);
    }

    /** Returns a damage/power multiplier based on the current charge */
    public float getChargeMultiplier(ItemStack stack) {
        return 1.0f + ((float) getSolarCharge(stack) / MAX_CHARGE);
    }
}