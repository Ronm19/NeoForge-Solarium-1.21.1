package net.ronm19.solarium.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.effect.ModEffects;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, SolariumMod.MOD_ID);

    public static final Holder<Potion> SOLAR_BLESSING_POTION = POTIONS.register("solar_blessing_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.SOLAR_BLESSING_EFFECT, 300, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
