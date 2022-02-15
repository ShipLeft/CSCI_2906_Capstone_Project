import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import java.util.Calendar;

/**
 *<h1>CalorieCounter</h1>
 *
 * <p>This class will open an application that will allow a user to track the amount of
 * nutrients (including calories, protein, fats, carbs, and sodium) in their meals throughout the day.
 * This will also print out all of the results and items into a file created by the program.</p>
 *
 * <p>Created: 12/1/2021 - 12/7/2021</p>
 *
 * @author Rhett Boatright
 */
public class TrackerGUI extends Application{

    //Create TextFields for each meal

    private BorderPane amountOfMeals = new BorderPane();

    //Create new food objects
    private Food singleMeal = new Food();
    private Food totalMeals = new Food();

    //Set variables for the totals.
    private int calorieTotal = 0, proteinTotal= 0, fatsTotal = 0, carbsTotal = 0, sodiumTotal = 0;
    private int mealCalories = 0, mealProtein = 0, mealFats = 0, mealCarbs = 0,
            mealSodium =0, mealNumber = 0, againButton = 0;

    //Create TextField arrays for each variable
    TextField[] namesTF = new TextField[8];
    TextField[] caloriesTF = new TextField[8];
    TextField[] proteinsTF = new TextField[8];
    TextField[] fatsTF = new TextField[8];
    TextField[] carbsTF = new TextField[8];
    TextField[] sodiumTF = new TextField[8];


    //Set up string arrays to accept TextField answers
    private String [] nameString = new String[8];
    private String [] caloriesString = new String[8];
    private String [] proteinsString = new String[8];
    private String [] fatsString = new String[8];
    private String [] carbsString = new String[8];
    private String [] sodiumString = new String[8];

    //Set up int arrays for the string arrays to be parsed to.
    int [] caloriesInt = new int[8];
    int [] proteinsInt = new int[8];
    int [] fatsInt = new int[8];
    int [] carbsInt = new int[8];
    int [] sodiumInt = new int[8];

    //variable to track how many items have been chosen.
    int times = 0;

    //Create Alert for NumberFormatException
    Alert numberAlert = new Alert(AlertType.ERROR);

    //Create a BorderPane with each meal inside
    /**
     * This method will create the BorderPane in which the application shows. It will also create
     * all the buttons, RadioButtons, and other interactable areas in the application. Most of
     * what the application shows will be created here.
     *
     * @return borderPane (BorderPane; the BorderPane that contains all the application information)
     */
    private BorderPane bPane(){
        //Set alert title
        numberAlert.setTitle("Number not Found");
        numberAlert.setContentText("Please enter numbers in all but item TextFields");

        //Create all buttons to be used by the application.
        Button next2 = new Button("Next");
        Button next3 = new Button("Next");
        Button next4 = new Button("Next");
        Button next5 = new Button("Next");
        Button save = new Button("Save");
        Button again = new Button("New Day!");

        //Create Radio Buttons for the amount  of meals
        HBox amountMeals = new HBox(20);
        amountMeals.setPadding(new Insets(5, 5, 5, 5));
        amountMeals.setStyle("-fx-border-color: black");
        RadioButton oneMeal = new RadioButton("1 Meal");
        RadioButton twoMeal = new RadioButton("2 Meals");
        RadioButton threeMeal = new RadioButton("3 Meals");
        RadioButton fourMeal = new RadioButton("4 Meals");
        RadioButton fiveMeal = new RadioButton("5 Meals");
        amountMeals.getChildren().addAll(oneMeal, twoMeal, threeMeal, fourMeal, fiveMeal);
        amountMeals.setAlignment(Pos.CENTER);

        //Create BorderPane for all items
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(amountMeals);

        //Set the toggle group for the RadioButtons
        ToggleGroup selectedMeals = new ToggleGroup();
        oneMeal.setToggleGroup(selectedMeals);
        twoMeal.setToggleGroup(selectedMeals);
        threeMeal.setToggleGroup(selectedMeals);
        fourMeal.setToggleGroup(selectedMeals);
        fiveMeal.setToggleGroup(selectedMeals);

        //Set each RadioButton to create the specified amount of meals.
        //Clear each time before adding new getItems.
        //Create save file for meals when the 'next' and 'save' buttons are pressed
        oneMeal.setOnAction(e -> {
            if(oneMeal.isSelected()){
                amountMeals.getChildren().clear();
                HBox bottom = new HBox();
                bottom.getChildren().add(save); //Save will end the day of meals.
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setCenter(getItems());
                borderPane.setBottom(amountOfMeals);

            }
        });

        twoMeal.setOnAction(e -> {
            if(twoMeal.isSelected()){
                amountMeals.getChildren().clear();
                HBox bottom = new HBox();
                bottom.getChildren().add(next2); //next2 will move the app to the last meal of the day.
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setCenter(getItems());
                borderPane.setBottom(amountOfMeals);
            }
        });

        threeMeal.setOnAction(e -> {
            if(threeMeal.isSelected()){
                amountMeals.getChildren().clear();
                HBox bottom = new HBox();
                bottom.getChildren().add(next3); //next3 will move the app to twoMeal.
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setCenter(getItems());
                borderPane.setBottom(amountOfMeals);
            }
        });

        fourMeal.setOnAction(e -> {
            if(fourMeal.isSelected()){
                amountMeals.getChildren().clear();
                HBox bottom = new HBox();
                bottom.getChildren().add(next4); //next4 will move the app to threeMeal.
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setCenter(getItems());
                borderPane.setBottom(amountOfMeals);
            }
        });

        fiveMeal.setOnAction(e -> {
            if(fiveMeal.isSelected()){
                amountOfMeals.getChildren().clear();
                HBox bottom = new HBox();
                bottom.getChildren().add(next5); //next5 will move the app to fourMeal.
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setCenter(getItems());
                borderPane.setBottom(amountOfMeals);
            }
        });

        //Create a path for the program to follow when each different button is pressed.
        next5.setOnAction(e ->{

            //Try catch to avoid NumberFormatException if a textField is left blank
            try{
                printOut();

                amountMeals.getChildren().clear();
                Text mealTotal = new Text(50,50,singleMeal.mealString());
                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                HBox bottom = new HBox();
                bottom.getChildren().add(next4); //Pass to next meal.
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setTop(getItems());
                borderPane.setCenter(mealTotal);
                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;

            }catch (NumberFormatException ex){
                System.out.println("Please enter numbers in all but item TextFields");
                numberAlert.showAndWait();
                amountMeals.getChildren().clear();
                Text mealTotal = new Text(50,50,singleMeal.mealString());
                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                HBox bottom = new HBox();
                bottom.getChildren().add(next5); //Send back to meal.
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);


                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;
            }
        });

        next4.setOnAction(e ->{

            //Try catch to avoid NumberFormatException if a textField is left blank
            try{
                printOut();

                amountMeals.getChildren().clear();
                Text mealTotal = new Text(50,50,singleMeal.mealString());
                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                HBox bottom = new HBox();
                bottom.getChildren().add(next3); //Send to next meal
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setTop(getItems());
                borderPane.setCenter(mealTotal);
                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;

            }catch (NumberFormatException ex){
                System.out.println("Please enter numbers in all but item TextFields");
                numberAlert.showAndWait();
                amountMeals.getChildren().clear();
                Text mealTotal = new Text(50,50,singleMeal.mealString());
                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                HBox bottom = new HBox();
                bottom.getChildren().add(next4); //Send back to meal
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;
            }
        });

        next3.setOnAction(e ->{

            //Try catch to avoid NumberFormatException if a textField is left blank
            try {
                printOut();

                amountMeals.getChildren().clear();
                Text mealTotal = new Text(50, 50, singleMeal.mealString());
                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                HBox bottom = new HBox();
                bottom.getChildren().add(next2); //Send to next meal
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setTop(getItems());
                borderPane.setCenter(mealTotal);
                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;
            }catch (NumberFormatException ex){
                System.out.println("Please enter numbers in all but item TextFields");
                numberAlert.showAndWait();
                amountMeals.getChildren().clear();
                Text mealTotal = new Text(50,50,singleMeal.mealString());
                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                HBox bottom = new HBox();
                bottom.getChildren().add(next3); //Send back to meal
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);


                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;
            }
        });

        next2.setOnAction(e -> {

            //Try catch to avoid NumberFormatException if a textField is left blank
            try {
                printOut();

                amountMeals.getChildren().clear();
                Text mealTotal = new Text(50, 50, singleMeal.mealString());
                HBox bottom = new HBox();
                bottom.getChildren().add(save); //Save the totals
                bottom.setAlignment(Pos.CENTER);

                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));

                amountOfMeals.setBottom(bottom);

                borderPane.setTop(getItems());
                borderPane.setCenter(mealTotal);
                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;
            }catch (NumberFormatException ex){
                System.out.println("Please enter numbers in all but item TextFields");
                numberAlert.showAndWait();
                amountMeals.getChildren().clear();
                Text mealTotal = new Text(50,50,singleMeal.mealString());
                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                HBox bottom = new HBox();
                bottom.getChildren().add(next2); //Send back to meal
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);


                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;
            }
        });


        //Create the total printout for when the save button is pressed.
        save.setOnAction(e ->{

            //Try catch to avoid NumberFormatException if a textField is left blank
            try{
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, againButton);
                totalMeals.setDate(calendar);
                printOut();
                Text mealTotal = new Text(50,50,singleMeal.mealString());
                Text total = new Text(50,50,totalMeals.totalString());

                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                total.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                try(
                        PrintStream printStream =
                                new PrintStream(new FileOutputStream("MealTracker.txt", true))
                ){
                    printStream.println(totalMeals.totalString());
                }catch (IOException ex){
                    System.out.println("File cannot be found/made");
                }

                borderPane.getChildren().clear();
                borderPane.setTop(mealTotal);
                borderPane.setLeft(total);
                HBox againButton = new HBox();
                againButton.getChildren().add(again);
                againButton.setAlignment(Pos.CENTER);
                borderPane.setBottom(againButton);

            }catch (NumberFormatException ex){
                System.out.println("Please enter numbers in all but item TextFields");
                numberAlert.showAndWait();
                Text mealTotal = new Text(50,50,singleMeal.mealString());
                mealTotal.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                HBox bottom = new HBox();
                bottom.getChildren().add(save); //Send back to meal.
                bottom.setAlignment(Pos.CENTER);

                amountOfMeals.setBottom(bottom);

                borderPane.setBottom(amountOfMeals);

                mealCalories = 0;
                mealProtein = 0;
                mealFats = 0;
                mealCarbs = 0;
                mealSodium = 0;
            }
        });

        //Allow the user to enter as many days as they would like to.
        again.setOnAction(e->{
            againButton ++;
            mealNumber = 0;
            borderPane.getChildren().clear();
            amountMeals.getChildren().addAll(oneMeal, twoMeal, threeMeal, fourMeal, fiveMeal);
            selectedMeals.getSelectedToggle().setSelected(false);
            borderPane.setCenter(amountMeals);
        });
        return borderPane;
    }

    /**
     * This method will create the scene, assign the bPane method to the scene,
     * put the scene into the primaryStage, and show the primaryStage.
     */
    @Override
    public void start(Stage primaryStage){
        Scene foodTracker = new Scene(bPane(), 900, 900);
        primaryStage.setTitle("Food Tracker");
        primaryStage.setScene(foodTracker);
        primaryStage.show();

    }

    /**
     * This method will launch the application. Needed for most IDE's
     *
     * @param args (String; placeholder for the main method)
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * This method will create a second BorderPane that contains a VBox with the amount of items
     * that the user would like to put down and a gridPane with all the textFields.
     *
     * @return borderPane (BorderPane; holds all the VBox and GridPane information)
     */
    private BorderPane getItems(){
        BorderPane borderPane = new BorderPane();

        //Add RadioButton to adjust item amount
        RadioButton oneItem = new RadioButton("1");
        RadioButton twoItems = new RadioButton("2");
        RadioButton threeItems = new RadioButton("3");
        RadioButton fourItems = new RadioButton("4");
        RadioButton fiveItems = new RadioButton("5");
        RadioButton sixItems = new RadioButton("6");
        RadioButton sevenItems = new RadioButton("7");
        RadioButton eightItems = new RadioButton("8");


        //Create VBox for the Addition of more items in each meal.
        VBox mealItems = new VBox(20);
        mealItems.setPadding(new Insets(5, 5, 5, 5));
        mealItems.getChildren().add(new Label("Items: "));
        mealItems.setStyle("-fx-border-color: black");
        mealItems.getChildren().addAll(oneItem, twoItems, threeItems,
                fourItems, fiveItems, sixItems, sevenItems, eightItems);
        mealItems.setAlignment(Pos.CENTER);

        //Create a GridPane for the meals and add one Item line.
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //Toggle group for the radio buttons
        ToggleGroup selectedItems = new ToggleGroup();
        oneItem.setToggleGroup(selectedItems);
        twoItems.setToggleGroup(selectedItems);
        threeItems.setToggleGroup(selectedItems);
        fourItems.setToggleGroup(selectedItems);
        fiveItems.setToggleGroup(selectedItems);
        sixItems.setToggleGroup(selectedItems);
        sevenItems.setToggleGroup(selectedItems);
        eightItems.setToggleGroup(selectedItems);

        //Use radio buttons to add or subtract items
        oneItem.setOnAction(e ->{
            if(oneItem.isSelected()){
                gridPane.getChildren().clear();
                gridPane.getChildren().addAll(gridPaneLayout(1));
                times = 1;
            }
        });
        twoItems.setOnAction(e ->{
            if(twoItems.isSelected()){
                gridPane.getChildren().clear();
                gridPane.getChildren().add(gridPaneLayout(2));
                times = 2;
            }
        });
        threeItems.setOnAction(e ->{
            if(threeItems.isSelected()){
                gridPane.getChildren().clear();
                gridPane.getChildren().add(gridPaneLayout(3));
                times = 3;
            }
        });
        fourItems.setOnAction(e ->{
            if(fourItems.isSelected()){
                gridPane.getChildren().clear();
                gridPane.getChildren().add(gridPaneLayout(4));
                times = 4;
            }
        });
        fiveItems.setOnAction(e ->{
            if(fiveItems.isSelected()){
                gridPane.getChildren().clear();
                gridPane.getChildren().add(gridPaneLayout(5));
                times = 5;
            }
        });
        sixItems.setOnAction(e ->{
            if(sixItems.isSelected()){
                gridPane.getChildren().clear();
                gridPane.getChildren().add(gridPaneLayout(6));
                times = 6;
            }
        });
        sevenItems.setOnAction(e ->{
            if(sevenItems.isSelected()){
                gridPane.getChildren().add(gridPaneLayout(7));
                times = 7;
            }
        });
        eightItems.setOnAction(e ->{
            if(eightItems.isSelected()){
                gridPane.getChildren().clear();
                gridPane.getChildren().add(gridPaneLayout(8));
                times = 8;
            }
        });

        borderPane.setLeft(mealItems); //Place the mealItems VBox into the borderPane
        borderPane.setCenter(gridPane);
        return borderPane;
    }

    /**
     * This method will create a GridPane layout to be used in the getItems method. The layout
     * includes all the text fields required by the program and will run according to the amount of
     * items that the user would like to input.
     *
     * @param times (int; Tracks how many times the for loop will be run)
     *
     * @return gridPane (GridPane; holds all the TextFields in the program)
     */
    private GridPane gridPaneLayout(int times){
        //Create GridPane for the TextFields.
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20); //Leave space in between layers.

        //For loops to make the new TextFields.
        for(int i = 0; i < namesTF.length; i++){
            namesTF[i] = new TextField();
            namesTF[i].setMaxWidth(150);
            namesTF[i].setPromptText("Item name");
        }
        for(int i = 0; i < caloriesTF.length; i++){
            caloriesTF[i] = new TextField();
            caloriesTF[i].setMaxWidth(75);
            caloriesTF[i].setPromptText("Calories");
        }
        for(int i = 0; i < proteinsTF.length; i++){
            proteinsTF[i] = new TextField();
            proteinsTF[i].setMaxWidth(75);
            proteinsTF[i].setPromptText("Proteins");
        }
        for(int i = 0; i < fatsTF.length; i++){
            fatsTF[i] = new TextField();
            fatsTF[i].setMaxWidth(75);
            fatsTF[i].setPromptText("Fats");
        }
        for(int i = 0; i < carbsTF.length; i++){
            carbsTF[i] = new TextField();
            carbsTF[i].setMaxWidth(75);
            carbsTF[i].setPromptText("Carbs");
        }
        for(int i = 0; i < sodiumTF.length; i++){
            sodiumTF[i] = new TextField();
            sodiumTF[i].setMaxWidth(75);
            sodiumTF[i].setPromptText("Sodium");
        }


        //For loop to create the grid pane layout
        for(int i = 0; i < times; i++) {

            gridPane.add(new Label("Item " + (i + 1) +":"),0, i);
            gridPane.add(namesTF[i],1,i);

            gridPane.add(new Label(" Calories:"),2, i);
            gridPane.add(caloriesTF[i],3, i);
            caloriesString [i] = caloriesTF[i].getText();

            gridPane.add(new Label(" Protein:"),4, i);
            gridPane.add(proteinsTF[i],5,i);
            proteinsString [i] = proteinsTF[i].getText();

            gridPane.add(new Label(" Fats:"),6, i);
            gridPane.add(fatsTF[i], 7, i);
            fatsString [i] = fatsTF[i].getText();

            gridPane.add(new Label(" Carbohydrates:"),8, i);
            gridPane.add(carbsTF[i],9, i);
            carbsString [i] = carbsTF[i].getText();

            gridPane.add(new Label(" Sodium:"),10, i);
            gridPane.add(sodiumTF[i], 11, i);
            sodiumString [i] = sodiumTF[i].getText();
        }
        return gridPane;
    }

    /**
     * This method will find all the variables and total anything up that needs to be before printing
     * all the information out to the Meals.txt file.
     */
    private void printOut(){

        //For loop to calculate total numbers.
        for(int i =0; i < times; i++){
            nameString [i] = namesTF[i].getText();
            caloriesString [i] = caloriesTF[i].getText();
            proteinsString [i] = proteinsTF[i].getText();
            fatsString[i] = fatsTF[i].getText();
            carbsString[i] = carbsTF[i].getText();
            sodiumString [i] = sodiumTF[i].getText();

            caloriesInt[i] = Integer.parseInt(caloriesString[i]);


            proteinsInt[i] = Integer.parseInt(proteinsString[i]);


            fatsInt[i] = Integer.parseInt(fatsString[i]);


            carbsInt[i] = Integer.parseInt(carbsString[i]);


            sodiumInt[i] = Integer.parseInt(sodiumString[i]);

            calorieTotal += caloriesInt[i];
            mealCalories += caloriesInt[i];
            proteinTotal += proteinsInt[i];
            mealProtein += proteinsInt[i];
            fatsTotal += fatsInt[i];
            mealFats += fatsInt[i];
            carbsTotal += carbsInt[i];
            mealCarbs += carbsInt[i];
            sodiumTotal += sodiumInt[i];
            mealSodium += sodiumInt[i];
        }

        //Make it so that the program will not run if a NumberFormatException happens.

        //Totals for everything as well as checking what Meal it is.
        mealNumber ++;
        singleMeal.setCalories(mealCalories);
        singleMeal.setProtein(mealProtein);
        singleMeal.setFat(mealFats);
        singleMeal.setCarbs(mealCarbs);
        singleMeal.setSodium(mealSodium);

        totalMeals.setCalories(+ calorieTotal);
        totalMeals.setProtein(+ proteinTotal);
        totalMeals.setFat(+ fatsTotal);
        totalMeals.setCarbs(+ carbsTotal);
        totalMeals.setSodium(+ sodiumTotal);

        //try catch for the printStream and print out the information to the text file.
        try(
                PrintStream printStream =
                        new PrintStream(new FileOutputStream("MealTracker.txt", true))
        ){
            printStream.print("\nMeal " + mealNumber +  ": ");
            printStream.println("Items in meal: ");
            for(int i = 0; i < times; i++){
                printStream.println("\n" + nameString[i] + "   Calories: " + caloriesString[i]
                        +"   Protein: " + proteinsString[i] + "   Fats: " + fatsString[i]
                        +"   Carbs: " + carbsString[i] + "   Sodium: " + sodiumString[i]);
            }
            printStream.println(singleMeal.mealString());
        }catch (IOException ex){
            System.out.println("File cannot be found/made");
        }
    }
}
