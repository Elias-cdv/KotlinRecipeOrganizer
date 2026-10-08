//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
package com.recipeorganizer

/**
 * Data class representing an individual Cooking Recipe.
 * Demonstrates Kotlin data classes, immutable properties (val), and calculated member functions.
 */
data class Recipe(
    val id: Int,
    val title: String,
    val category: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val ingredients: List<String>,
    val instructions: String
) {
    /**
     * Calculates total duration required for preparation and cooking.
     * Returns total duration in minutes.
     */
    fun getTotalTime(): Int {
        return prepTimeMinutes + cookTimeMinutes
    }

    /**
     * Prints a formatted summary block displaying recipe details to the console.
     */
    fun displayRecipeSummary() {
        println("==================================================")
        println("ID: #$id | Title: $title")
        println("Category: $category | Total Time: ${getTotalTime()} mins (Prep: $prepTimeMinutes m, Cook: $cookTimeMinutes m)")
        println("Ingredients: ${ingredients.joinToString(", ")}")
        println("Instructions: $instructions")
        println("==================================================")
    }
}

/**
 * Encapsulates the collection of recipes and handles core application operations.
 */
class RecipeManager {
    private val recipeList: MutableList<Recipe> = mutableListOf()
    private var nextRecipeId: Int = 1

    /**
     * Instantiates and adds a new Recipe object to the internal collection.
     */
    fun addRecipe(title: String, category: String, prepTime: Int, cookTime: Int, ingredients: List<String>, instructions: String) {
        val newRecipe = Recipe(
            id = nextRecipeId++,
            title = title,
            category = category,
            prepTimeMinutes = prepTime,
            cookTimeMinutes = cookTime,
            ingredients = ingredients,
            instructions = instructions
        )
        recipeList.add(newRecipe)
        println("\n[+] Recipe '${newRecipe.title}' successfully added with ID #${newRecipe.id}!")
    }

    /**
     * Displays all recipes currently stored in the system.
     */
    fun displayAllRecipes() {
        if (recipeList.isEmpty()) {
            println("\n[!] No recipes found in your collection.")
            return
        }
        println("\n--- All Stored Recipes (${recipeList.size} Total) ---")
        for (recipe in recipeList) {
            recipe.displayRecipeSummary()
        }
    }

    /**
     * Searches and displays recipes matching a given category string using filter expressions.
     */
    fun searchByCategory(categoryName: String) {
        val filtered = recipeList.filter { it.category.equals(categoryName, ignoreCase = true) }
        if (filtered.isEmpty()) {
            println("\n[!] No recipes found for category: '$categoryName'")
            return
        }
        println("\n--- Found ${filtered.size} Recipe(s) in Category '$categoryName' ---")
        filtered.forEach { it.displayRecipeSummary() }
    }

    /**
     * Computes and displays overall statistics for the recipe collection.
     */
    fun displayStatistics() {
        if (recipeList.isEmpty()) {
            println("\n[!] Cannot calculate statistics: Recipe collection is empty.")
            return
        }
        val totalRecipes = recipeList.size
        val avgTotalTime = recipeList.map { it.getTotalTime() }.average()

        println("\n==========================================")
        println("          RECIPE COLLECTION STATS         ")
        println("==========================================")
        println("Total Recipes Stored: $totalRecipes")
        println("Average Preparation + Cook Time: %.1f minutes".format(avgTotalTime))
        println("==========================================")
    }

    /**
     * Seeds the application with initial sample data for quick testing.
     */
    fun seedSampleData() {
        addRecipe(
            title = "Shakshuka",
            category = "Breakfast",
            prepTime = 10,
            cookTime = 20,
            ingredients = listOf("Eggs", "Tomatoes", "Bell Peppers", "Garlic", "Cumin"),
            instructions = "Poach eggs in simmering spiced tomato and pepper sauce."
        )
        addRecipe(
            title = "Shoyu Ramen",
            category = "Dinner",
            prepTime = 25,
            cookTime = 35,
            ingredients = listOf("Ramen Noodles", "Soy Sauce Broth", "Chashu Pork", "Soft Egg", "Green Onion"),
            instructions = "Prepare broth, boil noodles, and assemble with toppings."
        )
        addRecipe(
            title = "Coastal Ceviche",
            category = "Lunch",
            prepTime = 20,
            cookTime = 0,
            ingredients = listOf("Fresh Fish", "Lime Juice", "Red Onion", "Cilantro", "Chifles"),
            instructions = "Cure fresh fish in lime juice and toss with onion and cilantro."
        )
    }
}

/**
 * Main application entry point executing the interactive CLI menu loop.
 */
fun main() {
    val manager = RecipeManager()
    manager.seedSampleData()

    var isRunning = true

    println("==============================================")
    println("      WELCOME TO KOTLIN RECIPE ORGANIZER      ")
    println("==============================================")

    while (isRunning) {
        println("\nMAIN MENU:")
        println("1. View All Recipes")
        println("2. Add New Recipe")
        println("3. Search Recipes by Category")
        println("4. View Collection Statistics")
        println("5. Exit Application")
        print("Select an option (1-5): ")

        val input = readlnOrNull()?.trim()

        when (input) {
            "1" -> manager.displayAllRecipes()
            "2" -> promptAndAddRecipe(manager)
            "3" -> promptSearchCategory(manager)
            "4" -> manager.displayStatistics()
            "5" -> {
                println("\nThank you for using Recipe Organizer. Happy cooking!")
                isRunning = false
            }
            else -> println("\n[!] Invalid option. Please enter a number from 1 to 5.")
        }
    }
}

/**
 * Prompts user for interactive inputs to create a new recipe entry safely.
 */
fun promptAndAddRecipe(manager: RecipeManager) {
    println("\n--- Add a New Recipe ---")
    print("Enter Recipe Title: ")
    val title = readlnOrNull()?.trim().orEmpty()
    if (title.isBlank()) {
        println("[!] Recipe title cannot be empty.")
        return
    }

    print("Enter Category (e.g., Breakfast, Lunch, Dinner): ")
    val category = readlnOrNull()?.trim().orEmpty().ifBlank { "General" }

    print("Enter Prep Time (minutes): ")
    val prepTime = readlnOrNull()?.toIntOrNull() ?: 10

    print("Enter Cook Time (minutes): ")
    val cookTime = readlnOrNull()?.toIntOrNull() ?: 15

    print("Enter Ingredients (comma-separated): ")
    val rawIngredients = readlnOrNull()?.trim().orEmpty()
    val ingredientsList = rawIngredients.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    print("Enter Cooking Instructions: ")
    val instructions = readlnOrNull()?.trim().orEmpty().ifBlank { "No instructions provided." }

    manager.addRecipe(
        title = title,
        category = category,
        prepTime = prepTime,
        cookTime = cookTime,
        ingredients = if (ingredientsList.isEmpty()) listOf("Not specified") else ingredientsList,
        instructions = instructions
    )
}

/**
 * Prompts user for a category query and calls search logic in RecipeManager.
 */
fun promptSearchCategory(manager: RecipeManager) {
    print("\nEnter category to search: ")
    val query = readlnOrNull()?.trim().orEmpty()
    if (query.isBlank()) {
        println("[!] Search category cannot be empty.")
        return
    }
    manager.searchByCategory(query)
}