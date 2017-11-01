#Instructions to Execute and Compile the Codes
#@author Rachit Shrivastava
#For CSC591 - Game Engine Foundation Homework Assignment 2

---

##Part 1 
	Folder Name - HW2P1
	Steps-
		1. Import the folder as project in Eclipse IDE
		2. This is just a structre of how the game object model looks like and how i will be creating different objects using the GameObjec.java file and Factory.java file


##Part 2
	Folder Name - HW2P2
	Steps-
		1. Import the folder as project in Eclipse IDE
		2. Run the GameServer.java file as application to start the server
		3. Run the GameClient.java file as application to start a client, multiple clients can be run using this same file on a single machine. If you want to test clients on different machines please change the IP address from this file.
		4. For the first time when you run the client there's a java server application also started so the client isnt focused on the screen, please check the tabs at the bottom to locate the processing client for running it the first time.
		5. Once any client is closed exceptions are thrown since the socket and streams are not closed. It would be a better option to shut the server and then start again from step 2 if you want to check any functionality.


##Part 3
	Folder Name - HW2P3
	Steps-
		1. Import the folder as project in Eclipse IDE
		2. Run the GameServer.java file as application to start the server
		3. Run the GameClient.java file as application to start a client, multiple clients can be run using this same file on a single machine. If you want to test clients on different machines please change the IP address from this file.
		4. For the first time when you run the client there's a java server application also started so the client isnt focused on the screen, please check the tabs at the bottom to locate the processing client for running it the first time.
		5. Once any client is closed exceptions are thrown since the socket and streams are not closed. It would be a better option to shut the server and then start again from step 2 if you want to check any functionality.

##Part 4
	Folder Name - HW2P4
	Steps-
		1. Import the folder as project in Eclipse IDE
		2. It has two packages, called string and object. 
		3. The string package contains ServerString.java and ClientString.java, modify some parameters mentioned in ServerString.java file like iterations, no. of client values and then run ServerString.java as application.
		4. Now run ClientString.java as application make sure you run it the number of times you have mentioned the maxClients in Server file. By default it is 2. Your server Console will display the time taken by each client to communicate with server in milliseconds using strings as method for passing info.
		5. The object package contains GameObject files, ServerObject.java and ClientObject.java, modify some parameters mentioned in ServerObject.java file like iterations, maxClients, size of object list and then run ServerObject.java as application.
		6. Now run ClientObject.java as application make sure you run it the number of times you have mentioned the maxClients in Server file. By default is 2 and object size is 100. The server console will display the time taken by each client to communicate with server in milliseconds using objects as method for passing info. 



Please Note - I'm using the same socket number 7172 for all of my projects part files.


--- 
END 
---