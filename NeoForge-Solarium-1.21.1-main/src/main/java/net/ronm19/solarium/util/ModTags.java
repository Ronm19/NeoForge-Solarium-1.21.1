package net.ronm19.solarium.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.ronm19.solarium.SolariumMod;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> INCORRECT_FOR_SOLARIUM_TOOL = createTag("incorrect_for_solarium_tool");
        public static final TagKey<Block> NEEDS_SOLARIUM_TOOL = createTag("needs_solarium_tool");

        public static final TagKey<Block> SOLARIUM_PAXEL_MINEABLE = createTag("solarium_paxel_mineable");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, name));


        }
    }

    public static class Items {

        public static final TagKey<Item> SOLAR_AMBER_LOGS = createTag("solar_amber_logs");
        public static final TagKey<Item> SOLAR_ASH_LOGS = createTag("solar_ash_logs");

        private static TagKey<Item> createTag( String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, name));
        }
    }
}
