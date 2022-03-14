import java.util.ArrayList;
import java.util.List;

/** 
* This class creates a wordCountMap object to be used 
* for storing words and their frequencies.
*
* @author: Ani Laliashvili */

public class WordCountMap {
    private Node root;
    
    /**
     * Constructs an empty WordCountMap.
     */         
    public WordCountMap() {
        root = new Node(' ', 0);
    }
 
    /**
     * Adds 1 to the existing count for word, or adds word to the WordCountMap
     * with a count of 1 if it was not already present.
     * @param word word to be added
     */
    public void incrementCount(String word) {
        incrementCountFromNode(word, root);
    }
    
    /**
     * Helper method for incrementCount().
     */
    private void incrementCountFromNode(String partialWord, Node node) {
        boolean found = false;
        char firstChar = partialWord.charAt(0);
        if (partialWord.length() == 1) { // base case 1
            for (Node childNode: node.children) {
                if (childNode.data == firstChar) {
                    childNode.count += 1;
                    found = true;
                    break;
                }
            }
            if (!found) {
                node.children.add(new Node(firstChar, 1));
            }
        } else if (partialWord.length() > 1) {
            String subString = partialWord.substring(1);
            for (Node childNode: node.children) {
                if (firstChar == childNode.data) {
                    incrementCountFromNode(subString, childNode); // recursive case
                    found = true;
                    break;
                }
            }
            if (!found) { // base case 2
                Node currentNode = node;
                for (int i = 0; i < partialWord.length(); i++) {
                    currentNode.children.add(new Node(partialWord.charAt(i), 0));
                    currentNode = currentNode.children.get(currentNode.children.size() - 1);
                }
                currentNode.count += 1;
            }
        } else {
            System.err.println("partialWord is an empty string in incrementCountFromNode.");
        }
    }


    /**
     * Returns true if word is stored in this WordCountMap with
     * a count greater than 0, and false otherwise.
     */
    public boolean contains(String word) {
        return containsWord(word, root);
    }

    /**
     * Helper method for contains().
     */
    private boolean containsWord(String word, Node node) {
        List<Node> children = node.getChildren();
        Node child = new Node(' ', 0);
        boolean contains = false;
        int i = 0;
        
        // traverse through all children to find matches
        while (i < children.size() && contains == false) {
            
            // if at last character of the word, characters match and count is not zero set contains to true
            if (word.length() == 1 && word.charAt(0) == children.get(i).data && children.get(i).count > 0){
                contains = true;
                
            // otherwise if any of the children characters match, set child  
            } else if (word.charAt(0) == children.get(i).data) {
                child = children.get(i);
            } i++;
        }
        
        // if word is longer than one character, move to the next character recursively
        if (word.length() > 1){
             contains = containsWord(word.substring(1), child);
        }  
        return contains;
    }

    /**
     * Returns the count of word, or -1 if word is not in the WordCountMap.
     * Implementation must be recursive, not iterative.
     */
    public int getCount(String word) {
        return getCountWord(word, root);
    }
    
    /**
     * Helper method for getCount().
     */
    private int getCountWord(String word, Node node) {
        List<Node> children = node.getChildren();
        Node child = new Node(' ', 0);
        int count = -1;
        int i = 0;    
        
        while (i < children.size()) {
            // if at last character of the word, characters match and count is not zero set count
            if (word.length() == 1 && word.charAt(0) == children.get(i).data && children.get(i).count > 0){
                count = children.get(i).count;
            
            // otherwise if any of the children characters match, set child to that child  
            } else if (word.charAt(0) == children.get(i).data) {
                child = children.get(i);
            } i++;
        }
        
        // if word is longer than one character, move to the next character recursively
        if (word.length() > 1){
             count = getCountWord(word.substring(1), child);
        }  
        return count;
    }
    
    /** 
     * Returns a list of WordCount objects, one per word represented in this 
     * WordCountMap, sorted in decreasing order by count. 
     */
    public List<WordCount> getWordCountsByCount() {
        List<WordCount> wordCountList = new ArrayList<WordCount>();
        
        // add words to wordCountList
        getString(root, " ", wordCountList);
        
        // sort
        for (int j = 1; j < wordCountList.size(); j++){
            for (int m = 0; m < j; m++){
                if (wordCountList.get(j).count >= wordCountList.get(m).count) {
                    WordCount w = wordCountList.remove(j);
                    wordCountList.add(m, w);
                }
            }
        }
        return wordCountList;
    }
    
    /**
     * Helper method for getWordCountsByCount().
     */
    private String getString(Node node, String word, List<WordCount> list){
        List<Node> children = node.getChildren();
        Node child = node;
        int i = 0;
        
        // if node has children traverse through all children recursively
        if (children.size() != 0){
            while (i < children.size()) {  
                child = children.get(i); // set child
                word = word + String.valueOf(children.get(i).data); // add child character to word String
                
                // if count is not 0, add word to list with count
                if (child.count != 0) {
                    list.add(new WordCount(word, child.count));
                }
                
                // check children of child
                word = getString(child, word, list);
            
                // if leaf node, remove last character from word String
                if (children.get(i).isLeafNode() && word.length() != 0){
                    word = word.substring(0, word.length() - 1);
                } 
                
                // if last child, remove last character from word String
                if (children.size() == i+1 && word.length() != 0) {
                    word = word.substring(0, word.length() - 1);
                }
                
                i++;
            }
        }
        return word; 
    }
    
    
    /** 
     * Returns a count of the total number of nodes in the tree. 
     * A tree with only a root is a tree with one node; it is an acceptable
     * implementation to have a tree that represents no words have either
     * 1 node (the root) or 0 nodes.
     * Implementation must be recursive, not iterative.
     */
    public int getNodeCount() {
        return getNodeCountHelper(root);
    }

    /**
     * Helper method for getNodeCount().
     */
    private int getNodeCountHelper(Node node){
        List<Node> children = node.getChildren();
        Node child = new Node(' ', 0);
        int nodeCount = 1;
        
        // traverse through all children recursively
        if (children.size() != 0){
            for(int i = 0; i < children.size(); i++){
                child = children.get(i);
                nodeCount = nodeCount + getNodeCountHelper(child);
            }
        }
        return nodeCount;  
    }
    
    /**
     * Helper Node class.
     */
    private class Node {
        private char data;
        private int count;
        private List<Node> children;
        
        private Node(char character, int count) {
            data = character;
            this.count = count;
            children = new ArrayList<Node>();
        }
        
        private Node(char character, int count, List<Node> children) {
            data = character;
            this.count = count;
            this.children = children;
        }
        
        private boolean isLeafNode() {
            return children.size() == 0;
        }
        
        private List<Node> getChildren() {
          return children;
        }
        
        @Override
        public String toString() {
            String str = data + ":" + count + " with " + children.size() + " children: ";
            for (int i = 0; i < children.size(); i++) {
                str += (children.get(i).data + ":" + children.get(i).count + " ");
            }
            return str;
        }
    }
    
    /**
     * Tests WordCountMap class methods.
     */
    public static void main(String[] args) {
        WordCountMap map = new WordCountMap();
        
        // add words
        map.incrementCount("then");
        map.incrementCount("then");
        map.incrementCount("then");
        
        map.incrementCount("the");
        map.incrementCount("the");
        map.incrementCount("the");
        map.incrementCount("the");
        map.incrementCount("the");

        
        // test contains()
        System.out.println("Testing contains(), should return true: " + map.contains("then"));
        
        // test getCount()
        System.out.println("Testing getCount(), should return -1: " + map.getCount("cat"));
        System.out.println("Testing getCount(), should return 3: " + map.getCount("then"));
        
        // test getNodeCount()
        System.out.println("Testing getNodeCount(): ");
        System.out.println("This should return 5: " + map.getNodeCount());
        
        // add more words
        map.incrementCount("that");
        map.incrementCount("that");
        
        System.out.println("This should return 7: " + map.getNodeCount());
        
        // add "cat"
        map.incrementCount("cat");
        
        System.out.println("This should return 10: " + map.getNodeCount());
        
        // add "chomp"
        map.incrementCount("chomp");
        map.incrementCount("chomp");
        map.incrementCount("chomp");
        map.incrementCount("chomp");
        map.incrementCount("chomp");
        
        System.out.println("This should return 14: " + map.getNodeCount()); 
        
        // test getWordCountsByCount()
        System.out.println("Testing getWordCountsByCount, should return chomp:5, the:5, then:3, that:2, cat:1  ");
        List<WordCount> list = map.getWordCountsByCount();
        
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i).toString());
        }
        
        // add more words
        map.incrementCount("suspiciously");
        map.incrementCount("suspiciously");
        map.incrementCount("suspiciously");
        map.incrementCount("suspiciously");
        map.incrementCount("suspiciously");
        map.incrementCount("suspiciously");
        map.incrementCount("announced");
        map.incrementCount("suspiciously");
        map.incrementCount("possible");
        map.incrementCount("suspiciously");
        map.incrementCount("possible");
        map.incrementCount("suspiciously");
        
        System.out.println(" ");
        
        // test getWordCountsByCount()
        System.out.println("Testing getWordCountsByCount, should return suspiciously:9, chomp:5, the:5, then:3, possible:2, that:2, cat:1, announced:1 ");
        System.out.println(" ");
        
        list = map.getWordCountsByCount();
        
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i).toString());
        }
    }
}
