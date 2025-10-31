package net.ronm19.solarium.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solarium.SolariumMod;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, SolariumMod.MOD_ID);

    public static final Holder<MobEffect> SOLAR_BLESSING_EFFECT = MOB_EFFECTS.register("solar_blessing",
            () -> new SolarBlessingEffect(MobEffectCategory.BENEFICIAL, 0xFFD700)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solar_blessing_speed"),
                            0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE,
                            ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, "solar_blessing_attack"),
                            0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));


    public static void register( IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}