package net.ronm19.solarium.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class ModEffectSwordItem extends SwordItem {
    private final Holder<MobEffect> effect;

    public ModEffectSwordItem(Tier tier, Properties properties, Holder<MobEffect> effect) {
        super(tier, properties);
        this.effect = effect;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            // Apply the custom effect
            livingEntity.addEffect(new MobEffectInstance(effect, 900), player);

            // Set the target on fire for 4 seconds (80 ticks)
            livingEntity.igniteForTicks(4);
        }

        return super.onLeftClickEntity(stack, player, entity);
    }
}
