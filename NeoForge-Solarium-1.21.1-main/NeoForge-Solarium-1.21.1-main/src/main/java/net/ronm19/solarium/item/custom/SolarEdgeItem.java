package net.ronm19.solarium.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class SolarEdgeItem extends SwordItem {

    public SolarEdgeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    /**
     * Called when the sword hits an enemy.
     * Solar Edge sets enemies on fire, damages durability,
     * and plays a fiery sound effect.
     */
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level level = attacker.level();

        // Burn the target for 6 seconds
        target.igniteForSeconds(6);
        target.igniteForTicks(6);
        target.setRemainingFireTicks(6);


        // Solar impact sound
        level.playSound(null, target.blockPosition(),
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS,
                1.0f, 1.2f);

        return true;
    }

    /**
     * Small passive: while held, the Solar Edge glows faintly,
     * symbolizing stored solar energy.
     */
    @Override
    public boolean isFoil(ItemStack stack) {
        // Always has an enchanted glow
        return true;
    }
}
