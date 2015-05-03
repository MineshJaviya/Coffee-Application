# Coffee-Application
Description:
- The app downloads the json data from network using volley and shows it on custom list adapter
- On clicking any of the row of the list, you will be taken to new activity which contains detailed decription of that particular coffee.
- Volley is used for downloading data over netwrok and Jackson is used for parsing

Some Key Points:
- Android does not require a back button on the UI as all the android devices has hardware back button. But as the required design requires back button, i have included a basic up navigation on UI
- On Actionbar, android by default uses left indentation whereas iOS uses center. As per the requirement of the design, i have centered the app icon and removed the activity title by using background image. But this is discouraged.
- "Share" button was just show in the 2nd activity but its functionality is not implemented as no necessary information was provided.
