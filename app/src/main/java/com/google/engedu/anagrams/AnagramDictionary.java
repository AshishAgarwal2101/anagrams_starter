package com.google.engedu.anagrams;

import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordLength = DEFAULT_WORD_LENGTH;
    private ArrayList<String> wordList = new ArrayList<>();
    HashSet<String> wordSet = new HashSet<>();
    HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortWord = sortLetters(word);
            if(lettersToWord.containsKey(sortWord)){
                lettersToWord.get(sortWord).add(word);
            }
            else{
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                lettersToWord.put(sortWord, temp);
            }
            if(sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            }
            else{
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                sizeToWords.put(word.length(), temp);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(String compWord : wordList){
            if((sortLetters(compWord)).equals(sortLetters(targetWord))){
                result.add(compWord);
            }
        }
        return result;
    }
    private String sortLetters(String word){
        char letters[] = new char[word.length()];
        letters = word.toCharArray();
        Arrays.sort(letters);
        word = String.valueOf(letters);
        return word;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        word = sortLetters(word);
        if(lettersToWord.containsKey(word)) {
            for (char i = 'a'; i <= 'z'; i++) {
                String newV = sortLetters(word + i);
                if (lettersToWord.containsKey(newV)) {
                    ArrayList<String> val1 = new ArrayList<>();
                    val1 = lettersToWord.get(newV);
                    result.addAll(val1);
                    ArrayList<String> val2 = new ArrayList<>();
                    for(String s:val1){
                        if(!isGoodWord(s,word)){
                            val2.add(s);
                        }
                    }
                    result.removeAll(val2);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Random random = new Random();
        while (true) {
            int startPoint = random.nextInt(9999);
            //for(int i=startPoint; i<wordList.size(); i++){
            String s = wordList.get(startPoint);
            ArrayList<String> temp = lettersToWord.get(sortLetters(s));
            if (s.length() == wordLength && temp.size() >= MIN_NUM_ANAGRAMS) {
                if (s.length() <= MAX_WORD_LENGTH)
                    wordLength++;
                return s;
            }
        }
    }

}

