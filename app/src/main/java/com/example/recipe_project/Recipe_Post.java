package com.example.recipe_project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Recipe_Post {
    public int ID;
    public String name;
    public String link;
    public String IDs;
    public String tag;
    public String info;

    public Recipe_Post() {
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public Recipe_Post(int id, String name, String link, String IDs, String tag, String info) {
        this.ID = id;
        this.name = name;
        this.link = link;
        this.IDs = IDs;
        this.tag = tag;
        this.info = info;
    }
    public int Recipe_getID() {
        return this.ID;
    }
    public String Recipe_getname() {
        return this.name;
    }

    public String Recipe_getlink() {
        return this.link;
    }

    public String Recipe_getIDs() {
        return this.IDs;
    }

    public String Recipe_gettag() {
        return this.tag;
    }

    public String Recipe_getinfo() {
        return this.info;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("recipe_ID", ID);
        result.put("recipe_name", name);
        result.put("recipe_link", link);
        result.put("recipe_ingrediantIDs", IDs);
        result.put("recipe_tag", tag);
        result.put("recipe_info", info);
        return result;
    }
}