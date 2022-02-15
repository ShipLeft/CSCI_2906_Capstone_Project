import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * <h1>Food</h1>
 *
 * <p>This class will create a Food constructor to hold all of the name and nutrient values.
 * This will also have a string that will be able to print all of the information out.</p>
 *
 * <p>Created: 12/01/2021</p>
 *
 * @author Rhett Boatright
 */
class Food {
    //Variables for the constructor.
    private int calories, protein, fat, carbs, sodium;
    private String date;

    Food() { //Constructor
    }

    //Allow for the getting or setting of all the variables.
    void setCalories(int calories) {
        this.calories = calories;
    }

    void setProtein(int protein) {
        this.protein = protein;
    }

    void setFat(int fat) {
        this.fat = fat;
    }

    void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    void setSodium(int sodium) {
        this.sodium = sodium;
    }

    void setDate(Calendar date){
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