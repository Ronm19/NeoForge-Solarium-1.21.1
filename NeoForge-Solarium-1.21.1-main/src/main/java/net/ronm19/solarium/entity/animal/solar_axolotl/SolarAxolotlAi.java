package net.ronm19.solarium.entity.animal.solar_axolotl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.axolotl.AxolotlAi;
import net.minecraft.world.entity.animal.axolotl.PlayDead;
import net.minecraft.world.entity.animal.axolotl.ValidatePlayDead;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Predicate;

public class SolarAxolotlAi extends AxolotlAi { private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
    private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.2F;
    private static final float SPEED_MULTIPLIER_ON_LAND = 0.15F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
    private static final float SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER = 0.6F;
    private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT_IN_WATER = 0.6F;

    public static Brain<?> makeBrain( Brain<Axolotl> brain ) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initPlayDeadActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initPlayDeadActivity(Brain<Axolotl> brain) {
        brain.addActivityAndRemoveMemoriesWhenStopped(Activity.PLAY_DEAD, ImmutableList.of(Pair.of(0, new PlayDead()), Pair.of(1, EraseMemoryIf.create(BehaviorUtils ::isBreeding, MemoryModuleType.PLAY_DEAD_TICKS))), ImmutableSet.of(Pair.of(MemoryModuleType.PLAY_DEAD_TICKS, MemoryStatus.VALUE_PRESENT)), ImmutableSet.of(MemoryModuleType.PLAY_DEAD_TICKS));
    }

    private static void initFightActivity(Brain<Axolotl> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 0, ImmutableList.of(StopAttackingIfTargetInvalid.create(Axolotl::onStopAttacking), SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(SolarAxolotlAi::getSpeedModifierChasing), MeleeAttack.create(20), EraseMemoryIf.create(BehaviorUtils::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
    }

    private static void initCoreActivity(Brain<Axolotl> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), ValidatePlayDead.create(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
    }

    private static void initIdleActivity(Brain<Axolotl> brain) {
        brain.addActivity(Activity.IDLE, ImmutableList.of(Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), Pair.of(1, new AnimalMakeLove(EntityType.AXOLOTL, 0.2F, 2)), Pair.of(2, new RunOne(ImmutableList.of(Pair.of(new FollowTemptation(SolarAxolotlAi::getSpeedModifier), 1), Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, SolarAxolotlAi::getSpeedModifierFollowingAdult), 1)))), Pair.of(3, StartAttacking.create(SolarAxolotlAi::findNearestValidAttackTarget)), Pair.of(3, TryFindWater.create(6, 0.15F)), Pair.of(4, new GateBehavior(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, ImmutableList.of(Pair.of(RandomStroll.swim(0.5F), 2), Pair.of(RandomStroll.stroll(0.15F, false), 2), Pair.of(SetWalkTargetFromLookTarget.create(SolarAxolotlAi::canSetWalkTargetFromLookTarget, SolarAxolotlAi::getSpeedModifier, 3), 3), Pair.of(BehaviorBuilder.triggerIf(Entity ::isInWaterOrBubble), 5), Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), 5))))));
    }

    private static boolean canSetWalkTargetFromLookTarget( LivingEntity entity) {
        Level level = entity.level();
        Optional<PositionTracker> optional = entity.getBrain().getMemory(MemoryModuleType.LOOK_TARGET);
        if (optional.isPresent()) {
            BlockPos blockpos = ((PositionTracker)optional.get()).currentBlockPosition();
            return level.isWaterAt(blockpos) == entity.isInWaterOrBubble();
        } else {
            return false;
        }
    }

    public static void updateActivity(Axolotl axolotl) {
        Brain<Axolotl> brain = axolotl.getBrain();
        Activity activity = (Activity)brain.getActiveNonCoreActivity().orElse((Activity) null);
        if (activity != Activity.PLAY_DEAD) {
            brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.PLAY_DEAD, Activity.FIGHT, Activity.IDLE));
            if (activity == Activity.FIGHT && brain.getActiveNonCoreActivity().orElse((Activity) null) != Activity.FIGHT) {
                brain.setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
            }
        }

    }

    private static float getSpeedModifierChasing(LivingEntity entity) {
        return entity.isInWaterOrBubble() ? 0.6F : 0.15F;
    }

    private static float getSpeedModifierFollowingAdult(LivingEntity entity) {
        return entity.isInWaterOrBubble() ? 0.6F : 0.15F;
    }

    private static float getSpeedModifier(LivingEntity entity) {
        return entity.isInWaterOrBubble() ? 0.5F : 0.15F;
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Axolotl axolotl) {
        return BehaviorUtils.isBreeding(axolotl) ? Optional.empty() : axolotl.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }

    public static Predicate<ItemStack> getTemptations() {
        return (p_335657_) -> p_335657_.is(ItemTags.AXOLOTL_FOOD);
    }
}
