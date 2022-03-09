package com.example.nutrition;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * <h1>FoodItems</h1>
 *
 * <p>This class is the constructor of the FoodItems objects. This allows for the getting
 * and setting of each food item's name, id, calories, protein, carbs, fat, sodium, and group name.
 * This is used in creating the tables, getting totals, and sending to the database in FoodTracker.java</p>
 *
 * <p>Created: 02/22/2022</p>
 *
 * @author Rhett Boatright
 */
public class FoodItems {
    //Variables for the constructor.
    private  SimpleIntegerProperty id, calories, protein, carbs, sodium;
    private SimpleStringProperty name, group;
    private  SimpleDoubleProperty fat;

    //Populated constructor
    FoodItems(int id, String name, int calories, int protein,
              double fat, int carbs, int sodium, String group) { //Constructor
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.calories = new SimpleIntegerProperty(calories);
        this.protein = new SimpleIntegerProperty(protein);
        this.fat = new SimpleDoubleProperty(fat);
        this.carbs = new SimpleIntegerProperty(carbs);
        this.sodium = new SimpleIntegerProperty(sodium);
        this.group = new SimpleStringProperty(group);
    }
    //Empty constructor
    FoodItems(){
    }

    //Allow for the getting or setting of all the variables.
    public int getId(){
        return id.get();
    }
    void setId(int id){
        this.id.set(id);
    }

    public String getName(){
        return name.get();
    }
    void setName(String name){
        this.name.set(name);
    }

    public int getCalories(){
        return calories.get();
    }
    void setCalories(int calories) {
        this.calories.set(calories);
    }

    public int getProtein(){
        return protein.get();
    }
    void setProtein(int protein) {
        this.protein.set(protein);
    }

    public double getFat(){
        return fat.get();
    }
    void setFat(double fat) {
        this.fat.set(fat);
    }

    public int getCarbs(){
        return carbs.get();
    }
    void setCarbs(int carbs) {
        this.carbs.set(carbs);
    }

    public int getSodium(){
        return sodium.get();
    }
    void setSodium(int sodium) {
        this.sodium.set(sodium);
    }

    public String getGroup(){
        return group.get();
    }
    void setGroup(String group){
        this.group.set(group);
    }

    //Strings for both, meals and end of day totals.
    String itemString(){
        return "\nNutrition for the " + group.get() + " '" + name.get() + "':"
                +"\nCalories: " + calories.get()
                +"\nProtein: " + protein.get()
                +"\nFats: " + fat.get()
                +"\nCarbs: " + carbs.get()
                +"\nSodium: " + sodium.get();
    }
}