/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.mongodb.client;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 *
 * @author sandeep
 */
public class MongoClientSingleton {

    private static MongoClient mongoClient = null;

    // Private constructor prevents instantiation from other classes
    private MongoClientSingleton() {
    }

    /**
     * SingletonHolder is loaded on the first execution of
     * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
     * not before.
     */
    public static synchronized MongoClient getInstance(String host, int port) {
        if (mongoClient == null) {
            mongoClient = new MongoClient(new ServerAddress(host, port));

        }
        return mongoClient;

    }
}
