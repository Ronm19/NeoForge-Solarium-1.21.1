package net.ronm19.solarium.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.entity.custom.SolarAxolotlEntity;
import net.ronm19.solarium.entity.custom.SolarCreeperEntity;
import net.ronm19.solarium.entity.custom.SolarGhastEntity;
import net.ronm19.solarium.entity.custom.SolarStalkerEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SolariumMod.MOD_ID);

    public static final Supplier<EntityType<SolarCreeperEntity>> SOLAR_CREEPER =
            ENTITY_TYPES.register("solar_creeper", () -> EntityType.Builder.of(SolarCreeperEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 2.0f).build("solar_creeper"));

    public static final Supplier<EntityType<SolarGhastEntity>> SOLAR_GHAST =
            ENTITY_TYPES.register("solar_ghast", () -> EntityType.Builder.of(SolarGhastEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.0f).build("solar_ghast"));

    public static final Supplier<EntityType<SolarStalkerEntity>> SOLAR_STALKER =
            ENTITY_TYPES.register("solar_stalker", () -> EntityType.Builder.of(SolarStalkerEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 1.3f).build("solar_stalker"));



    public static final Supplier<EntityType<SolarAxolotlEntity>> SOLAR_AXOLOTL =
            ENTITY_TYPES.register("solar_axolotl", () -> EntityType.Builder.of(SolarAxolotlEntity ::new, MobCategory.AXOLOTLS)
                    .sized(0.8f, 1.0f).build("solar_ghast"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
