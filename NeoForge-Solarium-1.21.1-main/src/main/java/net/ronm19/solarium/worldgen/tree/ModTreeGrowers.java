package net.ronm19.solarium.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.worldgen.ModConfiguredFeatures;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower SOLAR_AMBER = new TreeGrower(SolariumMod.MOD_ID + ":solar_amber",
            Optional.empty(), Optional.of(ModConfiguredFeatures.SOLAR_AMBER_KEY), Optional.empty());
}
