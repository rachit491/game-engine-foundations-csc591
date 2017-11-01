#Instructions to Execute and Compile the Codes
#@author Rachit Shrivastava
#For CSC591 - Game Engine Foundation Homework Assignment 1

---

##Section 1 
	Folder Name - UsingProcessing
	Steps-
		1. Import the folder as project in Eclipse
		2. You would see a referenced libraries drop down in package explorer, if you don't see that follow instructions to import Processing Core from  https://processing.org/tutorials/eclipse/ 
		3. Run the HelloProcessing.java


##Section 2
	Folder Name - Section2
	Steps-
		1. The folder contains ForkExample1.java, ForkExample2.java, ForkExample3.java, and ForkExample4.java
		2. Use terminal to compile and run the files
		3. javac ForkExample1.java
		4. java ForkExample
		5. Continue this for all the 4 files, programs 1 and 3 will be stuck and won't execute completely. They meant to function like that. Just use Control + C to break the operation anytime


##Section 3
	Folder Name - Section3
	Steps-
		1. You can import the folder as project in Eclipse and Run the Server.java file first and then 3 times Client.java file since there are fixed number of clients for this program.
		2. Or you can open the src sub-folder in terminal and compile & run the files. Make sure you open clients and server in separate terminal tabs.


##Section 4
	Folder Name - Section4
	Steps-
		1. You can import the folder as project in Eclipse and Run the GEServer.java file first and then any number of times GClient.java file since there are no fixed number of clients for this program.
		2. The server console will have the Socket addresses for the client being connected. 
		3. Whatever is typed on the client consoles is sent to server and the server responds back with the UpperCase for the client string values.
		4. If you choose terminal to run this file, you get option to modify the port number and the ip address of server for the client can be connected to. 
		5. Compiling is similar to previous menthod. All you need to do is while running the server file type "java GEServer 7647" where 7647 is some random port number which the server will be listening to.
		6. For the client do the same, but it also takes in ip address of the server if hosted on different machine as input along with the server port number, type "java GClient 7647 192.168.10.1" where 7647 is same port number as for the server and second parameter is the ip address of the server, by default which is set to 127.0.0.1



##Section 5
	Folder Name - Section5
	Steps-
		1. The steps to run this section is same as section 4 instructions, just the file names differ.
		2. Instead of GEServer use "AsyncServer" and "AsyncClient" instead of GClient



--- 
END 
---