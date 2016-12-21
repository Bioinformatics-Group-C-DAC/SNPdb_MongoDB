/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdac.pirbright.snpwebapp;

import com.cdac.pirbright.snpwebapp.config.Config;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import in.cdac.pirbright.chicken.query.SNPChickenQuery;
import in.cdac.pirbright.mongodb.client.MongoClientSingleton;
import in.cdac.pirbright.parser.vcf.VCFLoaderMongoDB;
import in.cdac.pirbright.snpwebapp.OutputSNPBean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.bson.Document;

/**
 *
 * @author renu
 */
@Named(value = "snpHomeBean")
@SessionScoped
public class SNPHomeBean implements Serializable {

    private String searchOption;
//    private boolean value1;

    private SNPChickenQuery chickenQuery;

//    private List<Temp> strList;
    private List<String> selectedChickenLineInfoSetOne;

    private List<SelectItem> ChickenLineInfo;

    private List<String> selectedChickenLineInfoSetTwo;
    private String chromosome;
    private long startPosition = 1;

    List<OutputSNPBean> outputSNPBeans = new ArrayList<>();

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private boolean searchByRange;
    private boolean searchByGeneId;

    public boolean isSearchByRange() {
        return searchByRange;
    }

    public void setSearchByRange(boolean searchByRange) {
        this.searchByRange = searchByRange;
        if (searchByRange) {
            setSearchByGeneId(false);
        }
    }

    public boolean isSearchByGeneId() {
        return searchByGeneId;
    }

    public void setSearchByGeneId(boolean searchByGeneId) {
        this.searchByGeneId = searchByGeneId;
        if (searchByGeneId) {
            setSearchByRange(false);
        }
    }

    @PostConstruct
    public void init() {
        ChickenLineInfo = new ArrayList<>();

        ChickenLineInfo.add(new SelectItem("Line15", "Line15"));
        ChickenLineInfo.add(new SelectItem("Line6", "Line6"));
        ChickenLineInfo.add(new SelectItem("Line7", "Line7"));
        ChickenLineInfo.add(new SelectItem("LineC", "LineC"));
        ChickenLineInfo.add(new SelectItem("LineN", "LineN"));
        ChickenLineInfo.add(new SelectItem("Linep", "LineP"));
        ChickenLineInfo.add(new SelectItem("LineWellcome", "LineWellcome"));
        ChickenLineInfo.add(new SelectItem("LineZero", "LineZero"));

        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(new File(System.getProperty("user.home") + File.separator + Config.CONFIG_PATH))));
        } catch (IOException ex) {
            Logger.getLogger(SNPHomeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(properties);
        mongoClient = MongoClientSingleton.getInstance(properties.getProperty(Config.MONGO_DB_HOST), Integer.parseInt(properties.getProperty(Config.MONGO_DB_PORT)));
        database = mongoClient.getDatabase(properties.getProperty(Config.MONGO_DB_DATABASE_NAME));
        collection = database.getCollection(properties.getProperty(Config.MONGO_DB_COLLECTION_NAME));
        VCFLoaderMongoDB mongoDBLoader = new VCFLoaderMongoDB(collection);
        chickenQuery = new SNPChickenQuery(mongoDBLoader);
    }

    String strPosition;
    BufferedWriter writer = null;
    private String geneId;
    private String setOneHeader = "SetOne";
    private String setTwoHeader = "SetTwo";

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public List<SelectItem> getChickenLineInfo() {
        return ChickenLineInfo;
    }

    public void setChickenLineInfo(List<SelectItem> ChickenLineInfo) {
        this.ChickenLineInfo = ChickenLineInfo;
    }

    public String getSetOneHeader() {
        return setOneHeader;
    }

    public void setSetOneHeader(String setOneHeader) {
        this.setOneHeader = setOneHeader;
    }

    public String getSetTwoHeader() {
        return setTwoHeader;
    }

    public void setSetTwoHeader(String setTwoHeader) {
        this.setTwoHeader = setTwoHeader;
    }

    private String totalcount;

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public void submit() {
        outputSNPBeans = null;

        if (getSelectedChickenLineInfoSetOne().size() > 0 && getSelectedChickenLineInfoSetTwo().size() > 0) {
            String setone = makeCommaSeperatedString(getSelectedChickenLineInfoSetOne());
            String setTwo = makeCommaSeperatedString(getSelectedChickenLineInfoSetTwo());

            if (isSearchByRange()) {
                System.out.println("in Serach By Range");
                String ch = getChromosome().substring(0, getChromosome().indexOf(':'));
                strPosition = getChromosome().substring(getChromosome().indexOf(':') + 1, getChromosome().length());

                //                strList = chickenLineInfoFacade.callNOSQLMongoDB(setone, setTwo,ch,strPosition);
                outputSNPBeans = callNoSQLMongoDB(setone, setTwo, ch, strPosition);
            }

            if (isSearchByGeneId()) {
                System.out.println("In Search By GeneiD");

                if (getGeneId().equals("") || getGeneId().length() <= 0) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage("Enter valid Gene Id!!!"));
                    outputSNPBeans=null;
                }else{
                    outputSNPBeans = callNoSQLMongoDBSearchByGeneId(setone, setTwo, geneId);
                }                
              //  System.out.println(outputSNPBeans.size());

            }

            if (!isSearchByGeneId() && !isSearchByRange()) {
                System.out.println("in search By Normal");

                if ((!(getChromosome().equals("")) || getChromosome().length() > 0)
                        && (getStartPosition() > 0 && getEndPosition() > 0)) {
                    strPosition = startPosition + "-" + endPosition;
//                     strList = chickenLineInfoFacade.callNOSQLMongoDB(setone, setTwo,getChromosome(),strPosition);
                    outputSNPBeans = callNoSQLMongoDB(setone, setTwo, getChromosome(), strPosition);

                }

                if ((getChromosome().equals("") || getChromosome().length() <= 0)
                        && (getStartPosition() > 0 && getEndPosition() > 0)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage("Enter valid Chromosome!!!"));
                }

                if ((!(getChromosome().equals("")) || getChromosome().length() > 0)
                        && (getStartPosition() <= 0 && getEndPosition() > 0)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage("Enter valid start and end position!!!"));
                }

                if ((!(getChromosome().equals("")) || getChromosome().length() > 0)
                        && (getStartPosition() > 0 && getEndPosition() <= 0)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage("Enter valid start and end position!!!"));
                }

                if ((!(getChromosome().equals("")) || getChromosome().length() > 0)
                        && (getStartPosition() <= 0 && getEndPosition() <= 0)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Enter valid start and end position!!!"));
                }

                if ((getChromosome().equals("") || getChromosome().length() <= 0)
                        && (getStartPosition() <= 0 && getEndPosition() <= 0)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage("Enter valid chromosome and position!!!"));
                }

            }
            
            
            if (outputSNPBeans != null) {
                totalcount = Integer.toString(outputSNPBeans.size());
            } else {
                totalcount = "";
            }

            //compare(getStrList());
        }

    }

    public List<OutputSNPBean> callNoSQLMongoDB(String setone, String setTwo, String chromosome, String position) {
        String[] sa = setone.split(",");
        String[] sb = setTwo.split(",");
        String[] posa = position.split("-");
        int pos1 = Integer.parseInt(posa[0]);
        int pos2 = Integer.parseInt(posa[1]);
        List<String> leftList = Arrays.asList(sa);
        List<String> rightList = Arrays.asList(sb);
        setOneHeader = String.join(",", leftList);
        setTwoHeader = String.join(",", rightList);

        long startTime = System.currentTimeMillis();
        List<OutputSNPBean> retriveVCFRecords = chickenQuery.retriveVCFRecords(chromosome, pos1, pos2, leftList, rightList);
        System.out.println(retriveVCFRecords.size());
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken : " + (endTime - startTime) + " ms");

        return retriveVCFRecords;
    }

    public List<OutputSNPBean> callNoSQLMongoDBSearchByGeneId(String setone, String setTwo, String geneId) {
        String[] sa = setone.split(",");
        String[] sb = setTwo.split(",");

        List<String> leftList = Arrays.asList(sa);
        List<String> rightList = Arrays.asList(sb);
        setOneHeader = String.join(",", leftList);
        setTwoHeader = String.join(",", rightList);

        long startTime = System.currentTimeMillis();
        List<OutputSNPBean> retriveVCFRecords = chickenQuery.retriveVCFRecords(database, geneId, leftList, rightList);
        
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken : " + (endTime - startTime) + " ms");

        return retriveVCFRecords;
    }

    public ArrayList<Character> getCharacterString(String str) {
        ArrayList<Character> al = new ArrayList<Character>();

        StringTokenizer st = new StringTokenizer(str, ",");
        while (st.hasMoreTokens()) {
            al.add(st.nextToken().charAt(0));
        }

        return al;
    }

    public void compareSecond(String str, String settwo) {

        StringTokenizer st = new StringTokenizer(settwo, "|");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            System.out.println("--" + token);

        }
    }

    public void saveTxt() {

    }

    private String makeCommaSeperatedString(List<String> list) {
        String separator = ",";
        int total = list.size() * separator.length();
        for (String s : list) {
            total += s.length();
        }

        StringBuilder sb = new StringBuilder(total);
        for (String s : list) {
            sb.append(separator).append(s);
        }

        String result = sb.substring(separator.length());

        return result;

    }

    public String getSearchOption() {
        return searchOption;
    }

//    public boolean isValue1() {
//        return value1;
//    }
//
//    public void setValue1(boolean value1) {
//        this.value1 = value1;
//    }
    public void setSearchOption(String searchOption) {
        this.searchOption = searchOption;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    private long endPosition = 1;

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public List<String> getSelectedChickenLineInfoSetOne() {
        return selectedChickenLineInfoSetOne;
    }

    public void setSelectedChickenLineInfoSetOne(List<String> selectedChickenLineInfoSetOne) {
        this.selectedChickenLineInfoSetOne = selectedChickenLineInfoSetOne;
    }

    public List<String> getSelectedChickenLineInfoSetTwo() {
        return selectedChickenLineInfoSetTwo;
    }

    public void setSelectedChickenLineInfoSetTwo(List<String> selectedChickenLineInfoSetTwo) {
        this.selectedChickenLineInfoSetTwo = selectedChickenLineInfoSetTwo;
    }

    public List<OutputSNPBean> getOutputSNPBeans() {
        return outputSNPBeans;
    }

    public void setOutputSNPBeans(List<OutputSNPBean> outputSNPBeans) {
        this.outputSNPBeans = outputSNPBeans;
    }

}


/* Set One : Line15 Set Two : Line7 Chromosome : 6 Position : 18882796-18931965 */
