package com.example.recipe_project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Recipe_Post {
    public int recipe_ID;

    public String recipe_name;
    public String recipe_link;
    public String recipe_ingrediantIDs;
    public String recipe_tag;
    public String recipe_info;

    public Recipe_Post() {
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public Recipe_Post(int recipe_ID, String recipe_name, String recipe_link, String recipe_ingrediantIDs, String recipe_tag, String recipe_info) {
        this.recipe_ID = recipe_ID;
        this.recipe_name = recipe_name;
        this.recipe_info = recipe_info;
        this.recipe_link = recipe_link;
        this.recipe_ingrediantIDs = recipe_ingrediantIDs;
        this.recipe_tag = recipe_tag;
    }
    public int Recipe_getID() {
        return this.recipe_ID;
    }
    public String Recipe_getname() {
        return this.recipe_name;
    }

    public String Recipe_getlink() {
        return this.recipe_link;
    }

    public String Recipe_getIDs() {
        return this.recipe_ingrediantIDs;
    }

    public String Recipe_gettag() {
        return this.recipe_tag;
    }

    public String Recipe_getinfo() {
        return this.recipe_info;
    }

    public void Recipe_setID(int recipe_ID) {
        this.recipe_ID = recipe_ID;
    }
    public void Recipe_setname(String recipe_name) { this.recipe_name = recipe_name; }

    public void Recipe_setlink(String recipe_link) {
        this.recipe_link = recipe_link;
    }

    public void Recipe_setIDs(String recipe_ingrediantIDs) { this.recipe_ingrediantIDs = recipe_ingrediantIDs; }

    public void Recipe_settag(String recipe_tag) {
        this.recipe_tag = recipe_tag;
    }

    public void Recipe_setinfo(String recipe_info) { this.recipe_info = recipe_info; }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("recipe_ID", recipe_ID);
        result.put("recipe_name", recipe_name);
        result.put("recipe_link", recipe_link);
        result.put("recipe_ingrediantIDs", recipe_ingrediantIDs);
        result.put("recipe_tag", recipe_tag);
        result.put("recipe_info", recipe_info);
        return result;
    }
}