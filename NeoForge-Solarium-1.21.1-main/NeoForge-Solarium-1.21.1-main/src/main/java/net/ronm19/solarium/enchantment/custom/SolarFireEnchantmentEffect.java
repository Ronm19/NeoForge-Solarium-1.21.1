package net.ronm19.solarium.enchantment.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.ronm19.solarium.entity.custom.SolarFireballEntity;
import org.jetbrains.annotations.NotNull;

public record SolarFireEnchantmentEffect(int level) implements EnchantmentEntityEffect {

    public static final MapCodec<SolarFireEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("level").forGetter(SolarFireEnchantmentEffect::level)
            ).apply(instance, SolarFireEnchantmentEffect::new)
    );

    public SolarFireEnchantmentEffect {
        // Enforce level limits: minimum 1, maximum 2
        if (level < 1) level = 1;
        if (level > 2) level = 2;
    }

    @Override
    public void apply( @NotNull ServerLevel serverLevel, int effectLevel, @NotNull EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        if (!(entity instanceof LivingEntity player)) return;

        // Clamp final power (level + effectLevel) to max 2
        int power = Math.min(level + effectLevel, 2);

        // Calculate spawn position slightly in front of player
        LargeFireball fireball = getSolarFireballEntity(serverLevel, player, power);

        // Spawn fireball into the world
        serverLevel.addFreshEntity(fireball);

        // Spawn flame particles around player for visual feedback
        serverLevel.sendParticles(
                ParticleTypes.FLAME,
                player.getX(), player.getEyeY(), player.getZ(),
                10 + power * 2,
                0.2, 0.2, 0.2,
                0.02
        );
    }

        private static @NotNull LargeFireball getSolarFireballEntity( @NotNull ServerLevel serverLevel, LivingEntity player, int power) {
        Vec3 look = player.getLookAngle();

        // Calculate spawn position slightly in front of the player
        double spawnX = player.getX() + look.x * 1.5;
        double spawnY = player.getEyeY() - 0.1;
        double spawnZ = player.getZ() + look.z * 1.5;

        // Create the fireball using the Vec3 constructor
            LargeFireball fireball = new LargeFireball(serverLevel, player, look, power);

        // Move it to spawn position
        fireball.moveTo(spawnX, spawnY, spawnZ);

        // Already set owner and shoot direction in constructor, but you can tweak velocity if needed
        fireball.shoot(look.x, look.y, look.z, 1.5F + 0.3F * power, 0.05F);

        return fireball;
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
