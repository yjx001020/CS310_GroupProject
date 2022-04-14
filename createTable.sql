DROP DATABASE IF EXISTS CS310Project;
CREATE DATABASE CS310Project;
USE CS310Project;

CREATE TABLE Event (
  eventID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  accessType VARCHAR(45) NOT NULL,
  location VARCHAR(256) NOT NULL,
  description VARCHAR(1024) NULL,
  eventType VARCHAR(45) NOT NULL,
  dueTime DATETIME NOT NULL,
  timeDecided DATETIME NULL,
  ownerEmail VARCHAR(45) NOT NULL
);

CREATE TABLE Invitation (
  InvitationID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  userID VARCHAR(45) NOT NULL,
  eventID INT NOT NULL,
  acceptStatus TINYINT
);

CREATE TABLE Users(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL,
    UserPassword VARCHAR(50) NOT NULL,
    Fname VARCHAR(50)NULL,
    Lname VARCHAR(50)NULL,
    photoFileName VARCHAR(150) NULL,
    major VARCHAR(150) NULL,
    studyYear VARCHAR(50)NULL
  );
    
CREATE TABLE Timeslots(
    eventID INT NOT NULL,
    InvitationID INT NOT NULL,
    timeslots DATETIME NOT NULL,
    chosen TINYINT
);

Create table Notification (
 email varchar(50) not null,
 owner varchar(50) not null,
    message varchar(256) not null
);

Insert into Users (email, UserPassword, Fname, Lname, major, studyYear) values ('trojan@usc.edu', '123456hi','tommy', 'trojan', 'Econ', 'Freshman');
Insert into Users (email, UserPassword, Fname, Lname, major, studyYear) values ('taylor@usc.edu', 'hello123','Taylor', 'Swift', 'Music', 'Senior');
Insert into Users (email, UserPassword, Fname, Lname) values ('tony@usc.edu', 'hello123','Tony', 'Liang');

Insert into CS310Project.Event (accessType, location, description, eventType, dueTime, timeDecided, ownerEmail) values ('public', '32,117', 'ice cream social', 'social event', '2022-03-01\t16:00:00', null, 'taylor@usc.edu');
Insert into CS310Project.Event (accessType, location, description, eventType, dueTime, timeDecided, ownerEmail) values ('public', '34,118', 'study for CSCI 310 midterm', 'study group', '2022-03-28\t13:00:00', null, 'trojan@usc.edu');
Insert into CS310Project.Event (accessType, location, description, eventType, dueTime, timeDecided, ownerEmail) values ('private', '32,117', 'football training', 'sports event', '2022-04-01\t00:00:00', null, 'trojan@usc.edu');

Insert into CS310Project.Invitation (userID, eventID, acceptStatus) values ('taylor@usc.edu', 1, 0);
Insert into CS310Project.Invitation (userID, eventID, acceptStatus) values ('tony@usc.edu', 1, 1);
Insert into CS310Project.Invitation (userID, eventID, acceptStatus) values ('taylor@usc.edu', 2, 0);
Insert into CS310Project.Invitation (userID, eventID, acceptStatus) values ('tony@usc.edu', 3, 0);

Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (2, '2022-02-20\t16:00:00', 1, 1);
Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (2, '2022-02-20\t14:00:00', 0, 1);
Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (1, '2022-02-10\t16:00:00', 0, 1);
Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (1, '2022-02-10\t14:00:00', 0, 1);
Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (3, '2022-02-10\t16:00:00', 0, 2);
Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (3, '2022-02-10\t14:00:00', 0, 2);
Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (4, '2022-03-15\t16:00:00', 0, 3);
Insert into CS310Project.Timeslots (InvitationID, timeslots, chosen, eventID) values (4, '2022-03-15\t14:00:00', 0, 3);
INSERT INTO `CS310Project`.`Invitation` (`InvitationID`, `userID`, `eventID`) VALUES ('5', 'trojan@usc.edu', '3');
UPDATE `CS310Project`.`Invitation` SET `acceptStatus` = '1' WHERE (`InvitationID` = '5');
INSERT INTO `CS310Project`.`Invitation` (`InvitationID`, `userID`, `eventID`) VALUES ('6', 'trojan@usc.edu', '1');
UPDATE `CS310Project`.`Invitation` SET `acceptStatus` = '0' WHERE (`InvitationID` = '6');
ALTER TABLE `CS310Project`.`Notification` 
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT AFTER `message`,
ADD PRIMARY KEY (`id`);
INSERT INTO `CS310Project`.`Notification` (`email`, `owner`, `message`) VALUES ('trojan@usc.edu', 'taylor@usc.edu', 'hello');
INSERT INTO `CS310Project`.`Invitation` (`InvitationID`, `userID`, `eventID`, `acceptStatus`) VALUES ('7', 'trojan@usc.edu', '1', '0');
UPDATE `CS310Project`.`Invitation` SET `eventID` = '2' WHERE (`InvitationID` = '7');
