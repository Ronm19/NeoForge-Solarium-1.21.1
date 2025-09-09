package net.ronm19.solariummod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.ronm19.solariummod.SolariumMod;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> INCORRECT_FOR_SOLARIUM_INGOT_TOOL = createTag("incorrect_for_solarium_ingot_tool");
        public static final TagKey<Block> NEEDS_SOLARIUM_INGOT_TOOL = createTag("needs_solarium_ingot_tool");

        public static final TagKey<Block> SOLARIUM_PAXEL_MINEABLE =  createTag("mineable/solarium_paxel");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, name));

        }
    }


    public static class Items {
        private TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SolariumMod.MOD_ID, name));


        }
    }
}
