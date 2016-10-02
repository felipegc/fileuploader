# fileuploader

This app is for uploading, listing and downloading files into/from a server. The files uploaded to the server are performed by slicing them into chunks of 1 MB. To Download them, the server will put them together and send as a single file. You can follow the status of a file being uploaded by listing which brings the owner, name of file, time spent in seconds, amount of chunks and the status.

CONFIG:

In order to run this app you MUST setup a dir which will serve as database for the whole app.
1- You can setup it from the root folder you have cloned/extracted the app at: "fileuploader/src/main/resources/appsets.properties"
2- Change the property "files.storage" for a directory with read/write permissions.

NOTE:

This app were developed using "apache-tomcat-8.0.37" and JDK 7. 
The URL to have access to the app after deployed is "http://{address:port}/fileuploader". If you deploy it locally you should be able to open the app by typing the following url in your browser: "http://localhost:8080/fileuploader/".

You can have access through the API by using swagger. The URL is under "http://{address:port}/fileuploader/rest/swagger.json". If you deploy locally you can hit the URL as following: "http://localhost:8080/fileuploader/rest/swagger.json" 




