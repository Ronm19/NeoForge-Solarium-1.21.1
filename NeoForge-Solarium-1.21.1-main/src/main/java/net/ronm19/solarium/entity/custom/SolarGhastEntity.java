package net.ronm19.solarium.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class SolarGhastEntity extends Ghast implements Enemy {
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING;
    private int explosionPower = 1;

    public SolarGhastEntity( EntityType<? extends Ghast> entityType, Level level ) {
        super(entityType, level);
        this.xpReward = 5;
        this.moveControl = new SolarGhastMoveControl(this);
    }

    public static AttributeSupplier.Builder createSolarGhastAttributes() {
        return Ghast.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)       // More health than vanilla Ghast (vanilla: 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)    // Slightly faster flying
                .add(Attributes.FOLLOW_RANGE, 100.0D)     // Standard Ghast targeting range
                .add(Attributes.ATTACK_DAMAGE, 6.0D)    // More fireball damage than vanilla
                .add(Attributes.FLYING_SPEED, 0.25D);    // Custom flying speed
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(5, new SolarGhastEntity.RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new SolarGhastLookGoal(this));
        this.goalSelector.addGoal(7, new SolarGhastShootFireballGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, ( p_352811_ ) -> Math.abs(p_352811_.getY() - this.getY()) <= (double) 4.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, TamableAnimal.class, 10, true, false, ( p_352811_ ) -> Math.abs(p_352811_.getY() - this.getY()) <= (double) 4.0F));
    }

    public boolean isCharging() {
        return (Boolean) this.entityData.get(DATA_IS_CHARGING);
    }

    public void setCharging( boolean charging ) {
        this.entityData.set(DATA_IS_CHARGING, charging);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public int getExplosionPower() {
        return this.explosionPower;
    }

    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    private static boolean isReflectedFireball( DamageSource damageSource ) {
        return damageSource.getDirectEntity() instanceof LargeFireball && damageSource.getEntity() instanceof Player;
    }

    public boolean isInvulnerableTo( @NotNull DamageSource source ) {
        return this.isInvulnerable() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || !isReflectedFireball(source) && super.isInvulnerableTo(source);
    }

    public boolean hurt( @NotNull DamageSource source, float amount ) {
        if (isReflectedFireball(source)) {
            super.hurt(source, 1000.0F);
            return true;
        } else {
            return !this.isInvulnerableTo(source) && super.hurt(source, amount);
        }
    }

    protected void defineSynchedData( SynchedEntityData.Builder builder ) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_CHARGING, false);
    }

    public @NotNull SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    protected @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.GHAST_AMBIENT;
    }

    protected @NotNull SoundEvent getHurtSound( @NotNull DamageSource damageSource ) {
        return SoundEvents.GHAST_HURT;
    }

    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }

    protected float getSoundVolume() {
        return 5.0F;
    }

    public static boolean checkGhastSpawnRules( EntityType<Ghast> ghast, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random ) {
        return level.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(20) == 0 && checkMobSpawnRules(ghast, level, spawnType, pos, random);
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    public void addAdditionalSaveData( @NotNull CompoundTag compound ) {
        super.addAdditionalSaveData(compound);
        compound.putByte("ExplosionPower", (byte) this.explosionPower);
    }

    public void readAdditionalSaveData( @NotNull CompoundTag compound ) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("ExplosionPower", 99)) {
            this.explosionPower = compound.getByte("ExplosionPower");
        }

    }

    static {
        DATA_IS_CHARGING = SynchedEntityData.defineId(SolarGhastEntity.class, EntityDataSerializers.BOOLEAN);
    }

    static class SolarGhastLookGoal extends Goal {
        private final SolarGhastEntity solarGhast;

        public SolarGhastLookGoal( SolarGhastEntity solarGhast ) {
            this.solarGhast = solarGhast;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            return true;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.solarGhast.getTarget() == null) {
                Vec3 vec3 = this.solarGhast.getDeltaMovement();
                this.solarGhast.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float) Math.PI));
                this.solarGhast.yBodyRot = this.solarGhast.getYRot();
            } else {
                LivingEntity livingentity = this.solarGhast.getTarget();
                double d0 = (double) 64.0F;
                if (livingentity.distanceToSqr(this.solarGhast) < (double) 4096.0F) {
                    double d1 = livingentity.getX() - this.solarGhast.getX();
                    double d2 = livingentity.getZ() - this.solarGhast.getZ();
                    this.solarGhast.setYRot(-((float) Mth.atan2(d1, d2)) * (180F / (float) Math.PI));
                    this.solarGhast.yBodyRot = this.solarGhast.getYRot();
                }
            }

        }
    }

    static class SolarGhastMoveControl extends MoveControl {
        private final SolarGhastEntity solarGhast;
        private int floatDuration;

        public SolarGhastMoveControl( SolarGhastEntity solarGhast ) {
            super(solarGhast);
            this.solarGhast = solarGhast;
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO && this.floatDuration-- <= 0) {
                this.floatDuration = this.floatDuration + this.solarGhast.getRandom().nextInt(5) + 2;
                Vec3 vec3 = new Vec3(this.wantedX - this.solarGhast.getX(), this.wantedY - this.solarGhast.getY(), this.wantedZ - this.solarGhast.getZ());
                double d0 = vec3.length();
                vec3 = vec3.normalize();
                if (this.canReach(vec3, Mth.ceil(d0))) {
                    this.solarGhast.setDeltaMovement(this.solarGhast.getDeltaMovement().add(vec3.scale(0.1)));
                } else {
                    this.operation = Operation.WAIT;
                }
            }

        }

        private boolean canReach( Vec3 pos, int length ) {
            AABB aabb = this.solarGhast.getBoundingBox();

            for (int i = 1; i < length; ++i) {
                aabb = aabb.move(pos);
                if (!this.solarGhast.level().noCollision(this.solarGhast, aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

    static class SolarGhastShootFireballGoal extends Goal {
        private final SolarGhastEntity solarGhast;
        public int chargeTime;

        public SolarGhastShootFireballGoal( SolarGhastEntity solarGhast ) {
            this.solarGhast = solarGhast;
        }

        public boolean canUse() {
            return this.solarGhast.getTarget() != null;
        }

        public void start() {
            this.chargeTime = 0;
        }

        public void stop() {
            this.solarGhast.setCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.solarGhast.getTarget();
            if (livingentity != null) {
                double d0 = (double) 64.0F;
                if (livingentity.distanceToSqr(this.solarGhast) < (double) 4096.0F && this.solarGhast.hasLineOfSight(livingentity)) {
                    Level level = this.solarGhast.level();
                    ++this.chargeTime;
                    if (this.chargeTime == 10 && !this.solarGhast.isSilent()) {
                        level.levelEvent((Player) null, 1015, this.solarGhast.blockPosition(), 0);
                    }

                    if (this.chargeTime == 20) {
                        double d1 = (double) 4.0F;
                        Vec3 vec3 = this.solarGhast.getViewVector(1.0F);
                        double d2 = livingentity.getX() - (this.solarGhast.getX() + vec3.x * (double) 4.0F);
                        double d3 = livingentity.getY((double) 0.5F) - ((double) 0.5F + this.solarGhast.getY((double) 0.5F));
                        double d4 = livingentity.getZ() - (this.solarGhast.getZ() + vec3.z * (double) 4.0F);
                        Vec3 vec31 = new Vec3(d2, d3, d4);
                        if (!this.solarGhast.isSilent()) {
                            level.levelEvent((Player) null, 1016, this.solarGhast.blockPosition(), 0);
                        }

                        LargeFireball largefireball = new LargeFireball(level, this.solarGhast, vec31.normalize(), this.solarGhast.getExplosionPower());
                        largefireball.setPos(this.solarGhast.getX() + vec3.x * (double) 4.0F, this.solarGhast.getY((double) 0.5F) + (double) 0.5F, largefireball.getZ() + vec3.z * (double) 4.0F);
                        level.addFreshEntity(largefireball);
                        this.chargeTime = -40;
                    }
                } else if (this.chargeTime > 0) {
                    --this.chargeTime;
                }

                this.solarGhast.setCharging(this.chargeTime > 10);
            }

        }
    }

    static class RandomFloatAroundGoal extends Goal {
        private final Ghast ghast;

        public RandomFloatAroundGoal( Ghast ghast ) {
            this.ghast = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.ghast.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.ghast.getX();
                double d1 = movecontrol.getWantedY() - this.ghast.getY();
                double d2 = movecontrol.getWantedZ() - this.ghast.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < (double) 1.0F || d3 > (double) 3600.0F;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.ghast.getRandom();
            double d0 = this.ghast.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.ghast.getY() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.ghast.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, (double) 1.0F);
        }
    }
}
