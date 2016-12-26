/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.main;

import in.cdac.pirbright.parser.vcf.VCFLoaderMongoDB;
import com.beust.jcommander.JCommander;
import in.cdac.pirbright.parser.gene.GeneLoaderMongoDB;
import in.cdac.pirbright.parser.gene.GeneFilesToMongoDBStore;
import in.cdac.pirbright.commands.cli.MongoDBInfo;
import in.cdac.pirbright.commands.cli.MongodbVcfStoreCommand;
import in.cdac.pirbright.commands.cli.MongodbGeneStoreCommand;
import in.cdac.pirbright.commands.cli.MongodbQueryCommand;
import in.cdac.pirbright.chicken.query.SNPChickenQuery;
import in.cdac.pirbright.snpwebapp.OutputSNPBean;
import in.cdac.pirbright.parser.vcf.VCFFilesToMongoDBStore;
import in.cdac.pirbright.commands.core.SNPCommand;
import in.cdac.pirbright.mongodb.client.MongoClientSingleton;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author ramki
 */
public class CommandParser {

    public static void main(String[] args) {

        JCommander jc = new JCommander();

        MongodbVcfStoreCommand vcfstore = new MongodbVcfStoreCommand();
        MongodbQueryCommand vcfquery = new MongodbQueryCommand();
        MongodbGeneStoreCommand genestore = new MongodbGeneStoreCommand();

        jc.addCommand(SNPCommand.VCF_STORE_COMMAND, vcfstore);
        jc.addCommand(SNPCommand.VCF_QUERY_COMMAND, vcfquery);
        jc.addCommand(SNPCommand.GENE_STORE_COMMAND, genestore);

        try {
            //jc.parse( "store","-h","biograph","-d", "db1","-c", "chick","-i","/home/");
            //  jc.parse("store", "-i", "/home/", "-e");
            //
           // vcfstore.setProcessors(1);
            jc.parse(args);

            if (SNPCommand.VCF_STORE_COMMAND.equals(jc.getParsedCommand())) {
                MongoDBInfo mongoDBInfo = vcfstore.getMongoDBInfo();
                MongoCollection<Document> mongoDBCollection = getMongoDBCollection(mongoDBInfo);

                VCFLoaderMongoDB vcflmdb = new VCFLoaderMongoDB(mongoDBCollection);
                VCFFilesToMongoDBStore parserMain = new VCFFilesToMongoDBStore();
                parserMain.submit(vcfstore, vcflmdb);

            } else if (SNPCommand.GENE_STORE_COMMAND.equals(jc.getParsedCommand())) {

                MongoDBInfo mongoDBInfo = genestore.getMongoDBInfo();
                MongoCollection<Document> mongoDBCollection = getMongoDBCollection(mongoDBInfo);

                GeneLoaderMongoDB glmdb = new GeneLoaderMongoDB(mongoDBCollection);
                GeneFilesToMongoDBStore parserMain = new GeneFilesToMongoDBStore();
                parserMain.submit(genestore, glmdb);

            } else if (SNPCommand.VCF_QUERY_COMMAND.equals(jc.getParsedCommand())) {

                MongoDBInfo mongoDBInfo = vcfquery.getMongoDBInfo();

                MongoCollection<Document> mongoDBCollection = getMongoDBCollection(mongoDBInfo);

                if (vcfquery.getGeneId() != null) {
                    mongoDBInfo.setCollection("genetable");
                    MongoCollection<Document> genetableCollection = getMongoDBCollection(mongoDBInfo);
                    Document geneDocument = genetableCollection.find(Filters.eq("_id", vcfquery.getGeneId())).first();
                    vcfquery.setChromosome(geneDocument.getString("chromosome"));
                    vcfquery.setStart(geneDocument.getLong("start"));
                    vcfquery.setEnd(geneDocument.getLong("end"));

                }

                VCFLoaderMongoDB vcflmdb = new VCFLoaderMongoDB(mongoDBCollection);

                processVcfQuery(vcfquery, vcflmdb);
            } else {
                jc.usage();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            jc.usage();

        }

    }

    private static MongoCollection<Document> getMongoDBCollection(MongoDBInfo mongoDBInfo) {
        System.out.println("MongoDB Database Host   : " + mongoDBInfo.getHost());
        System.out.println("MongoDB Database Port   : " + mongoDBInfo.getPort());
        System.out.println("MongoDB Database Name   : " + mongoDBInfo.getDatabase());
        System.out.println("MongoDB Collection Name : " + mongoDBInfo.getCollection() + "\n\n");

        MongoClient mongoClient = MongoClientSingleton.getInstance(mongoDBInfo.getHost(), mongoDBInfo.getPort());
        MongoDatabase database = mongoClient.getDatabase(mongoDBInfo.getDatabase());
        MongoCollection<Document> collection = database.getCollection(mongoDBInfo.getCollection());
        return collection;
    }

//    private static GeneLoaderMongoDB getMongoDBGeneLoader(MongoDBInfo mongoDBInfo) {
//        System.out.println("MongoDB Database Host   : " + mongoDBInfo.getHost());
//        System.out.println("MongoDB Database Port   : " + mongoDBInfo.getPort());
//        System.out.println("MongoDB Database Name   : " + mongoDBInfo.getDatabase());
//        System.out.println("MongoDB Collection Name : " + mongoDBInfo.getCollection() + "\n\n");
//
//        GeneLoaderMongoDB mongoDBGeneLoader = new GeneLoaderMongoDB();
//        mongoDBGeneLoader.init(mongoDBInfo.getHost(), mongoDBInfo.getPort(), mongoDBInfo.getDatabase(), mongoDBInfo.getCollection());
//        return mongoDBGeneLoader;
//    }
    private static void processVcfQuery(MongodbQueryCommand vcfquery, VCFLoaderMongoDB mongoDBLoader) {

        SNPChickenQuery chickenQuery = new SNPChickenQuery(mongoDBLoader);

//                List<String> leftList = new ArrayList<>();
//                List<String> rightList = new ArrayList<>();
        List<String> leftList = vcfquery.getLeft();
        List<String> rightList = vcfquery.getRight();

        // leftList.add("Line15");
        //leftList.add("Linep");
        // rightList.add("Line7");
        System.out.println("Query Chromosome : " + vcfquery.getChromosome());
        System.out.println("Query Start      : " + vcfquery.getStart());
        System.out.println("Query End        : " + vcfquery.getEnd());
        System.out.println("Left Side        : " + vcfquery.getLeft());
        System.out.println("Right Side       : " + vcfquery.getRight() + "\n");

        long startTime = System.currentTimeMillis();
        List<OutputSNPBean> retriveVCFRecords = chickenQuery.retriveVCFRecords(vcfquery.getChromosome(), vcfquery.getStart(), vcfquery.getEnd(), leftList, rightList);
        for (OutputSNPBean retriveVCFRecord : retriveVCFRecords) {
            System.out.println(retriveVCFRecord);
        }
        System.out.println(retriveVCFRecords.size());
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken : " + (endTime - startTime) + " ms");
    }
}
