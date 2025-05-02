
Budget Beaters
Budget Beaters is a user-friendly financial tracking Android app that helps users manage and monitor their daily expenses, categorize spending, visualize finances using pie and bar charts, track progress through dashboards, and earn badges for reaching financial milestones.


GitHub Repository: Budget Beaters


Key Features

    •	 Dashboard & Analytics: View daily spending, progress charts, and income by categories using dynamic pie and bar graphs.
    •	 Expense Tracking: Add, remove, and view detailed expense history with descriptions, categories, and even images.
    •	 Category Management: Create and manage income/expense categories with goal tracking.
    •	 Badges & Awards: Get rewarded for meeting financial goals and staying within budget.
    •	 Shared Budgeting: Add members to budget together, inputting names and emails to collaborate.
    •	 Account & Navigation: Register/Login, reset forgotten passwords, and navigate easily through a modern swipe-based UI.
    •	 Info Centre: Access guides and tips to manage finances effectively.

Technologies & Dependencies:

   Built with Java/Kotlin in Android Studio using the following libraries:
   
       •	AndroidX Core, AppCompat, ConstraintLayout, Lifecycle
       •	Navigation Components (fragment.ktx, ui.ktx)
       •	Room (local DB) with KTX and Coroutines
       •	MPAndroidChart for charting
       •	Glide for image handling
       •	Espresso & JUnit for testing
       kotlin
       CopyEdit
       implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
       implementation("com.github.bumptech.glide:glide:4.12.0")
       kapt("com.github.bumptech.glide:compiler:4.12.0")

Functional Requirements

    1.	Register and login user accounts
    2.	Reset forgotten passwords
    3.	Add, edit, and delete expense entries
    4.	View expenses filtered by date or category
    5.	Track daily spending with bar charts
    6.	Visualize budget status with pie charts (under, near, or over budget)
    7.	Manage categories with goals and descriptions
    8.	Earn and view badges/awards based on usage
    9.	Share budgets with other users
    10.	Access app usage guides and financial tips

Non-Functional Requirements

    •	Usability: The app uses a simple, intuitive layout with swiping gestures and a clean navigation bar.
    •	Performance: Fast local data access using Room and smooth chart rendering.
    •	Scalability: Designed to support multiple users and shared budgeting scenarios.
    •	Security: Password authentication and email verification for reset.
    •	Reliability: Offline capabilities through local storage with Room database.
    •	Maintainability: Modular codebase using modern Android architecture (MVVM, Navigation Components).

Installation Instructions

    1.	Clone the repository: git clone https://github.com/JustJoshDW/Prog7313P2_JAKT_BUDGETBEATERS.git
    2.	Open in Android Studio
    3.	Sync Gradle to install all dependencies
    4.	Build and run on an emulator or Android device
    4. Run the App
    Choose how you want to run the app:
       Option A: On an Android Emulator
       1.	Click on AVD Manager (top-right toolbar).
       2.	Create a new virtual device (Pixel 5 or any other).
       3.	Select an API level (preferably 30 or above).
       4.	Start the emulator.
       5.	Click the Run  button or press Shift + F10 to deploy the app.
       
       Option B: On a Physical Android Device
       1.	Enable Developer Options and USB Debugging on your Android phone.
       2.	Connect your phone via USB.
       3.	Select your device in the Run target list.
       4.	Run the app.
    App Functionality (Feature Overview)
     User Authentication & Onboarding
    •	Main Page: Users swipe to enter.
    •	Registration Page:
       o	Input: Name, Email, Password, Confirm Password.
       o	Action: Tap Sign Up to create account.
       o	Link: Already registered? → Login here.
    •	Login Page:
       o	Input: Email & Password.
       o	Link: Forgot Password → navigate to reset page.
    •	Forgot Password Page:
       o	Input: Email address.
       o	Action: Submit to receive reset instructions via email.
________________________________________
 Navigation & Menu Options
 
    •	Once logged in, user is taken to the Menu Page.
    •	Navigation Bar includes options:
        o	Logout
        o	Information Details
        o	View Awards/Badges
        o	Return to Main Menu
________________________________________
 Pie Chart / Graph View
 
    •	Features:
        o	View existing pie chart of financial categories.
        o	View legend with color codes and category percentages.
    •	Add Category:
        o	Input: Name, optional description, min/max income goals.
        o	Action: Tap Save to add to pie chart.
    •	Delete Category:
        o	Select using tick box.
        o	Confirm deletion with Yes/No dialog.
________________________________________
 View & Manage Expenses
 
    •	Expense Page Features:
        o	Add Expense
        o	Remove Expense
        o	View All Expenses
        o	View Income by Category
     Add Expense:
      •	Inputs:
        o	Name, Amount, Date, Description, Category
        o	Optional: Add an image
        •	Action: Tap Save to store the expense.
    Remove Expense:
      •	Select expense using a list or checkbox.
      •	Confirm deletion with Yes/No prompt.
    View All Expenses:
      •	Filter Options:
        o	Select From Date and To Date
        o	Or choose to View All
      •	Filtered expenses will be displayed accordingly.
    View Income by Category:
      •	Select From Date, To Date, and a Category.
      •	Tap Submit to view filtered income stats.
________________________________________
 Daily Spending
 
    •	Input: From Date and To Date or view all.
    •	Display: A Bar Graph showing spending trends.
    •	Purpose: Helps users check if they're staying within budget.
________________________________________
 Progress Dashboard
 
    •	Select a Month.
    •	View a Pie Chart with:
        o	Green: Under Budget
        o	Yellow: Near Limit
        o	Red: Overspent
    •	Categories are labeled with percentages.
    •	Option to Export pie chart data.
________________________________________
 Shared Budgeting
 
    •	Add Members:
        o	Input: Number of members, names, and emails.
        o	Tap Submit to invite/add users.
________________________________________
 Categories Page
 
    •	View all financial categories.
    •	Double-click a category to see related expenses.
________________________________________
 Information Details
 
    •	Access guides/tutorials:
        o	How to use the app
        o	Financial management tips
________________________________________
 Awards & Badges
  •	View milestones and achievements earned by:
  
      •	Stayed Under Budget for a Week
      •	Completed 1st Month Without Overspending
      •	Beat Your Budget for 6 Months
      •	Reduced Spending in 3 or More Categories
      •	 Paid Off a Major Debt or Loan
      •	 Used Public Transport or Carpooling to Save Money
      •	 Logged All Expenses Correctly for 3+ Months
      •	 Hit All Major Savings Goals for the Year

Changelog:

Screen 2: Register page

    - The font and the textboxes had to be changed as we couldn't get those exact ones ones on Android Studio
    - We changed the Login link to a button as it is more user friendly 

Screen 3: Forgot Password

    - As a result of us using the local AppDatabase, we realised that we wouldn't be be able to actually send them an email for them to reset it. 
    - So, we directed them to a new page, which we added in, called Reset password.
    - This would then allow the user to change their password and it would update the database

Screen 4: Menu

    - We kept most of the designs the same, except we converted the bottom row of buttons into a navbar, which the user can navigate through
    - We additionally added a menu button on the navbar, so the user will always be redirected back to the menu, regardless of the page they are on
    - We also couldn't get the range of colours that we used for the buttons and so we decided to go with a Mint green - still keeping within the colour scheme

Screen 5: Budget Pie Chart 

    - As mentioned earlier, we had to change the colour scheme but we were able to keep the design the same
    - This pie chart is dynamic and so updates as the user adds and deletes categories

Screen 6: Add A Category 
 
    - We kept the design mostly the same, however we added 2 extra fields to allow the user to enter in a maximum goal and a minimum goal for their income

Screen 7: Remove a Category

    - We kept the design mostly the same, but we weren't able to incorporate the symbols next to each Category

Screen 8: View All Expenses

    - On this page, we decided to have 4 buttons: Add Expense, Remove Expense, View Expenses and View Income by Categories
    - We had discovered that it would be quite difficult to pull the filter between pages

Screen 9: Categories

    - We made this it's own page, that is linked to the main menu 
    - It was quite difficult to link the filtering to this page too

Screen 10: View Expense page

    - This is now it's own page that is linked to the View All Expenses page through a button. 
    - This bottom buttons have been removed from this page and a filter has been added
    - Once the user filters the dates, their expenses appear below it in a recyclerView

Screen 11: Add an Expense

    - We kept majority of the features the same, except like I mentioned before this, we removed the buttons
    - Instead of the user entering the date, they can choose from a date picker - it increases user friendliness
    - We also put a drop-down tab for the categories for the user to choose from the categories that were already added
    - We also removed the back button as we have the navbar that has a menu button

Screen 12: Remove an Expense

    - Instead of having a drop-down tab, we display all the expenses, if the user clicks on it, the details appear in a pop-up message and they can delete it if they want
    - Therefore, we removed the No and Yes buttons on the bottom as the pop-up message already has it


Screen 13: View Category Income

    - We added this page so that the use can view the total amount of money spent in a certain category during a certain time period

Screen 14 and 15: View Daily Spending

    - As mentioned before, there was an issue with pulling the filter between the 2 pages, so we combined both. 
    - The graph and filter are now on the same page
    - We removed the View All Daily Spending Button - it was not needed
  
Troubleshooting Tips

    •	Gradle Build Issues: Go to File > Invalidate Caches / Restart if the project fails to sync.
    •	Missing Dependencies: Ensure you have a stable internet connection to download libraries.
    •	Emulator Errors: Make sure Intel HAXM is installed or try using a different device image.
References

	•  Android Developers. (2019). Accessing data using Room DAOs | Android Developers. [online] Available at: https://developer.android.com/training/data-storage/room/accessing-data
	•  Android Developers. (2020). Defining data using Room entities | Android Developers. [online] Available at: https://developer.android.com/training/data-storage/room/defining-data
	•  Android Developers. (2024). Download Android Studio & App Tools - Android Developers. [online] Available at: https://developer.android.com/studio [Accessed 2 May 2025].
	•  Android Developers. (n.d.). Fragment. [online] Available at: https://developer.android.com/reference/android/app/Fragment
	•  Android Developers. (2025). Generated binding classes. [online] Available at: https://developer.android.com/topic/libraries/data-binding/generated-binding [Accessed 2 May 2025].
	•  Chernozubov, D. (2020). Recycler view, power of asynchronous view holders creation. [online] Medium. Available at: https://medium.com/@icesrgt/recycler-view-power-of-asynchronous-view-holders-creation-b3c9fe067702 [Accessed 2 May 2025].
	•  freeCodeCamp.org. (2024). How to Use Git and GitHub – a Guide for Beginners and Experienced Developers. [online] Available at: https://www.freecodecamp.org/news/guide-to-git-github-for-beginners-and-experienced-devs/
	•  GitHub. (2025). GitHub. [online] Available at: https://github.com/
	•  hangyuan. (2021). Understanding Java volatile visibility. [online] Stack Overflow. Available at: https://stackoverflow.com/questions/68427434/understanding-java-volatile-visibility
	•  OpenAI. (2025). ChatGPT. [online] Available at: https://chatgpt.com/


Done by:

    - Joshua de Wet - ST10313014
    - Ankriya Padayachee - ST10260507
    - Kyle Govender - ST10145498
    - Teagan Griffiths - ST10300913

