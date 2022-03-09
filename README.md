# CSCI_2906_Capstone_Project
This is the repository for my capstone project files for STech's Software Development Program.

## Synopsis
This project is called FoodTracker.java. It allows a user to input their weight, height, calories that they burn daily, and whether or not they are pregnant. This is combined with the ability to use a database of food items that has the more important nutrient values inputted and will total the amounts for the user. With the personal numbers, this is used to calculate where they stand in regards to the right nutrition for the day and will give the numbers back to the user. When the program shows the total, it is also written out to a .txt file in the project path.

## My Motivation Behind this Project
This is a more robust version of the program I made a little over two months ago. The reason why I choose to take another look at it is because the original was a little difficult to use, it didn't allow for the nutrition value calculations, and overall didn't preform as well as I would have liked it to. This is part of the reason why I have built this, but the main reason is because I want something that I can actually use. This will allow me to track all of that information with ease due to not needing to create any accounts, pay any money for the calculation, or have several apps just to get the same numbers. This makes things a little easier for me to use compared to the original, and I know that I would have loved to have something along these lines in high school while I was involved in competitive sports.

## How to Run
The files needed to run this application are FoodTracker.java, Food.java, FoodItems.java, FT.css (for styling), FTSecondScene.css (for styling), Banana.png, System San Francisco Display Regular (1).ttf, and Banana.png. FoodTracker, Food, and FoodItems are all in the src folder while the rest of the files are needed in the resources folder to run correctly. An IDE capable of running JavaFx is also needed.

## Code Example
```
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
```
This bit of code allowed me to pull information from the MySQL database inorder to put the information into the table and allow for the user to use it.
Without this, the code would not work as a database and the user would need to input every single item and there nutrition into the calculator.

## Test
Do to the methods not returning any values that I can test for, there is no JUnit test for this program.

## Contributors
Contributions made by Jason Adams, Rich Mallek, Drake Hunt, and Jaden Hunt who helped find solutions to issues, tested the program, or helped give feedback on what they believe could be tweaked.

### There are quite a few links that I used during the duration of the NutritionTracker development, they are listed below.

How many calories in a day vs good calories: https://www.foodnetwork.com/healthyeats/healthy-tips/how-many-calories-should-i-eat-in-a-day

How much protein in a day: https://www.sclhealth.org/blog/2019/07/how-much-protein-is-simply-too-much/

How much fat in a day: https://my.clevelandclinic.org/health/articles/11208-fat-what-you-need-toknow#:~:text=The%20dietary%20reference%20intake%20(DRI,because%20they%20provide%20health%20benefits.

How many carbs in a day: https://www.mayoclinic.org/healthy-lifestyle/nutrition-and-healthy-eating/in-depth/carbohydrates/art-20045705#:~:text=The%20Dietary%20Guidelines%20for%20Americans,grams%20of%20carbohydrates%20a%20day.

How much sodium in a day: https://www.heart.org/en/healthy-living/healthy-eating/eat-smart/sodium/how-much-sodium-should-i-eat-per-day

What is BMI and how is it used correctly: https://www.medicalnewstoday.com/articles/323622

Obesity statistics: https://www.singlecare.com/blog/news/obesity-statistics/

Anorexia Facts and Statistics: https://www.eatingrecoverycenter.com/conditions/anorexia/facts-statistics

Bulimia Facts and Statistics: https://americanaddictioncenters.org/bulimia-treatment/facts-and-statistics

Most of these deal with how people should structure their nutritional health, i.e. how much sodium should be ingested in a single day, while some of them deal with the facts and statistics of eating disorders and their impact on the USA. These links helped me find the correct equations to use for the calculations of the totals of the day, but I am not a doctor and this information should not be taken as medical advice. I am only using information that has been publish in other people's reasearch. These will help anybody learn more about health, how to eat healthy, the impacts of eating disorders, and how people can overcome obesity. Loosing weight and living a healthy lifestyle really isn't all that hard due to all of the technology of today, but it looks like it could be one of the most difficult things. This looks to make loosing wieght even easier so that it isn't daunting to look at, hopefully people are able to make changes due to this application. 

### More links are posted below for more reasearch into each nutrient being tracked here.

Calories per Day Calculator: https://www.healthline.com/nutrition/how-many-calories-per-day#calculator

Protien in the Diet: https://www.healthline.com/nutrition/how-much-protein-per-day

Fats in the Diet: https://www.healthline.com/nutrition/how-much-fat-to-eat#TOC_TITLE_HDR_6

Carbs in the Diet: https://www.healthline.com/nutrition/how-many-carbs-per-day-to-lose-weight#how-many-carbs-to-eat

Sodium in the Diet: https://www.fda.gov/food/nutrition-education-resources-materials/sodium-your-diet

### Below are links for the reasons why exercise is a vital part of healthy living and lifestyles.

Both are important: https://www.healthline.com/nutrition/diet-vs-exercise#bottom-line

Reasons to Exercise too: https://www.mayoclinic.org/healthy-lifestyle/fitness/in-depth/exercise/art-20048389
