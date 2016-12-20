/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.commands.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

/**
 *
 * @author sandeep
 */
@Parameters(separators = "=", commandDescription = "Gene Table info stored into the repository")

public class MongodbGeneStoreCommand {

    @ParametersDelegate
    private MongoDBInfo mongoDBInfo = new MongoDBInfo();

    @Parameter(names = {"--inputpath", "-i"},required = true, description = "Gene File/Files Input directory Path")
    private String path;

//     @Parameter(names = {"--processors", "-proc"}, description = "Number of Processors")
//    private int processors = 1;

    public MongoDBInfo getMongoDBInfo() {
        return mongoDBInfo;
    }

    public String getPath() {
        return path;
    }

//    public int getProcessors() {
//        return processors;
//    }

    @Override
    public String toString() {
        return "MongodbDumpCommand{" + "mongoDBInfo=" + mongoDBInfo + ", path=" + path + '}';
    }
     
    
    
    
    
    
    

}
