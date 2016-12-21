/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.cdac.pirbright.parser.vcf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sandeep
 */
public class VCFParser implements Runnable {

    private static String HEADER_MARKER = "#";
    private static int RECORD_COUNT = 5000;
    private VCFLoaderMongoDB mongoDBLoader;
    private File vcfFile;

    VCFParser(File vcfFile, VCFLoaderMongoDB mongoDBLoader) {
        this.vcfFile = vcfFile;
        this.mongoDBLoader = mongoDBLoader;
    }

    @Override
    public void run() {

        FileReader vcffr = null;
        try {
            System.out.println("File Name : " + vcfFile.getName());
            String vcfChickenLine = vcfFile.getName().substring(0, vcfFile.getName().indexOf("."));
            vcffr = new FileReader(vcfFile);
            try (BufferedReader vcfReader = new BufferedReader(vcffr)) {
                String vcfLine;
                int counter = 0;

                List<VCFBean> vcfBeans = new ArrayList<>(RECORD_COUNT);
                for (int i = 0; i < RECORD_COUNT; i++) {
                    VCFBean vcfb = new VCFBean();
                    vcfBeans.add(vcfb);
                }
                int index = 0;
                long start = System.currentTimeMillis();
                long localEnd, localStart;
                localStart = start;
                while ((vcfLine = vcfReader.readLine()) != null) {
                    if (!vcfLine.startsWith(HEADER_MARKER)) {
                        counter++;
                        VCFBean vcfb = vcfBeans.get(index++);

                        String[] vcfFileds = vcfLine.split("\t");
                        String chromosome = vcfFileds[0];
                        String positionString = vcfFileds[1].trim();
                        int position = Integer.parseInt(positionString);
                        String id = vcfFileds[2];
                        String ref = vcfFileds[3];
                        String alt = vcfFileds[4];

                        if (".1|".equalsIgnoreCase(chromosome)) {
                            chromosome = "MT";
                        }

                        vcfb.setChromosome(chromosome);
                        vcfb.setPosition(position);
                        vcfb.setId(id);
                        vcfb.setRef(ref);

                        String[] altArray = alt.split(",");
                        List<String> altList = Arrays.asList(altArray);
                        vcfb.setAlt(altList);

                        if ((index) % RECORD_COUNT == 0) {

                            storeVCFBean(vcfBeans, vcfChickenLine, index);

                            localEnd = System.currentTimeMillis();
                            System.out.println("File Name : "+vcfFile.getName()+"\tCounter : " + counter + "\t" + (localEnd - localStart) + " MilliSeconds");
                            localStart = System.currentTimeMillis();

                            for (int i = 0; i < RECORD_COUNT; i++) {
                                vcfBeans.get(i).reset();
                            }

                            index = 0;
                        }

                    }
                }

                storeVCFBean(vcfBeans, vcfChickenLine, index);
                for (int i = 0; i < RECORD_COUNT; i++) {
                    vcfBeans.get(i).reset();
                }
                long end = System.currentTimeMillis();
                System.out.println((end - start) / 1000);
                System.out.println("File Name : " + vcfFile.getName());
                System.out.println("Counter : " + counter);
            } catch (IOException ex) {
                Logger.getLogger(VCFParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VCFParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                vcffr.close();
            } catch (IOException ex) {
                Logger.getLogger(VCFParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    private void processVCFBean(VCFBean vcfb, String vcfChickenLine) {
//
//        mongoDBLoader.insert(vcfb, vcfChickenLine);
//
//    }
//
//    private void processVCFBean(List<VCFBean> beans, String vcfChickenLine, int count) {
//        mongoDBLoader.insert(beans, vcfChickenLine, count);
//    }
    private void storeVCFBean(List<VCFBean> beans, String vcfChickenLine, int count) {
        mongoDBLoader.update(beans, vcfChickenLine, count);
    }

}
