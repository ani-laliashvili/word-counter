public class WordCount {
    String word;
    int count;
    
    public WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }
    
    /**
     * Gets the word stored by this WordCount
     */
    public String getWord() {
        return word;
    }

    /** 
     * Gets the count stored by this WordCount
     */
    public int getCount() {
        return count;
    }
    
    @Override
    public String toString() {
        return word + ":" + count;
    }
    
}