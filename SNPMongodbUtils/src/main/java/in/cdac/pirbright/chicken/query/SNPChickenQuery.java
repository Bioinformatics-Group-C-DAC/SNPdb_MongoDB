/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.chicken.query;

import com.mongodb.MongoClient;
import in.cdac.pirbright.parser.vcf.VCFLoaderMongoDB;
import in.cdac.pirbright.snpwebapp.OutputSNPBean;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import in.cdac.pirbright.mongodb.client.MongoClientSingleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author ramki
 */
public class SNPChickenQuery {

    private MongoCollection<Document> collection;

    private String tab = "\t";
    // String nbsp = "&amp;nbsp";

    List<String> tempLeftList = new ArrayList<>();
    List<String> tempRightList = new ArrayList<>();

    public SNPChickenQuery(VCFLoaderMongoDB mongoDBLoader) {
        collection = mongoDBLoader.getChickenCollection();
    }

    public List<OutputSNPBean> retriveVCFRecords(MongoDatabase database, String geneId, List<String> leftList, List<String> rightList) {

        MongoCollection<Document> genetableCollection = database.getCollection("gentable");
        Document geneDocument = genetableCollection.find(Filters.eq("_id", geneId)).first();
         System.out.println("Gene Id : "+geneId+"\t Gene Doc : "+ geneDocument);
        if(geneDocument==null){
            return null;
        }
        String chromosome = geneDocument.getString("chromosome");
        long start = geneDocument.getLong("start");
        long end = geneDocument.getLong("end");
        System.out.println("Gene Id : "+geneId+"\t chromosome : "+chromosome+"\t start : "+start+"\t end : "+end);
        return retriveVCFRecords(chromosome, start, end, leftList, rightList);

    }

    public List<OutputSNPBean> retriveVCFRecords(String chromosomeName, long position1, long position2, List<String> leftList, List<String> rightList) {
        
        Collections.sort(leftList);
        Collections.sort(rightList);

        long start;
        long end;

        if (position1 > position2) {
            end = position1;
            start = position2;
        } else {

            start = position1;
            end = position2;
        }

        String chromosome = chromosomeName;

        Set<String> unionLineSet = new HashSet<>();

        unionLineSet.addAll(leftList);
        unionLineSet.addAll(rightList);

        Set<String> leftSet = new HashSet<>();
        Set<String> rightSet = new HashSet<>();

        Bson filter = Filters.and(Filters.eq("Chromosome", chromosome), Filters.gte("Position", start), Filters.lte("Position", end));

        FindIterable<Document> result = this.collection.find(filter);

        
        List<OutputSNPBean> listOfVCFStrings = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("Chromo").append(tab).append("Position").append(tab).append("dbSNPID").append(tab).append("REF").append(tab);
        sb.append(String.join(",", leftList));
        sb.append("#");

        sb.append(String.join(",", rightList));

        //  listOfVCFStrings.add(sb.toString());
        for (Document document : result) {

            OutputSNPBean resultSNPBean = this.processDocument(document, leftList, rightList, unionLineSet, leftSet, rightSet);

            if (resultSNPBean != null) {
                listOfVCFStrings.add(resultSNPBean);
            }

        }

        return listOfVCFStrings;

    }

    private OutputSNPBean processDocument(Document document, List<String> leftList, List<String> rightList, Set<String> unionLineSet, Set<String> leftSet, Set<String> rightSet) {
        String chromosome = document.getString("Chromosome");
        long position = document.getInteger("Position");
        String id = document.getString("ID");
        String ref = document.getString("REF");

        OutputSNPBean bean = new OutputSNPBean();

        bean.setChromosome(chromosome);
        bean.setChromosome_Position(position);
        bean.setRecordID(id);
        bean.setRef(ref);
        // StringBuilder sb = new StringBuilder();
        // sb.append(chromosome).append(tab).append(position).append(tab).append(id).append(tab).append(ref).append(tab);

        leftSet.clear();
        rightSet.clear();
        List<Document> linesDocuments = (List<Document>) document.get("Lines");
        //  String reference = document.getString("REF");

        for (String linesString : leftList) {
            boolean exist = false;
            for (Document lineDocument : linesDocuments) {
                Document altDocument = (Document) lineDocument.get(linesString);
                if (altDocument != null) {
                    exist = true;
                    leftSet.addAll((List<String>) altDocument.get("ALT"));
                    break;
                }

            }
            if (exist == false) {
                leftSet.add(ref);
            }
        }
        for (String linesString : rightList) {
            boolean exist = false;
            for (Document lineDocument : linesDocuments) {
                Document altDocument = (Document) lineDocument.get(linesString);
                if (altDocument != null) {
                    exist = true;
                    rightSet.addAll((List<String>) altDocument.get("ALT"));
                    break;
                }

            }
            if (exist == false) {
                rightSet.add(ref);
            }
        }
        boolean dnaExists = false;
        for (String dnaChar : leftSet) {
            if (rightSet.contains(dnaChar)) {
                dnaExists = true;
                break;
            }

        }
        if (!dnaExists) {

            Map<String, String> map = new HashMap<>();
            //StringBuilder right = new StringBuilder();

            for (Document lineDocument : linesDocuments) {
                Set lineSet = lineDocument.keySet();
                String lineNameString = (String) lineSet.iterator().next();

                boolean containsLinesString = unionLineSet.contains(lineNameString);

                if (containsLinesString) {
                    Document altDocument = (Document) lineDocument.get(lineNameString);
                    List<String> altStringList = (List<String>) altDocument.get("ALT");

                    String altString = String.join(",", altStringList);
                    map.put(lineNameString, altString);

                }

            }
            tempLeftList.clear();
            tempRightList.clear();

            for (String key : leftList) {
                String value = map.get(key);
                if (value == null) {
                    value = ref;
                }
                tempLeftList.add("{" + value + "}");
                //sb.append(value).append(",");

            }
            bean.setSetOne(String.join(",", tempLeftList));

            for (String key : rightList) {
                String value = map.get(key);
                if (value == null) {
                    value = ref;
                }
                tempRightList.add("{" + value + "}");
                //sb.append(value).append(",");

            }

            bean.setSetTwo(String.join(",", tempRightList));
            return bean;

            //  System.out.println("Matching Print Records :" +leftSet+"=="+rightSet);
        } else {
            // System.out.println("Wrong Print Records :" +leftSet+"=="+rightSet);
        }
        return null;

    }

    public static void main(String[] args) {
        
         MongoClient mongoClient = MongoClientSingleton.getInstance("biograph", 27017);
        MongoDatabase database = mongoClient.getDatabase("pcsnp");
        MongoCollection<Document> collection = database.getCollection("chicken");
        VCFLoaderMongoDB mongoDBLoader = new VCFLoaderMongoDB(collection);
       
        SNPChickenQuery main = new SNPChickenQuery(mongoDBLoader);

        List<String> leftList = new ArrayList<>();
        List<String> rightList = new ArrayList<>();

        leftList.add("Line15");
        //leftList.add("Linep");

        rightList.add("Line7");
        

        long startTime = System.currentTimeMillis();
        
       // List<OutputSNPBean> retriveVCFRecords = main.retriveVCFRecords("324234", 18882796, 18931965, leftList, rightList);
         List<OutputSNPBean> retriveVCFRecords = main.retriveVCFRecords(database,"hgjkhk", leftList, rightList);
        System.out.println(retriveVCFRecords.size());
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken : " + (endTime - startTime) + " ms");
        for (OutputSNPBean retriveVCFRecord : retriveVCFRecords) {
            System.out.println(retriveVCFRecord);
        }
        //  main.close();

    }
}
