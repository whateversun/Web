# Web
We have a simple web service that exposes some dummy people data (id, name, age, number).  
The details of the service are below.
The goal is to make a program (C#, Java, or Javascript) that outputs the 5 youngest users with 
valid us telephone numbers sorted by name

 

service endpoint

https://appsheettest1.azurewebsites.net/sample/

 

method

list


notes

This method will return an array of up to 10 user id's.  
If there are more than 10 results the response will also contain a token that can be used to retrieve the next set of results.  
This optional token can be passed as a query string parameter 
ex https://appsheettest1.azurewebsites.net/sample/list or https://appsheettest1.azurewebsites.net/sample/list?token=b32b3

 
method

detail/{user id}

 

notes

This method will returns the full details for a given user ex https://appsheettest1.azurewebsites.net/sample/detail/21

 
