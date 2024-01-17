Problem Statement

Design a ride sharing platform with 3 different entities – 

Traveller – 
•	Traveller can share a link to the companion through SMS or Whatsapp 
•	Traveller can also view the rides he has done
Companion – 
•	Companions can track the location of traveller during the ride.
•	 They get a notification on end of their ride with a feedback form
•	They also get a notification when the traveller is near their location.
Admin – 
•	View all the rides that are done on the app
•	Admins can also view the experience feedback from the companions. 

Basic Approach – [image](./Keshav.png)
 


Diving Deep into the Approach

•	Ride Database – Atrributes - Ride Database consists of TripID, TravellerID, CompanionID,Start Latitude, Start Longitude, End Latitude, End Longitude, Trip Start Time, Trip End Time, FeedbackID. 
•	Login Database – ID, Name, Phone Number, email, hashed_password, Roles.
•	Traveller Realtime Database – TravellerID, Phone Number, Traveller Name, Cab number, Current latitude and current longitude
•	Feedback Database – Consists of feedbackID, companionID, imageurl (if there are any images),  comments.
•	Login gateway – All the entities should login to the system.
•	Link sharing Service – Link sharing will be done through the Whatsapp API/SMS managing services.
o	Ride Link will be of the form domainname.com/tripID/companionID.
o	Feedback Link will be of the form domainname.com/tripID/companionID/feedbackID. 
•	Link Accessing Service - 
o	To access the ride link, After the companion logs in and then then the parameters of the ride will be sent to Location Service.
o	Feedback link will be sent after the trip ended. That is when the location of the 
•	Feedback Review Service – Used by Admins to review feedback from companions based on TripID and FeedbackID from Ride Database and the feedback fields from Feedback Database.
•	Notification Service – Consists of 2 functions
o	Notify Service which sends out the notification according to  
o	Distance Calculation based on location coordinates between Traveller and Companion and between Traveller and End Location.
•	Ride Audit Service 
o	Travellers can view the rides they have done
o	Admins can view all rides from the Ride Database
•	Location Service has 2 functions – 
o	Stores the realtime location of the Traveller every 5 seconds in the Traveller Realtime Database.
o	Calls an external maps API to get a map view of the traveller location and the coordinates of the companion if ride has not started or the end location coordinates if the ride is in progress

Traveller Realtime Database is updated every 5 seconds to match the current location of the traveller.

Distance Calculation is done in the following way – 
•	We bring in the Traveller realtime database to the Notification service.
•	Then batches of data from the Ride Database is taken at a time and sent to a distance calculation function which can be scaled.
•	The distance calculation function then joins both the databases and calculates the distance between them using the coordinates of Traveller from the Traveller Database and companion coordinates from Ride Database batch. It also takes into account End Location Coordinates from the batch and calculates the distance between end location and traveller if the Trip Start Time field is not NULL. 
•	Nearby Notification is sent when the Trip Start Time is NULL and the distance calculated between Traveller and companion is less than 100m. When this distance becomes 0, the Trip Start Time is set.
•	Trip End Notification is sent when Trip Start Time is not NULL and distance between Traveller and End location is 0. When this happens, Trip End time is set and a random feedbackID is generated and link is sent to the companion. 



