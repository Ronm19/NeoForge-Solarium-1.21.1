package net.ronm19.solarium.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.ronm19.solariummod.util.ModDataComponents;

public class SolariumSwordItem extends SwordItem {

    public SolariumSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);

        // If the sword is highly charged, burn the target
        if (charge >= 150) {
            target.igniteForTicks(4);
        }

        // Drain some solar energy per hit
        if (charge > 0) {
            stack.set(ModDataComponents.SOLAR_CHARGE.get(), Math.max(charge - 5, 0));
        }

        return super.hurtEnemy(stack, target, attacker);
    }
}
