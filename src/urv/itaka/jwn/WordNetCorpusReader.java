/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package urv.itaka.jwn;

import java.util.Dictionary;
import java.util.HashMap;

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
    

    
    public WordNetCorpusReader(String path) {
        
    }
    
}
