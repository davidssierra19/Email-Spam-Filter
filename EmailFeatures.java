import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EmailFeatures {

    private int emailID;
    private String[] words;

    private int wordCount;
    private int urlCount;
    private int specialCharacterWordCount;
    private String[][] nGramFrequency;
    private String[][] wordFrequency;
    private int[][] wordSizeDistribution;
    private int minWordSize;
    private int maxWordSize;
    private double meanWordSize;
    private double medianWordSize;
    private int modeWordSize;

    public EmailFeatures(int id, String[] words) {
        emailID = id;
        this.words = words;

        wordCount = words.length;
        urlCount = extractUrlCount();
        wordFrequency = extractWordFrequency(DatasetProcessor.uniqueWords);
        nGramFrequency = extractNGramFrequency(DatasetProcessor.spamNGrams);
        specialCharacterWordCount = extractSpecialCharacterWordCount();
        wordSizeDistribution = extractWordSizeDistribution(DatasetProcessor.largestWordSize);
        meanWordSize = extractMeanWordSize();
        medianWordSize = extractMedianWordSize();
        modeWordSize = extractModeWordSize();
    }

    public int extractUrlCount() {
        int count = 0;
        for(int i = 0; i < words.length; i++) {
            if(words[i].equals("url")) {
                count++;
            }
        }
        return count;
    }

    public String[][] extractWordFrequency(String[] uniqueWords) {
        String[][] wordFrequency = new String[uniqueWords.length][2];
        String[] sortedWords = new String[words.length];
        for(int i = 0; i < words.length; i++) {
            sortedWords[i] = words[i];
        }
        Arrays.sort(sortedWords);
        for(int i = 0; i < uniqueWords.length; i++) {
            wordFrequency[i][0] = uniqueWords[i];

            if(Arrays.binarySearch(sortedWords, uniqueWords[i]) < 0) {
                wordFrequency[i][1] = "0";
            }
            else {
                //count how many times uniqueWords[i] appears in words
                int count = 0;
                for(int j = 0; j < sortedWords.length; j++) {
                    if(sortedWords[j].equals(uniqueWords[i])) {
                        count++;
                    }
                }
                    wordFrequency[i][1] = count + "";
            }
        }
        return wordFrequency;
    }

    public String[][] extractNGramFrequency(String[] nGrams) {
        String[][] nGramFrequency = new String[nGrams.length][2];
        for(int i = 0; i < nGrams.length; i++) {
            nGramFrequency[i][0] = nGrams[i];
            String[] nGram = nGrams[i].split(" ");
            int count = 0;
            for(int j = 0; j < words.length; j++) {
                boolean exists = false;
                if(nGram[0].equals(words[j])) {
                    exists = true;
                    if(nGram.length + j > words.length) {
                        break;
                    }
                    for(int k = 1; k < nGram.length; k++) {
                        if(!(nGram[k].equals(words[j + k]))) {
                            exists = false;
                            j = j + k + 1;
                            break;
                        }
                    }
                }
                if(exists == true) {
                    count++;
                    j = j + nGram.length;
                }
            }
            nGramFrequency[i][1] = count + "";
        }
        return nGramFrequency;
    }

    public int extractSpecialCharacterWordCount() {
        int count = 0;
        for(int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length(); j++) {
                if (words[i].charAt(j) > 127) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public int[][] extractWordSizeDistribution(int largest) {
        int[][] wordSizeDistribution = new int[largest][2];
        for(int i = 0; i < largest; i++) {
            wordSizeDistribution[i][0] = i + 1;
            wordSizeDistribution[i][1] = 0;
        }
        for(int i = 0; i < words.length; i++) {
            wordSizeDistribution[words[i].length()-1][1]++;
        }
        return wordSizeDistribution;
    }

    public double extractMeanWordSize() {
        int totalLength = 0;

        for(String word : words) {
            totalLength += word.length();
        }
        if(totalLength == 0) {
            return 0.0;
        }
        return (double)totalLength / words.length;
    }

    public double extractMedianWordSize() {
        int[] wordLengths = new int[words.length];
        for(int i = 0; i < words.length; i++) {
            wordLengths[i] = words[i].length();
        }
        Arrays.sort(wordLengths);

        int middle = wordLengths.length / 2;
        if(wordLengths.length % 2 == 0) {
            return((wordLengths[middle - 1] + wordLengths[middle]) / 2.0);
        }
        else {
            return wordLengths[middle];
        }
    }

    public int extractModeWordSize() {
        HashMap<Integer, Integer> lengthFrequency = new HashMap<>();

        for(String word : words) {
            int length = word.length();
            lengthFrequency.put(length, lengthFrequency.getOrDefault(length, 0) + 1);
        }
        int modeLength = 0;
        int maxFrequency = 0;

        for(Map.Entry<Integer, Integer> entry : lengthFrequency.entrySet()) {
            if(entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                modeLength = entry.getKey();
            }
        }
        return modeLength;
    }



    public int getWordCount() {
        return wordCount;
    }
    public int getUrlCount() {
        return urlCount;
    }
    public int getSpecialCharacterWordCount() {
        return specialCharacterWordCount;
    }
    public String[][] getWordFrequency() {
        return wordFrequency;
    }
    public String[][] getNGramFrequency() {
        return nGramFrequency;
    }
    public int[][] getWordSizeDistribution() {
        return wordSizeDistribution;
    }
    public double getMeanWordSize() {
        return meanWordSize;
    }
    public double getMedianWordSize() {
        return medianWordSize;
    }
    public int getModeWordSize() {
        return modeWordSize;
    }
}
