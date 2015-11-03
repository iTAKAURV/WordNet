/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package urv.itaka.jwn;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

/**
 *
 * @author MHJ
 */
public class WordNetCorpusReader {
    
    
    // Part-of-speech constants
    public final static String ADJ = "a";
    public final static String ADJ_SAT = "s";
    public final static String ADV = "r";
    public final static String NOUN = "n";
    public final static String VERB = "v";
    
    public static final HashMap<String, String> FILE_MAP = new HashMap<>();
    //Part of speech constants
    public static final HashMap<String, Integer> POS_NUMBERS = new HashMap<>();
    public static final HashMap<Integer, String> POS_NAMES = new HashMap<>();
    
    // A list of file identifiers for all the fileids used by this
    // corpus reader.    
    public static final String[] FILES = new String[] {"cntlist.rev", "lexnames", "index.sense",
              "index.adj", "index.adv", "index.noun", "index.verb",
              "data.adj", "data.adv", "data.noun", "data.verb",
              "adj.exc", "adv.exc", "noun.exc", "verb.exc"};

    
    static {
        
        FILE_MAP.put(ADJ, "adj");
        FILE_MAP.put(ADV, "adv");
        FILE_MAP.put(NOUN, "noun");
        FILE_MAP.put(VERB, "verb");
        
        
        POS_NUMBERS.put(NOUN, 1);
        POS_NUMBERS.put(VERB, 2);
        POS_NUMBERS.put(ADJ, 3);
        POS_NUMBERS.put(ADV, 4);
        POS_NUMBERS.put(ADJ_SAT, 5);
        
        POS_NAMES.put(1, NOUN);
        POS_NAMES.put(2, VERB);
        POS_NAMES.put(3, ADJ);
        POS_NAMES.put(4, ADV);
        POS_NAMES.put(5, ADJ_SAT);
        
    }
    

    
    protected HashMap<String, HashMap<String, int[]>> lemmaPosOffsetsMap;
    protected HashMap<String, HashMap<String, String>> exceptionsMap;
    
    
    protected String rootPath;
    
    public WordNetCorpusReader(String rootPath) {
        this.rootPath = rootPath;
    }
    
    protected void loadLemmaPosOffsets() {
        
        for(String suffix : WordNetCorpusReader.FILE_MAP.values()) {
            String fileName = String.format("%s//index.%s", this.rootPath, suffix);
            BufferedInputStream bis = getInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(bis));
            
            int i = 0;
            
            try {
                for (String line = br.readLine(); line != null; i++) {
                    if(line.startsWith(" ")) continue;
                    
                    String[] vals = line.split(" ");
                    int j = 0;
                    try {
                        String lemma = vals[j++];
                        String pos = vals[j++];
                       
                        int noOfSynsets = Integer.parseInt(vals[j++]);
                        assert noOfSynsets > 0;
                        
                        int noOfPointers = Integer.parseInt(vals[j++]);
                        j += noOfPointers;
                        
                        int noOfSenses = Integer.parseInt(vals[j++]);
                        assert noOfSenses == noOfSynsets;
                        
                        j++;
                        int[] offsets = new int[noOfSynsets];
                        for (int k = 0; k < noOfSynsets; k++, j++) {
                            offsets[k] = Integer.parseInt(vals[j]);
                        }
                        
                        HashMap<String, int[]> map;
                        if(this.lemmaPosOffsetsMap.containsKey(lemma)) {
                            map = this.lemmaPosOffsetsMap.get(lemma);
                        }
                        else {
                            map = new HashMap<>();
                        }
                        
                        map.put(pos, offsets);
                        if (pos.equals(WordNetCorpusReader.ADJ)) {
                            map.put(WordNetCorpusReader.ADJ_SAT, offsets);
                        }
                        
                        this.lemmaPosOffsetsMap.put(lemma, map); 
                        
                    }
                    catch(AssertionError ae) {
                        Logger.getLogger(WordNetCorpusReader.class.getName()).log(Level.SEVERE, null, String.format("Error at index.%s, line %d, Message: %s", suffix, i + 1, ae.getMessage()));
                        //throw new WordNetException(String.format("Error at index.%s, line %d, Message: %s", suffix, i + 1, ae.getMessage()));
                    }
                    catch(ValueException ve) {
                        Logger.getLogger(WordNetCorpusReader.class.getName()).log(Level.SEVERE, null, String.format("Error at index.%s, line %d, Message: %s", suffix, i + 1, ve.getMessage()));
                        //throw new WordNetException(String.format("Error at index.%s, line %d, Message: %s", suffix, i + 1, ve.getMessage()));
                    }
                    
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(WordNetCorpusReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                bis.close();
            } catch (IOException ex) {
                Logger.getLogger(WordNetCorpusReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    protected void loadExceptionMap() {
        for(String pos: WordNetCorpusReader.FILE_MAP.keySet()) {
            String suffix = WordNetCorpusReader.FILE_MAP.get(pos);
            this.exceptionsMap.put(pos, new HashMap<>());
            String fileName = String.format("%s//%s.exc", this.rootPath, suffix);
            BufferedInputStream bis = getInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(bis)); 
            try {
                for (String line = br.readLine(); line != null;) {
                    String [] terms = line.split(" ");
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(WordNetCorpusReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    protected BufferedInputStream getInputStream(String fileName) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
            return bis;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WordNetCorpusReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
