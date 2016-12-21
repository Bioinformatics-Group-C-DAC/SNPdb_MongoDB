/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.parser.vcf;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.BsonString;
import org.bson.Document;

/**
 *
 * @author ramki
 */
public class VCFLoaderMongoDB {


    private MongoCollection<Document> chickenCollection;

    public VCFLoaderMongoDB(MongoCollection<Document> chickenCollection) {
        this.chickenCollection = chickenCollection;
    }
    
    
//
//    public void init(MongoCollection<Document> chickenCollection) {
//
//
//        this.chickenCollection = chickenCollection;
//    }

    public MongoCollection<Document> getChickenCollection() {
        return chickenCollection;
    }

//    public void insert(VCFBean vcfb, String vcfChickenLine) {
//
//        Document innerDoc = new Document("ALT", vcfb.getAlt());
//        Document lineDoc = new Document(vcfChickenLine, innerDoc);
//
//        Document document = new Document("_id", new BsonString(vcfb.getChromosome() + ":" + vcfb.getPosition()))
//                .append("Chromosome", vcfb.getChromosome())
//                .append("Position", vcfb.getPosition())
//                .append("ID", vcfb.getId())
//                .append("REF", vcfb.getRef())
//                .append("Lines", Arrays.asList(lineDoc));
//        collection.insertOne(document);
//
//    }
    public void display() {
        for (Document doc : chickenCollection.find()) {
            System.out.println(doc.toJson());
        }
    }

//    public void insert(List<VCFBean> beans, String vcfChickenLine, int count) {
//        List<Document> listOfDocument = new ArrayList<>(count);
//        for (int i = 0; i < count; i++) {
//            VCFBean vcfb = beans.get(i);
//            Document innerDoc = new Document("ALT", vcfb.getAlt());
//            Document lineDoc = new Document(vcfChickenLine, innerDoc);
//            // System.out.println(new BsonString(vcfb.getChromosome() + ":" + vcfb.getPosition()));
//            Document document = new Document("_id", new BsonString(vcfb.getChromosome() + ":" + vcfb.getPosition()))
//                    .append("Chromosome", vcfb.getChromosome())
//                    .append("Position", vcfb.getPosition())
//                    .append("ID", vcfb.getId())
//                    .append("REF", vcfb.getRef())
//                    .append("Lines", Arrays.asList(lineDoc));
//            listOfDocument.add(document);
//        }
//        collection.insertMany(listOfDocument);
//
//    }
    public void update(List<VCFBean> beans, String vcfChickenLine, int count) {

        int updateCounter = 0;
        int skipCounter = 0;
        List<Document> insertListOfDocument = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            VCFBean vcfb = beans.get(i);

            Document innerDoc = new Document("ALT", vcfb.getAlt());
            Document lineDoc = new Document(vcfChickenLine, innerDoc);
            //UpdateResult updateOne = collection.updateOne(new Document("_id",new BsonString(vcfb.getChromosome() + ":" + vcfb.getPosition())), Updates.push("Lines", lineDoc));
            Document id = new Document("_id", new BsonString(vcfb.getChromosome() + ":" + vcfb.getPosition()));
            Document target = chickenCollection.find(id).first();
            if (target != null) {
                List<Document> lineArray = (List<Document>) target.get("Lines");
                if (!lineArray.contains(lineDoc)) {
                    lineArray.add(lineDoc);
                    updateCounter++;
                    // System.out.println(target.toJson());
                    UpdateResult replaceOne = chickenCollection.replaceOne(id, target);

                } else {
                    skipCounter++;
                }

            } else {
                Document document = new Document("_id", new BsonString(vcfb.getChromosome() + ":" + vcfb.getPosition()))
                        .append("Chromosome", vcfb.getChromosome())
                        .append("Position", vcfb.getPosition())
                        .append("ID", vcfb.getId())
                        .append("REF", vcfb.getRef())
                        .append("Lines", Arrays.asList(lineDoc));
                insertListOfDocument.add(document);

            }

        }
        if (insertListOfDocument.size() > 0) {

            chickenCollection.insertMany(insertListOfDocument);
        }

//        System.out.println("Inserted : " + insertListOfDocument.size());
//        System.out.println("Updated  : " + updateCounter);
//        System.out.println("Skipped  : " + skipCounter);
//        System.out.println("Total Count = " + (insertListOfDocument.size() + updateCounter + skipCounter));
    }

  

}
