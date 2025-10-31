package net.ronm19.solarium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.ronm19.solarium.SolariumMod;
import net.ronm19.solarium.block.ModBlocks;
import net.ronm19.solarium.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider( PackOutput output, CompletableFuture<HolderLookup.Provider> registries ) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes( RecipeOutput recipeOutput ) {
        List<ItemLike> SOLARIUM_INGOT_SMELTABLES = List.of(ModItems.SOLARIUM_INGOT,
                ModBlocks.SOLARIUM_ORE, ModBlocks.DEEPSLATE_SOLARIUM_ORE);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SOLARIUM_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.SOLARIUM_INGOT.get())
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOLAR_ASH_BLOCK.get())
                .pattern("GGG")
                .pattern("GPG")
                .pattern("GGG")
                .define('G', Blocks.GRAVEL)
                .define('P', Items.GUNPOWDER)
                .unlockedBy("has_gunpowder", has(Items.GUNPOWDER)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOLAR_EMBER_BLOCK.get())
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .define('E', ModItems.SOLAR_EMBER)
                .unlockedBy("has_solar_ember", has(ModItems.SOLAR_EMBER)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOLAR_WORKBENCH_BLOCK.get())
                .pattern("SSS")
                .pattern("SCS")
                .pattern("SSS")
                .define('C', Blocks.CRAFTING_TABLE)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_crafting_table", has(Blocks.CRAFTING_TABLE)).save(recipeOutput);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLARIUM_SWORD.get())
                .pattern(" S ")
                .pattern(" S ")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLARIUM_PICKAXE.get())
                .pattern("SSS")
                .pattern(" T ")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLARIUM_AXE.get())
                .pattern("SS ")
                .pattern("ST ")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLARIUM_SHOVEL.get())
                .pattern(" S ")
                .pattern(" T ")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLARIUM_HOE.get())
                .pattern("SS ")
                .pattern(" T ")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLARIUM_PAXEL.get())
                .pattern("SSS")
                .pattern(" TS")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLARIUM_HAMMER.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLAR_DAGGER.get())
                .pattern(" S ")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SOLAR_FANG.get())
                .pattern("  S")
                .pattern(" S ")
                .pattern(" T ")
                .define('T', Items.STICK)
                .define('S', ModItems.SOLARIUM_INGOT)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT)).save(recipeOutput);


        // -------------- Shapeless Recipes ---------------------- //

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SOLARIUM_INGOT.get(), 9)
                .requires(ModBlocks.SOLARIUM_BLOCK.get())
                .unlockedBy("has_solarium_block", has(ModBlocks.SOLARIUM_BLOCK.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SOLAR_EMBER.get(), 9)
                .requires(ModBlocks.SOLAR_EMBER_BLOCK.get())
                .unlockedBy("has_solar_ember_block", has(ModBlocks.SOLAR_EMBER_BLOCK.get())).save(recipeOutput);

        stairBuilder(ModBlocks.SOLARIUM_STAIRS.get(), Ingredient.of(ModBlocks.SOLARIUM_BLOCK.get())).group("solarium")
                .unlockedBy("has_solarium_block", has(ModBlocks.SOLARIUM_BLOCK.get())).save(recipeOutput);

        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOLARIUM_SLAB.get(), ModBlocks.SOLARIUM_BLOCK.get());
        pressurePlate(recipeOutput, ModBlocks.SOLARIUM_PRESSURE_PLATE.get(), ModBlocks.SOLARIUM_BLOCK.get());

        buttonBuilder(ModBlocks.SOLARIUM_BUTTON.get(), Ingredient.of(ModBlocks.SOLARIUM_BLOCK.get())).group("solarium")
                .unlockedBy("has_solarium_block", has(ModBlocks.SOLARIUM_BLOCK.get())).save(recipeOutput);

        fenceBuilder(ModBlocks.SOLARIUM_FENCE.get(), Ingredient.of(ModBlocks.SOLARIUM_BLOCK.get())).group("solarium")
                .unlockedBy("has_solarium_block", has(ModBlocks.SOLARIUM_BLOCK.get())).save(recipeOutput);
        fenceGateBuilder(ModBlocks.SOLARIUM_FENCE_GATE.get(), Ingredient.of(ModBlocks.SOLARIUM_BLOCK.get())).group("solarium")
                .unlockedBy("has_solarium_block", has(ModBlocks.SOLARIUM_BLOCK.get())).save(recipeOutput);
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOLARIUM_WALL.get(), ModBlocks.SOLARIUM_BLOCK.get());

        doorBuilder(ModBlocks.SOLARIUM_DOOR.get(), Ingredient.of(ModBlocks.SOLARIUM_BLOCK.get())).group("solarium")
                .unlockedBy("has_solarium_block", has(ModBlocks.SOLARIUM_BLOCK.get())).save(recipeOutput);
        trapdoorBuilder(ModBlocks.SOLARIUM_TRAPDOOR.get(), Ingredient.of(ModBlocks.SOLARIUM_BLOCK.get())).group("solarium")
                .unlockedBy("has_solarium_block", has(ModBlocks.SOLARIUM_BLOCK.get())).save(recipeOutput);


        // ------------- Cooking Recipes ---------- //

        oreSmelting(recipeOutput, SOLARIUM_INGOT_SMELTABLES, RecipeCategory.MISC, ModItems.SOLARIUM_INGOT.get(), 0.25f, 200, "solarium_ingot");
        oreBlasting(recipeOutput, SOLARIUM_INGOT_SMELTABLES, RecipeCategory.MISC, ModItems.SOLARIUM_INGOT.get(), 0.25f, 100, "solarium_ingot");
    }



    protected static void oreSmelting( RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                       float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pRecipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe ::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting( RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                       float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pRecipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking( RecipeOutput pRecipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                        List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pRecipeOutput, SolariumMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}


