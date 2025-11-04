package net.ronm19.solarium.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class SolarStalkerEntity extends Monster implements Enemy {

    // -----------------------------
    // FIELDS
    // -----------------------------
    private int idleAnimationTimeout = 0;
    public final AnimationState idleAnimationState = new AnimationState();

    private int teleportCooldown = 0;
    private boolean recentlyTeleported = false;
    private boolean aggressivePhase = false;
    private boolean fleeing = false;
    private boolean provoked = false;

    @Nullable
    private Player stalkingPlayer = null;

    // -----------------------------
    // CONSTRUCTOR
    // -----------------------------
    public SolarStalkerEntity( EntityType<? extends Monster> type, Level level ) {
        super(type, level);
        this.xpReward = 15;
        this.moveControl = new MoveControl(this);
    }

    // -----------------------------
    // ATTRIBUTES
    // -----------------------------
    public static AttributeSupplier.Builder createSolarStalkerAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.FOLLOW_RANGE, 96.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D);
    }

    // -----------------------------
    // AI GOALS
    // -----------------------------
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new StalkPlayerGoal(1.0D, 6.0D, 14.0D)); // custom stalker AI
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    // -----------------------------
    // SPAWN RULES
    // -----------------------------
    public static boolean checkSolarStalkerSpawnRules( EntityType<SolarStalkerEntity> type,
                                                       ServerLevelAccessor world, MobSpawnType reason,
                                                       BlockPos pos, RandomSource random ) {
        return !world.getLevel().isDay() && Monster.checkMonsterSpawnRules(type, world, reason, pos, random);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn( ServerLevelAccessor world, DifficultyInstance difficulty,
                                         MobSpawnType reason, @Nullable SpawnGroupData data ) {
        this.setPersistenceRequired();
        return super.finalizeSpawn(world, difficulty, reason, data);
    }

    // -----------------------------
    // TICK & AI LOGIC
    // -----------------------------
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) setupIdleAnimation();

        if (teleportCooldown > 0) teleportCooldown--;
        else recentlyTeleported = false;

        updateAggressionPhase();

        if (stalkingPlayer != null && (stalkingPlayer.isDeadOrDying() || stalkingPlayer.distanceTo(this) > 120.0D)) {
            stalkingPlayer = null;
        }

        if (!this.level().isDay() && this.random.nextInt(6) == 0) {
            this.level().addParticle(ParticleTypes.END_ROD,
                    this.getX() + (random.nextDouble() - 0.5),
                    this.getY() + 1.0,
                    this.getZ() + (random.nextDouble() - 0.5),
                    0, 0.03, 0);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isDay()) {
            if (getTarget() == null && stalkingPlayer != null) {
                double dist = this.distanceTo(stalkingPlayer);
                if (dist <= 8.0D && !stalkingPlayer.isCreative() && !stalkingPlayer.isSpectator()) {
                    this.setTarget(stalkingPlayer);
                }
            }
        } else {
            if (!provoked) this.setTarget(null);
        }

        if (stalkingPlayer != null && this.distanceTo(stalkingPlayer) > 30.0D && !recentlyTeleported) {
            teleportToPlayer(stalkingPlayer);
            recentlyTeleported = true;
            teleportCooldown = 160;
        }

        if (fleeing) tryFleeFromPlayer();
    }

    // -----------------------------
    // AGGRESSION / DAMAGE / FLEE
    // -----------------------------
    private void updateAggressionPhase() {
        aggressivePhase = this.getHealth() <= 20.0F;
        if (this.getHealth() <= 10.0F && !fleeing) fleeing = true;
    }

    @Override
    public boolean hurt( @NotNull DamageSource source, float amount ) {
        Entity attacker = source.getEntity();
        if (attacker instanceof Player player) {
            provoked = true;
            stalkingPlayer = player;
            this.setTarget(player);
        }

        boolean result = super.hurt(source, amount);

        if (!aggressivePhase && this.getHealth() - amount <= 20.0F) fleeing = true;

        return result;
    }

    private void tryFleeFromPlayer() {
        Player nearest = this.level().getNearestPlayer(this, 16.0D);
        if (nearest != null) {
            Vec3 dir = this.position().subtract(nearest.position()).normalize().scale(6.0D);
            teleportWithEffect(this.getX() + dir.x, this.getY(), this.getZ() + dir.z);
        }
        fleeing = false;
    }

    // -----------------------------
    // TELEPORTATION HELPERS
    // -----------------------------
    private void teleportToPlayer( Player player ) {
        BlockPos ppos = player.blockPosition();
        double tx = ppos.getX() + (this.random.nextDouble() - 0.5) * 2.0;
        double ty = ppos.getY();
        double tz = ppos.getZ() + (this.random.nextDouble() - 0.5) * 2.0;
        teleportWithEffect(tx, ty, tz);
    }

    private void teleportWithEffect( double x, double y, double z ) {
        this.teleportTo(x, y, z);
        this.level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                this.getSoundSource(), 1.0F, 1.0F);
        for (int i = 0; i < 6; i++) {
            this.level().addParticle(ParticleTypes.SWEEP_ATTACK, this.getX(), this.getY() + 0.5,
                    this.getZ(), (random.nextDouble() - 0.5) * 0.1,
                    random.nextDouble() * 0.05,
                    (random.nextDouble() - 0.5) * 0.1);
        }
    }

    // -----------------------------
    // ANIMATIONS & DROPS
    // -----------------------------
    private void setupIdleAnimation() {
        if (idleAnimationTimeout <= 0) {
            idleAnimationTimeout = 24;
            idleAnimationState.start(this.tickCount);
        } else idleAnimationTimeout--;
    }

    @Override
    protected void dropCustomDeathLoot( ServerLevel level, DamageSource source, boolean recentlyHit ) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        if (this.random.nextFloat() < 0.8F) spawnAtLocation(Items.BLAZE_ROD);
        if (this.random.nextFloat() < 0.6F) spawnAtLocation(Items.ENDER_PEARL);
    }

    // -----------------------------
    // SOUNDS
    // -----------------------------
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound( @NotNull DamageSource source ) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    @Override
    protected void playStepSound( BlockPos pos, BlockState state ) {
        this.playSound(SoundEvents.GRASS_STEP, 0.15F, 1.0F);
    }

    // -----------------------------
    // VANILLA HELPERS
    // -----------------------------
    public float getEyeHeight( Pose pose, EntityDimensions size ) {
        return 2.3F;
    }

    @Override
    public boolean causeFallDamage( float fallDistance, float multiplier, DamageSource source ) {
        return false;
    }

    @Override
    public void travel( Vec3 travelVector ) {
        if (!this.isNoAi()) super.travel(travelVector);
    }

    @Override
    public void push( Entity entityIn ) {
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    // -----------------------------
// INNER: STALKER GOAL
// -----------------------------
    private class StalkPlayerGoal extends Goal {
        private final double speed;
        private final double minDist;
        private final double maxDist;
        private Player targetPlayer;

        public StalkPlayerGoal( double speed, double minDist, double maxDist ) {
            this.speed = speed;
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            // Don't stalk if already attacking someone
            if (SolarStalkerEntity.this.getTarget() != null || provoked) return false;

            // Find nearest player
            Player p = SolarStalkerEntity.this.level().getNearestPlayer(SolarStalkerEntity.this,
                    SolarStalkerEntity.this.getAttributeValue(Attributes.FOLLOW_RANGE));
            if (p == null || p.isSpectator() || p.isCreative()) return false;

            // Only stalk in Survival or Hardcore
            boolean isSurvivalOrHardcore = false;
            if (p.level().getLevelData().isHardcore()) {
                isSurvivalOrHardcore = true;
            } else if (p instanceof net.minecraft.server.level.ServerPlayer sp) {
                GameType type = sp.gameMode.getGameModeForPlayer();
                isSurvivalOrHardcore = type == GameType.SURVIVAL || type == GameType.ADVENTURE;
            }
            if (!isSurvivalOrHardcore) return false;

            this.targetPlayer = p;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.targetPlayer == null) return false;
            if (this.targetPlayer.isDeadOrDying()) return false;
            if (SolarStalkerEntity.this.getTarget() != null) return false;

            double d = SolarStalkerEntity.this.distanceTo(this.targetPlayer);
            return d <= SolarStalkerEntity.this.getAttributeValue(Attributes.FOLLOW_RANGE) && !provoked;
        }

        @Override
        public void start() {
            stalkingPlayer = this.targetPlayer;
        }

        @Override
        public void stop() {
            stalkingPlayer = null;
            this.targetPlayer = null;
            SolarStalkerEntity.this.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.targetPlayer == null) return;

            double dist = SolarStalkerEntity.this.distanceTo(this.targetPlayer);

            // Movement / teleporting logic
            if (dist > maxDist) {
                SolarStalkerEntity.this.getNavigation().moveTo(this.targetPlayer, this.speed * 1.2);
                if (dist > 30.0D && !recentlyTeleported && SolarStalkerEntity.this.teleportCooldown <= 0) {
                    teleportToPlayer(this.targetPlayer);
                    recentlyTeleported = true;
                    SolarStalkerEntity.this.teleportCooldown = 160;
                }
            } else if (dist < minDist) {
                // circle the player a bit
                double angle = SolarStalkerEntity.this.random.nextDouble() * Math.PI * 2.0;
                double cx = this.targetPlayer.getX() + Math.cos(angle) * ((minDist + maxDist) / 2.0);
                double cz = this.targetPlayer.getZ() + Math.sin(angle) * ((minDist + maxDist) / 2.0);
                SolarStalkerEntity.this.getNavigation().moveTo(cx, this.targetPlayer.getY(), cz, this.speed * 0.8);
            } else {
                SolarStalkerEntity.this.getNavigation().moveTo(this.targetPlayer, this.speed * 0.9);
            }

            // Look at the player
            SolarStalkerEntity.this.getLookControl().setLookAt(this.targetPlayer, 30.0F, 30.0F);

            // Dynamic whisper chance based on game mode
            float whisperChance = 0.05F; // default low for survival
            if (this.targetPlayer instanceof net.minecraft.server.level.ServerPlayer sp) {
                GameType mode = sp.gameMode.getGameModeForPlayer();
                if (mode == GameType.ADVENTURE || sp.level().getLevelData().isHardcore()) {
                    whisperChance = 0.15F; // higher for adventure/hardcore
                }
            }

            if (SolarStalkerEntity.this.random.nextFloat() < whisperChance) {
                SolarStalkerEntity.this.level().playSound(null,
                        this.targetPlayer.blockPosition(),
                        SoundEvents.WITHER_SPAWN,  // spooky whisper sound
                        SolarStalkerEntity.this.getSoundSource(),
                        0.3F, 1.0F
                );
                SolarStalkerEntity.this.level().addParticle(
                        ParticleTypes.SMOKE,
                        SolarStalkerEntity.this.getX() + (SolarStalkerEntity.this.random.nextDouble() - 0.5),
                        SolarStalkerEntity.this.getY() + 1.0,
                        SolarStalkerEntity.this.getZ() + (SolarStalkerEntity.this.random.nextDouble() - 0.5),
                        0.0, 0.01, 0.0
                );
            }
        }
    }
}
