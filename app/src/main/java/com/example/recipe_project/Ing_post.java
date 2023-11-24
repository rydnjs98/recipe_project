package com.example.recipe_project;

public class Ing_post {

     int ingrediant_ID;
     String ingrediant_link;

     String ingrediant_name;

     public Ing_post(){}


    public Ing_post(int ingrediant_ID, String ingrediant_link, String ingrediant_name)
    {
        this.ingrediant_ID=ingrediant_ID;
        this.ingrediant_link=ingrediant_link;
        this.ingrediant_name=ingrediant_name;
    }

    public int getingrediant_ID() {
        return ingrediant_ID;
    }

    public String getingrediant_link() {
        return ingrediant_link;
    }

    public String getingrediant_name() {
        return ingrediant_name;
    }

    public void setingrediant_ID(int ingrediant_ID) {
        this.ingrediant_ID=ingrediant_ID;
    }

    public void setingrediant_link(String ingrediant_link) {
        this.ingrediant_link = ingrediant_link;
    }

    public void setingrediant_name(String ingrediant_name) {
        this.ingrediant_name=ingrediant_name;
    }




}
