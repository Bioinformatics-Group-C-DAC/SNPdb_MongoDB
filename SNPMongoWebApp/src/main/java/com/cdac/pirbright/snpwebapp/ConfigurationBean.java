/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdac.pirbright.snpwebapp;

import com.cdac.pirbright.snpwebapp.config.Config;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author bioinfo
 */
@Named(value = "configurationBean")
@RequestScoped
public class ConfigurationBean  {

    /**
     * Creates a new instance of ConfigurationBean
     */
    public ConfigurationBean() {
    }
    
    private String MongodbIP;
    private String MongoPort;
    private String Database;
    private String collectionName;

    public String getMongodbIP() {
        return MongodbIP;
    }

    public void setMongodbIP(String MongodbIP) {
        this.MongodbIP = MongodbIP;
    }

    public String getMongoPort() {
        return MongoPort;
    }

    public void setMongoPort(String MongoPort) {
        this.MongoPort = MongoPort;
    }

    public String getDatabase() {
        return Database;
    }

    public void setDatabase(String Database) {
        this.Database = Database;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
      
       
    public String submit(){
        System.out.println("Ip : "+getMongodbIP()+" Port : "+getMongoPort()+" Database : "+getDatabase()+" Collection : "+getCollectionName());
        
        
     
        String userHome = System.getProperty("user.home");
       String basePath = userHome + File.separator + Config.CONFIG_PATH;
       
       File file = new File(basePath);
       FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigurationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       Properties p =new Properties();
       p.setProperty(Config.MONGO_DB_HOST, getMongodbIP());
       p.setProperty(Config.MONGO_DB_PORT, getMongoPort());
       p.setProperty(Config.MONGO_DB_DATABASE_NAME, getDatabase());
       p.setProperty(Config.MONGO_DB_COLLECTION_NAME, getCollectionName());
       
        try {
            p.store(fileOutputStream, "");
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "home";
    }
}
