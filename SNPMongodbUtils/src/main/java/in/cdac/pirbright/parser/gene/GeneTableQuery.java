/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.parser.gene;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author ramki
 */
public class GeneTableQuery {

    public List<GeneBean> retriveVCFRecords(MongoDatabase database, String chromosome, long startPosition, long endPosition) {

        List<GeneBean> listOfGeneBeans = new ArrayList<>();

        MongoCollection<Document> genetableCollection = database.getCollection("gentable");

        Bson filter = Filters.and(Filters.eq("chromosome", chromosome), Filters.gte("start", startPosition)); //  Filters.lte("end", endPosition)

        FindIterable<Document> result = genetableCollection.find(filter);

        for (Document document : result) {

            GeneBean geneBean = this.processGeneDocument(document);

            if (geneBean != null) {
                listOfGeneBeans.add(geneBean);
            }

        }
        return listOfGeneBeans;

    }

    private GeneBean processGeneDocument(Document document) {
        GeneBean bean = new GeneBean();

        bean.setGeneId(document.getString("_id"));
        bean.setChromosome(document.getString("chromosome"));
        bean.setStartPosition(document.getLong("start"));
        bean.setEndPosition(document.getLong("end"));

        return bean;

    }
}
