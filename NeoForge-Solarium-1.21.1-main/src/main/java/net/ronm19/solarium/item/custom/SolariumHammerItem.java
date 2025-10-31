package net.ronm19.solarium.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SolariumHammerItem extends DiggerItem {

    private static final float BASE_DAMAGE = 15.0F;
    private static final double KNOCKBACK_FORCE = 10.0;
    private static final double SOLAR_SMASH_RADIUS = 25.0;

    public SolariumHammerItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    /** Simple fixed AoE mining radius. */
    private int getMiningRange() {
        return 2; // 5x5 area
    }

    /** Collects blocks affected by AoE mining. */
    public static List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initialPos, ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(
                player.getEyePosition(1f),
                player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        if (traceResult.getType() == HitResult.Type.MISS) {
            return positions;
        }

        Direction face = traceResult.getDirection();
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                BlockPos pos;
                if (face == Direction.UP || face == Direction.DOWN) {
                    pos = initialPos.offset(x, 0, y);
                } else if (face == Direction.NORTH || face == Direction.SOUTH) {
                    pos = initialPos.offset(x, y, 0);
                } else {
                    pos = initialPos.offset(0, y, x);
                }
                positions.add(pos);
            }
        }
        return positions;
    }

    /** AoE Mining ability. */
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        int range = getMiningRange();
        List<BlockPos> blocksToBreak = getBlocksToBeDestroyed(range, pos, player);

        for (BlockPos targetPos : blocksToBreak) {
            BlockState targetState = level.getBlockState(targetPos);
            if (targetState.getDestroySpeed(level, targetPos) >= 0 && level instanceof ServerLevel serverLevel) {
                serverLevel.destroyBlock(targetPos, true, player);
            }
        }

        level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.2F, 0.8F);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, state));

        stack.hurtAndBreak(1, entity, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
        return true;
    }

    /** Combat: yeets mobs away with heavy knockback. */
    @Override
    public boolean hurtEnemy( @NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return super.hurtEnemy(stack, target, attacker);

        Vec3 look = player.getLookAngle().normalize();
        double horizontalBoost = KNOCKBACK_FORCE;
        double verticalBoost = 2.5;

        // Launch mob
        target.setDeltaMovement(look.x * horizontalBoost, verticalBoost, look.z * horizontalBoost);
        target.hurtMarked = true;

        // Deal strong damage
        target.hurt(player.level().damageSources().playerAttack(player), BASE_DAMAGE);

        // Play sound
        player.level().playSound(null, target.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 2.5F, 0.5F);

        stack.hurtAndBreak(1, attacker, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
        return true;
    }

    /** Right-click: Solar Smash AoE knockback. */
    @Override
    public @NotNull InteractionResult useOn( UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !(player.level() instanceof ServerLevel level)) return InteractionResult.PASS;

        Vec3 center = context.getClickedPos().getCenter();
        AABB area = new AABB(
                center.x - SOLAR_SMASH_RADIUS, center.y - SOLAR_SMASH_RADIUS, center.z - SOLAR_SMASH_RADIUS,
                center.x + SOLAR_SMASH_RADIUS, center.y + SOLAR_SMASH_RADIUS, center.z + SOLAR_SMASH_RADIUS
        );

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            if (entity != player) {
                Vec3 dir = entity.position().subtract(center).normalize();
                entity.setDeltaMovement(dir.x * KNOCKBACK_FORCE * 2.5, 2.5, dir.z * KNOCKBACK_FORCE * 2.5);
                entity.hurtMarked = true;
            }
        }

        level.playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 6.0F, 0.8F);
        level.sendParticles(net.minecraft.core.particles.ParticleTypes.EXPLOSION_EMITTER, center.x, center.y, center.z, 1, 0, 0, 0, 0);

        return InteractionResult.SUCCESS;
    }
}
