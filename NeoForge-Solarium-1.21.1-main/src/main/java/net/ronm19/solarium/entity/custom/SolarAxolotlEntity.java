package net.ronm19.solarium.entity.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.axolotl.AxolotlAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.ronm19.solarium.entity.ModEntities;
import net.ronm19.solarium.entity.animal.solar_axolotl.SolarAxolotlAi;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.*;

public class SolarAxolotlEntity extends Axolotl {
    public static final int TOTAL_PLAYDEAD_TIME = 200;
    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Axolotl>>> SENSOR_TYPES;
    protected static final ImmutableList<MemoryModuleType> MEMORY_TYPES;
    private static final EntityDataAccessor<Boolean> DATA_PLAYING_DEAD;
    private static final EntityDataAccessor<Boolean> FROM_BUCKET;
    public static final double PLAYER_REGEN_DETECTION_RANGE = (double) 20.0F;
    public static final int RARE_VARIANT_CHANCE = 1200;
    private static final int AXOLOTL_TOTAL_AIR_SUPPLY = 6000;
    private static final int REHYDRATE_AIR_SUPPLY = 1800;
    private static final int REGEN_BUFF_MAX_DURATION = 2400;
    private final Map<String, Vector3f> modelRotationValues = Maps.newHashMap();
    private static final int REGEN_BUFF_BASE_DURATION = 100;

    public SolarAxolotlEntity( EntityType<? extends Axolotl> entityType, Level level ) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new SolarAxolotlMoveControl(this);
        this.lookControl = new SolarAxolotlLookControl(this, 20);
    }

    public Map<String, Vector3f> getModelRotationValues() {
        return this.modelRotationValues;
    }

    public float getWalkTargetValue( BlockPos pos, LevelReader level ) {
        return 0.0F;
    }

    protected void defineSynchedData( SynchedEntityData.Builder builder ) {
        super.defineSynchedData(builder);
        builder.define(DATA_PLAYING_DEAD, false);
        builder.define(FROM_BUCKET, false);
    }

    public void addAdditionalSaveData( CompoundTag compound ) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    public void readAdditionalSaveData( CompoundTag compound ) {
        super.readAdditionalSaveData(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    public void playAmbientSound() {
        if (!this.isPlayingDead()) {
            super.playAmbientSound();
        }
    }

    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        if (!this.isNoAi()) {
            this.handleAirSupply(i);
        }

    }

    protected void handleAirSupply( int airSupply ) {
        if (this.isAlive() && !this.isInWaterRainOrBubble()) {
            this.setAirSupply(airSupply - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().dryOut(), 2.0F);
            }
        } else {
            this.setAirSupply(this.getMaxAirSupply());
        }

    }

    public void rehydrate() {
        int i = this.getAirSupply() + 1800;
        this.setAirSupply(Math.min(i, this.getMaxAirSupply()));
    }

    public int getMaxAirSupply() {
        return 6000;
    }

    public boolean checkSpawnObstruction( LevelReader level ) {
        return level.isUnobstructed(this);
    }

    public boolean isPushedByFluid() {
        return false;
    }

    public void setPlayingDead( boolean playingDead ) {
        this.entityData.set(DATA_PLAYING_DEAD, playingDead);
    }

    public boolean isPlayingDead() {
        return (Boolean) this.entityData.get(DATA_PLAYING_DEAD);
    }

    public boolean fromBucket() {
        return (Boolean) this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket( boolean fromBucket ) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public @org.jetbrains.annotations.Nullable AgeableMob getBreedOffspring( ServerLevel level, AgeableMob otherParent ) {
        return ModEntities.SOLAR_AXOLOTL.get().create(level);
    }

    public boolean isFood( ItemStack stack) {
        return stack.is(ItemTags.AXOLOTL_FOOD);
    }

    public boolean canBeLeashed() {
        return true;
    }

    protected void customServerAiStep() {
        this.level().getProfiler().push("SolaraxolotlBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("SolaraxolotlActivityUpdate");
        AxolotlAi.updateActivity(this);
        this.level().getProfiler().pop();
        if (!this.isNoAi()) {
            Optional<Integer> optional = this.getBrain().getMemory(MemoryModuleType.PLAY_DEAD_TICKS);
            this.setPlayingDead(optional.isPresent() && (Integer)optional.get() > 0);
        }

    }

    public static AttributeSupplier.Builder createSolarAxolotlAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, (double)30.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)1.0F)
                .add(Attributes.ATTACK_DAMAGE, (double)5.0F)
                .add(Attributes.STEP_HEIGHT, (double)1.4F);
    }

    protected @NotNull PathNavigation createNavigation( Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    public void playAttackSound() {
        this.playSound(SoundEvents.AXOLOTL_ATTACK, 1.0F, 1.0F);
    }

    public boolean hurt( @NotNull DamageSource source, float amount) {
        float f = this.getHealth();
        if (!this.level().isClientSide && !this.isNoAi() && this.level().random.nextInt(3) == 0 && ((float)this.level().random.nextInt(3) < amount || f / this.getMaxHealth() < 0.5F) && amount < f && this.isInWater() && (source.getEntity() != null || source.getDirectEntity() != null) && !this.isPlayingDead()) {
            this.brain.setMemory(MemoryModuleType.PLAY_DEAD_TICKS, 200);
        }

        return super.hurt(source, amount);
    }

    public int getMaxHeadXRot() {
        return 1;
    }

    public int getMaxHeadYRot() {
        return 1;
    }

    public @NotNull InteractionResult mobInteract( @NotNull Player player, @NotNull InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    public void saveToBucketTag( @NotNull ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, ( p_330644_) -> {
            p_330644_.putInt("Age", this.getAge());
            Brain<?> brain = this.getBrain();
            if (brain.hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN)) {
                p_330644_.putLong("HuntingCooldown", brain.getTimeUntilExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN));
            }

        });
    }

    public void loadFromBucketTag( @NotNull CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }

        if (tag.contains("HuntingCooldown")) {
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, tag.getLong("HuntingCooldown"));
        }

    }

    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.AXOLOTL_BUCKET);
    }

    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    public boolean canBeSeenAsEnemy() {
        return !this.isPlayingDead() && super.canBeSeenAsEnemy();
    }

    public static void onStopAttacking(SolarAxolotlEntity axolotl, LivingEntity target) {
        Level level = axolotl.level();
        if (target.isDeadOrDying()) {
            DamageSource damagesource = target.getLastDamageSource();
            if (damagesource != null) {
                Entity entity = damagesource.getEntity();
                if (entity != null && entity.getType() == EntityType.PLAYER) {
                    Player player = (Player)entity;
                    List<Player> list = level.getEntitiesOfClass(Player.class, axolotl.getBoundingBox().inflate((double)20.0F));
                    if (list.contains(player)) {
                        axolotl.applySupportingEffects(player);
                    }
                }
            }
        }

    }

    public void applySupportingEffects(Player player) {
        MobEffectInstance mobeffectinstance = player.getEffect(MobEffects.REGENERATION);
        if (mobeffectinstance == null || mobeffectinstance.endsWithin(2399)) {
            int i = mobeffectinstance != null ? mobeffectinstance.getDuration() : 0;
            int j = Math.min(2400, 100 + i);
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, j, 0), this);
        }

        player.removeEffect(MobEffects.DIG_SLOWDOWN);
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.AXOLOTL_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.AXOLOTL_DEATH;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.AXOLOTL_IDLE_WATER : SoundEvents.AXOLOTL_IDLE_AIR;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.AXOLOTL_SPLASH;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }

    protected Brain<?> makeBrain( Dynamic<?> dynamic) {
        return SolarAxolotlAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    public Brain<Axolotl> getBrain() {
        return super.getBrain();
    }

    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    public void travel( Vec3 travelVector) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(travelVector);
        }

    }

    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        if (stack.is(Items.TROPICAL_FISH_BUCKET)) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.WATER_BUCKET)));
        } else {
            super.usePlayerItem(player, hand, stack);
        }

    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public boolean removeWhenFarAway( double distanceToClosestPlayer) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Nullable
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    public static boolean checkAxolotlSpawnRules(EntityType<? extends LivingEntity> axolotl, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.AXOLOTLS_SPAWNABLE_ON);
    }

    static {
        SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.HURT_BY, SensorType.AXOLOTL_ATTACKABLES, SensorType.AXOLOTL_TEMPTATIONS);
        MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT, new MemoryModuleType[]{MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.PLAY_DEAD_TICKS, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.HAS_HUNTING_COOLDOWN, MemoryModuleType.IS_PANICKING});
        DATA_PLAYING_DEAD = SynchedEntityData.defineId(SolarAxolotlEntity.class, EntityDataSerializers.BOOLEAN);
        FROM_BUCKET = SynchedEntityData.defineId(SolarAxolotlEntity.class, EntityDataSerializers.BOOLEAN);
    }

    class SolarAxolotlLookControl extends SmoothSwimmingLookControl {
        public SolarAxolotlLookControl(SolarAxolotlEntity axolotl, int maxYRotFromCenter) {
            super(axolotl, maxYRotFromCenter);
        }

        public void tick() {
            if (!SolarAxolotlEntity.this.isPlayingDead()) {
                super.tick();
            }

        }
    }

    static class SolarAxolotlMoveControl extends SmoothSwimmingMoveControl {
        private final SolarAxolotlEntity axolotl;

        public SolarAxolotlMoveControl(SolarAxolotlEntity axolotl) {
            super(axolotl, 85, 10, 0.1F, 0.5F, false);
            this.axolotl = axolotl;
        }

        public void tick() {
            if (!this.axolotl.isPlayingDead()) {
                super.tick();
            }

        }
    }
}
