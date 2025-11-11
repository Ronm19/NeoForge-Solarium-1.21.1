package net.ronm19.solarium.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;

public class SolarCreeperEntity extends Creeper {
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR;
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED;
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED;
    private int oldSwell;
    private int swell;
    private int maxSwell = 20;
    private int explosionRadius = 6;

    public SolarCreeperEntity(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createSolarCreeperAttributes() {
        return Creeper.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)       // More health than vanilla creeper
                .add(Attributes.MOVEMENT_SPEED, 0.35D) // Slightly faster
                .add(Attributes.FOLLOW_RANGE, 46.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);   // Optional: can damage on contact
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, (double)1.0F, 1.2));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, (double)1.0F, 1.2));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, (double)1.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, TamableAnimal.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public int getMaxFallDistance() {
        return this.getTarget() == null ? this.getComfortableFallDistance(0.0F) : this.getComfortableFallDistance(this.getHealth() - 1.0F);
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        boolean flag = super.causeFallDamage(fallDistance, multiplier, source);
        this.swell += (int)(fallDistance * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return flag;
    }

    protected void defineSynchedData( SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SWELL_DIR, -1);
        builder.define(DATA_IS_POWERED, false);
        builder.define(DATA_IS_IGNITED, false);
    }

    public void addAdditionalSaveData( @NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if ((Boolean)this.entityData.get(DATA_IS_POWERED)) {
            compound.putBoolean("powered", true);
        }

        compound.putShort("Fuse", (short)this.maxSwell);
        compound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.putBoolean("ignited", this.isIgnited());
    }

    public void readAdditionalSaveData( @NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_IS_POWERED, compound.getBoolean("powered"));
        if (compound.contains("Fuse", 99)) {
            this.maxSwell = compound.getShort("Fuse");
        }

        if (compound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }

        if (compound.getBoolean("ignited")) {
            this.ignite();
        }

    }

    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }

        super.tick();
    }

    public void setTarget(@Nullable LivingEntity target) {
        if (!(target instanceof Goat)) {
            super.setTarget(target);
        }

    }

    protected @NotNull SoundEvent getHurtSound( @NotNull DamageSource damageSource) {
        return SoundEvents.CREEPER_HURT;
    }

    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    public boolean doHurtTarget( @NotNull Entity entity) {
        return true;
    }

    public boolean isPowered() {
        return (Boolean)this.entityData.get(DATA_IS_POWERED);
    }

    public float getSwelling(float partialTicks) {
        return Mth.lerp(partialTicks, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    public int getSwellDir() {
        return (Integer)this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int state) {
        this.entityData.set(DATA_SWELL_DIR, state);
    }

    public void thunderHit( @NotNull ServerLevel level, @NotNull LightningBolt lightning) {
        super.thunderHit(level, lightning);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    protected @NotNull InteractionResult mobInteract( Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent soundevent = itemstack.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
            this.level().playSound(player, this.getX(), this.getY(), this.getZ(), soundevent, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level().isClientSide) {
                this.ignite();
                if (!itemstack.isDamageableItem()) {
                    itemstack.shrink(1);
                } else {
                    itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    private void explodeCreeper() {
        if (!this.level().isClientSide) {
            float f = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, Level.ExplosionInteraction.MOB);
            this.spawnLingeringCloud();
            this.triggerOnDeathMobEffects(RemovalReason.KILLED);
            this.discard();
        }

    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5F);
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());

            for(MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            this.level().addFreshEntity(areaeffectcloud);
        }

    }

    public boolean isIgnited() {
        return (Boolean)this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }

    static {
        DATA_SWELL_DIR = SynchedEntityData.defineId(SolarCreeperEntity.class, EntityDataSerializers.INT);
        DATA_IS_POWERED = SynchedEntityData.defineId(SolarCreeperEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_IS_IGNITED = SynchedEntityData.defineId(SolarCreeperEntity.class, EntityDataSerializers.BOOLEAN);
    }
}