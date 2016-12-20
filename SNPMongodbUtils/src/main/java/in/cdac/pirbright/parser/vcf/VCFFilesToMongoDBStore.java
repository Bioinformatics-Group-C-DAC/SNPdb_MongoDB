/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.parser.vcf;

import in.cdac.pirbright.commands.cli.MongodbVcfStoreCommand;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ramki
 */
public class VCFFilesToMongoDBStore {

    public void submit(MongodbVcfStoreCommand command, VCFLoaderMongoDB mongoDBLoader) {
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
