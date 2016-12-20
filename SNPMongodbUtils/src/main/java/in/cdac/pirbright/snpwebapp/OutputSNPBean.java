/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.snpwebapp;

/**
 *
 * @author renu
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renu
 */
public class OutputSNPBean {

    private String recordID;

    private String Chromosome;

    private long Chromosome_Position;

    private String Ref;

    private String SetOne;
    
    private String SetTwo;

    public String getSetOne() {
        return SetOne;
    }

    public void setSetOne(String SetOne) {
        this.SetOne = SetOne;
    }

    public String getSetTwo() {
        return SetTwo;
    }

    public void setSetTwo(String SetTwo) {
        this.SetTwo = SetTwo;
    }

    public OutputSNPBean() {
    }

    
    
    public OutputSNPBean(String Chromosome, long Chromosome_Position, String Ref, String SetOne, String SetTwo) {
        this.Chromosome = Chromosome;
        this.Chromosome_Position = Chromosome_Position;
        this.Ref = Ref;
        this.SetOne = SetOne;
        this.SetTwo = SetTwo;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public String getChromosome() {
        return Chromosome;
    }

    public void setChromosome(String Chromosome) {
        this.Chromosome = Chromosome;
    }

    public long getChromosome_Position() {
        return Chromosome_Position;
    }

    public void setChromosome_Position(long Chromosome_Position) {
        this.Chromosome_Position = Chromosome_Position;
    }

    public String getRef() {
        return Ref;
    }

    public void setRef(String Ref) {
        this.Ref = Ref;
    }

    @Override
    public String toString() {
        return Chromosome + "\t" + Chromosome_Position + "\t"+ Ref + "\t" + SetOne + "\t" + SetTwo;
    }
    
    
    

}
