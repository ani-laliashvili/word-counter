import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.PrintWriter;

/** 
* This class counts the frequency of words in a text document
* and creates a word cloud html file when prompted. 
*
* @author: Ani Laliashvili @author: Hannah Howard */

public class WordCounter {
    private WordCountMap wordCountMap;
    private static List<String> stopWords;
    
    public WordCounter() {
        wordCountMap = new WordCountMap();
        stopWords = new ArrayList<String>();
    } // end constructor
    
    /**
     * Returns wordCountMap.
     */
    public WordCountMap getWordCountMap() {
        return wordCountMap;
    }
    
    /**
     * Returns list of specified number of words from wordCountMap.
     * @param number number of words desired
     * @return listFin list of desired number of words
     */
    public List<WordCount> getList(int number) {
        // get wordCountMap
        WordCountMap wMap = getWordCountMap();
        
        // read counts
        List<WordCount> list = wMap.getWordCountsByCount();
        
        List<WordCount> listFin = new ArrayList<WordCount>();
        
        // if there are more words in the list then desired, picks first number words
        if (list.size() >= number) {
            for (int i = 0; i < number; i++){
                listFin.add(list.get(i));
            }
        // otherwise prints all words    
        } else 
            listFin = list;
        
        return listFin;
    }
    
    /**
     * Prints all word counts.
     */
    public void printString() {
        // get wordCountMap
        WordCountMap wMap = getWordCountMap();
        
        // get word counts
        List<WordCount> list = wMap.getWordCountsByCount();
        
        // print
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i).toString());
        }
    }
    
    /**
     * Loads text file into a wordCountMap object.
     * @param filePath path of file with text
     */
    public void load(String filePath) {
        Scanner scanner = null;
        
        // try file path
        try {
            scanner = new Scanner(new File(filePath));  
        } catch (FileNotFoundException e) {
            System.err.println("Scanner error openning the file " + filePath);
        }
        
        // load stop words
        loadStopWords("StopWords.txt");
        
        // read file
        while(scanner.hasNext()) {
            String word = scanner.next().toLowerCase(); // lower-case
            word = removePunctuations(word); // remove punctuation
            word = word.trim(); // trim
            
            // if word is not a stop word add to wordCountMap object
            if (!isStopWord(word)){ 
             wordCountMap.incrementCount(word);   
            }
        } 
    }
    
    /**
     * Removes punctuations from word.
     */
    private static String removePunctuations(String word) {
        int frontIndex = 0;
        int endIndex = word.length() - 1;
        while (isPunctuation(word.charAt(frontIndex))) {
            frontIndex++;
        }
        while (isPunctuation(word.charAt(endIndex))) {
            endIndex--;
        }
        return word.substring(frontIndex, endIndex + 1);
    }
    
    /**
     * Helper method for removePunctuations().
     */
    private static boolean isPunctuation(char c) {
        return (c == ',' || c == '.' || c == '?' ||
                c == '!' || c == ';' || c == '\'' ||
                c == '"');
    }
    
    /**
     * Returns true if given word is a stop word.
     */
    public static boolean isStopWord(String word) {
        return stopWords.contains(word);
    }
    
    /**
     * Loads stop words.
     */
    public static void loadStopWords(String filePath) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filePath));  
        } catch (FileNotFoundException e) {
            System.err.println("Scanner error openning the file " + filePath);
        }
        while(scanner.hasNext()) {
            String word = scanner.next().toLowerCase();
            stopWords.add(word);
        }
    }
    
    /**
     * If one argument is specified prints out words with counts. If 3 arguments are specified
     * creates a word cloud as an html file.
     */
    public static void main(String[] args) {
        WordCounter count = new WordCounter();
        
        if (args.length == 1){
            count.load(args[0]); // load words
            count.printString(); // print counts
            
        } else if (args.length == 3){
            count.load(args[0]); // load words
            
            int numberWords =  Integer. valueOf(args[1]); // number of words desired
            String outfile = args[2]; // output file name
            
            // create word cloud
            String out = WordCloudMaker.getWordCloudHTML(" ", count.getList(numberWords));

            // create new PrintWriter object
            PrintWriter toFile = null;
            
            // try output file
            try {
                toFile = new PrintWriter(outfile);
            } catch (FileNotFoundException e) {
                System.err.println("Scanner error openning the file " + outfile);
            }
            
            toFile.print(out); // print the String returned from getWordCloudHTML
            toFile.close(); // close PrintWriter
        }
    }
}