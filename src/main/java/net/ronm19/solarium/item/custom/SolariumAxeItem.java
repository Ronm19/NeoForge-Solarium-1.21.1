package net.ronm19.solarium.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.ronm19.solariummod.util.ModDataComponents;

public class SolariumAxeItem extends AxeItem {

    private final Tier tier;

    public SolariumAxeItem(Tier tier, Properties properties) {
        super(tier, properties);
        this.tier = tier;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);

        // Extra damage when highly charged
        if (charge >= 150) {
            target.hurt(attacker.damageSources().playerAttack((Player) attacker), 4.0F);
        }

        // Drain some energy per attack
        if (charge > 0) {
            stack.set(ModDataComponents.SOLAR_CHARGE.get(), Math.max(charge - 3, 0));
        }

        return super.hurtEnemy(stack, target, attacker);
    }
}
