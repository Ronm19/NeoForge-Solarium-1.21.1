package net.ronm19.solariummod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.ronm19.solariummod.SolariumMod;
import net.ronm19.solariummod.block.ModBlocks;
import net.ronm19.solariummod.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.ronm19.solariummod.block.ModBlocks.SOLARIUM_PRESSURE_PLATE;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> SOLARIUM_INGOT_SMElTABLES = List.of(ModItems.RAW_SOLARIUM_INGOT,
                ModBlocks.SOLARIUM_ORE, ModBlocks.DEEPSLATE_SOLARIUM_ORE);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SOLARIUM_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.SOLARIUM_INGOT.get())
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);

         ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SOLAR_PYLON_BLOCK.get())
                .pattern("SSS")
                .pattern("SRS")
                .pattern("SSS")
                .define('S', ModItems.SOLARIUM_INGOT.get())
                 .define('R', Items.REDSTONE)
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);



        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SOLARIUM_INGOT.get(), 9)
                .requires(ModBlocks.SOLARIUM_BLOCK.get())
                .unlockedBy("has_solarium_block", has(ModBlocks.SOLARIUM_BLOCK.get())).save(recipeOutput);



        oreSmelting(recipeOutput, SOLARIUM_INGOT_SMElTABLES, RecipeCategory.MISC, ModItems.SOLARIUM_INGOT.get(), 0.25f, 200, "solarium_ingot");
        oreBlasting(recipeOutput, SOLARIUM_INGOT_SMElTABLES, RecipeCategory.MISC, ModItems.SOLARIUM_INGOT.get(), 0.25f, 100, "solarium_ingot");

        stairBuilder(ModBlocks.SOLARIUM_STAIRS.get(), Ingredient.of( ModItems.SOLARIUM_INGOT.get())).group("solarium_ingot")
                        .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);

        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOLARIUM_SLAB.get(), ModItems.SOLARIUM_INGOT.get());

        pressurePlate(recipeOutput, SOLARIUM_PRESSURE_PLATE.get(), ModItems.SOLARIUM_INGOT.get());

        buttonBuilder(ModBlocks.SOLARIUM_BUTTON.get(), Ingredient.of(ModItems.SOLARIUM_INGOT.get())).group("solarium_ingot")
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);

        fenceBuilder(ModBlocks.SOLARIUM_FENCE.get(), Ingredient.of(ModItems.SOLARIUM_INGOT.get())).group("solarium_ingot")
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);

        fenceGateBuilder(ModBlocks.SOLARIUM_FENCE_GATE.get(), Ingredient.of(ModItems.SOLARIUM_INGOT.get())).group("solarium_ingot")
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOLARIUM_WALL.get(), ModItems.SOLARIUM_INGOT);

        doorBuilder(ModBlocks.SOLARIUM_DOOR.get(), Ingredient.of(ModItems.SOLARIUM_INGOT.get())).group("solarium_ingot")
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);

        trapdoorBuilder(ModBlocks.SOLARIUM_TRAPDOOR.get(), Ingredient.of(ModItems.SOLARIUM_INGOT.get())).group("solarium_ingot")
                .unlockedBy("has_solarium_ingot", has(ModItems.SOLARIUM_INGOT.get())).save(recipeOutput);


    }

    protected static void oreSmelting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pRecipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pRecipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput pRecipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pRecipeOutput, SolariumMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
