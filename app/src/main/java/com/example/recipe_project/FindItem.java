package com.example.recipe_project;

public class FindItem {
    String recipe_name;
    String recipe_info;
    String recipe_link;

    String recipe_tag;

    String recipe_ingrediantIDs;

    int recipe_ID;

    public FindItem(){

    }
    public FindItem(int recipe_ID, String recipe_info,  String recipe_ingrediantIDs, String recipe_link, String recipe_name,   String recipe_tag) {
        this.recipe_ID = recipe_ID;
        this.recipe_name = recipe_name;
        this.recipe_info= recipe_info;
        this.recipe_link=recipe_link;
        this.recipe_ingrediantIDs = recipe_ingrediantIDs;
        this.recipe_tag =recipe_tag;
    }

    public int getRecipe_ID() {
        return recipe_ID;
    }

    public String getRecipe_Info() {
        return recipe_info;
    }

    public String getRecipe_Name() {
        return recipe_name;
    }

    public String getrecipe_Link() {
        return recipe_link;
    }
    public String getRecipe_IngrediantIDs() {
        return recipe_ingrediantIDs;
    }
    public String getRecipe_Tag() {
        return recipe_tag;
    }

    public void setRecipe_Info(String recipe_Info) {
        this.recipe_info = recipe_Info;
    }

    public void setRecipe_Name(String setrecipe_Name) {
        this.recipe_name = setrecipe_Name;
    }
    public void setRecipe_Link(String recipe_link) {
        this.recipe_link = recipe_link;
    }
    public void setRecipe_IngrediantIDs(String recipe_ingrediantIDs) {
        this.recipe_ingrediantIDs = recipe_ingrediantIDs;
    }
    public void setRecipe_Tag(String recipe_tag) {
        this.recipe_tag = recipe_tag;
    }
    public void setRecipe_ID(int resourceId) {
        this.recipe_ID = resourceId;
    }
}
