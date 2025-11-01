
package net.ronm19.solarium.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.ronm19.solarium.enchantment.ModEnchantments;
import net.ronm19.solarium.enchantment.custom.SolarFireEnchantmentEffect;
import net.ronm19.solarium.entity.custom.SolarFireballEntity;
import org.jetbrains.annotations.NotNull;

public class SolarFangItem extends SwordItem {


    public SolarFangItem( Tier tier, Properties properties ) {
        super(tier, properties);
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
    public @NotNull InteractionResultHolder<ItemStack> use( Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            int enchantLevel = getSolarFireEnchantmentLevel(stack, level);

            if (enchantLevel > 0) {
                // Enchanted -> large fireballs
                new SolarFireEnchantmentEffect(enchantLevel)
                        .apply((ServerLevel) level, enchantLevel, null, player, player.getLookAngle());
            } else {
                // Default -> small, fast fireballs
                spawnSmallFireballs(player, (ServerLevel) level, 1);
            }

            damageItem(stack, player, hand);
            player.swing(hand, true);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, Player attacker) {
        // Melee hit -> ignite the target
        target.igniteForTicks(5);

        // Damage the item
        if (attacker instanceof ServerPlayer serverPlayer) {
            stack.hurtAndBreak(1, serverPlayer,
                    attacker.getUsedItemHand() == InteractionHand.MAIN_HAND
                            ? EquipmentSlot.MAINHAND
                            : EquipmentSlot.OFFHAND);
        }

        return true;
    }

    private void spawnSmallFireballs(Player player, ServerLevel world, int count) {
        Vec3 look = player.getLookAngle();
        for (int i = 0; i < count; i++) {
            SolarFireballEntity fireball = new SolarFireballEntity(EntityType.SMALL_FIREBALL, world);
            fireball.setOwner(player);
            fireball.moveTo(player.getX(), player.getEyeY() - 0.1, player.getZ());
            fireball.shoot(look.x, look.y, look.z, 2.0F, 0.0F); // fast small fireball
            world.addFreshEntity(fireball);
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.2F);
    }

    private int getSolarFireEnchantmentLevel(ItemStack stack, Level level) {
        // Safe way to get the Holder and level
        return level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(ModEnchantments.SOLAR_FIRE)
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack))
                .orElse(0);
    }

    private void damageItem(ItemStack stack, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            stack.hurtAndBreak(1, serverPlayer, hand == InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND);
        }
    }
}
