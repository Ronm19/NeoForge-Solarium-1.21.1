package net.ronm19.solarium.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties SOLAR_APPLE = new FoodProperties.Builder()
            .nutrition(5) // restores 1.5 hunger bars
            .saturationModifier(0.35f)
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 1.0f) // 5 seconds
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0), 1.0f) // 3 seconds
            .build();

    public static final FoodProperties SOLAR_TOMATO = new FoodProperties.Builder()
            .nutrition(6) // restores 1.5 hunger bars
            .saturationModifier(0.25f)
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0), 1.0f) // 5 seconds
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0), 1.0f) // 3 seconds
            .build();
}
