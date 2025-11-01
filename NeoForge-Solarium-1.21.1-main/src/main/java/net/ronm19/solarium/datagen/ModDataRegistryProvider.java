package net.ronm19.solarium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.enchantment.ModEnchantments;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDataRegistryProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);

    public ModDataRegistryProvider( PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(SolariumMod.MOD_ID));
    }
}
