=======================================================================
# SNP with MongoDB Project Readme File
=======================================================================


## A. Install Mongodb


1. Download Mongodb from https://www.mongodb.com/download-center#community
2. Extract it at the appropriate directory
3. Update the ~/.bashrc for PATH environment variable
    
    ```sh
    export PATH=$PATH:<mongo_dir>/bin
    ```
4. Create directory for storing the data
    
    ```sh
    mkdir -p ~/mongo_data/db
    ```
5. Start mongodb with command (by default it binds 27017 port)
   
   ```sh
   mongod --dbpath ~/mongo_data/db
   ```
	Its run MongoDB server process in foreground

## B. Build SNP with MongoDB Project

1. Clone the SNP git repository
    
    ```sh
    git clone https://github.com/bio-cdac/SNPdb_MongoDB
    ```

2. ```sh 
   cd SNP
    ```

3. ```sh 
   mvn install
   ```
4. Download the VCF input files vcf.tar.bz2 (696M)
	
	```sh 
    https://drive.google.com/file/d/0B1MMYWhD1Om4OVBLcVBpNGhSalE/view?usp=sharing
    ```
   
4. Extract vcf input files from tar.bz2
   
   ```sh    
   tar -xjvf vcf.tar.bz2   
   ```
   
 ## C. Load VCF data into Mongo DB
 1. __SNPMongodbUtils/target/ChickenSNP-1.0.jar__ is library for query data from mongodb and  __SNPMongodbUtils/target/ChickenSNP-1.0-jar-with-dependencies.jar__ is standalone utility for load the vcf data into mongodb as well as query the mongo db
     
     ```sh
     java -jar SNPMongodbUtils/target/ChickenSNP-1.0-jar-with-dependencies.jar -h
     ```
     ChickenSNP-1.0-jar-with-dependencies.jar has 2 sub commands 
     store and query.
 2. Load the vcf files into Mongodb
 
 	```sh
    java -jar SNPMongodbUtils/target/SNPMongodbUtils/target/ChickenSNP-1.0-jar-with-dependencies.jar store --host localhost --port 27017 --database pcsnp --collection chicken --inputpath /vcf 
    ```
    here __/vcf__ is directory path where all vcf files are there
    
    Note : Its time consume task its may take upto 6 to 7 hrs to load 8 vcf files (7.2 GB)
    
3. To query the MongoDB use query sub command
	
	```sh
	java -jar SNPMongodbUtils/target/ChickenSNP-1.0-jar-with-dependencies.jar query --host localhost --port 27017 --database pcsnp --collection chicken --chromosome 1 --start 1000  --end 100000 -left LineN -left Line6 -right LineC
    ```
 	here -left and -right may come multiple times
 
 
 ## D. Run SNP Web Application
1. Run the Web Application (2 Option)
    1. If Already Wildfly server is there, then deploy the __SNPWebApp.war__ file from SNPMongoWebApp/target/SNPWebApp.war
    2. If Wildfly not there then run directly __SNPMongoWebApp/target/_SNPWebApp-swarm.jar___ file using java -jar command
   	 
   	 ```sh
     java -jar SNPMongoWebApp/target/SNPWebApp-swarm.jar  -Djava.net.preferIPv4Stack=true
     ```
    	
2. After deployment, In the web browser type url below:
  
    [http://localhost:8080/SNP](http://localhost:8080/SNP)
    
	
3. When you run first time the configuration page will come where you have to give information about 
* MongoDB database server Name/IP (localhost)
* MongoDB database port (27017)
* MongoDB database name (pcsnp)
* MongoDB collection/table name (chicken)


4. After feeding above information the SNPWebApp home page will be prompted when you can run it by feeding chromosome information

![SNP Web App with MongoDB](https://raw.githubusercontent.com/bio-cdac/SNPdb_MongoDB/master/mongo.gif)