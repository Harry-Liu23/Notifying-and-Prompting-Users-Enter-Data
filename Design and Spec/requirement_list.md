### Requirement List

**MoSCoW (Listing the importance of tasks):** Must-have, Should-have, Could-have, Would-have (Would be nice to have)

---

1. **Must-have** An interface (pop-up or a page in the app) that enables users to enter data

   - The main interface (Done, 6 hours)
     - Implement a main screen allowing data entry by displaying input fields for users to enter relevant information.
     - Design the interface as intuitive and accessible, possibly using layouts that scale well across different device sizes.
     - Incorporate options to save and clear data entries, ensuring data persists between app sessions through storage mechanisms like SQLite or shared preferences.

   - The analysis interface (Estimation 8-10 hours)
     - **Should-have** A graph analysis for data overview in the past week (Estimation 1-2 hours)
       - Integrate a graphical library or view to represent data visually over the last week. This could involve creating a weekly data set from saved records and rendering it in a simple line or bar graph format.
     - **Could-have** A comparison of suggested data and current status (Estimation 2-4 hours)
       - Store baseline data for recommended values and compare these with the user’s current data. Implement logic to calculate differences and display them on the interface in an easily digestible way (e.g., percentages or difference values).

   - **Must-have** The records interface that stores all the current records and export records function (Estimation 4 hours)
     - Create a storage layer using SQLite or a file system to retain entered records.
     - Implement a records screen that displays all entries in a list format and enables exporting data to a file format, like CSV, that the user can share or save externally.

   - **Must-have** Pop-up data entry field (Estimation 6 hours)
     - Trigger a data entry pop-up whenever the user interacts with a relevant notification. 
     - Use a dialog-style layout that prompts the user to enter data and includes an option to navigate directly to the main interface.
     - Ensure this pop-up handles cases where the user exits or navigates away, storing any data entered up to that point.

2. **All Must-have** Notifications

   - Send timely prompts when users unlock their phones after a specified period (typically 2 hours) of inactivity. (Done, 6 hours)
     - Register a broadcast receiver that listens for phone unlock events.
     - Use an AlarmManager or similar to manage the time interval and prompt users upon unlocking if the inactivity threshold is met.

   - Or send timely prompts when users are using certain types of apps. (Estimation 6 hours)
     - Monitor foreground activities (potentially through accessibility services, if permitted) to detect when specified apps are open.
     - Trigger notifications to remind users of data entry if a target app has been open for a specified time.

   - **Could-have** Allow customization of notification frequency and type (i.e., the time at which the app would prompt).
     - Add options for users to select how often notifications should be sent and which types of events (like unlocking the phone or opening specific apps) should prompt reminders.
     - Store these user preferences and ensure the notification system respects these settings dynamically.

3. **Would-have** Prompt the user with suggestions, such as reminding the user to stay hydrated.
   - Periodically assess the user’s data and, based on patterns or missing entries, prompt with suggestions (e.g., hydrate reminder).
   - Implement simple rules to analyze data entries and suggest actions based on patterns like infrequent water intake, lack of mood updates, etc.
      - The users must have Android devices
   - **Could-have** Second stage (Testing the set-up and customization of prompting settings)
