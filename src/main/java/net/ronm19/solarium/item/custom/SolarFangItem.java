package net.ronm19.solarium.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SolarFangItem extends Item {

    public SolarFangItem(Properties properties) {
        super(properties);
    }

    /**
     * Damages the item and breaks it if necessary.
     */
    public static void hurtAndBreak(ItemStack stack, int damage, ServerPlayer player, InteractionHand hand) {
        if (stack.isEmpty() || player == null) return;

        // Damage the item and let the game handle the break animation automatically
        stack.hurtAndBreak(damage, player, EquipmentSlot.MAINHAND);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Shoot a single fireball for now
            shootFireballs(player, (ServerLevel) level, 1);

            // Damage the item by 1 durability point
            if (player instanceof ServerPlayer serverPlayer) {
                hurtAndBreak(stack, 1, serverPlayer, hand);
            }

            // Swing hand to show use animation
            player.swing(hand, true);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private void shootFireballs(Player player, ServerLevel world, int count) {
        Vec3 look = player.getLookAngle();

        // Spread offsets (for single fireball it's just 0)
        float[] offsets = count == 1 ? new float[]{0f} :
                count == 2 ? new float[]{-0.05f, 0.05f} :
                        new float[]{-0.08f, 0f, 0.08f};

        for (float yawOffset : offsets) {
            double sin = Math.sin(yawOffset);
            double cos = Math.cos(yawOffset);

            Vec3 direction = new Vec3(
                    look.x * cos - look.z * sin,
                    look.y,
                    look.x * sin + look.z * cos
            );

            SmallFireball fireball = new SmallFireball(EntityType.SMALL_FIREBALL, world);
            fireball.setOwner(player);
            fireball.moveTo(player.getX(), player.getEyeY() - 0.1, player.getZ());
            fireball.shoot(direction.x, direction.y, direction.z, 1.5F, 0.0F);

            world.addFreshEntity(fireball);
        }

        // Play shooting sound
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, Player attacker) {
        // Set the target on fire for 5 seconds
        target.igniteForTicks(5);

        // Damage the item by 1 durability
        if (attacker instanceof ServerPlayer serverPlayer) {
            stack.hurtAndBreak(1, serverPlayer,
                    attacker.getUsedItemHand() == InteractionHand.MAIN_HAND
                            ? EquipmentSlot.MAINHAND
                            : EquipmentSlot.OFFHAND);
        }

        return true; // Hit was successful
    }
}
