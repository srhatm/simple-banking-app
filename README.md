# simple-banking-app
A simple banking app backend developed with spring boot

This project is a simple backend implementation of a banking application. The project is developed with spring boot. 
It consists of restful services, hibernate and in memory database(hsqldb). Also integration tests are implemented. 

The restful services are:
* /rest/banking/add to create a client
* /rest/banking/clients to get the clients
* /rest/banking/login to login the client
* /rest/banking/deposit to deposit money for the specied client
* /rest/banking/withdraw to withdraw money for the specied client
* /rest/banking/balanceStatement/{bankClientId} to get the balance and statement for the specied client
