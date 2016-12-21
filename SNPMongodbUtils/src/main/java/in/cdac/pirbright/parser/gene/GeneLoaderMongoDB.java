/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.parser.gene;

import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author sandeep
 */
public class GeneLoaderMongoDB {

    private MongoCollection<Document> geneCollection;

    public GeneLoaderMongoDB(MongoCollection<Document> geneCollection) {
        this.geneCollection = geneCollection;
    }

    
   
    public MongoCollection<Document> getGeneCollection() {
        return geneCollection;
    }

    public void insert(GeneBean geneb) {

        Document geneDocument = new Document("_id", geneb.getGeneId())
                .append("chromosome", geneb.getChromosome())
                .append("start", geneb.getStartPosition())
                .append("end", geneb.getEndPosition());

        geneCollection.insertOne(geneDocument);

    }

    public void displayGenes() {
        for (Document doc : geneCollection.find()) {
            System.out.println(doc.toJson());
        }
    }

    public void insert(List<GeneBean> beans, int count) {

        List<Document> listOfGeneDocument = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            GeneBean geneb = beans.get(i);

            Document geneDocument = new Document("_id", geneb.getGeneId())
                    .append("chromosome", geneb.getChromosome())
                    .append("start", geneb.getStartPosition())
                    .append("end", geneb.getEndPosition());
            listOfGeneDocument.add(geneDocument);
        }
        geneCollection.insertMany(listOfGeneDocument);

    }

}
