/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.commands.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ramki
 */
@Parameters(separators = "=", commandDescription = "Record fetched/retrieved from the repository")

public class MongodbQueryCommand {

    @ParametersDelegate
    private MongoDBInfo mongoDBInfo = new MongoDBInfo();

    @Parameter(names = {"--chromosome", "-ch"},required = false, description = "Chromosome Name")
    private String chromosome = "1";

    @Parameter(names = {"--start", "-s"}, required = false, description = "Start Position (inclusive)")
    private long start = 1;

    @Parameter(names = {"--end", "-e"}, required = false, description = "End Position (inclusive)")
    private long end = 1;

    @Parameter(names = {"--gene", "-g"}, required = false, description = "Gene ID")
    private String geneId;

    
    @Parameter(names = {"--output", "-o"}, description = "OutPut File Name")
    private String output;

    @Parameter(names = "-left",required = true, description = "Left ")
    private List<String> left = new ArrayList<>();

    @Parameter(names = "-right",required = true, description = "Right ")
    private List<String> right = new ArrayList<>();

    public MongoDBInfo getMongoDBInfo() {
        return mongoDBInfo;
    }

    public String getChromosome() {
        return chromosome;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public String getOutput() {
        return output;
    }

    public List<String> getLeft() {
        return left;
    }

    public List<String> getRight() {
        return right;
    }

    public String getGeneId() {
        return geneId;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    
    @Override
    public String toString() {
        return "MongodbQueryCommand{" + "mongoDBInfo=" + mongoDBInfo + ", chromosome=" + chromosome + ", start=" + start + ", end=" + end + ", output=" + output + ", left=" + left + ", right=" + right + '}';
    }
    

   

}
