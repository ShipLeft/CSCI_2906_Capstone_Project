package com.example.nutrition;

import java.sql.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *<h1>FoodTracker</h1>
 *
 * <p>This is a nutrition tracking application. This will take the user's information
 * (weight, height, calories burned daily, pregnant or not, and male or female). This allows for the program to
 * track the amounts more accurately. It will then have the user access a table to put food into meals. at the end,
 * the program will total everything up and present it to the user</p>
 *
 * <p>Created: 3/03/2022</p>
 *
 * @author Rhett Boatright
 */
public class FoodTracker extends Application {
    //Set Alerts for the program
    Alert numberAlert = new Alert(AlertType.ERROR);
    Alert selectionAlert = new Alert(AlertType.ERROR);
    Alert totalError = new Alert(AlertType.ERROR);
    Alert wrongTable = new Alert(AlertType.ERROR);

    //Connection to db
    Connection conn;
    Statement dbStatement;

    //Pane and boxes
    Pane pane = new Pane();
    VBox vBox = new VBox();
    HBox hBox = new HBox();


    //Table for the db
    TableColumn nameTC = new TableColumn("Name");
    TableColumn groupTC = new TableColumn("Group");
    TableColumn caloriesTC = new TableColumn("Calories");
    TableColumn proteinTC = new TableColumn("Protein");
    TableColumn fatTC = new TableColumn("Fat");
    TableColumn carbsTC = new TableColumn("Carbs");
    TableColumn sodiumTC = new TableColumn("Sodium");
    TableView<FoodItems> tableView = new TableView<>();

    //Table for the user's choices
    TableView userTable = new TableView();
    TableColumn userNameTC = new TableColumn("Name");
    TableColumn userGroupTC = new TableColumn("Group");



    //Menu Bar and its children
    MenuBar menuBar = new MenuBar();
    MenuItem foodMenu = new MenuItem("Nutrition");
    MenuItem personalVariables = new MenuItem("Personal");
    MenuItem exitMenu = new MenuItem("Exit");
    MenuItem totalUp = new MenuItem("Calculate");

    //List for the food
    private ObservableList<FoodItems> listFood = FXCollections.observableArrayList();
    private ObservableList<FoodItems> userChoice = FXCollections.observableArrayList();
    private ArrayList<Food> foodForDay = new ArrayList<>();
    private ArrayList<Food> foodForMeal = new ArrayList<>();

    //List for the groups
    ObservableList<String> groups = FXCollections.observableArrayList();


    //int for the amount of meals and days
    private int totalMealTimes = 1;
    private int totalDayTimes = 0;

    //ComboBoxes for personal along with the variables
    ComboBox pregnant = new ComboBox();
    String pregnantChoice = "";
    ComboBox heightFeet = new ComboBox();
    int heightFeetChoice = 0;
    ComboBox heightInches = new ComboBox();
    int heightInchesChoice = 0;
    int heightTotal = 0;

    //TextFields for personal along with the variables
    TextField weightTF = new TextField();
    double weight = 0.0;
    TextField caloriesBurnedTF = new TextField();
    double calBurnedDaily = 0.0;

    //Variable for the BMI checking
    double bMI = 0.0;

    //Variable to see what day to total up
    int daySelection = 0;

    //Variables to check if the total can be found
    boolean personalIsFilled = false;
    boolean foodIsFilled = false;

    //String for the FT.css and FTSecondScene.css
    String css = getClass().getResource("FT.css").toExternalForm();
    String cssSS = getClass().getResource("FTSecondScene.css").toExternalForm();

    @Override
    /**
     * This method is the start method for the application. It sets up all the alerts, the MenuItems and MenuBar,
     * the VBox, scene, and stage. This is the base of the entire application and allows
     * for flowing movement through it.
     */
    public void start(Stage primaryStage) throws Exception {
        //Set up the menuBar
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu menuSelection = new Menu("Options");
        menuSelection.getItems().addAll(personalVariables, foodMenu, totalUp, exitMenu);
        menuBar.getMenus().add(menuSelection);

        tableView.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX,Color.TRANSPARENT,2,0,0,0));

        //Set up the vBox and set into the pane
        vBox.getChildren().add(menuBar);
        vBox.getChildren().add(personal());
        vBox.prefWidthProperty().bind(primaryStage.widthProperty());
        pane.getChildren().add(vBox);

        //System alerts to avoid errors
        numberAlert.setTitle("Number not Found");
        numberAlert.setContentText("Please enter numbers in text areas");
        selectionAlert.setTitle("Missing Selection");
        selectionAlert.setContentText("Please select all items in all fields");
        totalError.setTitle("Missing information");
        totalError.setContentText
                ("Please fill out the Nutrition and Personal menus before entering the calculate menu");
        wrongTable.setTitle("Wrong table Selected");
        wrongTable.setContentText("Please select an item in the other table");

        //Gather the information from the database
        getFood();

        //Icon for the app
        Image icon = new Image(getClass().getResource("Banana.png").toExternalForm());
        primaryStage.getIcons().add(icon);

        //Set up the scene and the stage
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(pane, 960, 600);
        scene.getStylesheets().add(css);
        primaryStage.setTitle("Nutrition Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        //Set up the menu functionality
        foodMenu.setOnAction(e ->{
            hBox.getChildren().clear();
            vBox.getChildren().clear();//Clear the boxes to avoid errors
            vBox.getChildren().add(menuBar);//Set menuBar back into the vBox

            hBox.setAlignment(Pos.CENTER_RIGHT);//Set the alignment of the hBox
            hBox.maxWidthProperty().bind(vBox.widthProperty().subtract(15));// Bind the hBox with the vBox
            hBox.getChildren().addAll(bPane()); // Add the userTable and the tableView into the hBox

            hBox.setSpacing(80);//Space the tables from each other
           vBox.getChildren().add(hBox);//Add everything to the vBox
        });

        personalVariables.setOnAction(e ->{
            vBox.getChildren().clear();//Clear the vbox to avoid errors

            //Put the menu back into the vBox and add personal method  too.
            vBox.getChildren().addAll(menuBar, personal());
        });

        totalUp.setOnAction(e ->{
            if(foodIsFilled == true && personalIsFilled == true){ //Only allow if the other numbers are filled out
                vBox.getChildren().clear(); //Clear vBox to avoid errors
                vBox.getChildren().addAll(menuBar, totals()); //Add menuBar and totals method to the vBox
            }
            else totalError.showAndWait();//Show error if the other fields haven't been filled out
        });

        exitMenu.setOnAction(e-> {
            System.exit(0);//Exit the program if exitMenu is selected
        });
    }

    /**
     * This method is only needed for IDEs
     * @param args (String; placeholder for the main method)
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * This method will create the nutrition tab GUI. This includes both of the tables,
     * the buttons dealing with adding, removing, totaling, and adding to the database.
     * This is the bulk of the program as it is integrated with the database to work correctly.
     * @return
     */
    private BorderPane bPane(){
        tableView.setItems(listFood);//Set the database items into the tableView

        //Buttons for functionality
        Button foodNotFound = new Button("Add to Database");
        Button addFood = new Button("Add Food");
        Button removeFood = new Button("Remove Food");
        Button nextMeal = new Button("Next Meal");
        Button save = new Button("Total Up Day");
        Button againMeal = new Button("Next Meal!");
        Button again = new Button("Next Day!");
        Button totalDayBt = new Button("Calculate Total");


        //TextField for searching
        TextField keywordSearch = new TextField();

        //Label for table description
        Label infoLabel = new Label("Food in the meal:");

        //User table setup
        userTable.setMinWidth(260);
        userTable.setMaxWidth(260);
        userNameTC.setMinWidth(170);
        userNameTC.setCellValueFactory(new PropertyValueFactory<FoodItems, String>("name"));

        userGroupTC.setMinWidth(90);
        userGroupTC.setCellValueFactory(new PropertyValueFactory<FoodItems, String>("group"));
        userGroupTC.setId("custom-columnFx");

        userTable.getColumns().clear();
        userTable.getColumns().addAll(userNameTC, userGroupTC);


        //DB table setup
        tableView.setMaxWidth(632);
        tableView.setMinWidth(632);
        nameTC.setMinWidth(231);
        nameTC.setCellValueFactory(new PropertyValueFactory<FoodItems, String>("name"));

        caloriesTC.setMinWidth(60);
        caloriesTC.setCellValueFactory(new PropertyValueFactory<FoodItems, Integer>("calories"));

        proteinTC.setMinWidth(50);
        proteinTC.setCellValueFactory(new PropertyValueFactory<FoodItems, Integer>("protein"));

        fatTC.setMinWidth(50);
        fatTC.setCellValueFactory(new PropertyValueFactory<FoodItems, Double>("fat"));

        carbsTC.setMinWidth(50);
        carbsTC.setCellValueFactory(new PropertyValueFactory<FoodItems, Integer>("carbs"));

        sodiumTC.setMinWidth(50);
        sodiumTC.setCellValueFactory(new PropertyValueFactory<FoodItems, Integer>("sodium"));

        groupTC.setMinWidth(100);
        groupTC.setCellValueFactory(new PropertyValueFactory<FoodItems, String>("group"));

        tableView.getColumns().clear();
        tableView.getColumns().addAll(nameTC, caloriesTC, proteinTC, fatTC, carbsTC, sodiumTC, groupTC);


        //Filtered list to allow for searching through the items
        FilteredList<FoodItems> filteredFoodList = new FilteredList<>(listFood, b -> true);


        //Pane, box, and other GUI elements
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(24, 16, 8, 24));
        HBox buttonsAdd = new HBox(40);
        HBox buttonsTotal = new HBox(56);
        HBox searchInfo = new HBox(150);
        VBox buttons = new VBox(40);

        //Set the values for the keyword search
        keywordSearch.setMinWidth(250);
        keywordSearch.setPromptText("Search any column with keyword e.g. 'Sandwich':");

        //Add the buttons to buttons HBox
        buttonsAdd.getChildren().addAll(removeFood, addFood, foodNotFound);
        foodNotFound.setTranslateX(-16);
        addFood.setTranslateX(-16);
        removeFood.setTranslateX(-405);

        //Add the total buttons to the totalHBox
        buttonsTotal.getChildren().addAll(nextMeal, save);
        nextMeal.setTranslateX(-16);
        save.setTranslateX(-16);

        buttonsAdd.setAlignment(Pos.CENTER_RIGHT);
        buttonsTotal.setAlignment(Pos.BOTTOM_RIGHT);
        buttons.getChildren().addAll(buttonsAdd,buttonsTotal);

        searchInfo.getChildren().addAll(infoLabel, keywordSearch);
        keywordSearch.setMinWidth(605);
        infoLabel.setTranslateX(16);


        //Clear the borderPane and add the buttons, tableView, and the searchbar.
        borderPane.getChildren().clear();
        borderPane.setBottom(buttons);
        borderPane.setLeft(userTable);
        userTable.setTranslateX(-24);
        borderPane.setRight(tableView);
        borderPane.setTop(searchInfo);

        //Functions of the buttons
        addFood.setOnAction(e ->{
                addToMeal();//Adds items to the userTable
        });

        removeFood.setOnAction(e ->{
            try {
                removeFromMeal();//Removes the items from the userTable
            }catch (Exception ex){
                wrongTable.showAndWait();
            }
        });

        nextMeal.setOnAction(e ->{
            printMeal(); //Print the meal totals to external files

            //Clear spaces to not cause errors
            hBox.getChildren().clear();
            borderPane.getChildren().clear();

            //Set up new label to print out total
            Label mealLabel = new Label(foodForMeal.get(totalMealTimes -1).mealString());
            mealLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));

            //Print total to the GUI and add againMeal button
            borderPane.setTop(mealLabel);
            borderPane.setBottom(againMeal);
            hBox.getChildren().add(borderPane);
            hBox.setAlignment(Pos.CENTER);
            totalMealTimes++;//Add one to the daily meal counter
        });

        save.setOnAction(e ->{
            printMeal(); //Print out meal to the external file

            //Clear the spaces to not cause errors
            borderPane.getChildren().clear();
            hBox.getChildren().clear();
            buttons.getChildren().clear();

            //If the personal is Filled, the totalDayBt will be shown
            if(personalIsFilled == true) {
                buttons.getChildren().addAll(again, totalDayBt);
            }
            //Show the button again
            else buttons.getChildren().add(again);

            //New label for printing the meal total printing to the GUI
            Label mealLabel = new Label(foodForMeal.get(totalMealTimes -1).mealString());
            mealLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            borderPane.setTop(mealLabel);//Set the label in the borderPane

            totalMealTimes =  1;//reset the daily meal counter

            //Print the daily total out to the GUI and the txt file
            printDay();//txt file print out
            Label dayLabel = new Label(foodForDay.get(totalDayTimes).totalString());//Label for GUI print out
            dayLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            borderPane.setCenter(dayLabel); //Set the label into the borderPane
            borderPane.setBottom(buttons); //Set the buttons into the borderPane

            //Set up the hBox with the borderPane and set it to the Center of the HBox
            hBox.getChildren().add(borderPane);
            hBox.setAlignment(Pos.CENTER);
            totalDayTimes++;
            foodIsFilled = true; //Set the variable is filled


        });

        againMeal.setOnAction(e ->{
            hBox.getChildren().clear();//Clear the hBox to avoid errors
            hBox.getChildren().addAll(bPane());//Add the userTable and the bPane method back into the hBox
            hBox.setAlignment(Pos.CENTER_RIGHT);
        });

        again.setOnAction(e ->{
            hBox.getChildren().clear();//Clear the hBox to avoid errors
            hBox.getChildren().addAll(bPane());//Add the userTable and the bPane method back into the hBox
            hBox.setAlignment(Pos.CENTER_RIGHT);
        });

        totalDayBt.setOnAction(e ->{
            vBox.getChildren().clear();//Clear the vBox to avoid errors
            vBox.getChildren().addAll(menuBar, totals());//Add the menuBar and totals method to the vBox
        });

        foodNotFound.setOnAction(e ->{
            sendFood();//Send food back to the database
        });

        //Add the search functionality
        keywordSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredFoodList.setPredicate(foodItems -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null)
                    return true;//return if nothing is in the search

                String keyword = newValue.toLowerCase(); //convert the search keyword to lowercase for easier search

                if(foodItems.getName().toLowerCase().indexOf(keyword) > -1)
                    return true; //If the name is in the search, return true
                else if(String.valueOf(foodItems.getCalories()).indexOf(keyword) > -1)
                    return true; //If the calories are in the search, return true
                else if(String.valueOf(foodItems.getProtein()).indexOf(keyword) > -1)
                    return true; //If the protein is in the search, return true
                else if(String.valueOf(foodItems.getFat()).indexOf(keyword) > -1)
                    return true; //If the fat is in the search, return true
                else if(String.valueOf(foodItems.getCarbs()).indexOf(keyword) > -1)
                    return true; //If the carbs are in the search, return true
                else if(String.valueOf(foodItems.getSodium()).indexOf(keyword) > -1)
                    return true;//If the sodium is in the search, return true
                else if(foodItems.getGroup().toLowerCase().indexOf(keyword) > -1)
                    return true;//If the group is in the search, return true
                else
                    return false;//Return false if not found
            });

        });
        //Sorted list for the search
        SortedList<FoodItems> sortedList = new SortedList<>(filteredFoodList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedList);//Set the sorted list into the tableView
        return borderPane;
    }

    /**
     * This method will gather the items from the database in MySQL and put it into the table
     * @return
     * @throws Exception
     */
    public  Connection getFood(){
        String url = "jdbc:mysql://localhost:3306/nutrition";//The location of the database
        String username = "program";//username for the db
        String password = "password";//pass for the db
        String sql = "SELECT nutrients.Id, nutrients.Name, nutrients.Calories, nutrients.Proteins," +
                " nutrients.Fats, nutrients.Carbs, nutrients.Sodium," +
                " groups.Name FROM nutrients JOIN groups ON groups.Id = nutrients.GroupId";//SQL code to gather info

        //Show that it is connecting the database
        System.out.println("Connecting database...");
        try{
            conn = DriverManager.getConnection(url,username,password);//Connection to the db
            System.out.println("Database groups connected!");//Show that the db is connected
            PreparedStatement stmt = conn.prepareStatement(sql);//PreparedStatement to grab information
            dbStatement = conn.createStatement(); //Statement for the db
            ResultSet rs = stmt.executeQuery();//ResultSet for the db

            if(rs.next() == false){
                System.out.println("No records found");//If rs has not got another item, move on
            }else {
                listFood.clear();//Clear the listFood to avoid doubles
                do {
                    FoodItems foodItems = new FoodItems(rs.getInt(1), rs.getString(2),
                            rs.getInt(3), rs.getInt(4), rs.getDouble(5),
                            rs.getInt(6), rs.getInt(7), rs.getString(8));
                    listFood.add(foodItems);//Add items to the listFood
                }
                while(rs.next());//While the rs has next, keep adding to the list
            }
        }catch(Exception e){
            throw new IllegalStateException("Cannot connect the database!", e);//Show error for not connecting
        }
        return null;
    }


    public Connection getGroup(){
        String url = "jdbc:mysql://localhost:3306/nutrition";//The location of the database
        String username = "program";//username for the db
        String password = "password";//pass for the db
        String sql = "SELECT groups.Name From groups";//SQL code to gather info

        //Show that it is connecting the database
        System.out.println("Connecting database...");
        try{
            conn = DriverManager.getConnection(url,username,password);//Connection to the db
            System.out.println("Database groups connected!");//Show that the db is connected
            PreparedStatement stmt = conn.prepareStatement(sql);//PreparedStatement to grab information
            dbStatement = conn.createStatement(); //Statement for the db
            ResultSet rs = stmt.executeQuery();//ResultSet for the db

            if(rs.next() == false){
                System.out.println("No records found");//If rs has not got another item, move on
            }else {
                groups.clear();//Clear the listFood to avoid doubles
                do {
                    groups.add(rs.getString(1));
                }
                while(rs.next());//While the rs has next, keep adding to the list
            }
        }catch(Exception e){
            throw new IllegalStateException("Cannot connect the database!", e);//Show error for not connecting
        }
        return null;
    }

    private String groupName = "";
    /**
     * This method will send the new food to the database.
     */
    public void sendFood(){
        //Make a new borderpane, HBox, and GridPane for the information to be entered into
        BorderPane addPane = new BorderPane();
        HBox addHBox = new HBox();
        GridPane gridPane = new GridPane();
        GridPane groupPane = new GridPane();

        //Buttons for functionality
        Button nextName = new Button("Next");
        Button newName = new Button("New Group");
        Button addNewFood = new Button("Add to Database");
        Button backBT = new Button("Back");
        Button exitBt = new Button("Exit Window");

        gridPane.setVgap(10);
        gridPane.setHgap(10);//Set the gridPane gaps

        //TextFields for each area and the prompt text
        ComboBox groupCombo = new ComboBox();
        try {
            getGroup();
        }catch (java.lang.Exception ex){
        }
        groupCombo.setItems(groups);


        groupCombo.setPromptText("Select Group");
        groupCombo.setButtonCell(new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null){
                    setText("Select Group");
                }else{
                    setText(item);
                }
            }
        });

        //Set up the new TextFields for the new FoodItem.
        TextField groupAddition = new TextField();
        groupAddition.setMaxWidth(150);
        groupAddition.setPromptText("Group Name");

        TextField nameAddition = new TextField();
        nameAddition.setMaxWidth(150);
        nameAddition.setPromptText("Food Name");

        TextField calAddition = new TextField();
        calAddition.setMaxWidth(150);
        calAddition.setPromptText("Calories");

        TextField proAddition = new TextField();
        proAddition.setMaxWidth(150);
        proAddition.setPromptText("Proteins");

        TextField fatAddition = new TextField();
        fatAddition.setMaxWidth(150);
        fatAddition.setPromptText("Fats");

        TextField carbAddition = new TextField();
        carbAddition.setMaxWidth(150);
        carbAddition.setPromptText("Carbs");

        TextField sodiumAddition = new TextField();
        sodiumAddition.setMaxWidth(150);
        sodiumAddition.setPromptText("Sodium");

        //Add new label and group fields to the groupPane
        groupPane.add(new Label("Group Choices"), 0, 0);
        groupPane.add(groupCombo, 1, 0);

        groupPane.add(newName, 0, 2);
        nextName.setTranslateX(79);
        groupPane.add(nextName, 1, 2);
        groupPane.setHgap(10);
        groupPane.setVgap(20);
        groupPane.setAlignment(Pos.CENTER);

        addPane.setCenter(groupPane);


        //Create new stage for the second window for the adding to the db
        Stage secondaryStage = new Stage();
        //Icon for the app
        Image icon = new Image(getClass().getResource("Banana.png").toExternalForm());
        secondaryStage.getIcons().add(icon);

        Scene secondScene = new Scene(addPane, 270, 300);
        secondScene.getStylesheets().add(cssSS);
        secondaryStage.setTitle("New Food");
        secondaryStage.setScene(secondScene);
        secondaryStage.show();

        //Don't allow the app to have both group sections filled
        groupAddition.setOnKeyPressed(e ->{
            groupCombo.getSelectionModel().clearSelection();
        });

        groupCombo.setOnAction(e ->{
            groupAddition.setText("");
        });

        //Functionality of the buttons
        newName.setOnAction(e ->{
            groupPane.getChildren().clear();
            groupPane.add(new Label("New Group"), 0, 1);
            groupPane.add(groupAddition, 1, 1);
            groupPane.add(backBT, 0, 2);
            groupPane.add(nextName, 1, 2);
            nextName.setTranslateX(105);

        });

        //Button to go back in the selection
        backBT.setOnAction(e ->{
            groupPane.getChildren().clear();
            secondaryStage.close();
            sendFood();
        });

        //Button to move forward in the selection
        nextName.setOnAction(e ->{
            if(groupCombo.getValue() != null || !groupAddition.getText().isBlank()) {
                if (groupCombo.getValue() != null) {
                    groupName = groupCombo.getValue().toString();
                } else {
                    groupName = groupAddition.getText();
                }
                addPane.getChildren().clear();
                //Add new labels and textFields to the gridPane
                gridPane.add(new Label("Name:"), 0, 0);
                gridPane.add(nameAddition, 1, 0);

                gridPane.add(new Label("Calories:"), 0, 1);
                gridPane.add(calAddition, 1, 1);

                gridPane.add(new Label("Proteins:"), 0, 2);
                gridPane.add(proAddition, 1, 2);

                gridPane.add(new Label("Total Fat:"), 0, 3);
                gridPane.add(fatAddition, 1, 3);

                gridPane.add(new Label("Carbs:"), 0, 4);
                gridPane.add(carbAddition, 1, 4);

                gridPane.add(new Label("Sodium:"), 0, 5);
                gridPane.add(sodiumAddition, 1, 5);

                //add everything to the addPane and the addHBox
                addHBox.getChildren().addAll(exitBt, addNewFood);
                addHBox.setAlignment(Pos.CENTER);
                gridPane.setAlignment(Pos.CENTER);
                addHBox.setSpacing(20);
                addPane.setCenter(gridPane);
                addPane.setBottom(addHBox);
                addHBox.setTranslateY(-16);
                gridPane.setTranslateY(-8);
            }else
            selectionAlert.showAndWait();
        });

        //Button to save the new item into the database
        addNewFood.setOnAction(e ->{
            //Variables to gather information
            String foodNameUnchanged = "";
            String foodName = "";
            int calNumber = 0;
            int proNumber = 0;
            double fatNumber = 0.0;
            int carbNumber = 0;
            int sodiumNumber = 0;

            //If any of the TextFields are blank don't move forward
            if(!nameAddition.getText().isBlank() && !calAddition.getText().isBlank() &&
                    !proAddition.getText().isBlank() && !fatAddition.getText().isBlank() &&
                    !carbAddition.getText().isBlank() &&  !sodiumAddition.getText().isBlank()) {

                //Try to gather the numbers and catch any number formate errors.
                try {
                    foodNameUnchanged = nameAddition.getText();

                    //Change the text to allow "'" into the database through sql
                    if(foodNameUnchanged.contains("'")) {
                        foodName = foodNameUnchanged.replace("'", "''");
                    }else
                        foodName = foodNameUnchanged;
                    calNumber = Integer.parseInt(calAddition.getText());
                    proNumber = Integer.parseInt(proAddition.getText());
                    fatNumber = Double.parseDouble(fatAddition.getText());
                    carbNumber = Integer.parseInt(carbAddition.getText());
                    sodiumNumber = Integer.parseInt(sodiumAddition.getText());
                }
                //Catch the number format errors
                catch (NumberFormatException ex) {
                    numberAlert.showAndWait();
                }
            }

                //Group number variable
            int groupNumber = 0;

            //db information
            String url = "jdbc:mysql://localhost:3306/nutrition";
            String username = "program";
            String password = "password";

            //Print out that it is trying to connect to the db
            System.out.println("Connecting database...");

            //Try to connect to the db and add the group to the db
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println("Database groups connected!");
                Statement dbStatement = connection.createStatement();
                dbStatement.executeUpdate("INSERT INTO groups(Name) VALUES('" + groupName + "')");
            } catch (SQLException t) {
                System.out.println("Group already exists");
            }

            //Try to connect to the db and insert the information into nutrients
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                //If none of the TextFields are empty
                if(!nameAddition.getText().isBlank() && !calAddition.getText().isBlank() &&
                        !proAddition.getText().isBlank() && !fatAddition.getText().isBlank() &&
                        !carbAddition.getText().isBlank() &&  !sodiumAddition.getText().isBlank()) {

                    //String for the sql code
                    String sql = "SELECT groups.Id" +
                            " FROM groups WHERE groups.Name = '" + groupName + "'";

                    PreparedStatement stmt = connection.prepareStatement(sql); //Statement to set into sql
                    dbStatement = connection.createStatement();
                    ResultSet rs = stmt.executeQuery();

                    //if no information is found, return and close window
                    if (rs.next() == false) {
                        System.out.println("No Records found");
                    }
                    //Else, do while for all information
                    else {
                        do groupNumber = rs.getInt(1);
                        while (rs.next());
                    }
                    dbStatement.executeUpdate //insert info into the db
                            ("INSERT INTO nutrients(Name, Calories, Proteins, Fats, Carbs, Sodium, GroupId)" +
                                    " VALUES('" + foodName + "', '" + calNumber + "', '" + proNumber +
                                    "', '" + fatNumber + "', '" + carbNumber + "', '" +
                                    sodiumNumber + "', '" + groupNumber + "')");
                    connection.close();//close the connection
                    //close the stage after completion
                    secondaryStage.close();
                }else
                    System.out.println("Please enter into all fields");
            }
            //Catch sql errors
            catch (SQLException l) {
                throw new IllegalStateException("Cannot connect the database!", l);
            }

            //Get the new item and put into the table
            try {
                getFood(); //update the original table info with the new item(s)
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //exit on button selection
        exitBt.setOnAction(e ->{
            secondaryStage.close();
        });
    }

    /**
     * This method will add to the userTable from the tableView to add food to each meal.
     */
    public void addToMeal(){
        //Find where the user is highlighting the information
        TablePosition tablePos = tableView.getSelectionModel().getSelectedCells().get(0);
        int row = tablePos.getRow();//Grab the row of information

        //Insert the info into a new object and put into the observableList
        FoodItems fromTable = tableView.getItems().get(row);
        userChoice.add(fromTable);

        userTable.setItems(userChoice);//Set the observableList into the userTable
    }

    /**
     * This method will remove items from the userTable to not be counted in totals.
     */
    public void removeFromMeal(){
        //Grab the position of the highlighted information
        TablePosition tablePos = (TablePosition) userTable.getSelectionModel().getSelectedCells().get(0);
        int row = tablePos.getRow(); //Grab the row of information

        //Set info into a new object and remove from the table
        FoodItems fromTable = (FoodItems) userTable.getItems().get(row);
        userChoice.remove(fromTable);
        userTable.getItems().remove(userChoice);
    }

    /**
     * This method will print meal info out to a txt file in the same path as the program.
     */
    public void printMeal(){
        //Variables for totals
        int calMTotal = 0;
        int proMTotal = 0;
        double fatMTotal = 0;
        int carbMTotal = 0;
        int sodiuMTotal = 0;

        //for loop to gather information and total up
        for(int i = 0; i < userChoice.size(); i++) {
            calMTotal += userChoice.get(i).getCalories();
            proMTotal += userChoice.get(i).getProtein();
            fatMTotal += userChoice.get(i).getFat();
            carbMTotal += userChoice.get(i).getCarbs();
            sodiuMTotal += userChoice.get(i).getSodium();
        }

        //Set information into a Food object and insert into list
        Food mealTotal = new Food(calMTotal, proMTotal, fatMTotal, carbMTotal, sodiuMTotal);
        foodForMeal.add(mealTotal);

        //Try to print out information to txt file
        try(
                PrintStream printStream =
                        new PrintStream(new FileOutputStream("NutritionTracker.txt", true))
        ){
            printStream.print("\nMeal " + totalMealTimes +  ": ");
            printStream.println("Items in the meal: ");

            //for loop to print out the information to the file
            for(int i = 0; i < userChoice.size(); i++){
                printStream.println("\nFood: "+ userChoice.get(i).getName()
                        + "\tCalories: " + userChoice.get(i).getCalories()
                        +"\tProtein: " + userChoice.get(i).getProtein() + "\tFats: " + userChoice.get(i).getFat()
                        +"\n\tCarbs: " + userChoice.get(i).getCarbs() + "\tSodium: " + userChoice.get(i).getSodium());
            }

            //Print the totals of the meal
            printStream.println(mealTotal.mealString());
            userChoice.clear();
        }catch (IOException ex){
            System.out.println("File cannot be found/made");
        }
    }

    /**
     * This method will print out total day info out to a txt file.
     */
    public void printDay(){
        //Variables for the totals
        int calDTotal = 0;
        int proDTotal = 0;
        double fatDTotal = 0;
        int carbDTotal = 0;
        int sodiumDTotal = 0;

        //For loop to total information up
        for(int i = 0; i < foodForMeal.size(); i++){
            calDTotal += foodForMeal.get(i).getCalories();
            proDTotal += foodForMeal.get(i).getProtein();
            fatDTotal += foodForMeal.get(i).getFat();
            carbDTotal += foodForMeal.get(i).getCarbs();
            sodiumDTotal += foodForMeal.get(i).getSodium();
        }

        //Set total into Food object and set into list
        Food dayTotal = new Food(calDTotal, proDTotal, fatDTotal, carbDTotal, sodiumDTotal);
        dayTotal.setDate(totalDayTimes);
        foodForDay.add(dayTotal);

        //Try to print info to txt file
        try(
                PrintStream printStream =
                        new PrintStream(new FileOutputStream("NutritionTracker.txt", true))
        ){
            printStream.println(dayTotal.totalString());
            foodForMeal.clear();
        }catch (IOException ex){//Catch IO exceptions
            System.out.println("File cannot be found/made");
        }
    }

    /**
     * This method will create the GUI for the personal information. This includes tracking of the height,
     * weight, gender, and pregnancy of the user. This will change certain aspects of totals.
     *
     * @return
     */
    public BorderPane personal(){
        //Create border and grid pane for the personal GUI
        BorderPane borderPane = new BorderPane();
        GridPane personalVariables = new GridPane();

        //set the prompt text of the ComboBoxes
        pregnant.setPromptText("Select");
        pregnant.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select");
                } else {
                    setText(item);
                }
            }
        });
        heightFeet.setPromptText("Select Feet");
        heightInches.setPromptText("Select Inches");

        //Set the prompt text and styling of TextFields
        weightTF.setPromptText("Enter Pounds");
        caloriesBurnedTF.setPromptText("i.e. calories burnt sleeping, exercising, resting, etc.");
        caloriesBurnedTF.setMinWidth(300);

        //Button for saving info
        Button savePersonal = new Button("Save Information");

        //Set all items into their respective combo boxes
        pregnant.getItems().clear();
        pregnant.getItems().addAll("Yes", "No");

        heightFeet.getItems().clear();
        heightFeet.getItems().addAll(3, 4, 5, 6, 7, 8);

        heightInches.getItems().clear();
        heightInches.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);


        //Set all info into the gridPane
        //Weight
        personalVariables.add(new Label("Weight"), 0, 1);
        personalVariables.add(weightTF, 1, 1);

        //Calories Burned
        personalVariables.add(new Label("Calories burned Daily"), 0, 2);
        personalVariables.add(caloriesBurnedTF, 1, 2);

        //Height Feet
        personalVariables.add(new Label("Height:   Feet"), 0, 3);
        personalVariables.add(heightFeet, 1, 3);

        //Height Inches
        personalVariables.add(new Label("Inches"), 2, 3);
        personalVariables.add(heightInches, 3, 3);

        //Pregnant
        Label pregnantLabel = new Label("Pregnant");
        personalVariables.add(pregnantLabel, 0, 4);
        personalVariables.add(pregnant, 1, 4);

        //Save button
        personalVariables.add(savePersonal, 1, 5);
        personalVariables.setHgap(10);
        personalVariables.setVgap(10);//Set the gaps for spacing


        //Button functionality
        savePersonal.setOnAction(e ->{
            //Try to enter all information, if numbers are not correct show error
            try {
                //if all the information is inputted correctly
                if(heightFeet.getValue() != null && heightInches.getValue() != null) {
                    heightFeetChoice = (Integer) heightFeet.getValue();
                    heightInchesChoice = (Integer) heightInches.getValue();
                    heightTotal = (heightFeetChoice * 12) + heightInchesChoice;

                    //Gather info from the TextFields
                    weight = Double.parseDouble(weightTF.getText());
                    bMI = (weight / (heightTotal * heightTotal)) * 703;

                    calBurnedDaily = Double.parseDouble(caloriesBurnedTF.getText());

                    //If the gender is female and pregnant is not null
                        if(pregnant.getValue() != null) {
                            pregnantChoice = (String) pregnant.getValue();
                        }
                        //Don't allow progression if pregnant is null
                        else selectionAlert.showAndWait();

                    //If pregnant is "Yes" add 300 to calBurnedDaily
                    if(pregnantChoice.equalsIgnoreCase("Yes")){
                        calBurnedDaily += 300;
                    }

                    //Set the isFilled variable to true
                    personalIsFilled = true;

                    //Set the nutrition tab to automatically show after personal completion
                    hBox.getChildren().clear();
                    borderPane.getChildren().clear();

                    //Set info into hBox
                    hBox.getChildren().addAll(bPane());
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setSpacing(80);
                    hBox.maxWidthProperty().bind(vBox.widthProperty().subtract(15));

                    userTable.setTranslateX(-15);//Move table left by 15 pixels

                    //Set into a cleared vBox
                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(menuBar, hBox);
                }
                else
                    selectionAlert.showAndWait();

            }
            catch (NumberFormatException ex){
                System.out.println("Please enter numbers in text areas!");
                numberAlert.showAndWait();
            }
        });

        //Set the alignment and set into the borderPane
        personalVariables.setAlignment(Pos.CENTER);
        personalVariables.setTranslateY(150);
        borderPane.setCenter(personalVariables);
        return borderPane;
    }

    //Variables for the totals
    private String dateSelected = "";

    //Calories
    private double caloriesLeft = 0.0;
    private String calorieString = "";
    private Label calorieLabel = new Label();

    //Protein
    private double proteinLeft = 0.0;
    private double minProtein = 0.0;
    private double maxProtein = 0.0;
    private String proteinString = "";
    private Label proteinLabel = new Label();

    //Fat
    private double fatLeft = 0.0;
    private double minFat = 0.0;
    private double maxFat = 0.0;
    private String fatString = "";
    private Label fatLabel = new Label();

    //Carbs
    private double carbsLeft = 0.0;
    private double minCarbs = 0.0;
    private double maxCarbs = 0.0;
    private String carbString = "";
    private Label carbLabel = new Label();

    //Sodium
    private double sodiumLeft = 0.0;
    private double idealSodiumLeft = 0.0;
    private double minSodium = 0.0;
    private double maxSodium = 0.0;
    private double idealSodium = 0.0;
    private String sodiumString = "";

    //BMI
    private String bmiString = "";

    /**
     * This method will total information from the personal and nutrition tabs and display to user
     *
     * @return
     */
    public BorderPane totals(){
        //Create and format borderPane and gridPane
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setTranslateY(200);

        //Date and Decimal Format for future use
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        //Create and set information into ComboBox
        ComboBox selectDay = new ComboBox();
        selectDay.setPromptText("Select Date");
        //For loop to put date of meals into the ComboBox
        for(int i = 0; i < foodForDay.size(); i++) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, i);
            String formattedDate = dateFormat.format(date.getTime());
            selectDay.getItems().add(formattedDate);
        }
        //Buttons for functionality
        Button totalBt = new Button("Total");
        totalBt.setTranslateX(-25);
        Button totalAgainBt = new Button("New Total");
        totalAgainBt.setMinWidth(35);

        //Label for the comboBox
        Label selectDayLabel = new Label("Select Day");
        selectDayLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 25));
        gridPane.add(selectDayLabel, 0, 0);
        gridPane.add(selectDay, 1, 0);

        //Set button into gridPane
        gridPane.add(totalBt, 1, 1);
        borderPane.setCenter(gridPane);//Set into the borderPane

        //Functionality for buttons
        totalBt.setOnAction(e ->{
            //Gather the index of the date selected to be able to choose the correct meal set into a Food object
            daySelection = selectDay.getSelectionModel().getSelectedIndex();
            Food food = foodForDay.get(daySelection);

            //Grab the date and set into a Label
            dateSelected = food.getDate();
            Label dateLabel = new Label(dateSelected);
            dateLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));

            //Strings for the information
            //BMI String
            if(bMI >= 40){
                bmiString = "Your BMI is " + decimalFormat.format(bMI) +
                        ". You are in the Extremely Obese section of the BMI chart! Healthy is " +
                        decimalFormat.format(bMI - 24) + " numbers lower";
            }
            else if(bMI < 40 && bMI >= 30){
                bmiString = "Your BMI is " + decimalFormat.format(bMI) +
                        ". You are in the Obese section of the BMI chart! Healthy is " +
                        decimalFormat.format(bMI -24) + " numbers lower";
            }
            else if(bMI < 30 && bMI >= 25){
                bmiString = "Your BMI is " + decimalFormat.format(bMI) +
                        ". You are in the Overweight section of the BMI chart! Healthy is " +
                        decimalFormat.format(bMI -24) + " numbers lower";
            }
            else if(bMI < 25 && bMI >= 19){
                bmiString = "Your BMI is " + decimalFormat.format(bMI) +
                        ". You are in the Healthy section of the BMI chart!";
            }
            else if(bMI < 19){
                bmiString = "Your BMI is " + decimalFormat.format(bMI) +
                        ". You are in the Underweight section of the BMI chart. Healthy is " +
                        decimalFormat.format(19 - bMI) + " numbers higher";
            }
            //BMI Label
            Label bmiLabel = new Label(bmiString);
            bmiLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 13));

            //Calorie String
            if(food.getCalories() >calBurnedDaily){
                caloriesLeft = food.getCalories() - calBurnedDaily;
                calorieString = "You are eating " + decimalFormat.format(caloriesLeft) +
                        " more calories than you are burning!";
            }
            else if(food.getCalories() < calBurnedDaily){
                caloriesLeft = calBurnedDaily - food.getCalories();
                calorieString = "You are eating " + decimalFormat.format(caloriesLeft) +
                        " less calories than you are burning!";
            }
            else if(food.getCalories() == calBurnedDaily){
                caloriesLeft = food.getCalories() - calBurnedDaily;
                calorieString = "You are eating as many calories as you are burning today!";
            }
            //Calorie Label
            calorieLabel.setText(calorieString);
            calorieLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));

            //Protein String
            maxProtein = (food.getCalories() * .35) / 4;
            minProtein = (food.getCalories() * .10) / 4;

            if(food.getProtein() > maxProtein){
                proteinLeft = food.getProtein() - maxProtein;
                proteinString = "You are " + decimalFormat.format(proteinLeft) +
                        " grams above the maximum daily protein!";
            }
            else if(food.getProtein() < minProtein){
                proteinLeft = minProtein - food.getProtein();
                proteinString = "You are " + decimalFormat.format(proteinLeft) +
                        " grams below the minimum daily protein!";
            }
            else{
                proteinLeft = maxProtein - food.getProtein();
                proteinString = "You are at the right amount of protein! You have " +
                        decimalFormat.format(proteinLeft) + " grams before you hit the maximum daily protein.";
            }
            //Protein label
            proteinLabel.setText(proteinString);
            proteinLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));

            //Fat String
            maxFat = (food.getCalories() * .35) / 9;
            minFat = (food.getCalories() * .20) / 9;

            if(food.getFat() > maxFat){
                fatLeft = food.getFat() - maxFat;
                fatString = "You are " + decimalFormat.format(fatLeft) + " grams above the maximum daily fat!";
            }
            else if(food.getFat() < minFat){
                fatLeft = minFat - food.getFat();
                fatString = "You are " + decimalFormat.format(fatLeft) + " grams below the minimum daily fat!";
            }
            else{
                fatLeft = maxFat - food.getFat();
                fatString = "You are at the right amount of fat! You have " +
                        decimalFormat.format(fatLeft) + " grams before you hit the maximum daily fat.";
            }
            //Fat label
            fatLabel.setText(fatString);
            fatLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));

            //Carb String
            maxCarbs = (food.getCalories() * 0.65) / 4;
            minCarbs = (food.getCalories() * 0.45) / 4;

            if(food.getCarbs() > maxCarbs){
                carbsLeft = food.getCarbs() - maxCarbs;
                carbString = "You are " + decimalFormat.format(carbsLeft) + " grams over the maximum daily carbs!";
            }
            else if(food.getCarbs() < minCarbs){
                carbsLeft = minCarbs - food.getCarbs();
                carbString = "You are " + decimalFormat.format(carbsLeft) + " grams under the minimum daily carbs!";
            }
            else{
                carbsLeft = maxCarbs - food.getCarbs();
                carbString = "You are at the right amount of carbs! You have " +
                        decimalFormat.format(carbsLeft) + " grams before you hit the maximum daily carbs.";
            }
            //Carb label
            carbLabel.setText(carbString);
            carbLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));


            //Sodium String
            maxSodium = 2300;
            idealSodium = 1500;
            minSodium = 500;

            if(food.getSodium() > maxSodium){
                sodiumLeft = food.getSodium() - maxSodium;
                idealSodiumLeft = food.getSodium() - idealSodium;
                sodiumString = "You are " + decimalFormat.format(sodiumLeft) +
                        " mg over the maximum daily sodium! This means that you are " +
                        decimalFormat.format(idealSodiumLeft) + " mg over the ideal sodium limit of 1500 mg!";
            }
            else if (food.getSodium() < minSodium) {
                sodiumLeft = minSodium - food.getSodium();
                idealSodiumLeft = idealSodium - food.getSodium();
                sodiumString = "You are " + decimalFormat.format(sodiumLeft) +
                        " mg under the minimum daily sodium! This means that you " +
                        "are " + decimalFormat.format(idealSodiumLeft) + " mg under the ideal sodium limit of 1500 mg!";
            }
            else if (food.getSodium() < maxSodium && food.getSodium() > idealSodium){
                sodiumLeft = maxSodium - food.getSodium();
                idealSodiumLeft = food.getSodium() - idealSodium;
                sodiumString = "You are " + decimalFormat.format(sodiumLeft) +
                        " mg under the maximum daily sodium! This means that you are " +
                        decimalFormat.format(idealSodiumLeft) + " mg over the ideal sodium limit of 1500 mg!";
            }
            else if (food.getSodium() < idealSodium && food.getSodium() > minSodium){
                sodiumLeft = food.getSodium() - minSodium;
                idealSodiumLeft = idealSodium - food.getSodium();
                sodiumString = "You are " + decimalFormat.format(sodiumLeft) +
                        " mg over the minimum daily sodium! This means that you are " +
                        decimalFormat.format(idealSodiumLeft) + " mg under the ideal sodium limit of 1500 mg!";
            }
            else if (food.getSodium() == idealSodium){
                sodiumLeft = maxSodium - food.getSodium();
                idealSodiumLeft = idealSodium - food.getSodium();
                sodiumString = "You are " + decimalFormat.format(sodiumLeft) +
                        " mg under the maximum daily sodium! This means that you are at the ideal daily sodium limit!";
            }
            else{
                sodiumLeft = maxSodium - food.getSodium();
                idealSodiumLeft = food.getSodium();
                sodiumString = "You are at the maximum daily sodium! This means that you are" +
                        decimalFormat.format(idealSodium) + " mg above the ideal sodium limit of 1500 mg!";
            }
            //Sodium label
            Label sodiumLabel = new Label(sodiumString);
            sodiumLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));

            //Try to print the info out to txt file in the same path
            try(
                    PrintStream printStream =
                            new PrintStream(new FileOutputStream("NutritionTotal.txt", true))
            ){
                printStream.println("\n" + dateSelected + "\n" + calorieString + "\n" + proteinString +
                        "\n" + fatString + "\n" + carbString + "\n" + sodiumString);
            }catch (IOException ex) {
                System.out.println("File cannot be found/made");
            }

            //If pregnant is No
            if(pregnantChoice.equalsIgnoreCase("No")){
                gridPane.getChildren().clear();
                gridPane.add(dateLabel, 0, 0);
                gridPane.add(calorieLabel, 0,1);
                gridPane.add(proteinLabel, 0, 2);
                gridPane.add(fatLabel, 0, 3);
                gridPane.add(carbLabel, 0, 4);
                gridPane.add(sodiumLabel, 0, 5);
                gridPane.add(bmiLabel, 0, 6);
                gridPane.add(totalAgainBt, 1, 7);
                totalAgainBt.setTranslateX(-425);
            }
            //If pregnant is yes
            else if(pregnantChoice.equalsIgnoreCase("Yes")) {
                gridPane.getChildren().clear();
                gridPane.add(dateLabel, 0, 0);
                gridPane.add(calorieLabel, 0, 1);
                gridPane.add(proteinLabel, 0, 2);
                gridPane.add(fatLabel, 0, 3);
                gridPane.add(carbLabel, 0, 4);
                gridPane.add(sodiumLabel, 0, 5);
                gridPane.add(totalAgainBt, 1, 6);
                totalAgainBt.setTranslateX(-425);
            }
        });
        totalAgainBt.setOnAction(e -> {
            vBox.getChildren().clear();
            vBox.getChildren().addAll(menuBar, totals());//Add items back to the vBox
        });
        return borderPane;
    }
}
