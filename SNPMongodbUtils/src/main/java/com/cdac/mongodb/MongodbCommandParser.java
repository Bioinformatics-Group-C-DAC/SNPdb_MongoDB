/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdac.mongodb;

import com.beust.jcommander.JCommander;
import com.cdac.mongodb.cmd.MongoDBInfo;
import com.cdac.mongodb.cmd.MongodbDumpCommand;
import com.cdac.mongodb.cmd.MongodbQueryCommand;
import com.cdac.mongodb.query.SNPChickenQuery;
import com.cdac.pirbright.snpwebapp.OutputSNPBean;
import com.cdac.vcfparser.StoreVCFToMongoDB;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ramki
 */
public class MongodbCommandParser {

    public static void main(String[] args) throws InterruptedException {
        MongodbCommandParser main = new MongodbCommandParser();
        JCommander jc = new JCommander();

        MongodbDumpCommand store = new MongodbDumpCommand();
        jc.addCommand(MongodbCommand.STORE_COMMAND, store);
        MongodbQueryCommand query = new MongodbQueryCommand();
        jc.addCommand(MongodbCommand.QUERY_COMMAND, query);

        //jc.parse( "store","-h","biograph","-d", "db1","-c", "chick","-i","/home/");
        try {
            jc.parse(args);
            //  jc.parse("store", "-i", "/home/", "-e");

            if (MongodbCommand.STORE_COMMAND.equals(jc.getParsedCommand())) {
                MongoDBLoader mongoDBLoader = getMongoDBLoader(store.getMongoDBInfo());

                StoreVCFToMongoDB parserMain = new StoreVCFToMongoDB();
                parserMain.submit(store, mongoDBLoader);

            } else if (MongodbCommand.QUERY_COMMAND.equals(jc.getParsedCommand())) {
                MongoDBLoader mongoDBLoader = getMongoDBLoader(store.getMongoDBInfo());

                SNPChickenQuery chickenQuery = new SNPChickenQuery(mongoDBLoader);

//                List<String> leftList = new ArrayList<>();
//                List<String> rightList = new ArrayList<>();
                List<String> leftList = query.getLeft();
                List<String> rightList = query.getRight();

                // leftList.add("Line15");
                //leftList.add("Linep");
                // rightList.add("Line7");
                System.out.println("Query Chromosome : " + query.getChromosome());
                System.out.println("Query Start      : " + query.getStart());
                System.out.println("Query End        : " + query.getEnd());
                System.out.println("Left Side        : " + query.getLeft());
                System.out.println("Right Side       : " + query.getRight()+ "\n");

                long startTime = System.currentTimeMillis();
                List<OutputSNPBean> retriveVCFRecords = chickenQuery.retriveVCFRecords(query.getChromosome(), query.getStart(), query.getEnd(), leftList, rightList);
                for (OutputSNPBean retriveVCFRecord : retriveVCFRecords) {
                    System.out.println(retriveVCFRecord);
                }
                System.out.println(retriveVCFRecords.size());
                long endTime = System.currentTimeMillis();

                System.out.println("Time taken : " + (endTime - startTime) + " ms");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            jc.usage();

        }

    }

    private static MongoDBLoader getMongoDBLoader(MongoDBInfo mongoDBInfo) {
        System.out.println("MongoDB Database Host   : " + mongoDBInfo.getHost());
        System.out.println("MongoDB Database Port   : " + mongoDBInfo.getPort());
        System.out.println("MongoDB Database Name   : " + mongoDBInfo.getDatabase());
        System.out.println("MongoDB Collection Name : " + mongoDBInfo.getCollection() + "\n\n");

        MongoDBLoader mongoDBLoader = new MongoDBLoader();
        mongoDBLoader.init(mongoDBInfo.getHost(), mongoDBInfo.getPort(), mongoDBInfo.getDatabase(), mongoDBInfo.getCollection());
        return mongoDBLoader;
    }
}
