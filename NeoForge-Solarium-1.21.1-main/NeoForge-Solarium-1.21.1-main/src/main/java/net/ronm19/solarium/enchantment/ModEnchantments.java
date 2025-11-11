package net.ronm19.solarium.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.enchantment.custom.SolarFireEnchantmentEffect;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> SOLAR_FIRE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solar_fire"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantment = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        register(context, SOLAR_FIRE, Enchantment.enchantment(Enchantment.definition(items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
               items.getOrThrow(ItemTags.SWORD_ENCHANTABLE), 5, 2,
                Enchantment.dynamicCost(5, 8), Enchantment.dynamicCost(25, 8), 10,
                EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchantment.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM, new SolarFireEnchantmentEffect(1)));

    }

    private static void register( BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}
