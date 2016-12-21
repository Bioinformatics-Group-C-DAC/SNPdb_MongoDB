/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.parser.vcf;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author sandeep
 */
public class VCFBean {

    
    private String chromosome;
    private int position;
    private String id;
    private String ref;
    private List<String> alt;

        
    public VCFBean() {
    }
    
    public VCFBean(String chromosome, int position, String id, String ref, List<String> alt) {
        this.chromosome = chromosome;
        this.position = position;
        this.id = id;
        this.ref = ref;
        this.alt = alt;
    }
    

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public List<String> getAlt() {
        return alt;
    }

    public void setAlt(List<String> alt) {
        this.alt = alt;
    }

    void reset() {
        this.chromosome = null;
        this.position = 0;
        this.id = null;
        this.ref = null;
        this.alt = null;
    }
    
    
    
}
   