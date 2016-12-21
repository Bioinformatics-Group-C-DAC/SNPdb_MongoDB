/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.parser.gene;

import in.cdac.pirbright.commands.cli.MongodbGeneStoreCommand;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author sandeep
 */
public class GeneFilesToMongoDBStore {

    public void submit(MongodbGeneStoreCommand command, GeneLoaderMongoDB mongoDBGeneLoader) throws IOException {
        //ExecutorService executorService = Executors.newFixedThreadPool(command.getProcessors());

        File basePathFile = new File(command.getPath());

        if (basePathFile.isDirectory()) {
            for (File geneFile : basePathFile.listFiles()) {

                GeneParser geneParser = new GeneParser(geneFile, mongoDBGeneLoader);
                geneParser.parse();
                //executorService.execute(parser);
            }
        } else {
            GeneParser geneParser = new GeneParser(basePathFile, mongoDBGeneLoader);
            geneParser.parse();
            //executorService.execute(parser);
        }

        //executorService.shutdown();
        // mongoDBLoader.close();
    }
}
