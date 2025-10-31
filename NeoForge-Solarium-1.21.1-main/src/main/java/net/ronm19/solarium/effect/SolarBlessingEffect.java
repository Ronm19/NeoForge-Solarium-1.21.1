package net.ronm19.solarium.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.ronm19.solarium.SolariumMod;

import net.minecraft.resources.ResourceLocation;

public class SolarBlessingEffect extends MobEffect {

    protected SolarBlessingEffect(MobEffectCategory category, int color) {
        super(category, color);

        // Attribute modifiers
        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solar_blessing_attack_boost"),
                0.25D, // +25% attack damage
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solar_blessing_speed_boost"),
                0.15D, // +15% movement speed
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // Minor healing & clear fire every 2 seconds
        if (!entity.level().isClientSide() && entity.tickCount % 40 == 0) {
            entity.clearFire();
            entity.heal(0.5F + amplifier * 0.25F);
        }

        // Solar aura particles
        if (entity.level() instanceof ServerLevel serverLevel && serverLevel.random.nextInt(15) == 0) {
            serverLevel.sendParticles(
                    ParticleTypes.FLAME,
                    entity.getX(),
                    entity.getY() + 1.0,
                    entity.getZ(),
                    2, 0.2, 0.3, 0.2, 0.01
            );
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
