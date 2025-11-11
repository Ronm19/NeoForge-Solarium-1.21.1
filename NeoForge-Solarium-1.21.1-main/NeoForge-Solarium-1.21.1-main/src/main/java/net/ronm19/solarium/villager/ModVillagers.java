package net.ronm19.solarium.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;
import net.ronm19.solarium.sound.ModSounds;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, SolariumMod.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, SolariumMod.MOD_ID);

    public static final Holder<PoiType> SOLAR_POI = POI_TYPES.register("solar_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.SOLAR_WORKBENCH_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final Holder<VillagerProfession> SOLARGER = VILLAGER_PROFESSIONS.register("solarger",
            () -> new VillagerProfession("solarger", holder -> holder.value() == SOLAR_POI.value(),
                    holder -> holder.value() == SOLAR_POI.value(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.WITHER_SPAWN));

    public static void register( IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
