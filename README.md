# MasterAccountHistory
A simple SpringBoot application which fetch the transaction from a site mentioned below and evaluate the account balance. This projects also exposed Rest APis to perform get account balance for different site. 
Maven is used as build tool in this project.

**Instruction to run the project.**

Import the project as Maven project. Navigate to ../EOS-History/ where pom.xml exists.

Build as mvn clean install (.jar will be created in target folder)

Navigate to target folder and open cmd/terminal.

Enter the command " java -jar filename.jar"

Hit API from postman "http://localhost:8080/api/user/oxbhhozhdvew" 


# Site supported 
 1).https://eos.greymass.com
 
 2).https://app.dragonglass.me
 
 3).https://livenet.xrpl.org


**Get Account balance for EOS account**: -to get detials for EOS account, you need to pass the account name in the API.
**Example**

Rest API: **http://localhost:8080/api/user/oxbhhozhdvew**


Response 

{

    "availableFund": 1048.6974,
    "receivedFund": 25493.977,
    "sendFund": 25749.69740000015,
    "cpuStaked": 29312.36479999998,
    "netStaked": 3.7091999999999987,
    "totalTillNow": 30364.771399999983,
    "totalTillEndOf2020": 1304.4178000001511
    
}

**Get Account balance for DragonGlass account**: -to get detials for dragonGlass account, you need to pass the account name in the API.
**Example**

Rest API: **http://localhost:8080/api/dragon/user/0.0.54091**

Response 

{

    "totalNow": 231.879571,
    "receivedFundTill2020": 29.0,
    "sentFundTill2020": 3.93921655,
    "deltaTill2020": 25.06078345,
    "totalTillEndOf2020": 25.06078345,
    "asOf": "2021-08-11T13:31:50.854Z"
    
}

**Get Account balance for livenet account**: -to get detials for livenet account, you need to pass the account name in the API.
**Example**

Rest API: **localhost:8080/api/livenet/user/rD4G6gtD2KwHqsRf7pcyA8r1neUzXT61ix**

Response 

{
   
    "sentFundAfter2020": 976525.2475,
    "receivedFundAfter2020": 2130144.0000010002,
    "totalTillNow": 1538708.167595,
    "deltaAfter2020": 1153618.7525010002,
    "totalTillEndOf2020": 385089.4150939998,
    "asOf": "2021-08-11T17:29:58.980"  
}
