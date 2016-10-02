# fileuploader

This app is for uploading, listing and downloading files into/from a server. The files uploaded to the server are performed by slicing them into chunks of 1 MB. To Download them, the server will put them together and send as a single file. You can follow the status of a file being uploaded by listing which brings the owner, name of file, time spent in seconds, amount of chunks and the status.

CONFIG:

In order to run this app you MUST setup a dir which will serve as database for the whole app.
1- You can setup it from the root folder you have cloned/extracted the app at: "fileuploader/src/main/resources/appsets.properties"
2- Change the property "files.storage" for a directory with read/write permissions.

NOTE:

This app were developed using "apache-tomcat-8.0.37" and JDK 7.




