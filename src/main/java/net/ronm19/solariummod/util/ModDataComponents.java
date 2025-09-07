package net.ronm19.solariummod.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ronm19.solariummod.SolariumMod;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SolariumMod.MOD_ID);

    // Register our solar charge data component
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SOLAR_CHARGE =
            DATA_COMPONENTS.register("solar_charge", () ->
                    new DataComponentType.Builder<Integer>()
                            // Store the data persistently in NBT using Codec.INT
                            .persistent(Codec.INT)
                            // Automatically sync between server & client
                            .networkSynchronized(StreamCodec.of(
                                    (buf, value) -> buf.writeVarInt(value),
                                    RegistryFriendlyByteBuf::readVarInt
                            ))
                            .build()
            );
}