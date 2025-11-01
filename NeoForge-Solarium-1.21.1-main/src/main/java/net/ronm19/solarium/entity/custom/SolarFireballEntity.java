package net.ronm19.solarium.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SolarFireballEntity extends SmallFireball {

    private final int powerLevel;
    private static final Random RANDOM = new Random();
    private final Vec3 originalDirection = Vec3.ZERO;

    // Default constructor required for registration
    public SolarFireballEntity(EntityType<? extends SmallFireball> type, Level level) {
        super(type, level); // Required constructor for registration
        this.powerLevel = 2;
    }

    // Constructor to spawn the fireball
    public SolarFireballEntity(Level level, LivingEntity shooter, Vec3 direction, int power) {
        super(EntityType.SMALL_FIREBALL, level); // call registration constructor
        this.powerLevel = power;

        // Place fireball at shooter’s eye level
        this.moveTo(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());

        // Shoot in the desired direction
        this.shoot(direction.x, direction.y, direction.z, 1.5F + 0.3F * power, 0F);

        this.setOwner(shooter);
    }


    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide && this.getOwner() != null) {
            // Controlled chaos: slight correction toward original direction each tick
            Vec3 vel = this.getDeltaMovement();
            this.setDeltaMovement(
                    vel.scale(0.95) // keep 95% of current velocity
                            .add(originalDirection.scale(0.05)) // nudge slightly toward shooter direction
            );
        }
    }

    @Override
    protected void onHitEntity( @NotNull EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (!level().isClientSide() && hitResult.getEntity() instanceof LivingEntity target) {
            target.hurt(damageSources().fireball(this, this.getOwner()), 6.0F + powerLevel * 2.0F);
            target.igniteForSeconds(4 + powerLevel * 2);
            explodeAndFlame();
            discard();
        }
    }

    @Override
    protected void onHitBlock( @NotNull BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!level().isClientSide()) {
            explodeAndFlame();
            discard();
        }
    }

    private void explodeAndFlame() {
        // Explosion
        level().explode(
                this,
                getX(), getY(), getZ(),
                1.5F + powerLevel * 0.5F,
                false,
                Level.ExplosionInteraction.NONE
        );

        // Explosion sound
        level().playSound(
                null,
                new BlockPos((int) getX(), (int) getY(), (int) getZ()),
                SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.PLAYERS,
                0.8F + powerLevel * 0.2F,
                1.0F
        );

        if (level() instanceof ServerLevel serverLevel) {
            // Flame particles
            serverLevel.sendParticles(
                    ParticleTypes.FLAME,
                    getX(), getY(), getZ(),
                    20 + powerLevel * 10,
                    0.3, 0.3, 0.3,
                    0.02
            );
            serverLevel.sendParticles(
                    ParticleTypes.SMALL_FLAME,
                    getX(), getY(), getZ(),
                    10,
                    0.2, 0.2, 0.2,
                    0.02
            );
        }

        spreadSolarFlames();
    }

    private void spreadSolarFlames() {
        if (!(level() instanceof ServerLevel serverLevel)) return;

        int radius = 2 + powerLevel;
        BlockPos center = blockPosition();

        for (int i = 0; i < 12 + powerLevel * 4; i++) {
            BlockPos pos = center.offset(
                    RANDOM.nextInt(radius * 2) - radius,
                    -1,
                    RANDOM.nextInt(radius * 2) - radius
            );

            if (serverLevel.isEmptyBlock(pos.above()) &&
                    (serverLevel.getBlockState(pos).is(BlockTags.DIRT) ||
                            serverLevel.getBlockState(pos).is(BlockTags.BASE_STONE_OVERWORLD))) {
                serverLevel.setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
            }
        }
    }
}
