package net.ronm19.solarium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
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


        // -------------- Shapeless Recipes ---------------------- //

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SOLARIUM_INGOT.get(), 9)
                .requires(ModBlocks.SOLARIUM_BLOCK.get())
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


