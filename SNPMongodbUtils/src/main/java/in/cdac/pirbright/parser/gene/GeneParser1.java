/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.parser.gene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sandeep
 */
public class GeneParser1 {

    private static String HEADER_MARKER = "#";
    private static int RECORD_COUNT = 5000;
    private GeneLoaderMongoDB mongoDBGeneLoader;
    private File geneFile;

    GeneParser1(File geneFile, GeneLoaderMongoDB mongoDBGeneLoader) {
        this.geneFile = geneFile;
        this.mongoDBGeneLoader = mongoDBGeneLoader;
    }

    public void parse() throws IOException {

        FileReader genefr = null;
        BufferedReader geneReader = null;
        try {
            System.out.println("Parsing Gene File Name : " + geneFile.getName());
            genefr = new FileReader(geneFile);
            geneReader = new BufferedReader(genefr);
            String geneLine;
            int counter = 0;

            List<GeneBean> geneBeans = new ArrayList<>(RECORD_COUNT);
            for (int i = 0; i < RECORD_COUNT; i++) {
                GeneBean geneb = new GeneBean();
                geneBeans.add(geneb);
            }
            int index = 0;
            long start = System.currentTimeMillis();
            long localEnd, localStart;
            localStart = start;
            while ((geneLine = geneReader.readLine()) != null) {
                if (!geneLine.startsWith(HEADER_MARKER)) {

                    String[] geneFileds = geneLine.split("\t");
                    String geneExonCDS = geneFileds[2].trim();

                    if (!"gene".equals(geneExonCDS)) {
                        continue;
                    }

                    String chromosome = geneFileds[0];
                    if (".1|".equalsIgnoreCase(chromosome)) {
                        chromosome = "MT";
                    }

                    if (!isValidChromosome(chromosome)) {
                        continue;
                    }

                    String startPositionString = geneFileds[3].trim();
                    String endPositionString = geneFileds[4].trim();
                    long startPosition = Long.parseLong(startPositionString);
                    long endPosition = Long.parseLong(endPositionString);

                    String geneRecord = geneFileds[8];
                    String geneId = extractGeneId(geneRecord);

                    counter++;
                    GeneBean geneb = geneBeans.get(index);
                    index = index + 1;

                    geneb.setGeneId(geneId);
                    geneb.setChromosome(chromosome);
                    geneb.setStartPosition(startPosition);
                    geneb.setEndPosition(endPosition);

                    if ((index) % RECORD_COUNT == 0) {

                        storeGeneBean(geneBeans, index);

                        localEnd = System.currentTimeMillis();
                        //System.out.println("File Name : " + geneFile.getName() + "\tCounter : " + counter + "\t" + (localEnd - localStart) + " MilliSeconds");
                        localStart = System.currentTimeMillis();

                        for (int i = 0; i < RECORD_COUNT; i++) {
                            geneBeans.get(i).reset();
                        }

                        index = 0;
                    }

                }
            }

            storeGeneBean(geneBeans, index);
            for (int i = 0; i < RECORD_COUNT; i++) {
                geneBeans.get(i).reset();
            }
            long end = System.currentTimeMillis();
            System.out.println("Total Time " + (end - start) / 1000 + " in Seconds");
            // System.out.println("File Name : " + geneFile.getName());
            // System.out.println("Counter : " + counter);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneParser1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                geneReader.close();
            } catch (IOException ex) {
                Logger.getLogger(GeneParser1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void storeGeneBean(List<GeneBean> beans, int count) {

        mongoDBGeneLoader.insert(beans, count);
        System.out.println("Count=" + count + "\t" + "GeneCount=" + beans.size());
    }

    private String extractGeneId(String geneRecord) {

        String geneId = null;
        //gene_id "ENSGALG00000009771"; gene_source "ensembl"; gene_biotype "protein_coding"
        String[] records = geneRecord.split(";");

        String targetRecord = records[0];
        geneId = targetRecord.substring(targetRecord.indexOf("\"") + 1, (targetRecord.lastIndexOf("\"")));
        return geneId;
    }

    private boolean isValidChromosome(String chromosome) {
        boolean valid = false;

        if (chromosome.equals("MT") || chromosome.equals("W") || chromosome.equals("Z")) {
            valid = true;
        } else {

            try {
                int chNumber = Integer.parseInt(chromosome);
                if (chNumber >= 1 && chNumber <= 32) {
                    valid = true;
                } else {
                    valid = false;
                }
            } catch (NumberFormatException nfe) {
                valid = false;
            }

        }
        return valid;
    }

    public static void main(String[] args) throws IOException {
//        String test = "1	protein_coding	gene	1735	16308	.	+	.	gene_id \"ENSGALG00000009771\"; gene_source \"ensembl\"; gene_biotype \"protein_coding\"";
//
//        String[] sa = test.split("\t");
//        for (String s : sa) {
//            System.out.println(s);
//        }

        //String geneId = extractGeneId(sa[8]);
        //System.out.println(geneId);
     //   String geneFilePath = "/home/sandeep/Desktop/Gallus_gallus.Galgal4.76.gtf";
         String geneFilePath = "/Users/ramki/Documents/ProteinTable111_374862.txt";
        File geneFile = new File(geneFilePath);
        GeneParser1 geneParser = new GeneParser1(geneFile, null);
        geneParser.parse();

    }

}
