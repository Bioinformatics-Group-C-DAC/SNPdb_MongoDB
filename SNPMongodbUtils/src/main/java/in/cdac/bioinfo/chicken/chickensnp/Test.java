/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.bioinfo.chicken.chickensnp;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import com.mongodb.client.model.Sorts;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.currentDate;
import static com.mongodb.client.model.Updates.set;
import java.util.Arrays;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author ramki
 */
public class Test {

    public static void main(String[] args) {
        //  MongoClient mongoClient = new MongoClient("biograph" , 27017 );

        MongoClientURI connectionString = new MongoClientURI("mongodb://biograph:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        mongoClient.getDatabaseNames().forEach(System.out::println);
        for (String name : mongoClient.listDatabaseNames()) {
            System.out.println(name);
        }

        MongoDatabase database = mongoClient.getDatabase("ramki");

        for (String name : database.listCollectionNames()) {
            System.out.println(name);
        }

        MongoCollection<Document> collection = database.getCollection("test");

        /* 
        {
            "name" : "MongoDB",
            "type" : "database",
            "count" : 1,
            "versions": [ "v3.2", "v3.0", "v2.6" ],
            "info" : { x : 203, y : 102 }
        }
         */
        Document doc = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));

        collection.insertOne(doc);
        System.out.println(collection.count());

        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

//        List<Document> documents = new ArrayList<Document>();
//        for (int i = 0; i < 100; i++) {
//            documents.add(new Document("i", i));
//        }
//        collection.insertMany(documents);
        myDoc = collection.find(eq("i", 71)).first();
        System.out.println(myDoc.toJson());

        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };
        //collection.find(gt("i", 50)).forEach(printBlock);
        collection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);

        for (Document index : collection.listIndexes()) {
            System.out.println(index.toJson());
        }
        
        
        collection.find(and(gte("stars", 2), lt("stars", 5), eq("categories", "Bakery")))
                .projection(fields(include("name", "stars", "categories"), excludeId()))
                .forEach(printBlock);
        
        
       collection.find().sort(Sorts.ascending("name"));
       
       collection.updateOne(
                eq("_id", new ObjectId("57506d62f57802807471dd41")),
                combine(set("stars", 1), set("contact.phone", "228-555-9999"), currentDate("lastModified")));

    }

}
