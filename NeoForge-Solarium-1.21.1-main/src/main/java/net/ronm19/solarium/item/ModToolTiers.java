package net.ronm19.solarium.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.ronm19.solarium.util.ModTags;

public class ModToolTiers {
    public static final Tier SOLARIUM = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_SOLARIUM_TOOL, 2500, 2.6f, 4.5f, 25,
            () -> Ingredient.of(ModItems.SOLARIUM_INGOT.get()));
}
