To build this project,
Run createTable.sql on MySQLworkbench to generate the local database with connection to 127.0.0.1:3306
The connection username is "root". Password is "vincent0124!".
To use your own password, replace the global variable "PASSWORD" in app/src/main/java/com/csci310/cs310_groupproject/MainActivity.java to connect to the Database.


The improved capabilities since Project 2.4:
(1) allow user to upload profile photo
(2) when creating events, users can search for locations by typing in few starting words
(3) allow user to choose multiple time slots when sign up for events
(4) allow user to view event by clicking events in map view
(5) better UI for event management page
(6) notification when user sign up events, change events, received/reply invitation
(7) automatically determine event time after due time and notify all participants
