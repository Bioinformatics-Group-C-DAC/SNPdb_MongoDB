/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.commands.cli;

import com.beust.jcommander.Parameter;

/**
 *
 * @author ramki
 */
public class MongoDBInfo {
     @Parameter(names = {"--host", "-h"}, description = "MogoDB Server IP address")
    private String host = "localhost";

    @Parameter(names = {"--port", "-p"}, description = "MogoDB Server Port")
    private int port = 27017;

    @Parameter(names = {"--database", "-d"}, description = "MogoDB Database Name")
    private String database = "pcsnp";

    @Parameter(names = {"--collection", "-c"}, description = "MogoDB Collection/Table Name")
    private String collection = "chicken";

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    
    @Override
    public String toString() {
        return "MongoDBInfo{" + "host=" + host + ", port=" + port + ", database=" + database + ", collection=" + collection + '}';
    }
    
    
    
}
