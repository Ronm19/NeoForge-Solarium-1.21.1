package net.ronm19.solarium.item.custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.ronm19.solariummod.util.ModDataComponents;

import java.util.ArrayList;
import java.util.List;

public class SolariumHammerItem extends DiggerItem {

    private static final int MAX_CHARGE = 200;
    private static final float BASE_DAMAGE = 12.0F;
    private static final float MAX_KNOCKBACK = 18.0F;  // INSANE YEET FORCE
    private static final double SOLAR_NUKE_RADIUS = 40.0;

    public SolariumHammerItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    /** Dynamic AoE mining range based on solar charge. */
    private int getMiningRange(int charge) {
        if (charge >= 180) return 7;  // 15×15 GOD MODE
        if (charge >= 120) return 4;  // 9×9
        return 2;                     // Default 5×5
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

    /** AoE Mining based on solar charge. */
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);
        int range = getMiningRange(charge);

        // Dynamic solar cost
        int energyCost = switch (range) {
            case 7 -> 50; // 15×15
            case 4 -> 25; // 9×9
            default -> 10; // 5×5
        };

        // Mine blocks in AoE
        List<BlockPos> blocksToBreak = getBlocksToBeDestroyed(range, pos, player);
        for (BlockPos targetPos : blocksToBreak) {
            BlockState targetState = level.getBlockState(targetPos);
            if (targetState.getDestroySpeed(level, targetPos) >= 0 && level instanceof ServerLevel serverLevel) {
                serverLevel.destroyBlock(targetPos, true, player);
            }
        }

        // Drain energy
        stack.set(ModDataComponents.SOLAR_CHARGE.get(), Math.max(charge - energyCost, 0));

        // Play sound
        level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.2F, 0.8F);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, state));

        return true;
    }

    /** Special hammer combat — YEET mode + insane solar scaling. */
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return super.hurtEnemy(stack, target, attacker);

        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);

        // Damage scales up with solar charge
        float damage = BASE_DAMAGE + (charge / (float) MAX_CHARGE) * 25.0F;

        // Get player's facing direction
        Vec3 look = player.getLookAngle().normalize();

        // **INSANE KNOCKBACK SETTINGS**
        double horizontalBoost = 10.0 + (charge / 4.0); // Higher scaling, can reach >50 blocks
        double verticalBoost = 2.0 + (charge / 150.0);  // Launch mobs higher too

        // Launch mob into orbit 🚀
        target.setDeltaMovement(look.x * horizontalBoost, verticalBoost, look.z * horizontalBoost);
        target.hurtMarked = true;

        // Inflict boosted solar damage
        target.hurt(player.level().damageSources().playerAttack(player), damage);

        // Massive solar charge cost for massive YEET
        stack.set(ModDataComponents.SOLAR_CHARGE.get(), Math.max(charge - 50, 0));

        // Reduce durability
        stack.hurtAndBreak(1, attacker, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));

        // Play a thunderclap sound when YEETING
        player.level().playSound(null, target.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 2.5F, 0.5F);

        return true;
    }

    /** Right-click Solar Nuke — launches mobs in a 40-block radius. */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !(player.level() instanceof ServerLevel level)) return InteractionResult.PASS;

        ItemStack stack = context.getItemInHand();
        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);

        if (charge < MAX_CHARGE) {
            return InteractionResult.PASS; // Not enough energy for Solar Nuke
        }

        Vec3 center = context.getClickedPos().getCenter();

        // Correct AABB creation for 1.21.1
        AABB area = new AABB(
                center.x - SOLAR_NUKE_RADIUS, center.y - SOLAR_NUKE_RADIUS, center.z - SOLAR_NUKE_RADIUS,
                center.x + SOLAR_NUKE_RADIUS, center.y + SOLAR_NUKE_RADIUS, center.z + SOLAR_NUKE_RADIUS
        );

        // YEET all mobs in the blast radius
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) {
            if (entity != player) {
                Vec3 dir = entity.position().subtract(center).normalize();
                entity.setDeltaMovement(dir.x * MAX_KNOCKBACK * 2.5, 2.5, dir.z * MAX_KNOCKBACK * 2.5);
                entity.hurtMarked = true;
            }
        }

        // Explosion effect
        level.playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 6.0F, 0.8F);
        level.sendParticles(net.minecraft.core.particles.ParticleTypes.EXPLOSION_EMITTER, center.x, center.y, center.z, 1, 0, 0, 0, 0);

        // Drain full energy
        stack.set(ModDataComponents.SOLAR_CHARGE.get(), 0);

        return InteractionResult.SUCCESS;
    }


    /** Faster mining speed based on solar charge. */
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        int charge = stack.getOrDefault(ModDataComponents.SOLAR_CHARGE.get(), 0);
        return charge >= 180 ? 999.0F : super.getDestroySpeed(stack, state) + ((charge / (float) MAX_CHARGE) * 15.0F);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.solariummod.solarium_hammer.tooltip_1"));
            tooltipComponents.add(Component.translatable("tooltip.solariummod.solarium_hammer.tooltip_2"));

        } else {
            tooltipComponents.add(Component.translatable("tooltip.solariummod.solarium_hammer.tooltip_shift"));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
