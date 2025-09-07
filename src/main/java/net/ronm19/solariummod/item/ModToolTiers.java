package net.ronm19.solariummod.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.ronm19.solariummod.util.ModTags;

public class ModToolTiers {
    public static final Tier SOLARIUM_INGOT = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_SOLARIUM_INGOT_TOOL,
            2200, 3f, 4.5f, 30,
            () -> Ingredient.of(ModItems.SOLARIUM_INGOT.get()));
}
