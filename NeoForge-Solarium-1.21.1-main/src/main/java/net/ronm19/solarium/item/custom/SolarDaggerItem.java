package net.ronm19.solarium.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class SolarDaggerItem extends SwordItem {
    private final Holder<MobEffect> effect;


    public SolarDaggerItem( Tier tier, Properties properties, Holder<MobEffect> effect ) {
        super(tier, new Item.Properties().attributes(SwordItem.createAttributes(tier, 5.0F, -1.8F)).fireResistant());
        this.effect = effect;

        // 5.0F = attack damage, -1.8F = attack speed, plus fire resistance for thematics
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            // Apply the custom effect
            livingEntity.addEffect(new MobEffectInstance(effect, 900), player);
        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level level = attacker.level();

        // 🔥 Set target on fire for 5 seconds
        target.igniteForTicks(5);

        // 💀 Apply instant damage (Harming) effect
        target.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 0, false, true));

        // ☀️ Play fiery impact sound
        level.playSound(null, target.blockPosition(), SoundEvents.BLAZE_HURT, SoundSource.PLAYERS, 1.0F, 1.2F);

        // ⚔️ Deal base sword damage
        boolean success = super.hurtEnemy(stack, target, attacker);

        // 🌞 Add a small "solar critical" bonus
        if (attacker instanceof Player player && !level.isClientSide()) {
            float extra = 2.0F; // extra sunlight sting
            target.hurt(level.damageSources().playerAttack(player), extra);
        }
        return success;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
