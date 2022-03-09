package com.example.nutrition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * <h1>Food</h1>
 *
 * <p>This class will create a Food constructor to hold all of the name and nutrient values.
 * This will also have a string that will be able to print all of the information out.</p>
 *
 * <p>Created: 02/22/2022</p>
 *
 * @author Rhett Boatright
 */
class Food {
    //Variables for the constructor.
    private int calories, protein, carbs, sodium;
    private String date;
    private double fat;

    Food() { //Constructor
    }

    //Populated constructor
    Food(int calories, int protein, double fat, int carbs, int sodium){
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.sodium = sodium;
    }

    //Allow for the getting or setting of all the variables.
    public int getCalories(){
        return calories;
    }
    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein(){
        return protein;
    }
    public void setProtein(int protein) {
        this.protein = protein;
    }

    public double getFat(){
        return fat;
    }
    public void setFat(double fat) {
        this.fat = fat;
    }

    public int getCarbs(){
        return carbs;
    }
    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getSodium(){
        return sodium;
    }
    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public String getDate(){
        return date;
    }
    public void setDate(int day){
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, day);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        this.date = dateFormat.format(date.getTime());
    }

    //Strings for both, meals and end of day totals.
    String mealString(){
        return "\nTotal amounts for the meal:"
                +"\nCalories: " + calories
                +"\nProtein: " + protein
                +"\nFats: " + fat
                +"\nCarbs: " + carbs
                +"\nSodium: " + sodium;
    }

    String totalString(){
        return "\nTotal amounts for " + date + ":"
                +"\nCalories: " + calories
                +"\nProtein: " + protein
                +"\nFats: " + fat
                +"\nCarbs: " + carbs
                +"\nSodium: " + sodium;
    }
}