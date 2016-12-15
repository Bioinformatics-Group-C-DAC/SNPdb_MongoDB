/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdac.vcfparser;

import com.cdac.mongodb.MongoDBLoader;
import com.cdac.mongodb.cmd.MongodbDumpCommand;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ramki
 */
public class StoreVCFToMongoDB {

    public void submit(MongodbDumpCommand command, MongoDBLoader mongoDBLoader) {
        ExecutorService executorService = Executors.newFixedThreadPool(command.getProcessors());

        File basePathFile = new File(command.getPath());

        if (basePathFile.isDirectory()) {
            for (File vcfFile : basePathFile.listFiles()) {

                VCFParser parser = new VCFParser(vcfFile, mongoDBLoader);
                executorService.execute(parser);
            }
        } else {
            VCFParser parser = new VCFParser(basePathFile, mongoDBLoader);
            executorService.execute(parser);
        }

        executorService.shutdown();

        // mongoDBLoader.close();
    }
}
