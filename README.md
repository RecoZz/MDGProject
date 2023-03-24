# MDGProject
This Repo was for Assignment No. 1 of MDG Recruitments 2023.
The code and layout were developed in Android Studio, using java language.
The screen is divided into 25 by 25 grid.
The head and the food position is held on one of the blocks of the grid.
The food position is selected at random from given criteria, and head's position is directed on basis of Heading enum in Person.java class.
The API used for the project is https://rapidapi.com/edamam/api/edamam-food-and-grocery-database/, made by edaman.
Volley was used for extracting data from the api, and Picasso was used for loading on screen taken from the database.
The game starts with 600 Kcals. 
Each step deducts 5 Kcals, and eating food adds Kcals on basis on data given from API.
Once Kcals reach above 700, speed of head is halved, indicated by changing color of Kcals and time score.
Once Kcals reach below 5, game is stopped.
The time score is added to firecase.
**BONUS FEATURE:** The score is also checked against the HighScore on the database.
If its greater, NEW HIGHSCORE! is shown on the screen.
Again tapping the screen resets Kcals as 600 and score as 0 and the game restarts.
